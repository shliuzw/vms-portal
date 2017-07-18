package com.jy.common.utils.redis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spring on 2017/6/8.
 */
public class ListPage<T> {

    public List<T> pageList(Integer pageNo, Integer pageStart, List<T> list){
        int pageSize = 10;
        int totalCount = list.size();
        int pageCount = 0;
        int m = totalCount % pageSize;

        List<T> subList = null;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }
        if (m == 0) {
           subList = list.subList((pageNo - 1) * pageSize, pageSize * (pageNo));
            System.out.println(subList);
        } else {
            if (pageNo == pageCount) {
                subList = list.subList((pageNo - 1) * pageSize, totalCount);
                System.out.println(subList);
            } else {
                subList = list.subList((pageNo - 1) * pageSize, pageSize * (pageNo));
                System.out.println(subList);
            }
        }
        return subList;
    }
    public static void main(String[] args) {
        ListPage page = new ListPage();
//        page.pageList();
        page.pageList(1,0);
        page.pageList(2,10);
        page.pageList(3,20);
        page.pageList(4,30);
        page.pageList(5,40);
        page.pageList(6,50);
        page.pageList(7,60);
        page.pageList(8,70);
        page.pageList(9,80);
        page.pageList(10,90);
        page.pageList(11,100);
        page.pageList(12,110);
    }

    /**
     *
     * @param pageNo  当前页号
     * @param pageStart 每页记录的起始位
     *                  当pageSize=10时， pageStart当第1页时，传0；当第2页时，传10；当第3页时，传20；
     * @return
     */
    public void pageList(Integer pageNo,Integer pageStart){
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i < 117; i++) {
            list.add(i);
        }
        int pageSize = 10;
        int totalCount = list.size();
        int pageCount = 0;
        int m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }

        if (m == 0) {
            List<Integer> subList = list.subList((pageNo - 1) * pageSize, pageSize * (pageNo));
            System.out.println(subList);
        } else {
            if (pageNo == pageCount) {
                List<Integer> subList = list.subList((pageNo - 1) * pageSize, totalCount);
                System.out.println(subList);
            } else {
                List<Integer> subList = list.subList((pageNo - 1) * pageSize, pageSize * (pageNo));
                System.out.println(subList);
            }
        }
    }
    public void pageList(){
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i < 101; i++) {
            list.add(i);
        }

        int pageSize = 10;
        int totalCount = list.size();
        int pageCount = 0;
        int m = totalCount % pageSize;

        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }

        for (int i = 1; i <= pageCount; i++) {
            if (m == 0) {
                List<Integer> subList = list.subList((i - 1) * pageSize, pageSize * (i));
                System.out.println(subList);
            } else {
                if (i == pageCount) {
                    List<Integer> subList = list.subList((i - 1) * pageSize, totalCount);
                    System.out.println(subList);
                } else {
                    List<Integer> subList = list.subList((i - 1) * pageSize, pageSize * (i));
                    System.out.println(subList);
                }
            }
        }
    }
}
