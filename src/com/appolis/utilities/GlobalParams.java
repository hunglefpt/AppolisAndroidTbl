/**
 * Name: $RCSfile: GlobalParams.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.utilities;

/**
 * 
 * @author hoangnh11
 * Declare global variables
 */
public final class GlobalParams {
	public static final String SETTING_MESSAGE_APP_VERSION = "App Version: ";
	public static final String COMMA_SPACE_SEPARATOR = ", ";
	public static final String SETTING_MESSAGE_DEVICE_MODEL = "Device Model: ";
	public static final String SETTING_MESSAGE_SYSTEM_NAME = "OS Name: ";
	public static final String SETTING_MESSAGE_SYSTEM_VERSION = "OS Version: ";
	public static final String BLANK_CHARACTER = "";
	public static final String LANGUAGE_MODE_DEFAULT = "en";
	public static final int SWIPE_MIN_DISTANCE = 120;
	public static final int SWIPE_MAX_OFF_PATH = 250;
	public static final int SWIPE_THRESHOLD_VELOCITY = 200;
	public static final long DOUBLE_CLICK_TIME_DELTA = 500;//milliseconds
	public static final int TOAT_TIME = 4000;
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String Authorization = "Authorization";
	public static final String PIN = "9999";
	public static final String CONFIGURE = "configure";
	public static final String WAREHOUSE = "warehouse";
	public static final String BASIC = "Basic ";
	public static final String VERTICAL_TWO_DOT = ":";
	public static final String DOT = ".";
	public static final String WAREHOUSENAME = "warehouseName";
	public static final String CULTUREINFO = "cultureInfo";
	public static final String UTF_8 = "utf-8";
	public static final String dateFormatYYYY_MM_DD_T_HHMMSS = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String dateFormatYYYY_MM_DD = "yyyy-MM-dd";
	public static final String dateFormatMM_DD_YYYY = "MM-dd-yyyy";
	
	/*set min qty*/
	public static final int MIN_QTY = 1;	
	
	public static final String URL_ORIGINAL = "http://demo.appolis.com/ioswebapi/";
	public static final String URL_GET_USER = "http://demo.appolis.com/ioswebapi/api/User";
	 
	/*text for display*/
	public static final	String TAG = "Apolis";
	public static final String LOADING = "Authenticating...";
	public static final String LOADING_DATA = "Loading";
	public static final String CHECKING_DATA = "Cheking data...";
	public static final String PROCESS_DATA = "Procesing...";
	public static final String BLANK = "\"\"";
	public static final String BLANK_CONTENT ="";
	public static final String WRONG_USER = "Invalid User. Please try again.";
	public static final String WRONG_PIN = "Invalid Pin. Please try again.";
	public static final String offline_warnMsg = "offline_warnMsg";
	public static final String offlinenetwork = "The Internet connection appears to be offline. Please try again.";
	public static final String WARMING = "Warning";
	public static final String LOG_OUT = "Log out";
	public static final String YES = "YES";
	public static final String NO = "NO";
	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";
	public static final String UN_DO = "Undo";
	public static final String YES_TEXT = "Yes";
	public static final String NO_TEXT = "No";
	public static final String FLAG_ACTIVE = "FLAG ACTIVE";
	public static final String FLAG_INACTIVE = "FLAG INACTIVE";
	public static final String ERRORBINNOTFOUND_KEY = "ErrorBinNotFound";
	public static final String ERRORBINNOTFOUND_VALUE = "Bin/License Plate not found, please re-scan";
	public static final String MESSAGE_SELECT = "You must select one item to move";
	public static final String MESSAGE_CONFIRM_ZEROCOUNT= "Are you sure you want to zero count 1%?";
	public static final String MESSAGE_SELECT_LP_OR_BIN = "You must select one LP or Bin to move";
	public static final String MESSAGE_SCAN_LOCATION_KEY = "cycle_findmulti_itembarcode";
	public static final String MESSAGE_SCAN_LOCATION_VALUE = "Found more than one item barcode {0}. Please scan Lot number.";
	public static final String MESSAGE_SCAN_LP_OR_ITEM_VALUE = "Scan a valid Item or LP on the Cycle Count";
	public static final String MESSAGE_SCAN_LP_OR_ITEM_KEY = "cycle_mes_scan_invalid";
	public static final String MESSAGE_SCAN_ITEM = "Please scan a valid Item Barcode";
	public static final String CREATELICENSEPLATE_CREATIONFAILURE_KEY = "CreateLicensePlate_CreationFailure";
	public static final String CREATELICENSEPLATE_CREATIONFAILURE_VALUE = "Error occurred while creating License Plate. Please try again.";
	public static final String GET_DATA_FAIL = "Get data fail";
	public static final String PROCESS_DATA_FAIL = "Process data fail";
	public static final String RECEIVING_IN_PROCESS = "Receiving in Process";
	public static final String ERROR_CREATE_LP = "Error Creating LP";
	public static final String ERROR_SCAN_LP_VALUE = "Please Scan Location";
	public static final String ERROR_SCAN_LP_KEY = "Bin_lblMessageScanLocation";
	public static final String ERROR_SCAN_LOT = "Please scan Lot";
	public static final String MESSAGE_PRUMPT_CREATE_BIN_OR_LP_VALUE = "This location {0} is not found, do you want to create a new license plate?";
	public static final String MESSAGE_PRUMPT_CREATE_BIN_OR_LP_KEY = "mv_locationNotfound_msg";
	
