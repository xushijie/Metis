package vm.memory.simulator.gc;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import vm.memory.simulator.GCKind;
import vm.memory.simulator.ObjectNode;
import vm.memory.simulator.smartallocation.SmartAgent;


public class GCController {
	
	private static int gcCount=0; 
	
	private static Set<ObjectNode> _workings = new LinkedHashSet<ObjectNode>();

	private static GCController  _instance = new GCController();
	
	private static Map<GCKind, IGC> _actions = new LinkedHashMap<GCKind, IGC>();
	private static MarkSweepGC _msGC = new  MarkSweepGC();
	static{
		_actions.put(GCKind.MARK_SWAP, _msGC);
		_actions.put(GCKind.COPY_REFERENCE, new CopyReferenceGC());
		_actions.put(GCKind.RC_RECYCLE, new RCRecycler());
		_actions.put(GCKind.SMART, _msGC);
		SmartAgent.init(_workings);
	}
	
	private GCController(){
		
	}
	
	public static GCController getGCController(){
		return _instance;
	}
	
	public static Set<ObjectNode> getWorkings(){
		return _workings;
	}
	
	public void run(){
		gcCount++;
		_actions.get(GCKind.MARK_SWAP).run();
	}
	
	public void run(GCKind gc){
		gcCount++;
		_actions.get(gc).run();
	}
	
	public static int getGCCount(){
		return gcCount;
	}
	
	interface IGC{
		public void run();
	}

}
