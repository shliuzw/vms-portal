package com.jy.repository.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jy.common.utils.tree.entity.ZNodes;
import com.jy.entity.system.Account;
import com.jy.repository.BaseDao;
import com.jy.repository.JYBatis;


/**
 * 用户数据层
 */
@JYBatis
public interface AccountDao  extends BaseDao<Account>{
    /**
     * 根据登录帐号查找loginName和accountType，正常只有一条数据
     * and a.isvalid='1' and a.account_type='1'需要该条件
     * @param loginName
     * @return
     */
    public Account findFormatByLoginName(String loginName);
    /**
     * 根据登录帐号ID,正常只有一条数据
     * @param accountId
     * @return
     */
    public Account findAccountById(String accountId);
    /**
     * 设置个人化皮肤
     * @param o(需要ID和皮肤属性)
     */
    public void setSetting(Account o);
    /**
     * 获取个人资料
     * @param accountId 用户Id
     * @return
     */
    public Account getPerData(String accountId);
    /**
     * 设置个人资料
     * @param o(需要ID)
     */
    public void setPerData(Account o);
    /**
     * 设置头像
     * @param o
     * @return
     */
    public void setHeadpic(Account o);
    /**
     * 获得角色树
     * @return
     */
    public List<ZNodes> getRoles();
    /**
     * 通过登录名查找用户数量
     * @param loginName 用户名
     * @return
     */
    public int findCountByLoginName(@Param("loginName")String loginName);
    /**
     * 密码重置
     * @param o
     * @return
     */
    public void resetPwd(Account o);
    

}