	/* param to transfer via activity in here*/
	public static final String PARAM_MESSAGES_LOGIN = "PARAM_MESSAGES_LOGIN";
	public static final String PARAM_USER_LOGIN = "PARAM_USER_LOGIN";
	public static final String PARAM_PIN_LOGIN = "PARAM_PIN_LOGIN";
	public static final String PARAM_ID_JOB = "PARAM_ID_JOB" ;
	public static final String PARAM_PRINT_JOB = "PARAM_PRINT_JOB" ;
	public static final String PARAM_JOB = "JOB";
	public static final String SCAN_RESULT = "SCAN_RESULT";
	public static final String MOVEMENT_TYPE = "Movement_type";
	public static final String STRING_DATA = "string data";
	public static final String JOB_DETAIL = "job detail";
	public static final String JOB_ITEM_DETAIL = "job item detail";
	public static final String LIST_LP_DETAIL ="list Lp Detail";
	public static final String PARAM_JOB_ITEM_DETAIL_MOVE_LP_DETAIL = "PARAM_JOB_ITEM_DETAIL_MOVE_LP_DETAIL";
	public static final String PARAM_JOB_ITEM_DETAIL_MOVE_ORDER_DETAIL = "PARAM_JOB_ITEM_DETAIL_MOVE_ORDER_DETAIL";
	public static final String ORDER_CONTAINER_DETAIL_ID =  "ORDER_CONTAINER_DETAIL_ID";
	public static final String ORDER_CONTAINER_ID =  "ORDER_CONTAINER_ID";
	public static final String PARAM_BIN_INFO =  "PARAM_BIN_INFO";
	public static final String PARAM_POSITION =  "PARAM_POSITION";
	public static final String PARAM_ORDER_PERCENT_COMPLETE =  "PARAM_ORDER_PERCENT_COMPLETE";
	public static final String PARAM_EN_RECIVING_INFO =  "PARAM_EN_RECIVING_INFO";
	public static final String PARAM_EN_RECIVING_INFO_PO_NUMBER =  "PARAM_EN_RECIVING_INFO_PO_NUMBER";
	public static final String PARAM_EN_PURCHASE_ORDER_ITEM_INFOS =  "PARAM_EN_PURCHASE_ORDER_ITEM_INFOS";
	public static final String PARAM_EN_PURCHASE_ORDER_RECEIPT_INFO =  "PARAM_EN_PURCHASE_ORDER_RECEIPT_INFO";
	public static final String PARAM_LIST_EN_PURCHASE_ORDER_RECEIPT_INFO =  "PARAM_LIST_EN_PURCHASE_ORDER_RECEIPT_INFO";
	public static final String PARAM_EN_PURCHASE_ORDER_INFOS =  "PARAM_EN_PURCHASE_ORDER_INFOS";
	public static final String PARAM_EN_ITEM_LOT_INFO =  "PARAM_EN_ITEM_LOT_INFO";
	
	/*code for activity when user startActivityForResult*/
	public static final int AC_MAIN = 1;
	public static final int SCAN_SOCKET = 111;
	public static final int AC_RECEIVER_INVENTORY_LEVEL_ONE = 151;
	public static final int AC_MOVE_LEVEL_ONE = 31;
	public static final int AC_MOVE_LEVEL_TWO = 32;
	public static final int FIRSTLOGINSCREEN_TO_LOGINSCREEN = 9;
	public static final int LOGINSCREEN_TO_MAINSCREEN = 10;
	public static final int ADDJOBPART_TO_JOBDETAILSCREEN = 11;
	public static final int JOBDETAILSCREEN = 12;
	public static final int PRINT_JOB_ACTIVITY = 23;
	public static final int SCAN = 90;
	public static final int ERROR = 13;
	public static final int LIST_JOB = 14;
	public static final int POST_DATA_SUCCESS = 15;
	public static final int MOVE_SCEEN = 16;
	public static final int JOB_PART_DETAIL_SCREEN = 17;
	public static final int BIN_INFO_RESULT = 18;
	public static final int SHORT_SUCESSFULL =  19;
	public static final int RETURN_PARTS_ITEM_ACTIVITY = 20;
	public static final int RETURN_PARTS_LIST_ACTIVITY = 21;
	public static final int ADD_PARTS_SCREEN = 22;
	public static final int EXIT_APP = 1;
	public static final int LOGOUT_APP = 2;
	public static final int CAPTURE_BARCODE_CAMERA_ACTIVITY = 23;
	public static final int CAPTURE_BARCODE_CAMERA_ACTIVITY_WITH_LOT = 29;
	public static final int AC_RECEIVING_DETAILS_ACTIVITY = 24;	
	public static final int AC_CYCLE_OPTION_DETAIL = 30;	
	public static final int AC_RECEIVING_ITEM_DETAILS_ACTIVITY = 25;
	public static final int AC_CYCLE_COUNT_ADJUSMENT_ACTIVITY = 25;
	public static final int AC_CYCLE_COUNT_LOCATION_ACTIVITY = 26;
	public static final int AC_RECEIVE_OPTION_DAMAGE_ACTIVITY = 27;
	public static final int AC_CYCLE_COUNT_BINPATH_ACTIVITY = 28;
	public static final int AC_RECEIVE_OPTION_MOVE_ACTIVITY = 29;
	public static final int AC_PUT_AWAY_LEVEL_ONE = 11;
	public static final int AC_PUT_AWAY_LEVEL_TWO = 12;
	public static final int AC_PUT_AWAY_BIN_LEVEL_ONE = 11;
	public static final int AC_RECEIVE_ACQUIRE_BARCODE = 31;
	
