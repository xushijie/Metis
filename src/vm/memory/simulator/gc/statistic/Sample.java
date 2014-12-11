package vm.memory.simulator.gc.statistic;

import java.util.LinkedList;
import java.util.List;

import vm.memory.simulator.HeapNode;
import vm.memory.simulator.Node;


/**
 * Sample is bean encapuse after one GC cycle
 * @author shijiex
 *
 */
public class Sample {

	private List<Integer> _samples = new LinkedList<Integer>();
	private boolean _isProfile = true;
	public Sample(){
		
	}
	
	public Sample(boolean profile){
		_isProfile = profile;
	}
	
	public void addRecord(Node node){
		_samples.add(node.getLength());
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(_isProfile?"Profile  ":"Activation ");
		for(Integer intValue : _samples){
			builder.append(intValue).append(" ");
		}
		builder.append("\n");
		return builder.toString();
		
	}
}
