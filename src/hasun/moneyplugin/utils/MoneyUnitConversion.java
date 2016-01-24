package hasun.moneyplugin.utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class MoneyUnitConversion {
    public static final int[] KOREA_UNIT = {10000, 5000, 1000, 500, 100, 10, 1};

    public static Map<Integer, Integer> convertToMoneyUnit(BigInteger amount, final int[] units) {
        BigInteger a = amount;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < units.length; i++) {
            BigInteger count = a.divide(BigInteger.valueOf(units[i]));
            a = amount.mod(BigInteger.valueOf(units[i]));
            map.put(units[i], count.intValue());
        }
        return map;
    }
}