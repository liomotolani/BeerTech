package com.beerkhaton.mealtrackerapi.util;

import java.util.Random;

public abstract class GenericUtil {


    public static String generateAlphaNumeric(int length){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "0123456789" + "abcdefghijklmnopqrstuvwxyz";

        StringBuilder sb = new StringBuilder(length);

        for(int i = 0; i < length; i++){
            int index
                    = (int) (AlphaNumericString.length()
            * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public static String generateStaffId(){
        return "EP" + randomNumber(5);
    }

    private static String randomNumber(int len) {
        final String stringNumber = "0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(len);
        for(int i = 0; i < len; i++){
            stringBuilder.append(stringNumber.charAt(random.nextInt(stringNumber.length())));
        }
        return stringBuilder.toString();
    }
}
