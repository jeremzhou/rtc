package cn.utstarcom.rtc.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationContextImpl implements IMyApplicationContext {

	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;	
	}

	@Override
	public Object getBean(String beanName) {
	    
	    return this.applicationContext.getBean(beanName);
	}

}
