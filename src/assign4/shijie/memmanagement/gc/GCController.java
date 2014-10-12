package assign4.shijie.memmanagement.gc;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import assign4.shijie.memmanagement.ObjectNode;


public class GCController {
	
	private static Set<ObjectNode> _workings = new LinkedHashSet<ObjectNode>();
	private static List<IGCAction> _gcActions = new LinkedList<IGCAction>();

	private static GCController  _instance = new GCController();
	
	static{
		_gcActions.add(new Marker(_workings));
		_gcActions.add(new Sweeper(_workings));
		_gcActions.add(new PostRunner(_workings));
	}
	
	private GCController(){
		
	}
	
	public static GCController getGCController(){
		return _instance;
	}
	
	
	public void run(){
		for(IGCAction gcAct : _gcActions ){
			gcAct.run();
		}
	}
	
	
}
