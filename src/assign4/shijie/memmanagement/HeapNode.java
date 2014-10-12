package assign4.shijie.memmanagement;

/**
 *  This is heap Node for allocation
 * 
 * @author shijiex
 *
 */
public class HeapNode extends Node{

	public HeapNode(int start, int len) {
		super(start, len);
		// 
	}
/*	
	public Node allocate(int id, int len){
		if(_length > len ){
			Node node = new ObjectNode(id, _start, len);
			_start=_start + len;
			_length = _length -len ;
			return node;
		}
		return null;
	}
	*/
	/**
	 * 
	 * Merge two neighbour nodes: 
	 *      1) _start must be less than node._start
	 *      2) _start + _length == node._start
	 *      
	 * return true if compact success(Heap should remove node from freelist), otherwise false.
	 * @param node
	 * @return
	 */
	public boolean compact(Node node){
		if(_start+_length != node._start ){
			return false;
		}
		
		_length += node._length;
		return true;
	}

	
}
