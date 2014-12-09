package vm.memory.simulator.smartallocation;

import java.util.LinkedHashSet;
import java.util.Set;

import vm.memory.simulator.ObjectNode;

public class Group {
	private Set<Model> _nodes = new LinkedHashSet<Model>();

	/**
	 * Indicate objects allocated by PC in parentPCs can also assigned to this group. 
	 * This is because we can not guarteen the free order. That is Children ObjectNode Can be reclaimed first..So..
	 */
	private Set<PC>  _parentPCs = new LinkedHashSet<PC>();
	
	private int _buff;
	
	public Group(){
		_buff = 0;
	}
	
	public void addNode(ObjectNode node){
		_nodes.add(new Model(node));
	}
	
	public int estimateSize(){
		int size =0 ;
		for(Model model: _nodes){
			size+=model._size;
		}
		return size+_buff;
	}
	
	public void addParentPC(Set<PC> pc){
		_parentPCs.addAll(pc);
	}
	
	public Set<PC> getParentPCSet(){
		return _parentPCs;
	}
	
}
