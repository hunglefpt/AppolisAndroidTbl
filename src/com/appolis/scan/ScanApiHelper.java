/**
 * Name: $RCSfile: ScanApiHelper.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.scan;

import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.SocketMobile.ScanAPI.ISktScanApi;
import com.SocketMobile.ScanAPI.ISktScanDecodedData;
import com.SocketMobile.ScanAPI.ISktScanDevice;
import com.SocketMobile.ScanAPI.ISktScanEvent;
import com.SocketMobile.ScanAPI.ISktScanMsg;
import com.SocketMobile.ScanAPI.ISktScanObject;
import com.SocketMobile.ScanAPI.ISktScanProperty;
import com.SocketMobile.ScanAPI.ISktScanSymbology;
import com.SocketMobile.ScanAPI.SktClassFactory;
import com.SocketMobile.ScanAPI.SktScan;
import com.SocketMobile.ScanAPI.SktScanDeviceType;
import com.SocketMobile.ScanAPI.SktScanErrors;
import com.SocketMobile.ScanAPI.ISktScanProperty.values;

/**
 * this class provides a set of common functions to retrieve or configure a
 * scanner or ScanAPI and to receive decoded data from a scanner.
 * <p>
 * This helper manages a commands list so the application can send multiple
 * command in a row, the helper will send them one at a time. Each command has
 * an optional callback function that will be called each time a command
 * complete. By example, to get a device friendly name, use the
 * PostGetFriendlyName method and pass a callback function in which you can
 * update the UI with the newly fetched friendly name. This operation will be
 * completely asynchronous.
 * <p>
 * ScanAPI Helper manages a list of device information. Most of the time only
 * one device is connected to the host. This list could be configured to have
 * always one item, that will be a "No device connected" item in the case where
 * there is no device connected, or simply a device name when there is one
 * device connected. Use isDeviceConnected method to know if there is at least
 * one device connected to the host.<br>
 * Common usage scenario of ScanAPIHelper:<br>
 * <li>create an instance of ScanApiHelper: _scanApi=new ScanApiHelper();
 * <li>[optional] if a UI device list is used a no device connected string can
 * be
 * specified:_scanApi.setNoDeviceText(getString(R.string.no_device_connected));
 * <li>register for notification:
 * _scanApi.setNotification(_scanApiNotification);
 * <li>derive from ScanApiHelperNotification to handle the notifications coming
 * from ScanAPI including "Device Arrival", "Device Removal", "Decoded Data"
 * etc...
 * <li>open ScanAPI to start using it:_scanApi.open();
 * <li>check the ScanAPI initialization result in the notifications:
 * _scanApiNotification.onScanApiInitializeComplete(long result){}
 * <li>monitor a scanner connection by using the notifications:
 * _scanApiNotification.onDeviceArrival(long result,DeviceInfo newDevice){}
 * _scanApiNotification.onDeviceRemoval(DeviceInfo deviceRemoved){}
 * <li>retrieve the decoded data from a scanner
 * _scanApiNotification.onDecodedData(DeviceInfo device,ISktScanDecodedData
 * decodedData){}
 * <li>once the application is done using ScanAPI, close it using:
 * _scanApi.close();
 * 
 * @author ericg
 * 
 */
public class ScanApiHelper {

	/**
	 * notification coming from ScanApiHelper the application can override for
	 * its own purpose
	 * 
	 * @author ericg
	 * 
	 */
	public interface ScanApiHelperNotification {
		/**
		 * called each time a device connects to the host
		 * 
		 * @param result
		 *            contains the result of the connection
		 * @param newDevice
		 *            contains the device information
		 */
		void onDeviceArrival(long result, DeviceInfo newDevice);

		/**
		 * called each time a device disconnect from the host
		 * 
		 * @param deviceRemoved
		 *            contains the device information
		 */
		void onDeviceRemoval(DeviceInfo deviceRemoved);

		/**
		 * called each time ScanAPI is reporting an error
		 * 
		 * @param result
		 *            contains the error code
		 */
		void onError(long result);

		/**
		 * called each time ScanAPI receives decoded data from scanner
		 * 
		 * @param deviceInfo
		 *            contains the device information from which the data has
		 *            been decoded
		 * @param decodedData
		 *            contains the decoded data information
		 */
		void onDecodedData(DeviceInfo deviceInfo,
				ISktScanDecodedData decodedData);

		/**
		 * called when ScanAPI initialization has been completed
		 * 
		 * @param result
		 *            contains the initialization result
		 */
		void onScanApiInitializeComplete(long result);

		/**
		 * called when ScanAPI has been terminated. This will be the last
		 * message received from ScanAPI
		 */
		void onScanApiTerminated();

		/**
		 * called when an error occurs during the retrieval of a ScanObject from
		 * ScanAPI.
		 * 
		 * @param result
		 *            contains the retrieval error code
		 */
		void onErrorRetrievingScanObject(long result);
	}

