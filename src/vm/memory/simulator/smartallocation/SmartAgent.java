package vm.memory.simulator.smartallocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import vm.memory.simulator.BaseInst;
import vm.memory.simulator.ObjectNode;
import vm.memory.simulator.gc.GCController;
import vm.memory.simulator.gc.IGCAction;

public class SmartAgent implements IGCAction{
	private static Map<SmartAction, IPhase> _actions = new LinkedHashMap<SmartAction, IPhase>();
	private static SmartAgent _instance = new SmartAgent();
	
	private boolean isProfiling = true;

	public static int GC_THRESHOLD =2;
	
	private static Set<ObjectNode> _workingNodes;
	
	private SmartAgent(){
		
		_actions.put(SmartAction.PROFILE, new ProfilePhase());
		_actions.put(SmartAction.ACTIVITATION, new ActivitationPhase());
	}
	
	public static SmartAgent getAgent(){
		return _instance;
	}
	
	public static void init (Set<ObjectNode> workingNodes){
		_workingNodes = workingNodes;
	}
	
	
	
	//Below are for Agent operations 
	//private Set<Group> _groups = new LinkedHashSet<Group>();
	private Map<PC, Group> _inference = new LinkedHashMap<PC, Group>();
	
	/**
	 * Retrieve the group for given instructon or PC.
	 * 
	 * @param inst
	 * @return
	 */
	public Group getProfileGroup(BaseInst inst){
		return _inference.get(inst.getProfilePC());
	}
	

	public void activate(){
		isProfiling = false;
	}
	
	enum SmartAction{
		PROFILE,
		ACTIVITATION
	}
	
	interface IPhase{
		public void run(Object obj);
	}
	
	
	private class ProfilePhase implements IPhase{
		@Override
		public void run(Object obj) {
			if (obj instanceof ObjectNode) {
				ObjectNode node = (ObjectNode) obj;
				PC pc = node.getPC();
				if (_inference.containsKey(pc)) {
					// Nothing to do here
					return;
				}
				// ???How to find a group for new PC..
				addDiscreatGraph2Group(node);

			}
		}

		/**
		 * Add all children of node to a group if it has not in the group. 
		 * Notice that some children might have been addded in the group.
		 * @param node
		 * @return
		 */
		private void addDiscreatGraph2Group(ObjectNode node){
			if(node==null ) return;
			Queue<ObjectNode> nodes = new LinkedList<ObjectNode>();
			Set<ObjectNode> objs = new LinkedHashSet<ObjectNode>();
			Group group = null;
			nodes.offer(node);
			//Children section
			while(nodes.peek()!=null){
				ObjectNode oNode = nodes.poll();
				objs.add(oNode);
				
				if(_inference.keySet().contains(oNode.getPC())){
					//I find one Group via current node children
					group = _inference.get(oNode.getPC());
				}else{
					//I try to find group via children PC of current Node. 
					Collection<PC> intersection = CollectionUtils.intersection(_inference.keySet() ,oNode.getChildPC());
					for(PC pc: intersection){
						group = _inference.get(pc);
						break;
					}
				}
				
				for(int i=0;i< oNode.getReferedNodes().size(); i++){
					if(oNode.getReferedNodes().get(i)!=null){
						nodes.offer((ObjectNode)oNode.getReferedNodes().get(i));
					}
				}
			}
			
			if(group==null){
				group = new Group();
			}
			
			for(ObjectNode myNode : objs){
				if(!_inference.containsKey(myNode.getPC())){
					_inference.put(myNode.getPC(), group);
				}
			}
		}
	}
	
	private class ActivitationPhase implements IPhase{
		@Override
		public void run(Object obj) {
			
			
		}
	}

	public boolean isProfilePhase() {
		// TODO Auto-generated method stub
		return isProfiling;
	}

	@Override
	public void run() {
		if(isProfiling){
			_actions.get(SmartAction.PROFILE).run(null);
		}
		//_actions.get(isProfiling==true?SmartAction.PROFILE:SmartAction.ACTIVITATION).run(node);
	}
}
