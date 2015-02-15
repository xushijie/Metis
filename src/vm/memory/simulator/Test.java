package vm.memory.simulator;

public class Test {
	
	public static void print(Object... args){
		for(int i=0; i< args.length; i++)
			System.out.print(" "+ args[i].toString());
		
		System.out.println();
	}
	
	public static void main(String[] args){
		print("xu");
		print("xu", "shi");
		print("xu", "shijie", "jie");
		print(1, 2.0f, false);
		
	}

}
