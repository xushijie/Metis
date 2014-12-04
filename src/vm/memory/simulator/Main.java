package vm.memory.simulator;

import java.io.IOException;


public class Main {
	
	
	public static void main(String[] args) throws IOException {
		
		if(args.length != 5 ){
			System.err.println("java Main fileName heapSize increaseRatio threashold GCkind");
			
			System.err.println("GCKind:  0 => Mark_swap;  1 => COPY_REFERENCE;  2=>referenceCounting+RECCYCLE");
			
			System.exit(-1);
		}
		
		String fileName = args[0];
		int size = Integer.parseInt(args[1]);
		double p = Double.parseDouble(args[2]);
		double t = Double.parseDouble(args[3]);
		GCKind kind  = GCKind.MARK_SWAP;
		if(Integer.parseInt(args[4])==1) kind = GCKind.COPY_REFERENCE;
		else if(Integer.parseInt(args[4]) ==2)  kind = GCKind.RC_RECYCLE;
		else if(Integer.parseInt(args[4])==3) kind =GCKind.SMART;
		
//		boolean  recycle = Integer.parseInt(args[4])==1?true:false;
//		
//		Configuration.init(kind, recycle);
		
		Heap.init(size, p, t, kind);
		new VirtualMachine(fileName);
		System.out.println("execution complete ");
	}

}
