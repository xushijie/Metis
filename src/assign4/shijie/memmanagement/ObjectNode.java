package assign4.shijie.memmanagement;

import java.util.Arrays;
import java.util.List;

public class ObjectNode extends Node {

	MyThread _thread;
	int _payout;
	int _referenceCount;
	Node[] _referNodes;
	
	public ObjectNode(MyThread thread, int payout, int references, int id, int start){
		super(start, payout + 4* references, id);
		_thread = thread;
		//_thread.addObj2Set(this);  //The thread is the one who creates it. This should not be added to thread's RootSet.
		_payout = payout;
		_referNodes = new Node[references];
	} 
	
	public ObjectNode(int len, int references, int id, int start){
		super(start, len, id);
		
		_referenceCount= references;
	}
	
	public ObjectNode(int id, int start, int len){
		super(start, len, id);
	}
	
	public void setReference( int count){
		_referenceCount = count;
	}

	public void setReference(int index, ObjectNode target) {
//		if(_referNodes[index] != null ){
//			System.out.println("reset id "+ _referNodes[index]._id);
//		}
		_referNodes[index] = target;
	}
	
	public List<Node> getReferedNodes(){
		return Arrays.asList(_referNodes);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString()).append(", pointers:"+_referNodes.length);
		return builder.toString();
		
	}
	
}
