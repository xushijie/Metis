package assign4.shijie.memmanagement;

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
		_nodes.add(obj);
	}

	public void removeRootSet(Node obj) {
		_nodes.remove(obj);
	}
	
	public Set<Node> getRootset(){
		return _nodes;
	}
}
