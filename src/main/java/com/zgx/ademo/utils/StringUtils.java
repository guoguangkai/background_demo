package com.zgx.ademo.utils;

import java.util.Random;

public class StringUtils {
    //生成指定长度随机字符串的方法：
    public static String getRandomString(int length) {
        String base = "fghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            //会随机生成一个整数，这个整数的范围就是int类型的范围-2^31 ~ 2^31-1,但是如果在nextInt()括号中加入一个整数a那么，这个随机生成的随机数范围就变成[0,a)。
            int number = random.nextInt(base.length());
            //此方法返回位于字符串的指定索引处的字符。该字符串的索引从零开始。
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
