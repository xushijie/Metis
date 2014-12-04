package vm.memory.simulator;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import vm.memory.simulator.VirtualMachine.InstructionLoader;

public class InstructionFactory {

	private static Map<String, Class> _maps = new LinkedHashMap<String, Class>();
	
	static {
		_maps.put("a", AlloInst.class);
		_maps.put("+", PlusInst.class);
		_maps.put("-", SubInst.class);
		_maps.put("r", ResetInst.class);
		_maps.put("j", JmpInst.class);
	}
	
	
	
	public static IInstruction createInstruction(String line, InstructionLoader loader){
		if(line.trim().equals("") || line.indexOf(" ") ==-1) return null; 
		String action = line.trim().substring(0, line.indexOf(" "));
		
		try {
			BaseInst baseInst = (BaseInst) _maps.get(action).
					getConstructor(String.class, int.class).newInstance(line, loader.getPC());
			baseInst.setLoader(loader);
			return baseInst;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
}
