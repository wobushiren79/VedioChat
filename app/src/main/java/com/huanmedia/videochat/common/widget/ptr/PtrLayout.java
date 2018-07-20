package com.huanmedia.videochat.common.widget.ptr;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.IBaseRCAdapter;
import com.applecoffee.devtools.custom.layout.ptr.PtrRecyclerView;
import com.huanmedia.videochat.mvp.entity.results.ListDataResults;

import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class PtrLayout<T> extends PtrRecyclerView {

    private int page;
    private int pageSize;
    private ListDataResults listDataResults;
    private List<T> listData;

    private TextView mNoMoreDataView;
    private PtrDefaultHandler2 handler2;
    private PtrHandleCallBack callBack;


    public PtrLayout(Context context) {
        this(context, null);
    }

    public PtrLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void initView() {
        super.initView();
        setHeadLoadLayout(new PtrHeadLoadLayout(getContext()));
        setFootLoadLayout(new PtrHeadLoadLayout(getContext()));
        setNoDataLayout(new PtrNoDataLayout(getContext()));
        setHasDataVisibility(View.GONE);
    }


    @Override
    protected void initData() {
        super.initData();
        resetPage();
        setPtrHandle2(new CustomHandle());
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCallBack(PtrHandleCallBack callBack) {
        this.callBack = callBack;
    }


    public void setListDataResults(ListDataResults listDataResults, List<T> listData) {
        this.listDataResults = listDataResults;
        IBaseRCAdapter adapter = (IBaseRCAdapter) getAdapter();
        if (listDataResults.getPage() == 1 && listDataResults.getTotal() == 0) {
            // 第一页没有数据
            adapter.setData(listData);
            setHasDataVisibility(View.VISIBLE);
        } else if (listDataResults.getPage() == 1 && listData != null && listData.size() != 0) {
            //第一页 有数据
            adapter.setData(listData);
            page++;
        } else if (listDataResults.getPage() > 1 && (listData == null || listData.size() == 0)) {
            //第二页 没有数据
        } else if (listDataResults.getPage() > 1 && listData != null && listData.size() != 0) {
            //第二页 有数据
            adapter.addData(listData);
            page++;
        }
        this.setRefreshComplete();
    }


    public void resetPage() {
        page = 1;
        pageSize = 20;
    }

    public class CustomHandle extends PtrDefaultHandler2 {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (callBack != null)
                callBack.onLoadMoreBegin(frame);
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            resetPage();
            setHasDataVisibility(View.GONE);
            if (callBack != null)
                callBack.onRefreshBegin(frame);
        }
    }


    public interface PtrHandleCallBack {
        void onLoadMoreBegin(PtrFrameLayout frame);

        void onRefreshBegin(PtrFrameLayout frame);
    }
}
