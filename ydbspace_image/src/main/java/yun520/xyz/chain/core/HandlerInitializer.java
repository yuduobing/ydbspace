package yun520.xyz.chain.core;

//初始化
public abstract class HandlerInitializer extends PipelineImpl{
	public HandlerInitializer(ContextRequest request,ContextResponse response) {
		ContextHolder.setReq(request);
		ContextHolder.setRes(response);
	}

	protected abstract void initChannel(Pipeline pipeline);
}
