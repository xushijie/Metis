package vm.memory.simulator;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import vm.memory.simulator.gc.COLOR;
import vm.memory.simulator.gc.Candidates;
import vm.memory.simulator.smartallocation.PC;

public class ObjectNode extends Node {

	MyThread _thread;
	int _payout;
	int _referenceCount;  //Reference counting value   
	Node[] _referNodes;
	COLOR _color;
	
	//Below fields are used for smart allocation.. 
	PC _pc;   //PC that allocates this object
	Set<PC> _parentPCs = new LinkedHashSet<PC>();
	
	public ObjectNode(MyThread thread, int payout, int references, int id, int start){
		super(start, payout + 4* references, id);
		_thread = thread;
		//_thread.addObj2Set(this);  //The thread is the one who creates it. This should not be added to thread's RootSet.
		_payout = payout;
		_referNodes = new Node[references];
		
		_referenceCount = 0;
	} 
	
	private void increaseRC(boolean value){
		_referenceCount++;
		
	}
	public void increaseRC(){
		_referenceCount++;
		_color = COLOR.BLACK;
	}
	
	private  void decreaseRC(){
		_referenceCount--;
	}
	public void decreaseRC(int  level){
		decreaseRC();
		
		if(Heap._gcKind != GCKind.RC_RECYCLE){
//			if(_referenceCount ==0){
//				for(Node node: _referNodes){
//					if(node == null ) continue;
//					ObjectNode onode = (ObjectNode) node;
//					onode.decreaseRC(++level);
//				}
//				Heap.getHeap().free(_id);
//			}
			return;
		}else{
			if(_referenceCount == 0){
				for(Node node: _referNodes){
					if(node == null ) continue;
					ObjectNode onode = (ObjectNode) node;
					onode.decreaseRC(++level);
				}
				_color = COLOR.BLACK;
				if(!Candidates.get().isNodeContained(this)){
					Heap.getHeap().free(_id);
				}
			}else{
				Candidates.get().add(this);
			}
		}
		

	}
	
	public ObjectNode(int id, int start, int len){
		super(start, len, id);
		_referenceCount= 0;
	}
	

	public void setReference(int index, ObjectNode target) {
		if(Heap._gcKind == GCKind.RC_RECYCLE){
			target.increaseRC();
			if(_referNodes[index]!=null){
				ObjectNode node = (ObjectNode) _referNodes[index];
				node.decreaseRC(0);
			}	
		}else if(Heap._gcKind == GCKind.SMART){
			//Mark sweep with smart allocation
			ObjectNode orig = (ObjectNode) _referNodes[index];
			if(orig!=null){
				orig.removeParentPC(this);
				target.addNewParentPC(this);	
			}
			
		}
		
		_referNodes[index] = target;
	
	}
	
	public List<Node> getReferedNodes(){
		return Arrays.asList(_referNodes);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString()).append(", color:"+_color.toString()).append(" len: "+this._length);
		return builder.toString();
		
	}

	
	////////////////////////////////
	//Below methods for Candidates marker during recycle.
	
	public COLOR getColor() {
		// TODO Auto-generated method stub
		return _color;
	}
	
	public void setColor(COLOR color){
		_color = color;
		
	}
	
	public void markGrey(){
		if(_color != COLOR.GREY){
			_color = COLOR.GREY;
			for(int i=0; i<_referNodes.length; i++){
				if(_referNodes[i]!=null){
					ObjectNode child = (ObjectNode)_referNodes[i];
					child.decreaseRC();
					child.markGrey();
				}
			}
		}
	}
	
	public void markFree(){
		if(_color == COLOR.BLACK && this._referenceCount ==0){
			Heap.getHeap().free(_id);
		}
	}
	//////////////////////////////////////////

	// Below methods are for Scaner
	
	public int getRC() {
		return _referenceCount;
	}

	/**
	 * Repair references counts during trial Marker phases( rc--)
	 */
	public void scanBalck() {
		_color = COLOR.BLACK;
		for(int i=0; i<_referNodes.length; i++){
			if(_referNodes[i] == null ) continue;
			ObjectNode child = (ObjectNode)_referNodes[i];
			child.increaseRC(true);
			if(child.getColor() != COLOR.BLACK){
				child.scanBalck();
			}
			
		}
	}

	/**
	 *  Seem this node should be GCed.==> Its color becomes white  
	 *  This method is called during scaner phase. 
	 *  
	 */
	public void markWhite() {
		_color = COLOR.WHITE;
		for(int i=0; i<_referNodes.length; i++){
			if(_referNodes[i] == null ) continue;
			ObjectNode child = (ObjectNode)_referNodes[i];
			Candidates.scaner(child);
		}
	}

	
	/**
	 *  This is for collect phase..
	 */
	public void collectWhite() {
		if(_color == COLOR.WHITE && Candidates.get().isNodeContained(this) ==false){
			_color = COLOR.BLACK;
			for(int i=0; i<_referNodes.length; i++){
				if(_referNodes[i] == null ) continue;
				ObjectNode child = (ObjectNode)_referNodes[i];
				child.collectWhite();
			}
			Heap.getHeap().free(_id);
		}
		
	}
	
	//Below methods are for smart allocation
	/**
	 *  Tracks all 
	 * @return
	 */
	public Set<PC> getParentPCs() {
		return  _parentPCs;
	}

	private void addNewParentPC(ObjectNode target) {
		_parentPCs.add(target.getPC());
	}
	
	private void removeParentPC(ObjectNode target) {
		_parentPCs.remove(target.getPC());
	}
	public void setPC(BaseInst instr) {
		_pc = new PC(instr);
	}

	public PC getPC(){
		
		return _pc;
	}
}
