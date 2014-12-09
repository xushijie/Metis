package vm.memory.simulator.smartallocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import vm.memory.simulator.BaseInst;
import vm.memory.simulator.GCKind;
import vm.memory.simulator.Heap;
import vm.memory.simulator.IMemoryHook;
import vm.memory.simulator.ObjectNode;

public class SmartAgent implements IMemoryHook {
	private static Map<SmartAction, IPhase> _actions = new LinkedHashMap<SmartAction, IPhase>();
	private static SmartAgent _instance = new SmartAgent();

	private boolean isProfiling = true;

	public static int GC_THRESHOLD = 2;
	//
	private static Set<ObjectNode> _workingNodes;

	//
	private SmartAgent() {

		_actions.put(SmartAction.PROFILE, new ProfilePhase());
		_actions.put(SmartAction.ACTIVITATION, new ActivitationPhase());
	}

	public static SmartAgent getAgent() {
		return _instance;
	}

	public static void init(Set<ObjectNode> workingNodes) {
		if (Heap._gcKind.equals(GCKind.SMART))
			_workingNodes = workingNodes;
	}

	// Below are for Agent operations
	// private Set<Group> _groups = new LinkedHashSet<Group>();
	private Map<PC, Group> _inference = new LinkedHashMap<PC, Group>();

	/**
	 * Retrieve the group for given instructon or PC.
	 * 
	 * @param inst
	 * @return
	 */
	public Group getProfileGroup(BaseInst inst) {
		return _inference.get(inst.getProfilePC());
	}

	public void activate() {
		isProfiling = false;
	}

	enum SmartAction {
		PROFILE, ACTIVITATION
	}

	interface IPhase {
		public void run(Object obj);
	}

	private class ProfilePhase implements IPhase {

		@Override
		public void run(Object obj) {
			if (obj instanceof ObjectNode) {
				addDiscreatGraph2Group((ObjectNode) obj);

			}
		}

		private void iterateChildSet(Set<ObjectNode> set, ObjectNode node,
				Set<PC> graphPC, Set<PC> parentPCs) {
			if (node == null)
				return;

			// Reach boundary of node graph. The node is still alive..
			if (_workingNodes.contains(node))
				return;

			set.add(node);
			graphPC.add(node.getPC());
			parentPCs.addAll(node.getParentPCs());
			parentPCs.removeAll(graphPC);

			for (int i = 0; i < node.getReferedNodes().size(); i++) {
				ObjectNode childNode = (ObjectNode) node.getReferedNodes().get(
						i);
				if (childNode != null && !set.contains(childNode)) {
					iterateChildSet(set, childNode, graphPC, parentPCs);
				}
			}
		}

		private Group findGroupFromParentGC(Set<PC> currentPC) {
			for (Group group : _inference.values()) {
				Collection<PC> intersection = CollectionUtils.intersection(
						currentPC, group.getParentPCSet());
				if (intersection.size() != 0) {
					return group;
				}
			}

			return null;
		}

		/**
		 * Add all children of node to a group if it has not in the group.
		 * Notice that some children might have been addded in the group.
		 * 
		 * @param node
		 * @return
		 */
		private void addDiscreatGraph2Group(ObjectNode node) {
			if (node == null || _inference.keySet().contains(node.getPC()))
				return;

			Set<ObjectNode> objs = new LinkedHashSet<ObjectNode>();
			Set<PC> currentPC = new LinkedHashSet<PC>();
			Set<PC> parentPCs = new LinkedHashSet<PC>();

			// 1, Iterate PCs in the graph, and parent PC not in PC.
			iterateChildSet(objs, node, currentPC, parentPCs);

			// 2, Which group should i use for current group
			// Three Case:
			/**
			 *   O1  O 
			 *  / \ / \
			 * O   O2 O 
			 *      \ / 
			 *      O3
			 * 
			 * If node points to different O1 O2 O3, and OX might be in
			 * different region(Already in the group, alive objects, deads)
			 */
			Collection<PC> intersection = CollectionUtils.intersection(
					_inference.keySet(), currentPC);
			Group group = null;
			for (PC pc : intersection) {
				group = _inference.get(pc);
				break;
			}

			if (group == null) {
				group = findGroupFromParentGC(currentPC);
				if (group == null)
					group = new Group();
			}

			// Add node in current Graph into _inference and selected group.
			for (ObjectNode graphNode : objs) {
				group.addNode(graphNode);
				if (!_inference.containsKey(graphNode.getPC())) {
					_inference.put(graphNode.getPC(), group);
				}
			}

			group.addParentPC(parentPCs);

		}

	}

	private class ActivitationPhase implements IPhase {
		@Override
		public void run(Object obj) {

		}
	}

	public boolean isProfilePhase() {
		// TODO Auto-generated method stub
		return isProfiling;
	}

	@Override
	public void run(ObjectNode node) {
		if (Heap._gcKind == GCKind.SMART && isProfiling) {
			_actions.get(SmartAction.PROFILE).run(node);
		}
	}
}