	/*item for main screen*/	
	public static final String RECEIVE = "Receive";
	public static final String PUT_AWAY = "Put Away";
	public static final String PICK = "Pick";
	public static final String ADDPARTS = "AddParts";
	public static final String LP = "LP";
	public static final String LOC = "Loc";
	public static final String INVENTORY_ADJUSTMENT = "Inventory Adjustment";
	public static final String RETURN_PARTS = "Return Parts";
	public static final String INVENTORY_HISTORY = "Inventory History";
	public static final String JOBS = "Jobs";
	public static final String MANAGE_PARTS = "Manage Parts & Supplies";
	public static final String LP_OPERATIONS = "LP Operations";
	public static final String CYCLE_COUNT = "Cycle Count";
	public static final String CURRENT_LOCATION_KEY = "current_location_key";
	public static final String CURRENT_LOCATION_VALUE = "Current Location"; 
	public static final String MOVE = "Move";
	public static final String SHIP = "Ship";
	public static final String STAGE = "Stage";
	public static final String PROMPT = "Prompt";
	public static final String REPLENISH = "Replenish";
	public static final String LOGOUT = "Logout";
	public static final String PROFILE = "Profile";
	public static final String RECEIVE_INVENSTORY = "Receive Inventory";
	public static final String CREATE_BINS = "Create Bins";
	public static final String CREATE_ITEMS = "Create Items";
	
	public static final String[] NAME_ITEM = {RECEIVE,PUT_AWAY,PICK,MOVE,INVENTORY_ADJUSTMENT,CYCLE_COUNT,LP_OPERATIONS,
		SHIP,LOGOUT,REPLENISH,PROFILE,RETURN_PARTS,INVENTORY_HISTORY,JOBS,MANAGE_PARTS,RECEIVE_INVENSTORY,
		CREATE_BINS,CREATE_ITEMS};
	public static final String NEW_LINE = "\n";
	public static final String ERROR_TIME_OUT_NETWORK = "Internet connection issue - Socket time out. Please try gain or contact Appolis Administrator";
	public static final String ERROR_INVALID_NETWORK = "Cannot connect to Appolis server. Please check your internet connection settings";
	public static final int MAX_BUFFER_SIZE = 1024;
	public static final int LENGTH_PROGRESS_BAR_COLLEAGUE_MAP = 51200;
	public static final double LENGTH_PROGESS_BAR_BEFORE = 10240.00;
	public static final double LENGTH_PROGESS_BAR_WEB_SERVICE = 40960.00;
	
