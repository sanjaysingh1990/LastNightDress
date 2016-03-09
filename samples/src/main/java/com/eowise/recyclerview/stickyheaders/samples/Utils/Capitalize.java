package com.eowise.recyclerview.stickyheaders.samples.Utils;

/**
 * Created by sanjay on 12/13/2015.
 */
public class Capitalize {
    public static String capitalize(final String line) {
       try {
           String[] split = line.split(" ");
           String output = "";
           for (String str : split) {

               output += Character.toUpperCase(str.charAt(0)) + str.substring(1) + " ";
           }
           return output;
       }
       catch(Exception ex)
       {
           return line;
       }
       }
    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