	public final int MAX_RETRIES = 5;
	private Vector _commandContexts;
	private ISktScanApi _scanApi;
	private boolean _scanApiOpen;
	private ScanApiHelperNotification _notification;
	private Timer _scanApiConsumer;
	private ISktScanObject[] _scanObjReceived;
	private Vector _devicesList;// maintain a list of connected device (current
								// only one scanner at a time)
	private DeviceInfo _noDeviceConnected;
	private char _dataConfirmationMode = ISktScanProperty.values.confirmationMode.kSktScanDataConfirmationModeDevice;

	public ScanApiHelper() {
		_commandContexts = new Vector();
		_scanApi = SktClassFactory.createScanApiInstance();
		_notification = null;
		_scanObjReceived = new ISktScanObject[1];
		_devicesList = new Vector();
		_noDeviceConnected = new DeviceInfo("", null,
				(long) SktScanDeviceType.kSktScanDeviceTypeNone);
		_scanApiOpen = false;
	}

	/**
	 * register for notifications in order to receive notifications such as
	 * "Device Arrival", "Device Removal", "Decoded Data"...etc...
	 * 
	 * @param notification
	 */
	public void setNotification(ScanApiHelperNotification notification) {
		_notification = notification;
	}

	/**
	 * specifying a name to display when no device is connected will add a no
	 * device connected item in the list with the name specified, otherwise if
	 * there is no device connected the list will be empty.
	 */
	public void setNoDeviceText(String noDeviceText) {
		_noDeviceConnected.setName(noDeviceText);

	}

	/**
	 * update the friendly name in the list
	 */
	public void updateDevice(DeviceInfo newDevice) {
		synchronized (_devicesList) {
			_devicesList.removeAllElements();
			_devicesList.addElement(newDevice);
		}
	}

	/**
	 * get the list of devices. If there is no device connected and a text has
	 * been specified for when there is no device then the list will contain one
	 * item which is the no device in the list
	 * 
	 * @return
	 */
	public Vector getDevicesList() {
		return _devicesList;
	}

	/**
	 * check if there is a device connected
	 * 
	 * @return
	 */
	boolean isDeviceConnected() {
		boolean isDeviceConnected = false;
		synchronized (_devicesList) {
			if (_devicesList.size() > 0) {
				isDeviceConnected = !_devicesList.contains(_noDeviceConnected);
			}
		}
		return isDeviceConnected;
	}

	/**
	 * flag to know if ScanAPI is open
	 * 
	 * @return
	 */
	boolean isScanApiOpen() {
		return _scanApiOpen;
	}

	/**
	 * open ScanAPI and initialize ScanAPI The result of opening ScanAPI is
	 * returned in the callback onScanApiInitializeComplete
	 */
	public void open() {
		// make sure the devices list is empty
		// and if the No Device Connected has a name
		// then add it into the list
		_devicesList.removeAllElements();
		if (_noDeviceConnected.getName().length() > 0) {
			_devicesList.addElement(_noDeviceConnected);
		}

		ScanAPIInitialization init = new ScanAPIInitialization(_scanApi,
				_scanApiInitComplete);
		init.start();
		_scanApiOpen = true;
	}

	/**
	 * close ScanAPI. The callback onScanApiTerminated is invoked as soon as
	 * ScanAPI is completely closed. If a device is connected, a device removal
	 * will be received during the process of closing ScanAPI.
	 */
	public void close() {
		postScanApiAbort(null);
		_scanApiOpen = false;
	}

	/**
	 * remove the pending commands for a specific device or all the pending
	 * commands if null is passed as iDevice parameter
	 * 
	 * @param iDevice
	 *            reference to the device for which the commands must be removed
	 *            from the list or <b>null</b> if all the commands must be
	 *            removed.
	 */
	public void removeCommands(DeviceInfo device) {
		ISktScanDevice iDevice = null;
		
		if (device != null)
			iDevice = device.getSktScanDevice();
		
		// remove all the pending command for this device
		synchronized (_commandContexts) {
			if (iDevice != null) {
				Enumeration enumeration = _commandContexts.elements();
				
				while (enumeration.hasMoreElements()) {
					CommandContext command = (CommandContext) enumeration
							.nextElement();
					if (command.getScanDevice() == iDevice) {
						_commandContexts.removeElement(command);
					}
				}
			} else {
				_commandContexts.removeAllElements();
			}
		}
	}

