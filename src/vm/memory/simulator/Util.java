package vm.memory.simulator;



public class Util {

	/**
	 * This is used to calculate the object size for allocation. 
	 * Due to some bugs in the Trace collection, the pointers might be exceeds than 1000, which is not correct. 
	 * We template reset the pointers value to be 90 if its value is greater than 200 
	 * @param payout
	 * @param pointers
	 * @return
	 */
	public static int size(int payout, int pointers){
		if(pointers> 100) return 12;
		return pointers;
		
	} 
}
