package com.jy.common.utils;

import com.jy.common.utils.base.Const;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring上下文获取工具类
 *
 */
public class SpringWebContextUtil implements ServletContextListener {
	/**
	 * 上下文对象
	 */
	private static WebApplicationContext wac;
	/**
	 * 	上下文销毁时执行方法
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	/**
	 * 上下文初始时执行方法
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		wac = WebApplicationContextUtils.getWebApplicationContext(context);
		System.setProperty("xml.base.path", context.getRealPath("/")+ "/content/xml/");
		Const.confMap = PropertyUtil.getPropertyMap(Const.CONF_PROP);
	}
	/**
	 * 获取上下文对象方法
	 * @return 上下方对象
	 */
	public static ApplicationContext getApplicationContext(){
		return wac;
	}

}
