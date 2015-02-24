/*package com.appolis.entities;

import java.io.Serializable;
import java.util.HashMap;

import com.appolis.utilities.StringUtils;

public class EnMultiLanguage implements Serializable{

	*//**
	 * 
	 *//*
	private static final long serialVersionUID = -5096574821433457685L;
	private static EnMultiLanguage instance = null;

	protected EnMultiLanguage() {
		// Exists only to defeat instantiation.
	}

	public static EnMultiLanguage getInstance() {
		if (instance == null) {
			instance = new EnMultiLanguage();
		}
		return instance;
	}
	   
	private HashMap<String, String> mapLenguage;
	
	public HashMap<String, String> getMapLenguage() {
		return mapLenguage;
	}

	public void setMapLenguage(HashMap<String, String> mapLenguage) {
		this.mapLenguage = mapLenguage;
	}

	public void putLanguage(String key, String value){
		if(mapLenguage.containsKey(key)){
			mapLenguage.put(key, value);
		} else {
			mapLenguage.put(key, value);
		}
	}
	
	*//**
	 * getLanguage
	 * @param key
	 * @param returnIfNotFound
	 * @return
	 *//*
	public String getLanguage(String key, String returnIfNotFound){
		if(StringUtils.isBlank(key) || (null == mapLenguage || mapLenguage.size() == 0)){
			return returnIfNotFound;
		} 
		
		String str = "";
		try{
			str = mapLenguage.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			str = returnIfNotFound;
		}
		
		return str;
	}
}
*/