package assign6.shijie.memmanagement.smartallocation;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import assign6.shijie.memmanagement.BaseInst;

public class PC {
	public int _pc;
	public BaseInst _inst;
	
	public PC(BaseInst inst){
		_pc = inst.getPC();
		_inst = inst;
	}

	@Override
	public boolean equals(Object obj){
		if (obj instanceof PC){
			return ((PC) obj)._inst.equals(this._inst);
		} 
		return false;
	}
	
	@Override
	public int hashCode(){
		 return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
		            append(_pc).
		            append(_inst).
		            toHashCode();
	}
	
	@Override
	public String toString(){
		return _inst.toString();
	}
}
