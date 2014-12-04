package vm.memory.simulator.smartallocation;

import vm.memory.simulator.ObjectNode;

public class Model {

	public PC _pc;
	public int _objectId;
	
	public int _size;
	
	public Model(ObjectNode object){
		_pc = object.getPC();
		_objectId = object.getId();
		_size = object.getLength();
	}
}