	// public static final String DATA_NOT_SUPPORT = "DATA NOT SUPPORT.";
	public static final String PIN_BLANK =  "Please enter PIN to continue.";
	public static final String SITE_BLANK =  "Please enter site to continue.";
	public static final String USERNAME_BLANK =  "Please enter user name to continue.";
	public static final String PL_DETAIL = "pl detail";
	public static final String MAIN = "Main";
	public static final String DATA_ERROR = "Data error. Please try again.";
	public static final String OBJECT_BIN = "object bin";
	public static final String JOB = "Job";
	public static final String ITEM = "Item";
	public static final String LOT = "Lot";
	public static final String JOB_DETAIL_COMPLETE = "job detail complete";
	public static final String SCAN_BARCODE_NOT_MATCH_CURRENT = "Scanned Barcode does not match current order or is already complete.";
	public static final String INVALID_RETURN_SCAN = "ReturnPartMain_aler_invalidReturn";
	public static final String INVALID_SCAN = "Invalid scan, please try again";
	public static final String INVALID_LOT = "Lot number not valid for this item";
	public static final String PD_SHIPIMMEDIATELY_MSG_TEXT = "Would you like to ship this immediately?";
	public static final String ADDJOBPART = "add job part";
	public static final String NUMBER_ORDER = "number order";
	public static final String NAME_CUSTOMER = "name customer";
	public static final String LPRETURNPART_LBL_RETURNTITLE_TEXT = "Return #";
	public static final String TEXT_RECIVER_DETAIL = "Receive Detail";
	public static final String DMG_LBL_RECEIVEREQUEST_TEXT = "Receive Request";
	public static final String JOBPART_VALIDATE_ITEM_OR_LP_VALUE = "Please scan Item or License Plate to continue.";
	public static final String SCAN_BARCODE_UNSUPPORTED_VALUE = "Unsupported Barcode scanned. Please try again.";
	public static final String DMG_TITLE_DAMAGE_VALUE = "Damage";
	public static final String ERROR_OCCURRED_VALUE = "Error Occurred";
	public static final String RD_TXT_LOTTRACKEDIND_VALUE = "Lot Tracked";
	public static final String UNIT_OF_MEASURE_VALUE = "UOM";
	public static final String PICK_TXT_SEARCH_VALUE = "Scan or Select";
	public static final String RD_TITLE_RECEIVEDETAIL_VALUE = "Receive Detail";
	public static final String ACQUIRE_BARCODE_VALUE = "Acquire Barcode";
	public static final String SCAN_OR_ENTER_BARCODE_VALUE = "Scan or Enter Barcode";
	public static final String RE_LBL_RECEIVE1_VALUE = "Receive";
	public static final String ADD_VALUE = "Add";
	public static final String ITEM_VALUE = "Item";
	public static final String SETTINGS_BTN_CANCEL_VALUE = "Cancel";
	public static final String RE_PO_EMPTY_MSG_VALUE = "PO list empty";
	public static final String DMG_LBL_LOCATION_VALUE = "Location";
	public static final String DMG_LBL_MAXQTY_VALUE = "Max Qty";
	public static final String REST_GRD_LOT_VALUE = "Lot";
	public static final String RID_LBL_LEFT_VALUE = "Left";
	public static final String RID_LBL_RECEIVED_VALUE = "Recv";
	public static final String PID_BTN_OPTIONS_VALUE = "Options";
	public static final String PID_GRD_LOT_VALUE = "Lot #";
	public static final String PID_GRD_QTY_VALUE = "Qty";
	public static final String RD_INVALID_BARCODE_MSG_VALUE = "You must scan a valid Item barcode to continue. Please try again.";
	public static final String MV_MSG_SELECTORSCAN_VALUE = "Select or Scan Location to Move To";
	public static final String CYCLE_CURRENT_COUNT_KEY = "cycle_current_count";
	public static final String CYCLE_CURRENT_COUNT_VALUE = "Current Count";
	public static final String CYCLE_CHANGE_COUNT_KEY = "cycle_change_count";
	public static final String CYCLE_CHANGE_COUNT_VALUE = "Changed Count";
	public static final String RID_GRD_QTY_VALUE = "Qty:";
	public static final String PID_LBL_UNDO_VALUE = "Undo";
	public static final String RID_LBL_MOVE_VALUE = "Move";
	public static final String ITEM_NOT_IN_PO_VALUE = "This item is not in your PO. Please re-scan.";
	public static final String DMG_LBL_QUANTITYTODAMAGE_VALUE = "Quantity to Damage";
	public static final String RID_LBL_PRINTLABELS_VALUE = "Print Labels";
	public static final String RID_LBL_QTY_TO_RECEIVE_VALUE = "Qty to Receive";
	public static final String RID_TITLE_RECEIVE_ITEM_DETAIL_VALUE = "Receive Item Detail";
	public static final String MV_LIMITQTY_MSG_VALUE = "Quantity to move can't greater than Max Qty";
	public static final String ENTER_OR_SCAN_QTY_VALUE = "Enter/Scan Quantity";
	public static final String SCAN_SELECTLOC_SEARCH_VALUE = "Scan or Select Location";
	public static final String MV_LOCATION_NOT_FOUND_MSG_VALUE = "This location {0} is not found, do you want to create a new license plate?";
	public static final String RID_UNRECEIPT_ALERT_MSG_VALUE = "Are you sure you want to undo Item {0}Lot: {1} Quantity: {2}";
	public static final String BIN_MESSAGE_BOX_CREATE_LP_ERROR_VALUE = "Error creating Container License Plate, please Try Again.";
	public static final String ERROR_CONNECTING_TO_WEB_SERVICE_VALUE = "Error occurred while attempting to connect to web service. Please try again.";
	public static final String RECEIVING_PO_ITEMS_LBLSECTION_VALUE = "Receiving";
	public static final String CYCLE_COUNT_VALUE = "Cycle Count";
	public static final String NO_MORE_COUNTS_VALUE = "No More Counts";
	public static final String COMPLETED_VALUE = "Completed";
	public static final String INVALID_PO_SCANNED_VALUE = "Invalid PO scanned, please scan a PO on the list";
	public static final String MNP_MSG_INVALIDQTY_VALUE = "Invalid Quantity. Please try again.";
	public static final String MISS_LOT_MSG_VALUE = "Please enter or scan Lot # to continue.";
	public static final String RECEIVINGPO_LBLENTERQTYRECEIVED_VALUE = "Enter or scan quantity received";
	public static final String RID_INPUTQUANTITYERROR_MSG_VALUE = "Selected quantity larger than max quantity. Please try again.";
	public static final String CYCLE_QUES_COMPLETE_VALUE = "Would you like to complete this count?";
	public static final String ERROR_COMPLETING_COUNT_VALUE = "Error Completing Count";
	public static final String BIN_NOT_EXIST_VALUE = "Scanned Bin does not exist, please scan a valid system bin";
	public static final String CYCLE_MESS_ZEROQUESTION_VALUE = "Are you sure you want to zero count {0}?";
	public static final String JOBPART_VALIDATEITEMORLP_VALUE = "Please scan Item or License Plate to continue.";
	public static final String USER_SCAN_LOTNUMBER_VALUE = "Please scan Lot Number";
	public static final String MAINLIST_MENLOADING_VALUE = "Loading";
	public static final String CMM_COMPLETE_MSG_VALUE = "This item has already completed.";
	public static final String RD_RETRIEVEPO_MSG_VALUE = "Fail to retrieve PO Data";
	public static final String RD_LBL_LOTS_VALUE = "Lots";
	public static final String SCAN_NOTFOUND_VALUE = "Ambiguous Scan. Currently unimplemented.";
	public static final String RE_SCANPO_MSG_VALUE = "You must scan a valid PO Number to continue. Please try again.";
	public static final String BINTRANSFER_MSGLPMOVETOSELFERROR_VALUE = "Cannot move an LP onto itself.";
	public static final String MV_MOVEFAILED_VALUE = "Move action failed. Please try again.";
	public static final String PID_EMPTY_QTY_MOVE_VALUE = "Please select some quantities to move";
	public static final String ITEM_NOT_YOUR_PO_VALUE = "This item is not in your PO";
	public static final String DAMAGED_VALUE = "Damaged";
	public static final String PLEASE_SCAN_BIN_OR_LP = "Please scan Bin or LP";
	public static final String LICENSE_PLATE = "License Plate";
	
