package com.jy.service.system;

import java.util.Map;

import com.jy.entity.system.DataDict;
import com.jy.service.BaseService;

public interface DataDictService extends BaseService<DataDict>{
	/**
	 * 查找数据字典
	 * @param ids  标签Id（多个可逗号隔开）
	 * @param keys 关键字（多个可逗号隔开）
	 * @return
	 */
   public Map<String,DataDict> findDatas(String ids,String keys);  
   /**
    * 新增数据字典
    * @param dataDict 数据字典类(包括数据字典字段类)
    * @return
    */
   public int insertDataDict(DataDict dataDict);
   /**
    * 修改数据字典
    * @param dataDict 数据字典类
    * @return
    */
   public int updateDataDict(DataDict dataDict);
}
