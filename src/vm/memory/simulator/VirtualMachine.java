package vm.memory.simulator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
				//System.out.println(inst.toString());
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
	
	
	public static class InstructionLoader{
		
		int _pc = 0;
		List<IInstruction> _icache = new ArrayList<IInstruction>();
		
		int _base =0;
		public static final int BLOCK = 12;
		
		FileInputStream _fin;
		BufferedReader _reader ;
		
		public InstructionLoader(String fileName) throws FileNotFoundException{
			_fin = new FileInputStream(fileName);
			_reader = new BufferedReader(new InputStreamReader(_fin));
			_base = 0;
			loadNextBlock();
		}
		
		/**
		 *    Load another BLOCK instruction from trace files. Before loading, it clears existing instruction
		 * pools and updates cursor.
		 *   
		 * @return true if load success
		 *         false if end of file
		 */
		private boolean loadNextBlock(){
			String line;
			try {
				//Clear the instruction pool. 
				_icache.clear();
				int i=0;
				line = _reader.readLine();
				while(line!=null && !line.equals("") && i < BLOCK){
					IInstruction instr = InstructionFactory.createInstruction(line, this);
					instr.setPC(_base+i);
					_icache.add(instr);
					i++;
					if(i==BLOCK) break;
					line = _reader.readLine();
				}
				return _icache.size()!=0;
				//reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Loading instruction failure..");
				try {
					_reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return false;
			}
			
		}

		public IInstruction getNextInst(){
			int t = _pc++;
			return _icache.get(t-_base);
		}
		
		public boolean hasNextInst() throws IOException{
			if(!(_pc>=_base && _pc  < _base + _icache.size())){
				_base +=BLOCK;
				if(loadNextBlock() == false){
					return false;
				}
			}
			return true;
		}
		
		public void gotoAddress(int address) throws Exception{
			if(address <  0 || address > _pc) {
				//The JMP only goes back. 
				throw new Exception("Illegal instruction address.");
			}
			if( address >= _base && address <_pc ){
				//If instruction with address is already in the instruction cache
				_pc = address;
				return;
			}else{
				//Reload instruction.
				_fin.getChannel().position(0);
				move2Base(address);
				loadNextBlock();
				_pc =address;

			}
			
		}

		private boolean move2Base(int pc){
			_base = 0;
			if(pc<BLOCK) return true;
			
			int i=0;
			String line;
			try {
				line = _reader.readLine();
				while(line != null){
					if(line!= "") {
						i++;  pc--;
						if(i==BLOCK){
							_base++; i=0; //RECOUNT i value. 
						}
						if(pc<BLOCK){
							break;
						}
					}
					line = _reader.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			return false;
		}
		
		
		public int getPC(){
			return _pc;
		}
	}

	
	public static void main(String[] args){
		try {
			VirtualMachine.InstructionLoader loader = new VirtualMachine.InstructionLoader("/tmp/test.trace");
			
			while(loader.hasNextInst()){
				IInstruction inst = loader.getNextInst();
				System.out.println(inst.toString());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
