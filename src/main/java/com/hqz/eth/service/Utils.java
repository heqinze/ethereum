package com.hqz.eth.service;

import org.apache.commons.lang3.StringEscapeUtils;

public class Utils {
    public static String strToUnicode(String str) {
        return StringEscapeUtils.escapeJava(str);
    }
    public static String unicodeToStr(String unicode) {
        return StringEscapeUtils.unescapeJava(unicode);
    }
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
    public static String hexStringToString(String s){
        if(s == null || s.equals("")){
            return null;
        }
        s = s.replace(" ","");
        byte[] baKeyword =new byte[s.length()/2];
        for(int i =0; i < baKeyword.length; i++){
            try{
                baKeyword[i]=(byte)(0xff& Integer.parseInt(
                        s.substring(i *2, i *2+2),16));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        try{
            s =new String(baKeyword,"gbk");
            new String();
        }catch(Exception e1){
            e1.printStackTrace();
        }
        return s;
    }
}
