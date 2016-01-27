import java.math.BigInteger;

public class Main {
    public static void main(String[] args){
        BigInteger bigInteger = new BigInteger("9999999999999999999999999999565600000000000");
        System.out.printf("dexp: %.0f\n",(double)BigIntegerUtil.convertToLong(bigInteger));
        System.out.println(BigIntegerUtil.convertToLong(bigInteger));
    }
}
