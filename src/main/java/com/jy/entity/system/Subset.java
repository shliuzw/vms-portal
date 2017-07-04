package com.jy.entity.system;

import com.jy.entity.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 用户评论表
 */
@Alias("Subset")
public class Subset extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private String numNo;
	private List<PlayInfo> plays;

	public String getNumNo() {
		return numNo;
	}

	public void setNumNo(String numNo) {
		this.numNo = numNo;
	}

	public List<PlayInfo> getPlays() {
		return plays;
	}

	public void setPlays(List<PlayInfo> plays) {
		this.plays = plays;
	}

	@Override
	public String toString() {
		return "Subset{" +
				"numNo='" + numNo + '\'' +
				", plays=" + plays +
				'}';
	}
}