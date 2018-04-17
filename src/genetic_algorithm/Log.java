package genetic_algorithm;

public class Log {
    public static void debugMsg(String msg) {
        boolean debugOn = true;
        if (debugOn) {
            System.out.println("* * * " + msg);
        }
    }
}
