package vm.memory.simulator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import vm.memory.simulator.gc.GCController;
import vm.memory.simulator.gc.statistic.Sample;
import vm.memory.simulator.gc.statistic.Tools;
import vm.memory.simulator.smartallocation.Group;
import vm.memory.simulator.smartallocation.GroupRegion;
import vm.memory.simulator.smartallocation.SmartAgent;


public class SmartHeap extends Heap{

	private Map<Group, GroupRegion> _groups = new LinkedHashMap<Group, GroupRegion>();
	
	private int _regionSize;
	private int _occupiedRegionSize=0;
	public SmartHeap(int size){
		super(size);
		_regionSize = 0;
		_occupiedRegionSize = 0;
		
	}

	/*
	 * The allocation here is mainly for group block region. 
	 * (non-Javadoc)
	 * @see vm.memory.simulator.Heap#allocate(vm.memory.simulator.BaseInst)
	 */
	@Override
	public int allocate(BaseInst instr){
		int size = Util.size(instr._payLoad, instr._pointers);
		Group group = SmartAgent.getAgent().getProfileGroup(instr);
		if(group == null){
			//System.err.println("The Profile Agent does not see this instructure previous. "+instr.toString());
			return defaultAllocation(size, instr._payLoad, instr._pointers, instr._objectId, instr._threadId, instr._pc);
			
		}else{
			GroupRegion region = _groups.get(group);
			if(region == null){
				int start = allocate(group.estimateSize());
				if(start ==-1) {
					/**
					 *  Insufficient space for whole region allocation..
					 *  Go to default way..
					 */
					return defaultAllocation(size, instr._payLoad, instr._pointers, instr._objectId, instr._threadId, instr.getPC());
				}
				
				region = new GroupRegion(start, group.estimateSize());
				_regionSize+=group.estimateSize();
				_groups.put(group, region);
				
			}
		
			int address = region.allocate(size, instr._payLoad, instr._pointers, instr._objectId, instr._threadId);
			if(address == -1){
				/**
				 * Group is full.. Then i need to do the normal way allocation.
				 */
				address = defaultAllocation(size, instr._payLoad, instr._pointers, instr._objectId, instr._threadId, instr._pc);
			}else{
				_occupiedSize+=size;
			}
			
			return address;
		}
		
	}
	
	private int defaultAllocation(int size, int payLoad, int points, int objectId, int threadid, int pc){
		//Allocate in non-Region space 
		int address = allocate(size);
		if(address == -1){
			/**
			 * Default from free list failure.. Then fill it in a group..
			 * First come first fill..
			 */
			for(Map.Entry<Group, GroupRegion> entry: _groups.entrySet()){
				if(entry.getValue().getFreeSize()>size){
					address = entry.getValue().allocate(size, payLoad, points, objectId, threadid);
					if(address!=-1){
						_occupiedSize+=size;
						return address;
					}
				}
			}
		}
		if(address == -1 ){
			int gcBefore = _occupiedSize;
			int groupSizeBefore = 0;
			int occupiedgroupSizeBefore = 0;
			for(Map.Entry<Group, GroupRegion> entry: _groups.entrySet()){
				groupSizeBefore+=entry.getValue().getSize();
				occupiedgroupSizeBefore+=(entry.getValue().getSize()-entry.getValue().getFreeSize());
			}
			int groupNumber = _groups.entrySet().size();
			/**
			 *  The output format: 
			 *  Inst Number, "HeapSize" 
			 *  "total occupiedSize" "OccupiedSize From GroupRegion"  "GroupSize" "Group number" 
			 *  "Total OccupiedSize after GC" "GroupSize after GC"  "Group Number"  "Occupied RegionGroup size After GC"
			 *    
			 */
			GCController.getGCController().run(_gcKind);
			
			int groupSize=0;
			int occupiedgroupSize = 0;
			for(Map.Entry<Group, GroupRegion> entry: _groups.entrySet()){
				groupSize+=entry.getValue().getSize();
				occupiedgroupSize+=(entry.getValue().getSize()-entry.getValue().getFreeSize());
			}
			
			/**
			 * Make statistics.
			 */
			Sample sample = new Sample(false);
			afterGC(sample);
			Tools.getTools().addSample(sample);
			
			System.out.println("Inst number: " +pc+" SmartHeap "+
					            " occupied_size_before: " + gcBefore +  " occupied_region_size: "+occupiedgroupSizeBefore+
					            " group_size_before: " + groupSizeBefore+" Group_Number_before: "+groupNumber+
					            " occupied_size_after: "+_occupiedSize+" group_size_after: "+groupSize+
						       " group number: "+ _groups.entrySet().size()+ " occupied_region_group_size: "+occupiedgroupSize);
			address = allocate(size);
			if(address == -1) {
				for(Map.Entry<Group, GroupRegion> entry: _groups.entrySet()){
					if(entry.getValue().getFreeSize()>size){
						address = entry.getValue().allocate(size, payLoad, points, objectId, threadid);
						if(address!=-1){
							_occupiedSize+=size;
							return address;
						}
					}
				}
			} 
		}

		if(address == -1 ) 
			return -1;
		
		ObjectNode node = new ObjectNode(ThreadPool.getThread(threadid), payLoad,points,
				objectId, address);
	
		_workingNodes.put(node.getId(), node);
		_occupiedSize+=node.getLength();	
		
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
			int origSize = region.getFreeSize();
			
			region.gc(workings);
			
			//Here I update thye occupiedSize since the free method does not update it.
			_occupiedSize-= (region.getFreeSize() - origSize);
			if(region._workingNodes.size() == 0){
				//Remove region Group from _groups
				iter.remove();
				_regionSize-=region.getSize();
				//Add memory of regionGroup back to free list..
				free(region.getStartAddress(), region.getSize());
				//System.out.println("Deallocation Group: ("+region.getStartAddress()+","+region.getSize()+")");
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
	
	@Override
	public void afterGC(Sample sample){
		for( GroupRegion entry: _groups.values()){
			entry.afterGC(sample);
		}
		
		super.afterGC(sample);
	}
	
}