	/**
	 * postGetScanAPIVersion retrieve the ScanAPI Version
	 */
	public void postGetScanAPIVersion(ICommandContextCallback callback) {
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdVersion);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeNone);
		CommandContext command = new CommandContext(true, newScanObj, _scanApi,
				null, callback);
		addCommand(command);
	}

	/**
	 * postGetScanAPIConfiguration retrieve the ScanAPI Configuration
	 */
	public void postGetScanAPIConfiguration(String configurationName,
			ICommandContextCallback callback) {
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdConfiguration);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeString);
		newScanObj.getProperty().getString().setValue(configurationName);
		CommandContext command = new CommandContext(true, newScanObj, _scanApi,
				null, callback);
		addCommand(command);
	}

	/**
	 * postGetScanAPIConfiguration retrieve the ScanAPI Configuration
	 */
	public void postSetScanAPIConfiguration(String configurationName,
			String value, ICommandContextCallback callback) {
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdConfiguration);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeString);
		newScanObj.getProperty().getString()
				.setValue(configurationName + "=" + value);
		CommandContext command = new CommandContext(false, newScanObj,
				_scanApi, null, callback);
		addCommand(command);
	}

	/**
	 * postSetConfirmationMode Configures ScanAPI so that scanned data must be
	 * confirmed by this application before the scanner can be triggered again.
	 */
	public void postSetConfirmationMode(char mode,
			ICommandContextCallback callback) {
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdDataConfirmationMode);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeByte);
		newScanObj.getProperty().setByte(mode);

		CommandContext command = new CommandContext(false, newScanObj,
				_scanApi, null, callback);
		addCommand(command);
		return;
	}

	/**
	 * postSetDataConfirmation acknowledge the decoded data
	 * <p>
	 * This is only required if the scanner Confirmation Mode is set to
	 * kSktScanDataConfirmationModeApp
	 */
	public void postSetDataConfirmation(DeviceInfo deviceInfo,
			ICommandContextCallback callback) {

		ISktScanDevice device = deviceInfo.getSktScanDevice();
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdDataConfirmationDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeUlong);
		newScanObj
				.getProperty()
				.setUlong(
						SktScan.helper
								.SKTDATACONFIRMATION(
										0,
										ISktScanProperty.values.dataConfirmation.kSktScanDataConfirmationRumbleNone,
										ISktScanProperty.values.dataConfirmation.kSktScanDataConfirmationBeepGood,
										ISktScanProperty.values.dataConfirmation.kSktScanDataConfirmationLedGreen));

		CommandContext command = new CommandContext(false, newScanObj, device,
				null, callback);
		if (_commandContexts.isEmpty() == true)
			addCommand(command);
		else {
			int index = 0;
			CommandContext pendingCommand = (CommandContext) _commandContexts
					.elementAt(index);
			if (pendingCommand.getStatus() == CommandContext.statusNotCompleted)
				_commandContexts.insertElementAt(command, index + 1);
		}

		// try to see if the confirmation can be sent right away
		sendNextCommand();
	}

	/**
	 * postGetBtAddress Creates a TSktScanObject and initializes it to perform a
	 * request for the Bluetooth address in the scanner.
	 */
	public void postGetBtAddress(DeviceInfo deviceInfo,
			ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		// create and initialize the property to send to the device
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdBluetoothAddressDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeNone);

		// add the property and the device to the command context list
		// to send it as soon as it is possible
		CommandContext command = new CommandContext(true, newScanObj, device,
				deviceInfo, callback);
		addCommand(command);
	}

	/**
	 * postGetFirmware Creates a TSktScanObject and initializes it to perform a
	 * request for the firmware revision in the scanner.
	 */
	public void postGetFirmware(DeviceInfo deviceInfo,
			ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		// create and initialize the property to send to the device
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdVersionDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeNone);

		// add the property and the device to the command context list
		// to send it as soon as it is possible
		CommandContext command = new CommandContext(true, newScanObj, device,
				deviceInfo, callback);
		addCommand(command);
	}

	/**
	 * postGetBattery Creates a TSktScanObject and initializes it to perform a
	 * request for the battery level in the scanner.
	 */
	public void postGetBattery(DeviceInfo deviceInfo,
			ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		// create and initialize the property to send to the device
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdBatteryLevelDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeNone);

		// add the property and the device to the command context list
		// to send it as soon as it is possible
		CommandContext command = new CommandContext(true, newScanObj, device,
				deviceInfo, callback);
		addCommand(command);
	}

	/**
	 * postGetDecodeAction
	 * 
	 * Creates a TSktScanObject and initializes it to perform a request for the
	 * Decode Action in the scanner.
	 * 
	 */
	public void postGetDecodeAction(DeviceInfo deviceInfo,
			ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		// create and initialize the property to send to the device
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdLocalDecodeActionDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeNone);

		// add the property and the device to the command context list
		// to send it as soon as it is possible
		CommandContext command = new CommandContext(true, newScanObj, device,
				deviceInfo, callback);
		addCommand(command);

	}

	/**
	 * postGetCapabilitiesDevice
	 * 
	 * Creates a TSktScanObject and initializes it to perform a request for the
	 * Capabilities Device in the scanner.
	 */
	public void postGetCapabilitiesDevice(DeviceInfo deviceInfo,
			ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		// create and initialize the property to send to the device
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdCapabilitiesDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeByte);
		newScanObj
				.getProperty()
				.setByte(
						(char) ISktScanProperty.values.capabilityGroup.kSktScanCapabilityLocalFunctions);

		// add the property and the device to the command context list
		// to send it as soon as it is possible
		CommandContext command = new CommandContext(true, newScanObj, device,
				deviceInfo, callback);
		addCommand(command);
	}

	/**
	 * postGetPostambleDevice
	 * 
	 * Creates a TSktScanObject and initializes it to perform a request for the
	 * Postamble Device in the scanner.
	 * 
	 */
	public void postGetPostambleDevice(DeviceInfo deviceInfo,
			ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		// create and initialize the property to send to the device
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdPostambleDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeNone);

		// add the property and the device to the command context list
		// to send it as soon as it is possible
		CommandContext command = new CommandContext(true, newScanObj, device,
				deviceInfo, callback);
		addCommand(command);
	}

	/**
	 * postGetSymbologyInfo
	 * 
	 * Creates a TSktScanObject and initializes it to perform a request for the
	 * Symbology Info in the scanner.
	 * 
	 */
	public void postGetSymbologyInfo(DeviceInfo deviceInfo, int symbologyId,
			ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		// create and initialize the property to send to the device
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdSymbologyDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeSymbology);
		newScanObj.getProperty().getSymbology()
				.setFlags(ISktScanSymbology.flags.kSktScanSymbologyFlagStatus);
		newScanObj.getProperty().getSymbology().setID(symbologyId);
		// add the property and the device to the command context list
		// to send it as soon as it is possible
		CommandContext command = new CommandContext(true, newScanObj, device,
				deviceInfo, callback);
		addCommand(command);
	}

	/**
	 * postGetAllSymbologyInfo
	 * 
	 * Post a series of get Symbology info in order to retrieve all the
	 * Symbology Info of the scanner. The callback would be called each time a
	 * Get Symbology request has completed
	 */
	public void postGetAllSymbologyInfo(DeviceInfo deviceInfo,
			ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		// create and initialize the property to send to the device
		for (int symbologyId = ISktScanSymbology.id.kSktScanSymbologyNotSpecified + 1; symbologyId < ISktScanSymbology.id.kSktScanSymbologyLastSymbolID; symbologyId++) {
			ISktScanObject newScanObj = SktClassFactory.createScanObject();
			newScanObj.getProperty().setID(
					ISktScanProperty.propId.kSktScanPropIdSymbologyDevice);
			newScanObj.getProperty().setType(
					ISktScanProperty.types.kSktScanPropTypeSymbology);
			newScanObj
					.getProperty()
					.getSymbology()
					.setFlags(
							ISktScanSymbology.flags.kSktScanSymbologyFlagStatus);
			newScanObj.getProperty().getSymbology().setID(symbologyId);
			// add the property and the device to the command context list
			// to send it as soon as it is possible
			CommandContext command = new CommandContext(true, newScanObj,
					device, deviceInfo, callback);
			addCommand(command);
		}
	}

	/**
	 * postSetSymbologyInfo Constructs a request object for setting the
	 * Symbology Info in the scanner
	 * 
	 */
	public void postSetSymbologyInfo(DeviceInfo deviceInfo, int Symbology,
			boolean Status, ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdSymbologyDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeSymbology);
		newScanObj.getProperty().getSymbology()
				.setFlags(ISktScanSymbology.flags.kSktScanSymbologyFlagStatus);
		newScanObj.getProperty().getSymbology().setID(Symbology);
		if (Status)
			newScanObj
					.getProperty()
					.getSymbology()
					.setStatus(
							ISktScanSymbology.status.kSktScanSymbologyStatusEnable);
		else
			newScanObj
					.getProperty()
					.getSymbology()
					.setStatus(
							ISktScanSymbology.status.kSktScanSymbologyStatusDisable);

		CommandContext command = new CommandContext(false, newScanObj, device,
				null, callback);
		command.setSymbologyId(Symbology);// keep the symbology ID because the
											// Set Complete won't return it
		addCommand(command);
	}

	/**
	 * postGetFriendlyName
	 * 
	 * Creates a TSktScanObject and initializes it to perform a request for the
	 * friendly name in the scanner.
	 * 
	 */

	public void postGetFriendlyName(DeviceInfo deviceInfo,
			ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		// create and initialize the property to send to the device
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdFriendlyNameDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeNone);
		// add the property and the device to the command context list
		// to send it as soon as it is possible
		CommandContext command = new CommandContext(true, newScanObj, device,
				deviceInfo, callback);
		addCommand(command);
	}

	/**
	 * postSetFriendlyName Constructs a request object for setting the Friendly
	 * Name in the scanner
	 * 
	 */
	public void postSetFriendlyName(String friendlyName, DeviceInfo deviceInfo,
			ICommandContextCallback callback) {
		ISktScanDevice device = deviceInfo.getSktScanDevice();
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdFriendlyNameDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeString);
		newScanObj.getProperty().getString().setValue(friendlyName);
		CommandContext command = new CommandContext(false, newScanObj, device,
				null, callback);
		addCommand(command);
	}

	/**
	 * postSetDecodeAction
	 * 
	 * Configure the local decode action of the device
	 * 
	 * @param device
	 * @param decodeVal
	 */
	public void postSetDecodeAction(DeviceInfo device, int decodeVal,
			ICommandContextCallback callback) {
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdLocalDecodeActionDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeByte);
		newScanObj.getProperty().setByte((char) (decodeVal & 0xffff));

		CommandContext command = new CommandContext(false, newScanObj,
				device.getSktScanDevice(), null, callback);
		addCommand(command);
	}

	/**
	 * postSetPostamble
	 * 
	 * Configure the postamble of the device
	 * 
	 * @param device
	 * @param suffix
	 */
	public void postSetPostamble(DeviceInfo device, String suffix,
			ICommandContextCallback callback) {
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdPostambleDevice);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeString);
		newScanObj.getProperty().getString().setValue(suffix);

		CommandContext command = new CommandContext(false, newScanObj,
				device.getSktScanDevice(), null, callback);
		addCommand(command);
	}

	/**
	 * postSetProfileConfigDevice
	 * 
	 * Set the Profile Config of the Device
	 * 
	 * @param device
	 * @param hostddress
	 *            should be in the form 112233445566 without colons.
	 */
	public void postSetProfileConfigDevice(DeviceInfo device,
			String hostAddress, ICommandContextCallback callback) {
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdProfileConfigDevice);
		newScanObj
				.getProperty()
				.setType(
						SktScan.helper
								.SKTRETRIEVESETTYPE(ISktScanProperty.propId.kSktScanPropIdProfileConfigDevice));

		char[] valuesProfile = new char[values.profileConfig.kProfileConfigSize];

		valuesProfile[0] = ((values.profile.kSktScanProfileSelectSpp >> 8) & 0xff);
		valuesProfile[1] = (values.profile.kSktScanProfileSelectSpp & 0xff);
		valuesProfile[2] = ((values.profileConfig.kSktScanProfileConfigInitiator >> 8) & 0xff);
		valuesProfile[3] = (values.profileConfig.kSktScanProfileConfigInitiator & 0xff);
		ConvertStringtoCharArray(valuesProfile, 4, hostAddress);

		char[] cod = new char[3];
		cod[0] = 0x00;
		cod[1] = 0x1F;
		cod[2] = 0x00;

		for (int i = 0; i < cod.length; i++)
			valuesProfile[10 + i] = cod[i];

		newScanObj.getProperty().getArray()
				.setValue(valuesProfile, valuesProfile.length);

		CommandContext command = new CommandContext(false, newScanObj,
				device.getSktScanDevice(), device, callback);
		addCommand(command);
	}

	/**
	 * postSetDisconnectDevice
	 * 
	 * Disconnect the device
	 * 
	 * @param device
	 * @param suffix
	 */
	public void postSetDisconnectDevice(DeviceInfo device,
			ICommandContextCallback callback) {
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdDisconnectDevice);
		newScanObj
				.getProperty()
				.setType(
						SktScan.helper
								.SKTRETRIEVESETTYPE(ISktScanProperty.propId.kSktScanPropIdDisconnectDevice));
		newScanObj
				.getProperty()
				.setByte(
						(char) ISktScanProperty.values.disconnect.kSktScanDisconnectStartProfile);

		CommandContext command = new CommandContext(false, newScanObj,
				device.getSktScanDevice(), device, callback);
		addCommand(command);
	}

	/**
	 * postScanApiAbort
	 * 
	 * Request ScanAPI to shutdown. If there is some devices connected we will
	 * receive Remove event for each of them, and once all the outstanding
	 * devices are closed, then ScanAPI will send a Terminate event upon which
	 * we can close this application. If the ScanAPI Abort command failed, then
	 * the callback will close ScanAPI
	 */
	public void postScanApiAbort(ICommandContextCallback callback) {
		// create and initialize the property to send to the device
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdAbort);
		newScanObj.getProperty().setType(
				ISktScanProperty.types.kSktScanPropTypeNone);

		CommandContext command = new CommandContext(false, newScanObj,
				_scanApi, null, callback);
		addCommand(command);
	}

	/**
	 * postGetTimersDevice
	 * 
	 * retrieve the timers of the device
	 * 
	 * @param device
	 *            device info to which the timers has to be retrieved
	 * @param callback
	 *            callback when the Get Complete is received
	 */
	public void postGetTimersDevice(DeviceInfo device,
			ICommandContextCallback callback) {
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdTimersDevice);
		newScanObj
				.getProperty()
				.setType(
						SktScan.helper
								.SKTRETRIEVEGETTYPE(ISktScanProperty.propId.kSktScanPropIdTimersDevice));

		CommandContext command = new CommandContext(true, newScanObj,
				device.getSktScanDevice(), device, callback);
		addCommand(command);
	}

	/**
	 * postSetTimersDevice
	 * 
	 * modify the timers of the device
	 * 
	 * @param device
	 *            device info to which the timers has to be modified
	 * @param timerMask
	 *            mask of the timer to change
	 * @param lockOutTimer
	 *            Trigger Lock Out timer value
	 * @param disconnectedAutoOff
	 *            Disconnected Auto power off
	 * @param connectedAutoOff
	 *            Connected Auto power off
	 * @param callback
	 *            callback when the Set Complete is received
	 */
	public void postSetTimersDevice(DeviceInfo device, int timerMask,
			int lockOutTimer, int disconnectedAutoOff, int connectedAutoOff,
			ICommandContextCallback callback) {
		ISktScanObject newScanObj = SktClassFactory.createScanObject();
		newScanObj.getProperty().setID(
				ISktScanProperty.propId.kSktScanPropIdTimersDevice);
		newScanObj
				.getProperty()
				.setType(
						SktScan.helper
								.SKTRETRIEVESETTYPE(ISktScanProperty.propId.kSktScanPropIdTimersDevice));

		char[] deviceTimers = new char[4 * 2];// mask+lockout+disconnect+connected=8
												// bytes
		deviceTimers[0] = (char) (timerMask >> 8);
		deviceTimers[1] = (char) (timerMask);
		deviceTimers[2] = (char) (lockOutTimer >> 8);
		deviceTimers[3] = (char) (lockOutTimer);
		deviceTimers[4] = (char) (disconnectedAutoOff >> 8);
		deviceTimers[5] = (char) (disconnectedAutoOff);
		deviceTimers[6] = (char) (connectedAutoOff >> 8);
		deviceTimers[7] = (char) (connectedAutoOff);
		newScanObj.getProperty().getArray()
				.setValue(deviceTimers, deviceTimers.length);
		CommandContext command = new CommandContext(false, newScanObj,
				device.getSktScanDevice(), device, callback);
		addCommand(command);
	}

	/**
	 * @param destination
	 * @param destinationOffset
	 * @param source
	 */
	private void ConvertStringtoCharArray(char[] destination,
			int destinationOffset, String source) {
		int count = source.length();
		int index = 0;
		for (int i = 0; i < count / 2; i++) {
			destination[i + destinationOffset] = ConvertToByte(source
					.substring(index, index + 2));
			index += 2;
		}
	}

	/**
	 * @param substring
	 * @return
	 */
	private char ConvertToByte(String substring) {
		char value = 0;
		for (int i = 0; i < 2; i++) {
			value <<= 4;
			if ((substring.charAt(i) >= '0') && (substring.charAt(i) <= '9')) {
				value += substring.charAt(i) - '0';
			} else if ((substring.charAt(i) >= 'A')
					&& (substring.charAt(i) <= 'F')) {
				value += substring.charAt(i) - 'A';
				value += 10;
			} else if ((substring.charAt(i) >= 'a')
					&& (substring.charAt(i) <= 'f')) {
				value += substring.charAt(i) - 'a';
				value += 10;
			}
		}
		return value;
	}

	/**
	 * ScanAPI Init Complete callback
	 * <p>
	 * this callback is called when ScanAPI is opened. If the open is successful
	 * a timer task is created and used to consume ScanObject from ScanAPI. This
	 * timer task will end during ScanAPI close process once it receives the
	 * ScanAPI Terminate event.
	 */
	private ScanAPIInitialization.ICallback _scanApiInitComplete = new ScanAPIInitialization.ICallback() {

		public void completed(long result) {
			if (_notification != null)
				_notification.onScanApiInitializeComplete(result);
			if (SktScanErrors.SKTSUCCESS(result)) {
				_scanApiConsumer = new Timer();
				_scanApiConsumer.schedule(new TimerTask() {

					public void run() {
						boolean closeScanApi = false;
						long result = _scanApi.WaitForScanObject(
								_scanObjReceived, 1);
						if (SktScanErrors.SKTSUCCESS(result)) {
							if (result != SktScanErrors.ESKT_WAITTIMEOUT) {
								closeScanApi = handleScanObject(_scanObjReceived[0]);
								_scanApi.ReleaseScanObject(_scanObjReceived[0]);
							}
							if (closeScanApi == false) {
								// if there is a command to send
								// now might be a good time
								sendNextCommand();
							} else {
								Debug.MSG(Debug.kLevelTrace,
										"About to close ScanAPI");
								_scanApi.Close();
								Debug.MSG(Debug.kLevelTrace,
										"ScanAPI close, about to kill the consummer task");
								_scanApiConsumer.cancel();
								Debug.MSG(Debug.kLevelTrace,
										"Consummer task killed");
								if (_notification != null)
									_notification.onScanApiTerminated();
							}
						} else {

							Debug.MSG(Debug.kLevelTrace,
									"About to close ScanAPI");
							_scanApi.Close();
							Debug.MSG(Debug.kLevelTrace,
									"ScanAPI close, about to kill the consummer task");
							_scanApiConsumer.cancel();
							if (_notification != null) {
								_notification
										.onErrorRetrievingScanObject(result);
								_notification.onScanApiTerminated();
							}
						}
					}
				}, 1, 200);

				// set the decoded data confirmation mode of the device
				// the data confirmation mode can be:
				// kSktScanDataConfirmationModeDevice: the device acks decoded
				// data locally
				// kSktScanDataConfirmationModeScanApi: ScanAPI acks the decoded
				// data upon reception
				// kSktScanDataConfirmationModeApp: this app has to ack the
				// decoded data
				postSetConfirmationMode(_dataConfirmationMode, null);
			}
		}
	};

	/**
	 * doGetOrSetComplete "Get Complete" events arrive asynchonously via code in
	 * the timer handler of the Scanner List dialog. Even though they may arrive
	 * asynchonously, they only arrive as the result of a successful
	 * corresponding "Get" request.
	 * 
	 * This function examines the get complete event given in the pScanObj arg,
	 * and dispatches it to the correct handler depending on the Property ID it
	 * contains.
	 * 
	 * Each property handler must return ESKT_NOERROR if it has successfully
	 * performed its processing.
	 */
	private long doGetOrSetComplete(ISktScanObject scanObj) {
		long result = SktScanErrors.ESKT_NOERROR;
		boolean remove = true;
		boolean doCallback = true;
		if (scanObj != null) {
			result = scanObj.getMessage().getResult();
			CommandContext command = (CommandContext) scanObj.getProperty()
					.getContext();
			Debug.MSG(Debug.kLevelTrace, "Complete event received for Context:"
					+ command + "\n");
			if (command != null) {
				if (!SktScanErrors.SKTSUCCESS(result)) {
					if (command.getRetries() >= MAX_RETRIES) {
						remove = true;
					} else {
						remove = false;// don't remove the command for a retry
						doCallback = false;// don't call the callback for a
											// silent retry
						result = SktScanErrors.ESKT_NOERROR;
					}
				}

				if (doCallback)
					command.doCallback(scanObj);

				if (remove == true) {
					synchronized (_commandContexts) {
						Debug.MSG(Debug.kLevelTrace,
								"Remove command from the list\n");
						_commandContexts.removeElement(command);
					}
				} else {
					command.setStatus(CommandContext.statusReady);
				}
			}
			if (SktScanErrors.SKTSUCCESS(result)) {
				result = sendNextCommand();
			}
		}
		return result;
	}

	/**
	 * sendNextCommand This method checks if there is a command ready to be sent
	 * at the top of the list.
	 */
	private long sendNextCommand() {
		long result = SktScanErrors.ESKT_NOERROR;

		synchronized (_commandContexts) {
			if (_commandContexts.isEmpty() == false) {
				Debug.MSG(Debug.kLevelTrace,
						"There are some commands to send\n");
				CommandContext command = (CommandContext) _commandContexts
						.firstElement();
				Debug.MSG(Debug.kLevelTrace, "And this one has status="
						+ command.getStatus() + " for command: "
						+ command.getScanObject().getProperty().getID());
				if (command.getStatus() == CommandContext.statusReady) {
					result = command.DoGetOrSetProperty();
					if (!SktScanErrors.SKTSUCCESS(result)) {
						_commandContexts.removeElement(command);
						// case where the command is not supported by the device
						// we can ignore it
						if (result == SktScanErrors.ESKT_NOTSUPPORTED) {
							Debug.MSG(Debug.kLevelWarning,
									"Remove an unsupported command\n");
						}
						// case where the device handle is invalid (propably
						// disconnected)
						// we can ignore it
						else if (result == SktScanErrors.ESKT_INVALIDHANDLE) {
							Debug.MSG(Debug.kLevelWarning,
									"Remove a command with an invalid handle\n");
						}
					}
				}
			}
		}
		return result;
	}

	private void addCommand(CommandContext newCommand) {
		synchronized (_commandContexts) {
			if (newCommand.getScanObject().getProperty().getID() == ISktScanProperty.propId.kSktScanPropIdAbort) {
				Debug.MSG(Debug.kLevelTrace,
						"About to Add a ScanAPI Abort command so remove all previous commands");
				_commandContexts.removeAllElements();
			}
			_commandContexts.addElement(newCommand);
			Debug.MSG(Debug.kLevelTrace, "Add a new command to send");
		}
	}

	/**
	 * handleScanObject This method is called each time this application
	 * receives a ScanObject from ScanAPI. It returns true is the caller can
	 * safely close ScanAPI and terminate its ScanAPI consumer.
	 */
	protected boolean handleScanObject(ISktScanObject scanObject) {
		boolean closeScanApi = false;
		switch (scanObject.getMessage().getID()) {
		case ISktScanMsg.kSktScanMsgIdDeviceArrival:
			handleDeviceArrival(scanObject);
			break;
		case ISktScanMsg.kSktScanMsgIdDeviceRemoval:
			handleDeviceRemoval(scanObject);
			break;
		case ISktScanMsg.kSktScanMsgGetComplete:
		case ISktScanMsg.kSktScanMsgSetComplete:
			doGetOrSetComplete(scanObject);
			break;
		case ISktScanMsg.kSktScanMsgIdTerminate:
			Debug.MSG(Debug.kLevelTrace,
					"Receive a Terminate event, ask to close ScanAPI");
			closeScanApi = true;
			break;
		case ISktScanMsg.kSktScanMsgEvent:
			handleEvent(scanObject);
			break;
		}
		return closeScanApi;
	}

	/**
	 * handleDeviceArrival This method is called each time a device connect to
	 * the host.
	 * 
	 * We create a device info object to hold all the necessary information
	 * about this device, including its interface which is used as handle
	 */
	private void handleDeviceArrival(ISktScanObject scanObject) {

		String friendlyName = scanObject.getMessage().getDeviceName();
		String deviceGuid = scanObject.getMessage().getDeviceGuid();
		long type = scanObject.getMessage().getDeviceType();
		ISktScanDevice device = SktClassFactory.createDeviceInstance(_scanApi);
		DeviceInfo newDevice = null;
		long result = device.Open(deviceGuid);
		if (SktScanErrors.SKTSUCCESS(result)) {
			// add the new device into the list
			newDevice = new DeviceInfo(friendlyName, device, type);
			synchronized (_devicesList) {
				_devicesList.addElement(newDevice);
				_devicesList.removeElement(_noDeviceConnected);
			}
		}
		if (_notification != null)
			_notification.onDeviceArrival(result, newDevice);
	}

	/**
	 * handleDeviceRemoval This method is called each time a device is
	 * disconnected from the host. Usually this will be a good opportunity to
	 * close the device
	 */
	private void handleDeviceRemoval(ISktScanObject scanObject) {
		ISktScanDevice iDevice = scanObject.getMessage().getDeviceInterface();
		DeviceInfo deviceFound = null;
		synchronized (_devicesList) {
			Enumeration enumerator = _devicesList.elements();
			while (enumerator.hasMoreElements()) {
				DeviceInfo device = (DeviceInfo) enumerator.nextElement();
				if (device.getSktScanDevice() == iDevice) {
					deviceFound = device;
					break;
				}
			}

			// let's notify whatever UI we might have
			if (deviceFound != null) {
				removeCommands(deviceFound);
				_devicesList.removeElement(deviceFound);
				if (_devicesList.isEmpty()) {
					if (_noDeviceConnected.getName().length() > 0)
						_devicesList.addElement(_noDeviceConnected);
				}
				if (_notification != null)
					_notification.onDeviceRemoval(deviceFound);
			}
		}
		iDevice.Close();

	}

	/**
	 * handleEvent
	 * 
	 * This method handles asynchronous events coming from ScanAPI including
	 * decoded data
	 */
	private void handleEvent(ISktScanObject scanObject) {
		ISktScanEvent event = scanObject.getMessage().getEvent();
		ISktScanDevice iDevice = scanObject.getMessage().getDeviceInterface();
		switch (event.getID()) {
		case ISktScanEvent.id.kSktScanEventError:
			if (_notification != null) {
				_notification.onError(scanObject.getMessage().getResult());
			}
			break;
		case ISktScanEvent.id.kSktScanEventDecodedData:
			ISktScanDecodedData decodedData = event.getDataDecodedData();
			DeviceInfo deviceInfo = getDeviceInfo(iDevice);
			if (_notification != null) {
				_notification.onDecodedData(deviceInfo, decodedData);
			}

			// if the Data Confirmation mode is set to App
			// then confirm Data here
			if (_dataConfirmationMode == ISktScanProperty.values.confirmationMode.kSktScanDataConfirmationModeApp) {
				postSetDataConfirmation(deviceInfo, null);

			}
			break;
		case ISktScanEvent.id.kSktScanEventPower:
			break;
		case ISktScanEvent.id.kSktScanEventButtons:
			break;
		}
	}

	/**
	 * retrieve the deviceInfo object matching to its ISktScanDevice interface
	 * 
	 * @param iDevice
	 *            ScanAPI device interface
	 * @return a deviceInfo object if it finds a matching device interface null
	 *         otherwise
	 */
	private DeviceInfo getDeviceInfo(ISktScanDevice iDevice) {
		DeviceInfo deviceInfo = null;
		boolean found = false;
		if (iDevice != null) {
			synchronized (_devicesList) {
				Enumeration enumerator = _devicesList.elements();
				while (enumerator.hasMoreElements()) {
					deviceInfo = (DeviceInfo) enumerator.nextElement();
					if (deviceInfo.getSktScanDevice() == iDevice) {
						found = true;
						break;
					}
				}
				if (found == false)
					deviceInfo = null;
			}
		}
		return deviceInfo;
	}

}
