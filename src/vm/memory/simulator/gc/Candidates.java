package vm.memory.simulator.gc;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import vm.memory.simulator.Node;
import vm.memory.simulator.ObjectNode;

public class Candidates {

	private Set<ObjectNode> _sets = new LinkedHashSet<ObjectNode>();
	private static  Candidates _instance = new Candidates();
	
	private Candidates(){
		
	}
	
	public static Candidates get(){
		return _instance;
	}
	
	
	public boolean isNodeContained(Node node){
		return _sets.contains(node);
	}

	public void add(ObjectNode objectNode) {
		if(objectNode.getColor()!= COLOR.PURPLE){
			objectNode.setColor(COLOR.PURPLE);
			_sets.add(objectNode);
		}
	}

	public void marker() {
		Iterator<ObjectNode> iter = _sets.iterator();
		while(iter.hasNext()){
			ObjectNode node = iter.next();
			if(node.getColor() == COLOR.PURPLE){
				//node.setColor(COLOR.GREY);
				node.markGrey();
			}else{
				iter.remove();
				node.markFree();
			}
		}
	}

	public void scaner() {
		for(ObjectNode node : _sets){
			scaner(node);
		}
	}
	
	public static void scaner(ObjectNode node){
		
		if(node == null || node.getColor() != COLOR.GREY){
			return;
		}
		
		if(node.getRC() > 0 ){
			/* Node is still active and there are external references to it */
			node.scanBalck();
		}else{
			/*It is gargabage ..*/
			node.markWhite();
		}
	}

	/**
	 *  Collect white object nodes...
	 */
	public void collect() {
		Iterator<ObjectNode> iter  = _sets.iterator();
		while(iter.hasNext()){
			ObjectNode node = iter.next();
			iter.remove();
			node.collectWhite();
		}
	}
	
	
}
