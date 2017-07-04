package com.jy.entity.system;

import com.jy.entity.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 电视剧内容
 */
@Alias("Content")
public class Content extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private String contId;
	private String name;
	private String director;
	private String actor;
	private String contType;
	private String language;
	private String category;
	private String totalNum;
	private String latestNum;
	private String contDesc;
	private String provider;
	private String h16image;
	private String v2image;
	private List<Subset> subsets;

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

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getContType() {
		return contType;
	}

	public void setContType(String contType) {
		this.contType = contType;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public String getContDesc() {
		return contDesc;
	}

	public void setContDesc(String contDesc) {
		this.contDesc = contDesc;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getH16image() {
		return h16image;
	}

	public void setH16image(String h16image) {
		this.h16image = h16image;
	}

	public String getV2image() {
		return v2image;
	}

	public void setV2image(String v2image) {
		this.v2image = v2image;
	}

	public List<Subset> getSubsets() {
		return subsets;
	}

	public void setSubsets(List<Subset> subsets) {
		this.subsets = subsets;
	}

	@Override
	public String toString() {
		return "Content{" +
				"contId='" + contId + '\'' +
				", name='" + name + '\'' +
				", director='" + director + '\'' +
				", actor='" + actor + '\'' +
				", contType='" + contType + '\'' +
				", language='" + language + '\'' +
				", category='" + category + '\'' +
				", totalNum='" + totalNum + '\'' +
				", latestNum='" + latestNum + '\'' +
				", contDesc='" + contDesc + '\'' +
				", provider='" + provider + '\'' +
				", subsets=" + subsets +
				'}';
	}
}