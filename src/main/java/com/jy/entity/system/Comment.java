package com.jy.entity.system;

import com.jy.entity.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 用户评论表
 */
@Alias("Comment")
public class Comment extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer contId;
	private String loginName;
	private String detail;
	private Date publishTime;
	private Integer isValid;
	private Date createTime;
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getContId() {
		return contId;
	}

	public void setContId(Integer contId) {
		this.contId = contId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "Comment{" +
				"id=" + id +
				", contId=" + contId +
				", loginName='" + loginName + '\'' +
				", detail='" + detail + '\'' +
				", publishTime=" + publishTime +
				", isValid=" + isValid +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}