package com.jy.interceptor.shiro;


import com.jy.common.utils.base.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.jy.common.utils.base.Const;
import com.jy.common.utils.security.CipherUtil;
import com.jy.entity.system.Account;
import com.jy.service.system.AccountService;



/**
 * 
 */
public class ShiroRealm extends AuthorizingRealm {

	 /**
     * 账户类服务层注入
     */
    @Autowired
    private AccountService accountService;

	/*
	 * 登录信息和用户验证信息验证(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		 UsernameSmsToken token = (UsernameSmsToken) authcToken;
		 AuthenticationInfo authenticationInfo = null;
		 String username=new String(token.getUsername());//用户名
		 String code=new String(token.getCode());//短信码
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		 String sessionCode = (String)session.getAttribute(Const.SESSION_SECURITY_CODE);		//获取session中的验证码
	     Account loginAccount = accountService.findFormatByLoginName(username);//通过登录名 寻找用户
	     if (loginAccount == null) {
	    	 loginAccount = new Account();
			 loginAccount.setAccountId(UuidUtil.get32UUID());
			 loginAccount.setLoginName(username);
			 loginAccount.setIsValid(1);
			 loginAccount.setName(username);
			 loginAccount.setToken((String)session.getId());
			 try {
				 accountService.insertAccount(loginAccount);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }
		// 短信码验证
		if(StringUtils.isNotEmpty(sessionCode) && sessionCode.equalsIgnoreCase(code)){
			authenticationInfo = new SimpleAuthenticationInfo(loginAccount.getLoginName(), code, getName());
			// 登录验证，成功后将当前用户对象Account放到session
			this.setSession(Const.SESSION_USER, loginAccount);
			return authenticationInfo;
		}else{
			throw new IncorrectCredentialsException(); /*错误认证异常*/
		}
	}
	
	/*
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {	
		  // 因为非正常退出，即没有显式调用 SecurityUtils.getSubject().logout()
        // (可能是关闭浏览器，或超时)，但此时缓存依旧存在(principals)，所以会自己跑到授权方法里。
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            doClearCache(pc);
            SecurityUtils.getSubject().logout();
            return null;
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
	}

	  /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     *
     * @see
     */
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
