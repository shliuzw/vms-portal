package com.jy.controller.system;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jy.common.utils.base.UuidUtil;
import com.jy.common.utils.security.CipherUtil;
import com.jy.entity.system.Account;
import com.jy.interceptor.shiro.UsernameSmsToken;
import com.jy.service.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy.common.utils.IPUtil;
import com.jy.common.utils.base.Const;
import com.jy.common.utils.security.AccountShiroUtil;
import com.jy.common.utils.webpage.PageData;
import com.jy.controller.BaseController;
import com.jy.entity.system.LoginLog;
import com.jy.service.system.AccountService;
import com.jy.service.system.LoginLogService;


@Controller
public class LoginController extends BaseController<Object>{

	@Autowired
	private AccountService accountService;
	@Autowired
	private LoginLogService loginLogService;
	@Autowired
	private RedisService accountRedisService;

	/**
	 * 请求登录，验证用户
	 */
	@RequestMapping(value="/system_login" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> login()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String errInfo = "";
		String KEYDATA[] = pd.getString("keyData").split(",jy,");
		if(null != KEYDATA && KEYDATA.length == 2){
			//shiro管理的session
			Subject currentUser = SecurityUtils.getSubject();  
			Session session = currentUser.getSession();
			String cookie = (String)session.getId();
			System.out.println(cookie + "======cookie1======");
			String sessionCode = (String)session.getAttribute(Const.SESSION_SECURITY_CODE);		//获取session中的验证码		
			String code = KEYDATA[1];
			String username = KEYDATA[0];
			if(null == code || "".equals(code)){
				errInfo = "nullcode"; //验证码为空
			}else if(StringUtils.isEmpty(username)){
				errInfo = "nullup";	//缺少用户名或密码
			}else{
				//先验证短信码，没有问题，才能验证账号
				if(StringUtils.isNotEmpty(sessionCode) && sessionCode.equalsIgnoreCase(code)){										
					// shiro加入身份验证
					UsernamePasswordToken token = new UsernamePasswordToken(username, cookie.toUpperCase());
					token.setRememberMe(true);
					try {
						if (!currentUser.isAuthenticated()) {
							currentUser.login(token);
						}
						//记录登录日志
						String accountId=AccountShiroUtil.getCurrentUser().getAccountId();
						String loginIP=IPUtil.getIpAddr(getRequest());//获取用户登录IP
						LoginLog loginLog=new LoginLog(accountId,loginIP);
						loginLogService.saveLoginLog(loginLog);
					} catch (UnknownAccountException uae) {
						errInfo = "usererror";// 用户名或密码有误
					} catch (IncorrectCredentialsException ice) {
						errInfo = "usererror"; // 密码错误
					} catch (LockedAccountException lae) {
						errInfo = "inactive";// 未激活
					} catch (ExcessiveAttemptsException eae) {
						errInfo = "attemptserror";// 错误次数过多
					} catch (AuthenticationException ae) {
						errInfo = "codeerror";// 验证未通过
					}
					// 验证是否登录成功
					if (!currentUser.isAuthenticated()) {
						token.clear();
					}
				}else{
					errInfo = "codeerror";				 	//验证码输入有误
				}
				if(StringUtils.isEmpty(errInfo)){
					errInfo = "success";					//验证成功
					session.removeAttribute(Const.SESSION_SECURITY_CODE);//移除SESSION的验证
				}
			}
		}else{
			errInfo = "error";	//缺少参数
		}
		map.put("result", errInfo);
		return map;
	}
	/**
	 * 请求登录,通过手机号码和短信码，验证用户
	 * 参数keyData格式：手机号码,jy,短信码
	 */
	@RequestMapping(value="/login" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> loginSms()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String errInfo = "";
		String loginType = pd.getString("loginType");

		//shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();

		if (StringUtils.isNotEmpty(loginType) && loginType.equalsIgnoreCase("1")){ // 短信验证码登录
			String keyData[] = pd.getString("keyData").split(",jy,");
			String username = keyData[0];
			String code = keyData[1];
			if(null != keyData && keyData.length == 2) {
				// shiro加入身份验证
				UsernameSmsToken token = new UsernameSmsToken(username, code);
				token.setRememberMe(true);
				try {
					if (!currentUser.isAuthenticated()) {
						currentUser.login(token);
					}
					//记录登录日志
					String accountId=AccountShiroUtil.getCurrentUser().getAccountId();
//					String loginIP=IPUtil.getIpAddr(getRequest());//获取用户登录IP
//					LoginLog loginLog=new LoginLog(accountId,loginIP);
//					loginLogService.saveLoginLog(loginLog);
				} catch (UnknownAccountException uae) {
					errInfo = "usererror";// 用户名或短信码有误
				} catch (IncorrectCredentialsException ice) {
					errInfo = "codeerror"; // 密码错误
				} catch (LockedAccountException lae) {
					errInfo = "inactive";// 未激活
				} catch (ExcessiveAttemptsException eae) {
					errInfo = "attemptserror";// 错误次数过多
				} catch (AuthenticationException ae) {
					errInfo = "checkerror";// 验证未通过
				}
				// 验证是否登录成功
				if (!currentUser.isAuthenticated()) {
					token.clear();
				}
			}
			String tokenId = "";
			if(StringUtils.isEmpty(errInfo)){
				errInfo = "success";//验证成功
				session.removeAttribute(Const.SESSION_SECURITY_CODE);//移除SESSION的短信码
				tokenId = (String)session.getId();
			}
			map.put("token",tokenId);
			map.put("result", errInfo);
			return map;
		}else if (StringUtils.isNotEmpty(loginType) && loginType.equalsIgnoreCase("2")) { //token登录
			String loginToken = pd.getString("token");
//			Account ret = accountService.findFormatByToken(loginToken);
			Account ret = (Account)accountRedisService.getMap("token",loginToken);
			if (ret != null){
				this.setSession(Const.SESSION_USER, ret);
				errInfo = "success";//验证成功
				map.put("result", errInfo);
				return map;
			}else {
				Account ret2 = accountService.findFormatByToken(loginToken);
				if (ret2 == null){
					errInfo = "token is error.";//验证失败
					map.put("result", errInfo);
					return map;
				}else {
					accountRedisService.setMap("token",loginToken,ret2);
					this.setSession(Const.SESSION_USER, ret2);
					errInfo = "success";//验证成功
					map.put("result", errInfo);
					return map;
				}
			}
		}else if (StringUtils.isNotEmpty(loginType) && loginType.equalsIgnoreCase("3")){ // 用户名密码登录

		}else{
			errInfo = "error";	//缺少参数
		}
		map.put("result", errInfo);
		return map;
	}


	  /**
     * 帐号注销
     * @return
     */
	@RequestMapping(value="/logout" ,produces="application/json;charset=UTF-8")
	@ResponseBody
    public Map<String, Object> logout(HttpServletRequest request,HttpSession session) {
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "";
		try {
			PageData pd = this.getPageData();
			String token = pd.getString("token");
			accountRedisService.delMapField("token",token);
			Account ret2 = accountService.findFormatByToken(token);
			if (ret2 !=null) {
				accountRedisService.delMapField("username", ret2.getLoginName());
				ret2.setUpdateTime(new Date());
				accountService.sysResetToken(ret2);
			}
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.logout();
			session = request.getSession(true);
			session.removeAttribute(Const.SESSION_USER);

			errInfo = "success";
		} catch(Exception e){
			e.printStackTrace();
			errInfo = "error logout";
		}
		map.put("result",errInfo);
		return map;
    }

	private void setSession(Object key, Object value) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			if (null != session) {
				session.setAttribute(key, value);
			}
		}
	}
}
