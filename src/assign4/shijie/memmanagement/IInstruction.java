package assign4.shijie.memmanagement;

public interface IInstruction {
	public static String THREAD = "T";
	public static String OBJECT = "O";
	public static String PAYLOAD = "S";
	public static String POINTS = "N";
	public static String SOURCEID = "P";
	public static String INDEXID = "#";
	
	public void run();
	public Action getAction();
}
