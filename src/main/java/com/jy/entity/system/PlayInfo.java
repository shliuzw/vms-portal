package com.jy.entity.system;

import com.jy.entity.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 用户评论表
 */
@Alias("PlayInfo")
public class PlayInfo extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private String language;
	private String codeRate;
	private String playUrl;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCodeRate() {
		return codeRate;
	}

	public void setCodeRate(String codeRate) {
		this.codeRate = codeRate;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	@Override
	public String toString() {
		return "PlayInfo{" +
				"language='" + language + '\'' +
				", codeRate='" + codeRate + '\'' +
				", playUrl='" + playUrl + '\'' +
				'}';
	}
}