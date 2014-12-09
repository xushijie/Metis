package vm.memory.simulator;

abstract public class Node {

	int _start;
	int _length;
	
	int _id;
	
	public Node(int start, int len){
		_length = len ;
		_start = start;
	}
	
	public Node(int start, int len, int id){
		_id = id;
		_length = len ;
		_start = start;
	}
	
	/**
	 *  Allocate first len bytes for allocation.
	 * 
	 *  return true if this node should be removed from freelist
	 * @param len
	 */
	public boolean allocate(int len){
		if(_length > len){
			_start +=len;
			_length -= len;
			return false;
		}else if(_length == len){
			//This should not happend...
			//This branch should not be happen since we have perform condition check previously.
			System.err.println("Can not be happen");
		}
		return true;
	}
	
	public int getStartAddress(){
		return _start;
	}
	
	public int getLength(){
		return _length;
	}

	public int getId(){
		return _id;
	}
	
	public void increaseLength(int size){
		_length +=size;
	}
	
	/**
	 * 
	 * @param size
	 */
	public void resetAddress(int size){
		_start -=size;
		_length+=size;
		
	}
	
	public void setAddress(int startAddress){
		_start = startAddress;
	}
	/**
	 * Return true if this node is still >0 length
	 * Return false if it is =0. In this case, the Heap should remove it from freelist
	 * @return
	 */
	public boolean isAvailableNode(){
		return _length != 0;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(" id:").append(_id).append(",start:").append(_start).append(",len:"+_length);
		return builder.toString();
		
	}

}