	/*key to support multiple language*/
	public static final String JOB_TITLE_JOB = "job_title_Job";
	public static final String JOB_LBL_JOB = "job_lbl_Job";
	public static final String PICK_TXT_COMPLETE = "pick_txt_Complete";
	public static final String ZONE = "Zone";
	public static final String JOB_DETAIL_TITLE = "job_detail_title";
	public static final String JD_LBL_JOBNUMBER = "jd_lbl_JobNumber";
	public static final String LOADING_MSG = "loading_msg";
	public static final String PARTDETAIL_TITLE = "partdetail_title";
	public static final String BACK = "Back";
	public static final String OPTIONS = "Options";
	public static final String SETTINGS_BTN_CANCEL = "settings_btn_Cancel";
	public static final String CYCLE_ZERO_KEY = "cycle_zero";
	public static final String CYCLE_ZERO_VALUE = "Zero Count";
	public static final String CYCLE_ADJUSTMENT_KEY = "cycle_adjustment";
	public static final String CYCLE_ADJUSTMENT_VALUE = "Cycle Count Adjustment";
	public static final String PRINTLABELS_KEY = "rid_lbl_PrintLabels";
	public static final String PRINTLABELS_VALUE = "Print Labels";
	public static final String PD_LBL_LOCATION = "pd_lbl_Location";
	public static final String JID_LBL_TITLE = "jid_lbl_title";
	public static final String PID_LBL_LOCATION = "pid_lbl_Location";
	public static final String PID_LBL_QTYTOPICK = "pid_lbl_QtyToPick";
	public static final String PID_LBL_PICKED = "pid_lbl_Picked";
	public static final String PID_LBL_LEFT = "pid_lbl_Left";
	public static final String PID_BTN_OPTIONS = "pid_btn_Options";
	public static final String LOCATION = "Location";
	public static final String RID_GRD_LOT = "rid_grd_Lot";
	public static final String RID_GRD_QTY = "rid_grd_Qty";
	public static final String ADD_KEY = "Add";
	public static final String ITEM_KEY = "Item";
	public static final String RID_LBL_PRINTLABELS = "rid_lbl_PrintLabels";
	public static final String PID_LBL_GETNEXTLOCATION = "pid_lbl_GetNextLocation";
	public static final String PID_LBL_UNDO_KEY = "pid_lbl_Undo";
	public static final String PID_LBL_SHORT = "pid_lbl_Short";
	public static final String PID_LBL_UOM = "pid_lbl_UOM"; 
	public static final String INVALID_PO_SCANNED_KEY = "invalid_po_scanned_key";
	public static final String MV_MSG_SELECTORSCAN_KEY = "mv_msg_SelectOrScan";
	public static final String RID_LBL_MOVE_KEY = "rid_lbl_Move";
	public static final String TRANSFER = "Transfer";
	public static final String MV_LBL_MAXQTY = "mv_lbl_MaxQty";
	public static final String PRINTRECLPS_MSGENTERVALIDLOTNUM_KEY = "PrintRecLPs_msgEnterValidLotNum";
	public static final String PRINTRECLPS_MSGENTERVALIDLOTNUM_VALUE = "Please enter a valid Lot number";
	
	public static final String MV_LBL_FROM = "mv_lbl_From";
	public static final String QTY = "Qty:";
	public static final String MV_LBL_TO = "mv_lbl_To";
	public static final String MV_NEED_LOC_MSG = "mv_need_loc_msg";
	public static final String MV_LIMITQTY_MSG = "mv_limitQty_msg";
	public static final String MV_EMPTY_QTY_MSG = "mv_empty_qty_msg";
	public static final String VALIDATING_MSG = "validating_msg";
	public static final String MV_INVALID_LP_MSG = "mv_invalid_lp_msg";
	public static final String CYCLECOUNT_INVALID_LOT_NUMBER = "Invalid lot number. Please try again.";
	public static final String SELECT = "Select";
	public static final String PART_SEARCH_TITLE = "part_search_title";
	public static final String ERRORUNABLETOCONTACTSERVER = "ErrorUnableToContactServer";
	public static final String ERRORAMBIGUOUSSCAN = "ErrorAmbiguousScan";
	public static final String ERRORAMBIGUOUSSCAN_DEFAULT = "Ambiguous Scan or barcode not found.";
	public static final String ERROR_INVALID_ITEM_SCAN = "ErrorInvalidItemScan";
	public static final String JOBPART_DETAIL_TITLE = "jobpart_detail_title";
	public static final String UNIT_OF_MEASURE = "UnitOfMeasure";
	public static final String REASON = "Reason";
	public static final String NOTE = "Note";
	
