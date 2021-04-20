package com.Eremej;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ParseUtils {
    public interface NetworkFinder {
        boolean find();
        IPSubnet getSubnet();
    }

    public interface IPFinder {
        boolean find();
        String getIP();
    }

    public interface IPRangeFinder {
        boolean find();
        String getIPRange();
    }


    private static final String IP_DIGIT_REGEX = "\\d{1,3}";

    private static final String CIDR_PREFIX_REGEX = "/\\d{1,2}";

    private static final String IP_FULL_REGEX = IP_DIGIT_REGEX + "\\." + IP_DIGIT_REGEX + "\\." + IP_DIGIT_REGEX + "\\." + IP_DIGIT_REGEX;

    private static final String IP_RANGE_REGEX = IP_FULL_REGEX + "-" + IP_FULL_REGEX;

    private static final String NETWORK_REGEX = IP_FULL_REGEX + CIDR_PREFIX_REGEX;

    private static final String URL_REGEX = "^(https?://)?(www\\.)?([\\w-?!/=]+\\.[\\w-?!/=]+)+$";


    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    private static final Pattern IP_PATTERN = Pattern.compile(IP_FULL_REGEX);

    private static final Pattern NETWORK_PATTERN = Pattern.compile(NETWORK_REGEX);

    private static final Pattern RANGE_PATTERN = Pattern.compile(IP_RANGE_REGEX);


    public static NetworkFinder getNetworkParser(String input) {
        return new NetworkFinder() {
            private Matcher mMatcher = NETWORK_PATTERN.matcher(input);

            @Override
            public boolean find() {
                return mMatcher.find();
            }

            @Override
            public IPSubnet getSubnet() {
                Matcher ipMatcher = Pattern.compile(IP_FULL_REGEX).matcher(mMatcher.group());
                Matcher cidrMatcher = Pattern.compile(CIDR_PREFIX_REGEX).matcher(mMatcher.group());

                ipMatcher.find();
                cidrMatcher.find();

                return new IPSubnet(ipMatcher.group(), Integer.parseInt(cidrMatcher.group().substring(1)));
            }
        };
    }

    public static IPFinder getIPParser(String input) {
        return new IPFinder() {
            private Matcher mMatcher = IP_PATTERN.matcher(input);

            @Override
            public boolean find() {
                return mMatcher.find();
            }

            @Override
            public String getIP() {
                return mMatcher.group();
            }
        };
    }

    public static IPRangeFinder getIPRangeParser(String input) {
        return new IPRangeFinder() {
            private Matcher mMatcher = RANGE_PATTERN.matcher(input);

            @Override
            public boolean find() {
                return mMatcher.find();
            }

            @Override
            public String getIPRange() {
                return mMatcher.group();
            }
        };
    }

    public static String parseFirstIPRange(String input) {
        Matcher matcher = RANGE_PATTERN.matcher(input);
        if (matcher.find())
            return matcher.group();
        else
            return null;
    }

    public static String parseFirstIP(String input) {
        Matcher matcher = IP_PATTERN.matcher(input);
        if (matcher.find())
            return matcher.group();
        else
            return null;
    }

    public static String parseFirstURL(String input) {
        Matcher matcher = URL_PATTERN.matcher(input);
        if (matcher.find())
            return matcher.group();
        else
            return null;
    }
}
