package com.example.shookisha.utility;

import android.content.res.Resources;

import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;

public class ToolsUtility {

    public String dateToString( long dateInt){

        Date date = new Date(dateInt);

        if(Resources.getSystem().getConfiguration().locale.getLanguage().equals(new Locale("fr").getLanguage())){

            System.out.println(DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE).format(date));
            return DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE).format(date);
        }else{
            System.out.println(DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE).format(date));
            System.out.println(DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).format(date));
            System.out.println(DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(date));

            return DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).format(date);
        }

    }
}
