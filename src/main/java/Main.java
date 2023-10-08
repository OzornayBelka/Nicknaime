import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger length3 = new AtomicInteger();
    public static AtomicInteger length4 = new AtomicInteger();
    public static AtomicInteger length5 = new AtomicInteger();
    private static final int counter1 = 3;
    private static final int counter2 = 4;
    private static final int counter3 = 5;


    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        var threadPalindrom = new Thread(() -> {
            for (String text : texts) {
                if (checkPalindrom(text)) {
                    incrementResult(text.length());
                }
            }
        });
        threadPalindrom.start();

        var treadIdentity = new Thread(() -> {
            for (String text : texts) {
                if (checkIdentity(text)) {
                    incrementResult(text.length());
                }
            }
        });
        treadIdentity.start();

        var treadIncrease = new Thread(() -> {
            for (String text : texts) {
                if (checkIncrease(text) && !checkIdentity(text)) {
                    incrementResult(text.length());
                }
            }
        });
        treadIncrease.start();

        threadPalindrom.join();
        treadIdentity.join();
        treadIncrease.join();
        printResult(counter1, length3);
        printResult(counter2, length4);
        printResult(counter3, length5);


    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean checkPalindrom(String text) {
        return text.equals(new StringBuilder(text).reverse().toString());
    }

    public static boolean checkIdentity(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1))
                return false;
        }
        return true;
    }

    public static boolean checkIncrease(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1))
                return false;
        }
        return true;
    }

    public static void incrementResult(int textLength) {
        switch (textLength) {
            case counter1 -> length3.getAndIncrement();
            case counter2 -> length4.getAndIncrement();
            case counter3 -> length5.getAndIncrement();
        }
    }

    public static void printResult(int counter, AtomicInteger result) {
        System.out.println("Красивых слов с длиной " + counter + " : " + result
                + " шт");
    }
}
