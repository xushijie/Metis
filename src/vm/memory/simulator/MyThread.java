package vm.memory.simulator;

import java.util.LinkedHashSet;
import java.util.Set;

public class MyThread {

	private int _id;
	private Set<Node> _nodes = new LinkedHashSet<Node>();
	
	public MyThread(int id){
		_id = id;
	}

	public Integer getId() {
		// TODO Auto-generated method stub
		return _id;
	}

	public void addObj2Set(Node obj) {
		if(obj instanceof ObjectNode){
			ObjectNode node = (ObjectNode) obj;
			_nodes.add(obj);
			node.increaseRC();
		}
		
	}

	public void removeRootSet(Node obj) {
		if(obj instanceof ObjectNode){
			ObjectNode node = (ObjectNode) obj;
			_nodes.remove(obj);
			node.decreaseRC(0);
		}
		
		
	
	}
	
	public Set<Node> getRootset(){
		return _nodes;
	}
}
