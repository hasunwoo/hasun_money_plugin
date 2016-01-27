
import java.math.BigInteger;

public class BigIntegerUtil {
    public static long convertToLong(BigInteger bigInteger){
        if(bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE))==1){
            return Long.MAX_VALUE;
        }else{
            return bigInteger.longValue();
        }
    }
    public static double convertToDouble(BigInteger bigInteger){
        double d = bigInteger.doubleValue();
        if(d==Double.POSITIVE_INFINITY){
            return Long.MAX_VALUE;
        }else if(d==Double.NEGATIVE_INFINITY){
            return Long.MIN_VALUE;
        }
        return bigInteger.doubleValue();
    }
}
