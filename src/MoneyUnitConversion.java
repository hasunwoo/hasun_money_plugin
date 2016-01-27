import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class MoneyUnitConversion {
    public static final int[] KOREA_UNIT = {10000,5000,1000,500,100,10,1};
    public static Map<Integer, BigInteger> convertToMoneyUnit(BigInteger amount, final int[] units){
        Map<Integer, BigInteger> map = new HashMap<Integer, BigInteger>();
        for(int i=0;i<units.length;i++){
            BigInteger count = amount.divide(BigInteger.valueOf(units[i]));
            amount = amount.mod(BigInteger.valueOf(units[i]));
            map.put(units[i],count);
        }
        return map;
    }
}
