package assign6.shijie.memmanagement.smartallocation;

import assign6.shijie.memmanagement.ObjectNode;

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
