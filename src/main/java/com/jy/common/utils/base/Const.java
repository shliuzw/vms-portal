package com.jy.common.utils.base;

import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局静态资源：
 * 
*/
public class Const {
	
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";
	public static final String SESSION_USER = "sessionUser";            
	/**
	 *上传配置文件位置
	 */
	public static final String CONF_PROP= "conf.properties";


	public static Map<String,String> confMap = new HashMap<String,String>();

	/**
	 *没有权限返回的URL
	 */
	public static final String NO_AUTHORIZED_URL="/system/noAuthorized";//没有权限返回的URL
	/**
	 *没有权限返回中文说明
	 */
	public static final String NO_AUTHORIZED_MSG="当前角色没有权限";//
	/**
	 *返回值 没有权限 100
	 */
	public static final int NO_AUTHORIZED=100;
	/**
	 *返回值 成功(1)
	 */
	public static final int SUCCEED = 1;
	/**
	 *返回值 失败(0)
	 */
	public static final int FAIL = 0;
	/**
	 *保存成功
	 */
	public static final String SAVE_SUCCEED = "保存成功";
	/**
	 *保存失败
	 */
	public static final String SAVE_FAIL = "保存失败";
	/**
	 *删除成功
	 */
	public static final String DEL_SUCCEED = "删除成功";
	/**
	 *删除失败
	 */
	public static final String DEL_FAIL = "删除失败";
	/**
	 *修改成功
	 */
	public static final String UPDATE_SUCCEED = "修改成功";
	/**
	 *修改失败
	 */
	public static final String UPDATE_FAIL = "修改失败";
	/**
	 *数据获取成功
	 */
	public static final String DATA_SUCCEED = "数据获取成功";
	/**
	 *数据获取失败
	 */
	public static final String DATA_FAIL = "数据获取失败";
}
