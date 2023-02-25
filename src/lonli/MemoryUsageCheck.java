package lonli;

public class MemoryUsageCheck {
	
	public static int percent() {
		Runtime runtime = Runtime.getRuntime();
		
		long memoryMax = runtime.maxMemory();
		int percent = (int) Math.round((used() * 100.0) / memoryMax);
		
		return percent;
	}
	
	public static long used() {
		Runtime runtime = Runtime.getRuntime();
		
		long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
		
		return memoryUsed;
	}
	
}