package test_algorithm;

public class test {
    public static void main(String[] args) {
        System.out.println(fibNaive(4));
    }

    static long fibNaive(int n){
        if (n <= 1)
            return 1;
        return fibNaive(n - 1) + fibNaive(n - 2);
    }
}
