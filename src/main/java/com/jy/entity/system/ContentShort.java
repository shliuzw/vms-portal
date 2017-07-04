package com.jy.entity.system;

import com.jy.entity.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 电视剧内容简版
 */
@Alias("ContentShort")
public class ContentShort extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private String contId;
	private String name;
	private String category;
	private String totalNum;
	private String latestNum;
	private String provider;
	private String h16imagePath;
	private String v2imagePath;

	public String getContId() {
		return contId;
	}

	public void setContId(String contId) {
		this.contId = contId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String getLatestNum() {
		return latestNum;
	}

	public void setLatestNum(String latestNum) {
		this.latestNum = latestNum;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getH16imagePath() {
		return h16imagePath;
	}

	public void setH16imagePath(String h16imagePath) {
		this.h16imagePath = h16imagePath;
	}

	public String getV2imagePath() {
		return v2imagePath;
	}

	public void setV2imagePath(String v2imagePath) {
		this.v2imagePath = v2imagePath;
	}

	@Override
	public String toString() {
		return "ContentShort{" +
				"contId='" + contId + '\'' +
				", name='" + name + '\'' +
				", category='" + category + '\'' +
				", totalNum='" + totalNum + '\'' +
				", latestNum='" + latestNum + '\'' +
				", provider='" + provider + '\'' +
				", h16imagePath='" + h16imagePath + '\'' +
				", v2imagePath='" + v2imagePath + '\'' +
				'}';
	}
}