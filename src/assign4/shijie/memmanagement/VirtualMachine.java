package assign4.shijie.memmanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class VirtualMachine{

	public VirtualMachine(){
		
	}
	
	public VirtualMachine(String instFile){
		try {
			InstructionLoader loader = new InstructionLoader(instFile);
			
			while(loader.hasNextInst()){
				IInstruction inst = loader.getNextInst();
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
		
		BufferedReader reader = null;
		
		String _curr =  null;
		
		public InstructionLoader(String fileName) throws FileNotFoundException{
			reader = new BufferedReader(new FileReader(fileName));
		}
		
		
		public IInstruction getNextInst(){
			return InstructionFactory.createInstruction(_curr);
		}
		
		public boolean hasNextInst() throws IOException{
			try {
				_curr = reader.readLine();
				if(_curr == null ){
					reader.close();
				}
				return _curr!=null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				reader.close();
			}
			
			return false;
		}
	}
}
