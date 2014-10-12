package assign4.shijie.memmanagement.gc;

import java.util.Set;

import assign4.shijie.memmanagement.Node;
import assign4.shijie.memmanagement.ThreadPool;
import assign4.shijie.memmanagement.MyThread;
import assign4.shijie.memmanagement.ObjectNode;

public class Marker implements IGCAction {


	Set<ObjectNode> _workings ;
	
	public Marker(Set<ObjectNode> sets){
		_workings = sets;
	}
	
	@Override
	public void run() {
		for(MyThread thread : ThreadPool.getThreads()){
			for(Node node : thread.getRootset()){
				if(node instanceof ObjectNode){
					ObjectNode oNode = (ObjectNode) node;
					if(oNode != null && !_workings.contains(oNode)){
						_workings.add(oNode);
						mark(oNode);
					}
				}
			}
		}
		
	}
	
	
	private void mark(ObjectNode oNode){
		if(oNode == null) return;
		
		for(Node node : oNode.getReferedNodes()){
			
			if(node == null) continue;
			
			if(!(node instanceof ObjectNode)){
				System.err.println("Encounter error typing ( ) "+ node.toString());
				continue;
			}
			
			ObjectNode childNode = (ObjectNode) node;
			
			if(!_workings.contains(childNode)){
				_workings.add(childNode);
				mark(childNode);
			}
			
		}
	}
}
