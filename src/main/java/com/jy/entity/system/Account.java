package com.jy.entity.system;

import java.util.Date;
import org.apache.ibatis.type.Alias;

import com.jy.entity.BaseEntity;
import com.jy.entity.system.LoginLog;
/**
 * 用户帐号表
 */
@Alias("BaseAccount")
public class Account extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private String accountId;
	private String loginName;
	private String password;
	private String salt;
	private String name;
	private String picUrl;
	private String skin;
	private String roleId;
	private String roleName;
	private String email;
	private String token;
	private String description;
	private Integer isValid;
	private Date createTime;
	private Date updateTime;
	
    private LoginLog loginLog=new LoginLog();
	
	private String keyWord;
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "Account{" +
				"accountId='" + accountId + '\'' +
				", loginName='" + loginName + '\'' +
				", password='" + password + '\'' +
				", salt='" + salt + '\'' +
				", name='" + name + '\'' +
				", picUrl='" + picUrl + '\'' +
				", skin='" + skin + '\'' +
				", roleId='" + roleId + '\'' +
				", roleName='" + roleName + '\'' +
				", email='" + email + '\'' +
				", token='" + token + '\'' +
				", description='" + description + '\'' +
				", isValid=" + isValid +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", loginLog=" + loginLog +
				", keyWord='" + keyWord + '\'' +
				'}';
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public LoginLog getLoginLog() {
		return loginLog;
	}

	public void setLoginLog(LoginLog loginLog) {
		this.loginLog = loginLog;
	}

}