	public static final String WITHOUTWIRE = "WithoutWire";
	public static final String MAINLIST_MENRECEIVEINVENTORY = "MainList_menReceiveInventory";
	public static final String MAINLIST_MENPUTAWAY = "MainList_menPutAway";
	public static final String MAINLIST_MENBINTRANSFER = "MainList_menBinTransfer";
	public static final String INVENTORYADJUSTMENT_NAME = "InventoryAdjustment_Name";
	public static final String MAINLIST_MENCYCLECOUNT = "MainList_menCycleCount";
	public static final String MAINLIST_MENLPOPERATIONS = "MainList_menLPOperations";
	public static final String MAINLIST_MENEXITLOGOUT = "MainList_menExitLogout";
	public static final String MAINLIST_MENUSERPROFILE = "MainList_menUserProfile";
	public static final String USER_PROFILE = "User Profile";
	public static final String MAINLIST_MENINVENTORYHISTORY = "MainList_menInventoryHistory";
	public static final String MAINLIST_MENJOBS = "MainList_menJobs";
	public static final String MAINLIST_MENMANAGEPARTSANDSUPPLIES = "MainList_menManagePartsandSupplies";
	public static final String USERPROFILE_LBL_EDIT = "UserProfile_lbl_Edit";
	public static final String EDIT_USER_INFORMATION = "Edit User Information";
	public static final String USERPROFILE_LBL_VERSION = "UserProfile_lbl_Version";
	public static final String VERSION = "Version";	
	public static final String APP_VERSION = "App Version";
	public static final String USERPROFILE_LBL_CURRPASS = "UserProfile_lbl_currPass";
	public static final String CURRENT_PASSWORD = "Current Password";
	public static final String USERPROFILE_LBL_NEWPASS = "UserProfile_lbl_newPass";
	public static final String NEW_PASSWORD = "New Password";
	public static final String USERPROFILE_LBL_RENEWPASS = "UserProfile_lbl_renewPass";
	public static final String RE_ENTER_NEW_PASSWORD = "Re-enter New Password";
	public static final String CANCEL = "Cancel";
	public static final String OK = "OK";
	public static final String NEW_PASSWORD_AND_RENEW_PASS_NOT_MATCHED = "new password and renew-pass not matched";
	public static final String PASSWORD_NOT_MATCHED = "Password not matched";
	public static final String ERROR_STRING = "Error";
	public static final String NETWORK_ERROR = "Network error";
	public static final String BIZ_ERROR = "biz error";
	public static final String PIN_CHANGED_SUCCESS = "Pin changed success!";
	public static final String CHANGE_SUCCESS = "Change success";
	public static final String INVALID_CURRENT_PASSWORD = "Invalid current Password. Please try again.";
	public static final String REST_LBL_SCAN = "reST_lbl_Scan";
	public static final String SCAN_LP_TO_BEGIN = "Scan LP to Begin";
	public static final String REST_TXT_LP = "reST_txt_LP";
	public static final String LP_Number = "LP Number";
	public static final String PO = "PO";
	public static final String REST_LBL_TOTALLP = "reST_lbl_TotalLP";
	public static final String TOTAL_LP_RECEIVED = "Total LP\'s Received";
	public static final String SCANITEM = "ScanItem";
	public static final String SCAN_ITEM_OR_LICENCE_PLATE = "Scan Item or Licence Plate";
	public static final String SCAN_BIN = "Scan Bin";
	public static final String SELECT_OR_SCAN_LOCATION_TO_MOVE_TO = "Select or Scan Location to Move To";	
	public static final String MV_LBL_UOM = "mv_lbl_UOM";
	public static final String LOTNUMBER = "LotNumber";
	public static final String LOT_NUMBER_STRING = "Lot Number";
	public static final String FROM = "From";
	public static final String BIN_NUMBER_LOWER = "Bin Number";
	public static final String BINNUMBER = "BinNumber";
	public static final String QUANTITY = "Quantity";
	public static final String TO = "To";
	public static final String BIN_MESSAGEBOXINVALIDLOCATIONSCAN = "Bin_messageBoxInvalidLocationScan";
	public static final String INVALID_LOCATION_SCAN_PLEASE_SCAN_A_VALID_LOCATION = "Invalid Location Scan. Please scan a valid Location";
	public static final String BIN_MESSAGEBOXTITLEINVALIDLP = "Bin_messageBoxTitleInvalidLP";
	public static final String INVALID_LICENSE_PLATE = "Invalid license plate";
	public static final String MV_TITLE_MOVE = "mv_title_Move";
	public static final String PUTAWAY_TITLE_PUTAWAY = "putAway_title_PutAway";
	public static final String PUT_AWAY_BINS = "Put Away Bins";
	public static final String WITHOUTWIRE_ANDROID_EDITION = "WithoutWire Android Edition";
	public static final String MESSAGECONFIRMEXIT = "MessageConfirmExit";
	public static final String ARE_YOU_SURE_YOU_WANT_TO_EXIT = "Are you sure you want to exit?";
	public static final String JOBPART_LICENSEPLATE_TITLE = "jobpart_licensePlate_title";
	public static final String INVENTORY_HISTORY_ = "inventory_history";
	public static final String PART_VALIDATE_CODE = "part_validate_code";
	public static final String OBJECTITEM = "objectitem";
	public static final String RETURNNUMBER = "RETURNNUMBER";
	public static final String ERROR_GETTING_ORDERS = "ErrorGettingOrders";
	public static final String ERROR_OCCURRED_KEY = "ErrorOccurred";
	public static final String ERROR_GETTING_NEXT_ORDER = "ErrorGettingNextOrder";
	public static final String REQUESTING_MSG = "requesting_msg";
	public static final String OH = "OH";
	public static final String REST_GRD_QTY = "reST_grd_qty";
	public static final String BIN_LBLMESSAGESCANLOCATION = "Bin_lblMessageScanLocation";
	public static final String RETURNPARTMAIN_LBL_HEADER = "ReturnPartMain_lbl_header";
	public static final String JOBPART_VALIDATEQUANTITYONHAND = "jobpart_ValidateQuantityOnHand";
	public static final String SCANLOCATION_MSG = "pd_scanLocation_msg";
	public static final String LPRETURNPART_LBL_RETURNTITLE = "LPReturnPart_lbl_returnTitle";
	public static final String SCAN_BARCODE_UNSUPPORTED_KEY = "scan_barcode_Unsupported";
	public static final String PD_SHIPIMMEDIATELY_MSG = "pd_shipImmediately_msg";
	public static final String YES_KEY = "Yes";
	public static final String NO_KEY = "cmm_alert_No";
	public static final String OK_KEY = "OK";
	public static final String CMM_COMPLETE_MSG_KEY = "cmm_complete_msg";
	public static final String DMG_LBL_QUANTITYTODAMAGE_KEY = "dmg_lbl_QuantityToDamage";
	public static final String BIN_KEY = "Bin";
	public static final String ITEM_NOT_IN_PO_KEY = "item_is_not_in_po";
	public static final String RID_INPUTQUANTITYERROR_MSG_KEY = "rid_inputQuantityError_msg";
	public static final String RECEIVINGPO_LBLENTERQTYRECEIVED_KEY = "ReceivingPO_lblEnterQtyReceived";
	public static final String PID_GRD_QTY_KEY = "pid_grd_Qty";
	public static final String RID_GRD_QTY_KEY = "rid_grd_Qty";
	public static final String RID_LBL_LEFT_KEY = "rid_lbl_Left";
	public static final String PID_EMPTY_QTY_MOVE_KEY = "pid_empty_qty_move";
	public static final String MV_MOVEFAILED_KEY = "mv_movefailed";
	public static final String BINTRANSFER_MSGLPMOVETOSELFERROR_KEY = "BinTransfer_msgLPMoveToSelfError";
	public static final String MNP_MSG_INVALIDQTY_KEY = "mnp_msg_invalidqty";
	public static final String PID_BTN_OPTIONS_KEY = "pid_btn_Options";
	public static final String RE_SCANPO_MSG_KEY = "re_scanPO_msg";
	public static final String RID_LBL_QTY_TO_RECEIVE_KEY = "rid_lbl_QtyToReceive";
	public static final String RID_LBL_RECEIVED_KEY = "rid_lbl_Received";
	public static final String RID_TITLE_RECEIVE_ITEM_DETAIL_KEY = "rid_title_ReceiveItemDetail";
	public static final String RE_LBL_RECEIVE1_KEY = "re_lbl_Receive1";
	public static final String PICK_TXT_SEARCH_KEY = "pick_txt_Search";
	public static final String RD_TITLE_RECEIVEDETAIL_KEY = "rd_title_ReceiveDetail";
	public static final String RE_PO_EMPTY_MSG_KEY = "re_poEmpty_msg";
	public static final String SETTINGS_BTN_CANCEL_KEY = "settings_btn_Cancel";
	public static final String JOBPART_VALIDATE_ITEM_OR_LP_KEY = "jobpart_validateItemOrLP";
	public static final String PICKREALTIME_LBLCOMMENTS = "PickRealTime_lblComments";
	public static final String DMG_LBL_RECEIVEREQUEST_KEY = "dmg_lbl_ReceiveRequest";
	public static final String DMG_TITLE_DAMAGE_KEY = "dmg_title_Damage";
	public static final String RD_TXT_LOTTRACKEDIND_KEY = "rd_txt_LotTrackedInd";
	public static final String UNIT_OF_MEASURE_KEY = "UnitOfMeasure";
	public static final String DMG_LBL_LOCATION_KEY = "dmg_lbl_Location";
	public static final String PID_GRD_LOT_KEY = "pid_grd_Lot";
	public static final String DMG_LBL_MAXQTY_KEY = "dmg_lbl_MaxQty";
	public static final String REST_GRD_LOT_KEY = "reST_grd_Lot";
	public static final String MISS_LOT_MSG_KEY = "miss_lot_msg";
	public static final String ACQUIRE_BARCODE_KEY = "acquire_Barcode";
	public static final String SCAN_OR_ENTER_BARCODE_KEY = "scan_or_Enter_Barcode";
	public static final String SCAN_NOTFOUND_KEY = "scan_notFound";
	public static final String RD_INVALID_BARCODE_MSG_KEY = "rd_invalid_barcode_msg";
	public static final String ITEM_NOT_YOUR_PO_KEY = "item_not_your_po_key";
	public static final String RID_LBL_PRINTLABELS_KEY = "rid_lbl_PrintLabels";
	public static final String RID_UNRECEIPT_ALERT_MSG_KEY = "rid_unreceipt_alert_msg";
	public static final String MV_LIMITQTY_MSG_KEY = "mv_limitQty_msg";
	public static final String ENTER_OR_SCAN_QTY_KEY = "EnterOrScanQty";
	public static final String SCAN_SELECTLOC_SEARCH_KEY = "scan_selectLoc_search";
	public static final String MV_LOCATION_NOT_FOUND_MSG_KEY = "mv_locationNotfound_msg";
	public static final String BIN_MESSAGE_BOX_CREATE_LP_ERROR_KEY = "Bin_messageBoxCreateLPError";
	public static final String ERROR_CONNECTING_TO_WEB_SERVICE_KEY = "ErrorConnectingToWebService";
	public static final String RECEIVING_PO_ITEMS_LBLSECTION_KEY = "ReceivingPOItems_lblSection";

