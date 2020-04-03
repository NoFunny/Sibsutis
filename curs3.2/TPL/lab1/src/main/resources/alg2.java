public class Main {
    public static void main(String[] args) {
        int a = 54;
        float b = 23.4;
        while (a != b) {
            if (a > b) {
                a = a - b;
            } else if (a < b) {
                b = b - a;
            }
        }
        println(a);
    }
}
