package vm.memory.simulator.gc.statistic;

import java.util.LinkedList;
import java.util.List;

public class Tools {

	List<Sample> _samples = new LinkedList<Sample>();
	
	private static Tools _instance = new Tools();
	private Tools(){
		
	}
	
	public static Tools getTools(){
		return _instance ;
	}
	
	
	public void addSample(Sample sample){
		_samples.add(sample);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(Sample sample: _samples){
			builder.append(sample.toString());
		}
		return builder.toString();
	}
	
}