	public static final String CYCLE_COUNT_KEY = "CountBinRealTime_lblSection";
	public static final String NO_MORE_COUNTS_KEY = "CountBinRealTime_messageBoxNoCounts";
	public static final String CYCLE_QUES_COMPLETE_KEY = "cycle_ques_complete";
	public static final String ERROR_COMPLETING_COUNT_KEY = "CountBinRealTime_lblError";
	public static final String BIN_NOT_EXIST_KEY = "CountItemAddRealTime_lblErrorNonExistentBin";
	public static final String CYCLE_MESS_ZEROQUESTION_KEY = "cycle_mess_zeroquestion";
	public static final String JOBPART_VALIDATEITEMORLP_KEY = "jobpart_validateItemOrLP";
	public static final String USER_SCAN_LOTNUMBER_KEY = "user_scan_LotNumber";
	public static final String PD_BTN_OK = "pd_btn_Ok";
	public static final String MAINLIST_MENLOADING_KEY = "MainList_menLoading";
	public static final String RD_RETRIEVEPO_MSG_KEY = "rd_retrievePO_msg";
	public static final String RD_LBL_LOTS_KEY = "rd_lbl_Lots";
	public static final String DAMAGED_KEY = "Damaged";
	
	/* number of status app */
	public static final int ONE_STATUS = 1; // cusseccfull
	public static final int TWO_STATUS = 2; // network
	public static final int THREE_STATUS = 3; 
	public static final int FOUR_STATUS = 4; // show popup
	public static final int FIVE_STATUS = 5; // qty invalide
	public static final int APOLIS_NETWORK_EXCEPTION = 55;
	public static final int PROCESS_RESULT_DEFAULT = 0;
	public static final int PROCESS_RESULT_EXCEPTION = 3;
	public static final int PROCESS_RESULT_SUCCESSFULL = 1;
	
