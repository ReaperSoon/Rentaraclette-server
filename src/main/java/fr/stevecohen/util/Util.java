package fr.stevecohen.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {
	
	public static String getFormattedDateFrom(String date) {
		return new SimpleDateFormat(date).format(Calendar.getInstance().getTime());
	}

}
