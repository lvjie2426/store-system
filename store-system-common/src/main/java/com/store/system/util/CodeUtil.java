package com.store.system.util;

import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WidthCodedPainter;

import java.util.Random;

public class CodeUtil {

    private static final int maxLength = 5;

    private static final int[] code = new int[]{7, 9, 6, 2, 8 , 1, 3, 0, 5, 4};

    private static String toCode(Long id) {
        String idStr = id.toString();
        StringBuilder idsbs = new StringBuilder();
        for (int i = idStr.length() - 1 ; i >= 0; i--) {
            idsbs.append(code[idStr.charAt(i)-'0']);
        }
        return idsbs.append(getRandom(maxLength - idStr.length())).toString();
    }

    private static String getDateTime(){
        return String.valueOf(System.currentTimeMillis());
    }

    public static long getRandom(long len) {
        long min = 1,max = 9;
        for (int i = 1; i < len; i++) {
            min *= 10;
            max *= 10;
        }
        long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min ;
        return rangeLong;
    }

    public static String getCode(){
        //id = id == null ? 1000 : id;
        int count = 0;
        String result = "";
        try {
            Random random = new Random();
            for(int i = 0; i < 100; i++) {
                count = random.nextInt(51) + 10;
            }
            result = getDateTime() + toCode(Long.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getCode());

        JBarcode productBarcode = new JBarcode(Code128Encoder.getInstance(),
                WidthCodedPainter.getInstance(),
                EAN13TextPainter.getInstance());



    }

}
