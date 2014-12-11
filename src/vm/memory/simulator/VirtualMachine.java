package vm.memory.simulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VirtualMachine{

	static InstructionLoader _loader = null; 
	public VirtualMachine(){
		
	}
	
	public VirtualMachine(String instFile){
		try {
			_loader = new InstructionLoader(instFile);
			
			while(_loader.hasNextInst()){
				IInstruction inst = _loader.getNextInst();
				inst.run();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public class InstructionLoader{
		
		int _pc = 0;
		List<IInstruction> _insts = new ArrayList<IInstruction>();
		
		public InstructionLoader(String fileName) throws FileNotFoundException{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			try {
				int i=0;
				line = reader.readLine();
				while(line!=null && !line.equals("")){
					IInstruction instr = InstructionFactory.createInstruction(line, this);
					instr.setPC(i++);
					_insts.add(instr);
					
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		public IInstruction getNextInst(){
			return _insts.get(_pc++);
		}
		
		public boolean hasNextInst() throws IOException{
			return _pc>=0 && _pc < _insts.size(); 
		}
		
		public void gotoAddress(int address) throws Exception{
			
			if(address < 0 || address > _insts.size()){
				throw new Exception("Illegal instruction address.");
			}
			_pc = address;
		}
		
		public int getPC(){
			return _pc;
		}
	}
	
	
}
