package yun520.xyz.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SpringContentUtils implements ApplicationContextAware{
	public static ApplicationContext applicationContext = null;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(SpringContentUtils.applicationContext == null){
			SpringContentUtils.applicationContext  = applicationContext;
		}
	}
	


	public  Object getBean(String name) {
		// 先判断是否存在上下文对象
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(name);
	}
}
