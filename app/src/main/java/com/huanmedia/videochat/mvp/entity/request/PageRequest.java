package com.huanmedia.videochat.mvp.entity.request;

public class PageRequest {
    private int page;//页数
    private int pagesize;//每页条数

    private int year;//条件筛选（年） 默认0
    private int month;//条件筛选（月） 默认0

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
