package com.huanmedia.videochat.common;

/**
 * Created by yb on 2016/6/29.
 */
public class PageBean {

    private int currentPage=0;//当前客户端位于服务器页面
    private int pageSize=20;//服务器获取数据分页大小
    private int totalSize;
    public PageBean(int pageSize) {
        this.pageSize = pageSize;
    }

    public PageBean() {
    }
    public void reset(){
        currentPage =0;
        totalSize =0;
    }
    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
    public int getTotalPage(){
        return (int) Math.ceil((totalSize*1.0)/(pageSize*1.0f));
    }
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int nextpage(){
        return currentPage+=1;
    }
}