	public static final String PICK_ITEM = "pick_item";
	public static final String PD_SCANLOCATION_MSG = "pd_scanLocation_msg";
	
	public static final String PD_SCANITEM = "pd_scanItem";
	public static final String REST_GRD_LOT = "reST_grd_Lot";
	public static final String RE_TXT_SEARCH = "re_txt_Search";
	public static final String ERRORINVALIDLOTNUMFORITEM = "ErrorInvalidLotNumForItem";
	public static final String MES_INVALID_ITEM = "mes_invalid_item";
	public static final String MNP_MSG_INVALIDJOB = "mnp_msg_invalidjob";
	public static final String ADDMFGINV_MSGENTERLOTNUMBER = "AddMfgInv_msgEnterLotNumber";
	public static final String PID_EMPTY_QTY = "pid_empty_qty";
	public static final String UPLOADING_MSG = "uploading_msg";	
	public static final String RESULTSCAN = "RESULTSCAN";
	public static final String SCREEN_TO_SCREEN = "SCREEN TO SCREEN";
	public static final String PO_OBJECT = "PO Object";
	public static final String TWO_DOT_VERTICAL = ":";
	public static final String SPACE = " ";
	public static final String DEVICE_SOCKET = "DEVICE SOCKET";
	public static final String P = "P";
	public static final String S = "S";
	public static final String PRIMARY = "Primary";
	public static final String SECONDARY = "Secondary";
	public static final String REPLENISH_ITEMMATCHERROR_KEY = "Replenish_ItemMatchError";
	public static final String REPLENISH_ITEMMATCHERROR_VALUE = "No Item match found, please scan Item on list.";
	
	/* Screen */
	public static final String BARCODE_MOVE = "BARCODE MOVE";
	public static final String CHECK_LP_OR_NOT_LP = "CHECK LP OR NOT LP";
	public static final String CHECK_BIN_OR_NOT_BIN = "CHECK BIN OR NOT BIN";
	public static final String LOT_NUMBER = "LOT NUMBER";
	public static final String BIN_NUMBER = "BIN NUMBER";
	public static final String QTY_NUMBER = "QTY NUMBER";
	public static final String LP_NUMBER = "LP NUMBER";
	public static final String PUT_AWAY_BIN = "PUT AWAY BIN";
	public static final String PUT_AWAY_BIN_DATA = "PUT AWAY BIN DATA";
	public static final String PUT_PASS_AWAY_BIN_DATA = "PUT PASS AWAY BIN DATA";
	public static final String _ITEMNUMBER = "_itemNumber";
	
	/* MoveScreen */
	public static final String MAINACTIVITY = "MainActivity";
	
	/*
	 * This is the key from ISB Vietnam when you register
	 */
	public static final String APF_SECRET_KEY = "ODUwMzJDQzM4RjRCMTFDMEVDRUU3Qzg1ODUyNTE0NzNFN0U0OEU5QzMwNTA2RjQxNTc1MjY0Mjk4MzZEMkJCQkI4RjBFNjEwQkM2QjMyMUFEN0FGNjZFNjg5QjNFMjJGMDhENjNEQjhGQjkzNjAwN0UzOEM1NzU3MzI3NDA5RjQ2RjU0RDQyQzMwMkVDMkZENTlERUI4QTE3N0M2QUI3QUZFODA5ODRBNUUyOENGOEJFM0MxRDdCREU4MjFCOTVGQjg1RjM0Q0IyRDgzNzQ3NjNENjZGMzUxQzQxNTkzRDM4QTIxMjJFRkVGREQyMkU1Mzc2OTExRDE0MTM2QzhBRkZDOTE3Mzg0NUIyNkZCMjc2RURBQTJBMzM1Q0EwRERGRTM3M0JEMkRDQkUwQjM4NDkyQTQ0MEYwOTE0NUZCRkI3MEI3NTRFNjQzQjJBRjE0NDlCNEQwRUE5QUQyMERDMg==";
}
