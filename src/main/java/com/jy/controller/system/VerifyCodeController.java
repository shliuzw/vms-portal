package com.jy.controller.system;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.jy.common.utils.PatternUtil;
import com.jy.common.utils.webpage.PageData;
import com.sun.tools.javac.jvm.Code;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jy.common.utils.base.Const;
import com.jy.common.utils.verifyCode.VerifyCodeUtil;
import com.jy.controller.BaseController;
import org.springframework.web.bind.annotation.ResponseBody;


/** 
 * 生成验证码
 * @version
 */
@Controller
@RequestMapping("/verifyCode")
public class VerifyCodeController extends BaseController<Object>{

	/**
	 * 生成短信验证码，并调用云通讯短信接口
	 * @return 云通讯的返回结果
	 */
	@RequestMapping(value="/slogin" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> slogin(HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String errInfo = "";
		String mobileNo = pd.getString("uname");
		//设置页面不缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成短信码
        String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_NUM_ONLY, 4, null);

		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();

		session.setAttribute(Const.SESSION_SECURITY_CODE, verifyCode);
		try {
			if(!PatternUtil.isMobile(mobileNo)){
				errInfo = "mobileerror";
			}
	        //发送短信
			if (StringUtils.isEmpty(errInfo)){
				if (!sendSms(mobileNo, verifyCode)){ //发送失败
					session.removeAttribute(Const.SESSION_SECURITY_CODE);//移除SESSION的验证
					errInfo = "senderror";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (StringUtils.isEmpty(errInfo)) {
			errInfo = "success";
		}
		map.put("result", errInfo);
		return map;
	}
	private boolean sendSms(String mobileNo,String code) {
		boolean flag = false;
		HashMap<String, Object> result = null;
		CCPRestSDK restAPI = CCPRestSDK.getInstance();
		restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount("8a216da85582647801559af1b26d15ec", "a720955b1e7a43c193f5acc2e06e372e");// 初始化主帐号和主帐号TOKEN
		restAPI.setAppId("8a216da85582647801559af1b2eb15f2");// 初始化应用ID
		result = restAPI.sendTemplateSMS(mobileNo,"1" ,new String[]{code,"5"});
		System.out.println("SDKTestSendTemplateSMS result=" + result);

		if("000000".equals(result.get("statusCode"))){
			// 成功返回
			flag = true;
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			flag = false;
		}
		return flag;
	}
	
}
