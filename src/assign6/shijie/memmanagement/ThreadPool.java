package assign6.shijie.memmanagement;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThreadPool {

	private static Map<Integer, MyThread> _pool = new LinkedHashMap<Integer, MyThread>();
	
	public static MyThread getThread(int threadId){
		if(_pool.containsKey(threadId)){
			return _pool.get(threadId);
		}else{
			MyThread thread = new MyThread(threadId);
			_pool.put(thread.getId(), thread);
			return thread;
		}
	}
	
	public static Collection<MyThread> getThreads(){
		return _pool.values();
	}
	
}
