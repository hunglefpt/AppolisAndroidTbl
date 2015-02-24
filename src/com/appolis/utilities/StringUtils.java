/**
 * Name: $RCSfile: StringUtils.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2015/01/06 11:23:49 $
 *
 * Copyright (C) 2015 FPT Software. All rights reserved.
 */
package com.appolis.utilities;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provide util functions handling a string or an array.
 * 
 * @author HoangNH11
 */
public class StringUtils
{	
	private static final String TAG = "StringUtils";
    private static final String NUMBER_PATTERB = "^[0-9]{1,10}$";
     
     /**
     * 05/02/2013
     * This pattern string is update version of EMAIL_PATTERN, to determine
     * whether email is correct or not
     */
    private static final String EMAIL_PATTERN_UPDATE = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    
    /**
     * 05/02/2013
     * This pattern string is used to determine whether phone number is correct
     * or not. Example:
     * (222)444-6666
     * (222) 444-6666 // not handle
     * 2224446666
     * 222-444-6666
     * 333.444.5555
     */
    private static final String PHONE_NUMBER_PATTERN = "^\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$";
    
    /**
     * Check whether or not a string is not null, not blank and not equals to "null".
     * 
     * @param str
     * @return boolean
     */
    public static boolean validNormalString(String str)
    {
        if (isBlank(str) || "null".equalsIgnoreCase(trim(str)))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Checks the string is blank, is not it?
     * 
     * @param str the string that user enters
     * 
     * @return the checking result
     */
    public static boolean isBlank(String str)
    {
        int strLen = 0;
        if (str == null || (strLen = str.length()) == 0)
        {
            return Boolean.TRUE;
        }
        
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(str.charAt(i)))
            {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
    
    /**
     * Check the string is not format email, is it?
     * @param target
     * @return
     */
	public static boolean isValidEmail(final String email)
	{
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

    /**
     * Checks the string is not blank, is it?
     * 
     * @param str the string that user enters
     * 
     * @return the checking result
     */
    public static boolean isNotBlank(String str)
    {
        return !isBlank(str);
    }

    /**
     * Check the string is not Numeric, is it?
     * It can't check decimal number (eg. 12.5)
     * 
     * @param str String input
     * @return true if it is numeric.
     */
    public static boolean isNumeric(String str)
    {
        if (str == null)
        {
            return false;
        }
        
        int sz = str.length();
        if (sz == 0)
        {
            return false;
        }

        for (int i = 0; i < sz; i++)
        {
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }

        return true;
    }
    
    /**
     * This method is used to check a number is float format or not
     * 
     * @param str String input
     * @return true if is is float
     */
    public static boolean isFloat(String str)
    {
        try
        {
            Float.parseFloat(str);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Remove the white space in the string
     * 
     * @param str the string
     * 
     * @return the string does not have the white space
     */
    public static String trim(String str)
    {
        return str != null ? str.trim() : null;
    }
    
    /**
     * Get substring with length and this string appended ...
     *  
     * @param s
     * @param leng
     * @return
     */
    public static String getSubStringWithLength(String s, int leng)
    {
        StringBuilder sb = new StringBuilder("");
        
        if (s == null || leng == 0)
        {
            return "";
        }
        
        if (s.length() > leng)
        {
            return sb.append(s.substring(0, leng - 1)).append("...").toString();
        }
        
        return s;
    }
    
    /**
     * Create full File Path Name base on file path and file name.
     * 
     * @param filePath
     * @param fileName
     * @return
     */
    public static String createFullFilePathName(String filePath, String fileName)
    {
        String fullFilePathName = null;
        if (filePath != null)
        {
            if (filePath.endsWith(File.separator))
            {
                fullFilePathName = filePath + fileName;
            }
            else
            {
                fullFilePathName = filePath + File.separator + fileName;
            }
        }
        else
        {
            fullFilePathName = fileName;
        }

        return fullFilePathName;
    }
    
    /**
     * Check whether the string that user enters is email or not
     *  
     * @param email The email string
     * 
     * @return The result check
     */
    public static boolean isEmail(String email)
    {
        boolean isEmailValid = false;
        
        if (isNotBlank(email))
        {
            Pattern emailPattern = Pattern.compile(EMAIL_PATTERN_UPDATE);
            Matcher emailMatcher = emailPattern.matcher(email);
            isEmailValid = emailMatcher.matches();
        }
        else if (isBlank(email))//accept case that email is empty
        {
            return true;
        }
        
        return isEmailValid;
    }
    
    /**
     * Check whether the string that user enters is phone or not
     * @param phoneNumber
     * @return The result check
     */
    public static boolean isPhoneNumber(String phoneNumber)
    {
        boolean isPhoneNumberValid = false;
        
        if (isNotBlank(phoneNumber))
        {
            Pattern phonePattern = Pattern.compile(PHONE_NUMBER_PATTERN);
            Matcher phoneMatcher = phonePattern.matcher(phoneNumber);
            isPhoneNumberValid = phoneMatcher.matches();
        } else {
            // set valid = true if blank
            isPhoneNumberValid = true;
        }
        
        return isPhoneNumberValid;
    }
    
    /**
     * Check whether the string that user enters is pager or not
     * @param pager
     * @return The result check
     */
    public static boolean isPager(String pager)
    {
        boolean isPagerNumberValid = false;
        if (isNotBlank(pager))
        {
            Pattern phonePattern = Pattern.compile(NUMBER_PATTERB);
            Matcher phoneMatcher = phonePattern.matcher(pager);
            isPagerNumberValid = phoneMatcher.matches();
        } else {
            // If blank, set valid = true
            isPagerNumberValid = true;
        }
        return isPagerNumberValid;
    }
    
    /**
     * remove characters (, ), - in a string
     * @param initString
     * @return
     */
    public static String removeCharactersFromString(String initString)
    {
        String tmp = initString;
        
        if (isNotBlank(tmp))
        {
            tmp = tmp.replace("(", "");
            tmp = tmp.replace(")", "");
            tmp = tmp.replace(" ", "");
            tmp = tmp.replace("-", "");
            
            return tmp;
        }
        
        return GlobalParams.BLANK_CHARACTER;
    }

	/**
	 * format long value to number string by Vietnamese ;
	 * return null if number is not long value
	 * @param number
	 * @return
	 */
	public static String formatVietnameseValueNumber(long number)
	{
		return formatVietnameseValueNumber(Long.toString(number));
	}

	/**
	 * format long value to number string by Vietnamese ;
	 * return null if number is not long value
	 * @param number
	 * @return
	 */
	public static String formatVietnameseValueNumber(String number)
	{
		return formatNumberValue(number, ".");
	}

	/**
	 * format int value to number string by English ;
	 * return null if number is not long value
	 * @param number
	 * @return
	 */
	public static String formatEnglishValueNumber(int number)
	{
		return formatEnglishValueNumber(Integer.toString(number));
	}
	
	/**
	 * format long value to number string by English ;
	 * return null if number is not long value
	 * @param number
	 * @return
	 */
	public static String formatEnglishValueNumber(long number)
	{
		return formatEnglishValueNumber(Long.toString(number));
	}
	
	/**
	 * format long value to number string by English ;
	 * return null if number is not long value
	 * @param number
	 * @return
	 */
	public static String formatEnglishValueNumber(String number)
	{
		return formatNumberValue(number, ",");
	}

	/**
	 * format long value to number string ;
	 * return null if number is not long value
	 * @param number
	 * @param decimalChar
	 * @return
	 */
	public static String formatNumberValue(String number, String decimalChar)
	{
		try
		{
			Long.parseLong(number);
		}
		catch (Exception e)
		{
			return null;
		}
		StringBuilder builder = new StringBuilder(number);
		int index = builder.length() - 3;
		while (index > 0)
		{
			builder.insert(index, decimalChar);
			index = index - 3;
		}
		return builder.toString();
	}

	/**
	 * convert number string (formated by Vietnamese) to long value;
	 * if error return -1
	 * @param number
	 * @return
	 */
	public static long convertVietnameseValueNumberToLong(String number)
	{
		return convertValueNumberToLong(number, ".");
	}

	/**
	 * convert number string (formated by English) to long value;
	 * if error return -1
	 * @param number
	 * @return
	 */
	public static long convertEnglishValueNumberToLong(String number)
	{
		return convertValueNumberToLong(number, ",");
	}

	/**
	 * convert number string (formated) to long value;
	 * if error return -1
	 * @param number
	 * @param decimalChar
	 * @return
	 */
	public static long convertValueNumberToLong(String number, String decimalChar)
	{
		StringBuilder builder = new StringBuilder(number);
		int index = builder.indexOf(decimalChar);
		while (index > 0)
		{
			builder.delete(index, index + 1);
			index = builder.indexOf(decimalChar);
		}
		try
		{
			return Long.parseLong(builder.toString());
		}
		catch (Exception e)
		{
			return -1l;
		}
	}
	
	/**
	 * format date string from local format to service format
	 * @param date
	 * @return
	 */
	public static String formatDateFromLocalToServer(String date)
	{
		StringBuilder builder = new StringBuilder(date);
		int index = builder.indexOf("/");
		while (index >= 0)
		{
			builder.replace(index, index + 1, "-");
			index = builder.indexOf("/");
		}
		return builder.toString();
	}
	
	/**
	 * format param string from local format to service format
	 * @param param
	 * @return
	 */
	public static String formatParamFromLocalToServer(String param)
	{
		StringBuilder builder = new StringBuilder(param);
		int index = builder.indexOf(" ");
		while (index >= 0)
		{
			builder.replace(index, index + 1, "+");
			index = builder.indexOf(" ");
		}
		return builder.toString();
	}
	
	/** 
     * Convert format date to format number dd/mm/yyyy
     * @param s
     * @return
     */
    public static String ConvertDate(String s)
    {
    	String[] subString;
    	if (s.length() > 0) 
    	{
    		subString = s.split("[ ]");
    		if (subString[0].equalsIgnoreCase("jan")){
    			subString[0] = "01";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("feb")){
    			subString[0] = "02";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("mar")){
    			subString[0] = "03";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("apr")){
    			subString[0] = "04";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("may")){
    			subString[0] = "05";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("jun")){
    			subString[0] = "06";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("jul")){
    			subString[0] = "07";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("aug")){
    			subString[0] = "08";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("sep")){
    			subString[0] = "09";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("oct")){
    			subString[0] = "10";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("nov")){
    			subString[0] = "11";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}else if (subString[0].equalsIgnoreCase("dec")){
    			subString[0] = "12";
    			s = subString[1].substring(0, subString[1].length()-1) + "/" + subString[0] + "/" + subString[2];
			}
		}
    	
		return s;
    }
    
    /** 
     * Convert format month to format number dd/mm/yyyy
     * @param s
     * @return
     */
    public static String ConvertMonth(String s)
    {    	
    	if (s.length() > 0)
    	{    		
    		if (s.equals("01") || s.equals("1")){    			
    			s = "JAN";
			}else if (s.equals("02") || s.equals("2")){
				s = "FEB";
			}else if (s.equals("03") || s.equals("3")){
				s = "MAR";
			}else if (s.equals("04") || s.equals("4")){
				s = "APR";
			}else if (s.equals("05") || s.equals("5")){
				s = "MAY";
			}else if (s.equals("06") || s.equals("6")){
				s = "JUN";
			}else if (s.equals("07") || s.equals("7")){
				s = "JUL";
			}else if (s.equals("08") || s.equals("8")){
				s = "AUG";
			}else if (s.equals("09") || s.equals("9")){
				s = "SEP";
			}else if (s.equals("10")){
				s = "OCT";
			}else if (s.equals("11")){
				s = "NOV";
			}else if (s.equals("12")){
				s = "DEC";
			}
		}
    	
		return s;
    }
    
    /** 
     * Convert format month to format number dd/mm/yyyy
     * @param s
     * @return
     */
    public static String ConvertMonthToNumber(String s)
    {    	
    	if (s.length() > 0)
    	{    		
    		if (s.equalsIgnoreCase("jan")){    			
    			s = "01";
			}else if (s.equalsIgnoreCase("feb")){
				s = "02";
			}else if (s.equalsIgnoreCase("mar")){
				s = "03";
			}else if (s.equalsIgnoreCase("apr")){
				s = "04";
			}else if (s.equalsIgnoreCase("may")){
				s = "05";
			}else if (s.equalsIgnoreCase("jun")){
				s = "06";
			}else if (s.equalsIgnoreCase("jul")){
				s = "07";
			}else if (s.equalsIgnoreCase("aug")){
				s = "08";
			}else if (s.equalsIgnoreCase("sep")){
				s = "09";
			}else if (s.equalsIgnoreCase("oct")){
				s = "10";
			}else if (s.equalsIgnoreCase("nov")){
				s = "11";
			}else if (s.equalsIgnoreCase("dec")){
				s = "12";
			}
		}
    	
		return s;
    }
    
    /** 
     * Calculate Bonus
     * @param s
     * @return
     */
    public static String CalculateBonus(String miles, String qualMiles)
    {
    	int m = Integer.parseInt(miles);
    	int qm = Integer.parseInt(qualMiles);
    	String result = String.valueOf(Math.abs(qm - m));
    	
		return result;
    }
    
    /**
     * format date form server to GUI
     * @param dateFromServer
     * @return
     */
    public static String formatDateForGUI(String dateFromServer)
	{
    	StringBuilder builder = new StringBuilder(dateFromServer);
		int index = builder.indexOf("-");
		while (index >= 0)
		{
			builder.replace(index, index + 1, "/");
			index = builder.indexOf("-");
		}
		return builder.toString();
	}
    
	public static String convertDateTimeEnglish(String strDate)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);		
		Date date = new Date();
		try {
			date = dateFormat.parse(strDate);			
		} catch (ParseException e) {		
//			Log.e(TAG, "ParseException: " + e);
		}
		return String.valueOf(date);
	}
	
	public static String getDateTime(String format) {
		final Calendar c = Calendar.getInstance();
	    int mYear = c.get(Calendar.YEAR);
	    int mMonth = c.get(Calendar.MONTH) + 1;
	    int mDay = c.get(Calendar.DAY_OF_MONTH);
		String result = (""+mDay+format+mMonth+format+mYear);
		return result;
	}
	
	public static String getDateTimeHour(String format) {
		final Calendar c = Calendar.getInstance();
	    int mYear = c.get(Calendar.YEAR);
	    int mMonth = c.get(Calendar.MONTH) + 1;
	    int mDay = c.get(Calendar.DAY_OF_MONTH);
	    int mHour = c.get(Calendar.HOUR_OF_DAY);
	    int mMinute = c.get(Calendar.MINUTE);
	    int mSecond = c.get(Calendar.SECOND);
		String result = (""+mYear+format+mMonth+format+mDay+"T"+mHour+format+mMinute+format+mSecond);
		return result;
	}
	
	public static String getDateTimeHourMinute(String format) {
		final Calendar c = Calendar.getInstance();
	    int mYear = c.get(Calendar.YEAR);
	    int mMonth = c.get(Calendar.MONTH) + 1;
	    int mDay = c.get(Calendar.DAY_OF_MONTH);
	    int mHour = c.get(Calendar.HOUR_OF_DAY);
	    int mMinute = c.get(Calendar.MINUTE);	 
		String result = (""+mYear+format+mMonth+format+mDay+"T"+mHour+":"+mMinute);
		return result;
	}
}
