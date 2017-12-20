package com.example.seung.soundtest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProcFile {

	
	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	public static String makeFileNameWithDateTime() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd_HHmmss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	public static String getCurrentTimeStamp(String format1) {
	    SimpleDateFormat sdfDate = new SimpleDateFormat(format1);//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
}
