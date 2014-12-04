package assign6.shijie.memmanagement;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import assign6.shijie.memmanagement.smartallocation.Group;
import assign6.shijie.memmanagement.smartallocation.GroupRegion;
import assign6.shijie.memmanagement.smartallocation.SmartAgent;


public class SmartHeap extends Heap{

	private Map<Group, GroupRegion> _groups = new LinkedHashMap<Group, GroupRegion>();
	
	private int regionHeader = _size -1 ;
	
	
	public SmartHeap(int size){
		super(size);
	}
	
	@Override
	public int allocate(BaseInst instr){
		Group group = SmartAgent.getAgent().getProfileGroup(instr);
		if(group == null){
			System.err.println("The Profile Agent does not see this instructure previous. "+instr.toString());
			return -1;
			
		}else{
			GroupRegion region = _groups.get(group);
			
			if(region == null){
				if(regionHeader < _size ){
					//TODO: Region Size Overflow..
					return -1;
				}
				int start = regionHeader - group.estimateSize();
				
				if(start < _size ){
					start = _size;
				}
				
				region = new GroupRegion(start, group.estimateSize());
				_groups.put(group, region);
				
			}
			int size = instr._payLoad + 4 * instr._pointers;
			int address = _groups.get(group).allocate(size, instr._payLoad, instr._pointers, instr._objectId, instr._threadId);
			if(address == -1){
				//TODO: This should not be happen in our current version, because of trace file.. 
				System.err.println("Insufficient GroupRegion Size ");

			}
			return address;
		}
		
	}
	
	/**
	 *  GC the free heap space and GroupRegion spaces. 
	 */
	@Override
	public void gc(Set<ObjectNode> workings){
		for(GroupRegion region: _groups.values()){
			region.gc(workings);
		}
		super.gc(workings);
		
	}
	
}
