package vm.memory.simulator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import vm.memory.simulator.smartallocation.Group;
import vm.memory.simulator.smartallocation.GroupRegion;
import vm.memory.simulator.smartallocation.SmartAgent;


public class SmartHeap extends Heap{

	private Map<Group, GroupRegion> _groups = new LinkedHashMap<Group, GroupRegion>();
	
	public SmartHeap(int size){
		super(size);
	}

	/*
	 * The allocation here is mainly for group block region. 
	 * (non-Javadoc)
	 * @see vm.memory.simulator.Heap#allocate(vm.memory.simulator.BaseInst)
	 */
	@Override
	public int allocate(BaseInst instr){
		int size = instr._payLoad + 4 * instr._pointers;
		Group group = SmartAgent.getAgent().getProfileGroup(instr);
		if(group == null){
			//System.err.println("The Profile Agent does not see this instructure previous. "+instr.toString());
			return defaultAllocation(size, instr._payLoad, instr._pointers, instr._objectId, instr._threadId);
			
		}else{
			GroupRegion region = _groups.get(group);
			if(region == null){
				//1, First allocate Region Space from Free List
				int start = allocate(group.estimateSize());
				if(start ==-1) {
					/**
					 *  Insufficient space for whole region allocation..
					 *  Go to default way..
					 */
					return defaultAllocation(size, instr._payLoad, instr._pointers, instr._objectId, instr._threadId);
				}
				
				region = new GroupRegion(start, group.estimateSize());
				_occupiedSize+=region.getSize();
				_groups.put(group, region);
				
			}
		
			int address = _groups.get(group).allocate(size, instr._payLoad, instr._pointers, instr._objectId, instr._threadId);
			if(address == -1){
				/**
				 * Group is full.. Then i need to do the normal way allocation.
				 */
				address = defaultAllocation(size, instr._payLoad, instr._pointers, instr._objectId, instr._threadId);
			}
			return address;
		}
		
	}
	
	private int defaultAllocation(int size, int payLoad, int points, int objectId, int threadid){
		//Allocate in non-Region space 
		int address = allocate(size, payLoad, points, objectId, threadid);
		if(address == -1){
			/**
			 * Default from free list failure.. Then fill it in a group..
			 * First come first fill..
			 */
			Group candidate =  null;
			for(Map.Entry<Group, GroupRegion> entry: _groups.entrySet()){
				if(entry.getValue().getFreeSize()>size){
					address = entry.getValue().allocate(size, payLoad, points, objectId, threadid);
				}
			}
		}
		return address;
	}
	
	/**
	 *  GC the free heap space and GroupRegion spaces. 
	 */
	@Override
	public void gc(Set<ObjectNode> workings){
		Iterator<Group> iter = _groups.keySet().iterator();
		while(iter.hasNext()){
			GroupRegion region = _groups.get(iter.next());
			int size = region._workingNodes.size();
			region.gc(workings);
			if(region._workingNodes.size() == 0){
				//System.out.println("The region "+region.toString()+" are whole GCed!  size:"+size);
				//Remove region Group from _groups
				iter.remove();
				//Add memory of regionGroup back to free list..
				free(region.getStartAddress(), region.getSize());
				_occupiedSize-=region.getSize();
				//System.out.println("Deallocation Group: ("+region.getStartAddress()+","+region.getSize()+")");
			}else{
				//System.out.println("The region "+region.toString()+" are partial GCed! size "+ size);
			}
		}

		super.gc(workings);
		
	}
	
	@Override
	public Node getObject(int objectId) {
		if(_workingNodes.containsKey(objectId)){
			return super.getObject(objectId);
		}else{
			for(GroupRegion region : _groups.values()){
				if(region._workingNodes.containsKey(objectId)){
					return region.getObject(objectId);
				}
			}
		}
		return null;
	}
	
}
