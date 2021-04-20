package com.Eremej;

import java.util.List;

public abstract class SubnetUtils {
    private static final List<Integer> DIGITS_MAPPING = List.of(new Integer[]{
            0,
            128,
            192,
            224,
            240,
            248,
            252,
            254,
            255
    });

    public static int getCIDR(String mask) {
        String[] digits = mask.split("[0-9][0-9]?[0-9]?");
        int cidr = 0;

        for (String digit : digits)
            cidr += DIGITS_MAPPING.indexOf(Integer.parseInt(digit));

        return cidr;
    }

    public static String getMask(int cidr) {
        StringBuilder maskBuilder = new StringBuilder();

        int fullDigitsCount = cidr / 8;
        int lastDigit = cidr - fullDigitsCount * 8;

        for (int i = 0; i < 4; i++) {
            if (fullDigitsCount-- > 0)
                maskBuilder.append("255");
            else {
                maskBuilder.append(DIGITS_MAPPING.get(lastDigit));
                lastDigit = 0;
            }
            if (i < 3)
                maskBuilder.append(".");
        }

        return maskBuilder.toString();
    }
}