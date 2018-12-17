package Util;

public class StringUtil {

    public static String getLastLine(String content){
        String[] lines = content.split("\n");
        if(lines.length == 1){
            return content;
        }else{
            return lines[lines.length-1];
        }
    }
}
