package yun520.xyz.chain.core;
//锁扣
public class PipelineImpl implements Pipeline{
	public Handler firstHandler=null;
	public Handler currentHandler=null;
	
	@Override
	public void addLast(Handler handler) {
		if(currentHandler==null){
			currentHandler=handler;
			firstHandler=handler;
		}else{			
			currentHandler.setNextHandler(handler);
			currentHandler=handler;			
		}
	}
}
