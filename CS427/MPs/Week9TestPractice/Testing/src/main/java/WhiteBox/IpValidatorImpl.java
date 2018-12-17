package WhiteBox;

import java.util.HashSet;

public class IpValidatorImpl implements IpValidator{
    public static HashSet<String> hash_set = new HashSet<String>();

    public String validIPAddress(String IP) {
        if(isValidIPv4(IP)){
            hash_set.add("A");
            System.out.println(hash_set);
            return "IPv4";
        }
        else if(isValidIPv6(IP)){
            hash_set.add("B");
            System.out.println(hash_set);
            return "IPv6";
        }
        else{
            hash_set.add("C");
            System.out.println(hash_set);
            return "Neither";
        }
    }

    private boolean isValidIPv4(String ip) {
        if(ip.length()<7){
            hash_set.add("D");
            System.out.println(hash_set);
            return false;
        }
        if(ip.charAt(0)=='.'){
            hash_set.add("E");
            System.out.println(hash_set);
            return false;
        }
        if(ip.charAt(ip.length()-1)=='.'){
            hash_set.add("F");
            System.out.println(hash_set);
            return false;
        }
        String[] tokens = ip.split("\\.");
        if(tokens.length!=4){
            hash_set.add("G");
            System.out.println(hash_set);
            return false;
        }
        for(String token:tokens) {
            if(!isValidIPv4Token(token)){
                hash_set.add("H");
                System.out.println(hash_set);
                return false;
            } else {
                hash_set.add("I");
                System.out.println(hash_set);
            }
        }
        hash_set.add("J");
        System.out.println(hash_set);
        return true;
    }
    private boolean isValidIPv4Token(String token) {
        if(token.startsWith("0") && token.length()>1){
            hash_set.add("K");
            System.out.println(hash_set);
            return false;
        }
        try {
            hash_set.add("L");
            System.out.println(hash_set);
            int parsedInt = Integer.parseInt(token);
            if(parsedInt<0 || parsedInt>255){
                hash_set.add("M");
                System.out.println(hash_set);
                return false;
            }
            if(parsedInt==0 && token.charAt(0)!='0'){
                hash_set.add("N");
                System.out.println(hash_set);
                return false;
            }
        } catch(NumberFormatException nfe) {
            hash_set.add("O");
            System.out.println(hash_set);
            return false;
        }
        hash_set.add("P");
        System.out.println(hash_set);
        return true;
    }

    private boolean isValidIPv6(String ip) {
        if(ip.length()<15){
            hash_set.add("Q");
            System.out.println(hash_set);
            return false;
        }
        if(ip.charAt(0)==':'){
            hash_set.add("R");
            System.out.println(hash_set);
            return false;
        }
        if(ip.charAt(ip.length()-1)==':'){
            hash_set.add("S");
            System.out.println(hash_set);
            return false;
        }
        String[] tokens = ip.split(":");
        if(tokens.length!=8){
            hash_set.add("T");
            System.out.println(hash_set);
            return false;
        }
        for(String token: tokens) {
            if(!isValidIPv6Token(token)){
                hash_set.add("U");
                System.out.println(hash_set);
                return false;
            }
        }
        hash_set.add("V");
        System.out.println(hash_set);
        return true;
    }
    private boolean isValidIPv6Token(String token) {
        if(token.length()>4 || token.length()==0){
            hash_set.add("W");
            System.out.println(hash_set);
            return false;
        }
        char[] chars = token.toCharArray();
        for(char c:chars) {
            boolean isDigit = c>=48 && c<=57;
            boolean isUppercaseAF = c>=65 && c<=70;
            boolean isLowerCaseAF = c>=97 && c<=102;
            if (!isDigit && !isUppercaseAF && !isLowerCaseAF) hash_set.add("X1");
            if (isDigit && !isUppercaseAF && !isLowerCaseAF) hash_set.add("X2");
            if (!isDigit && !isUppercaseAF && isLowerCaseAF) hash_set.add("X5");
            if (!isDigit && isUppercaseAF && !isLowerCaseAF) hash_set.add("X7");

            if(!(isDigit || isUppercaseAF || isLowerCaseAF)) {
                hash_set.add("X");
                System.out.println(hash_set);
                return false;
            }
            hash_set.add("Y");
            System.out.println(hash_set);
        }
        hash_set.add("Z");
        System.out.println(hash_set);
        return true;
    }
}
