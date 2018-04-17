package genetic_algorithm;

public class Timer {
    
    private static Long start;
   
    
    public Timer() {
        
    }
    
    public static void start() {
        Timer.start = System.currentTimeMillis();
    }
    
    public static void end() {
        System.out.format("Time taken %d", System.currentTimeMillis() - Timer.start);
    }
    
    public static long getTime() {
        return System.currentTimeMillis() / 1000L;
    }
    
}
