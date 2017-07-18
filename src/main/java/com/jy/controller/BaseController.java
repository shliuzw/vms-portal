package com.jy.controller;

import javax.servlet.http.HttpServletRequest;

import com.jy.common.utils.DateUtils;
import com.jy.common.utils.base.DES3;
import com.jy.entity.system.Content;
import com.jy.entity.system.PlayInfo;
import com.jy.entity.system.Subset;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.jy.common.ajax.AjaxRes;
import com.jy.common.mybatis.Page;
import com.jy.common.utils.base.UuidUtil;
import com.jy.common.utils.webpage.PageData;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class BaseController<T> {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 得到PageData
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	public AjaxRes getAjaxRes(){
		return new AjaxRes();
	}
	
	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();	
		return request;
	}

	/**
	 * 得到32位的uuid
	 * @return
	 */
	public String get32UUID(){	
		return UuidUtil.get32UUID();
	}
	
	/**
	 * 得到分页列表的信息 
	 * @param <T>
	 */
	@SuppressWarnings("hiding")
	public <T> Page<T> getPage(){	
		return new Page<T>();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}

	public boolean auth(String reqSecurityKey) {
		boolean flag = true;
		try {
			if (StringUtils.isNotEmpty(reqSecurityKey)){
				flag = false;
				return flag;
			}
			String secuKey = DES3.decryptMode(reqSecurityKey);

			String[] keys = secuKey.split(",jy,");
			if (keys == null || keys.length != 2) {
				flag = false;
				return flag;
			}
			// 播放地址链接有效期使用2小时
			long reqTime = DateUtils.addSeconds(DateUtils.parseDate2(keys[1], DateUtils.DATETIME_PATTERN),60).getTime();
			long curTime = new Date().getTime();
			if (reqTime < curTime){
				flag = false;
				return flag;
			}
			//验证SecurityKey是否一致
			String sysSecurityKey = DES3.encryptMode(keys[0].concat(",jy,").concat(keys[1] + ""));
			if (!reqSecurityKey.equals(sysSecurityKey)){
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 解析内容源XML文件
	 * @param
	 */
	public Content parseContXml(String contPath){
		try{
			SAXReader saxReader = new SAXReader();
			File xmlFile = new File(contPath);
			if (!xmlFile.exists()){
				return null;
			}
			Content content = new Content();
			Document document = saxReader.read(xmlFile);
			Element root = document.getRootElement();
			Node contId = root.selectSingleNode("/Content/Id");
			Node contName = root.selectSingleNode("/Content/Name");
			Node category = root.selectSingleNode("/Content/Category");
			Node language = root.selectSingleNode("/Content/Language");
			Node actor = root.selectSingleNode("/Content/Actor");
			Node director = root.selectSingleNode("/Content/Director");
			Node totalSum = root.selectSingleNode("/Content/TotalSum");
			Node latestNum = root.selectSingleNode("/Content/LatestNum");
			Node contType = root.selectSingleNode("/Content/ConType");
			Node contDesc = root.selectSingleNode("/Content/ConDesc");
			Node provider = root.selectSingleNode("/Content/Provider/Name");
			content.setActor(actor.getText());
			content.setCategory(category.getText());
			content.setContId(contId.getText());
			content.setContType(contType.getText());
			content.setDirector(director.getText());
			content.setLanguage(language.getText());
			content.setLatestNum(latestNum.getText());
			content.setName(contName.getText());
			content.setTotalNum(totalSum.getText());
			content.setContDesc(contDesc.getText());
			content.setProvider(provider.getText());

			List imageList = document.selectNodes("//Image");// 获取所有的图片
			Iterator iterImage = imageList.iterator();
			while(iterImage.hasNext()) {
				Element eleImage = (Element) iterImage.next();
				Node imageType = eleImage.selectSingleNode("Type");
				Node imagePath = eleImage.selectSingleNode("Path");
				if ("1".equals(imageType.getText())){ //横图
					content.setH16image("/content/image/"+imagePath.getText());
				}else if ("2".equals(imageType.getText())){ //竖图
					content.setV2image("/content/image/"+imagePath.getText());
				}
			}

			List subsetList = document.selectNodes("//Subset" );// 获取所有的子集
			Iterator iterSubset = subsetList.iterator();
			List<Subset> subsets = new ArrayList<Subset>();
			while(iterSubset.hasNext()){
				Subset subset = new Subset();
				Element eleSubset = (Element)iterSubset.next();
				Node subsetNum = eleSubset.selectSingleNode("Num");
				List playList = eleSubset.selectNodes("PlayInfos/PlayInfo");
				subset.setNumNo(subsetNum.getText());
				List<PlayInfo> playInfos = new ArrayList<PlayInfo>();
				Iterator iterPlay = playList.iterator();
				while(iterPlay.hasNext()){
					PlayInfo play = new PlayInfo();
					Element elePlay = (Element)iterPlay.next();
					Node codeRate = elePlay.selectSingleNode("CodeRate");
					Node langPaly = elePlay.selectSingleNode("Language");
					Node playUrl = elePlay.selectSingleNode("PlayUrl");
					play.setCodeRate(codeRate.getText());
					play.setLanguage(langPaly.getText());
					play.setPlayUrl(playUrl.getText());
					playInfos.add(play);
				}
				subset.setPlays(playInfos);
				subsets.add(subset);
			}
			content.setSubsets(subsets);
			return content;
		} catch(DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
}
