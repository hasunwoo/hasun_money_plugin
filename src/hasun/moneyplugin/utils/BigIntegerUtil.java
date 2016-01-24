package hasun.moneyplugin.utils;

import java.math.BigInteger;

public class BigIntegerUtil {
    public static long convertToLong(BigInteger bigInteger) {
        if (bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) == 1) {
            return Long.MAX_VALUE;
        } else {
            return bigInteger.longValue();
        }
    }

    public static double convertToDouble(BigInteger bigInteger) {
        return (double) convertToLong(bigInteger);
    }
}
