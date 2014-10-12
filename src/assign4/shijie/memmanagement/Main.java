package assign4.shijie.memmanagement;

import java.io.IOException;

public class Main {
	
	
	public static void main(String[] args) throws IOException {
		
		if(args.length != 4 ){
			System.err.println("java Main fileName heapSize");
			System.exit(-1);
		}
		
		String fileName = args[0];
		int size = Integer.parseInt(args[1]);
		double p = Double.parseDouble(args[2]);
		double t = Double.parseDouble(args[3]);
		Heap.init(size, p, t);
		
		new VirtualMachine(fileName);
		System.out.println("execution complete ");
	}

}
