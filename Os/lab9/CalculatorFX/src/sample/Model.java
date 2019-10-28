package sample;

import java.math.BigInteger;

public class Model {
    public BigInteger calculation(BigInteger a, BigInteger b, String operator) {
        switch (operator) {
            case "+":
                return a.add(b);
            case "-":
                return a.subtract(b);
            case "*":
                return a.multiply(b);
            case "/":
                if (b.compareTo(BigInteger.ONE) == 0) return new BigInteger("0");
                return a.divide(b);
        }
        System.out.println("Unknown operator" + operator);
        return new BigInteger("0");
    }
}
