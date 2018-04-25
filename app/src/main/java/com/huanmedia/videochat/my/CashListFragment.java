package com.huanmedia.videochat.my;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanmedia.ilibray.utils.Spanny;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.PageBean;
import com.huanmedia.videochat.common.SimpleLoadMoreView;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.CenterImageSpan;
import com.huanmedia.videochat.repository.entity.BillDetialEntity;

import butterknife.BindView;

public class CashListFragment extends BaseFragment {
    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.fragment_ap_rv)
    RecyclerView mFragmentApRv;

    private OnCashFragmentListener mListener;
    private PageBean mPage = new PageBean();
    private String mDate="";
    private BaseQuickAdapter<BillDetialEntity.ItemsEntity, BaseViewHolder> mAdapter;
    private int position;

    public CashListFragment() {
    }

    public static CashListFragment newInstance(int position) {
        CashListFragment fragment = new CashListFragment();
        Bundle  args = new Bundle();
        args.putInt(ARG_TYPE,position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            position =getArguments().getInt(ARG_TYPE,-1);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account_particulars;
    }

    public void loadData(int page, String data) {
        if (mListener != null) {
            mListener.getDataForType(position,page, data);
        }
    }

    @Override
    protected void initData() {
        loadData(mPage.nextpage(),mDate);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCashFragmentListener) {
            mListener = (OnCashFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCashFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initView(View view) {
            mAdapter = new BaseQuickAdapter<BillDetialEntity.ItemsEntity, BaseViewHolder>(R.layout.item_account_particulars) {
                @Override
                protected void convert(BaseViewHolder helper, BillDetialEntity.ItemsEntity item) {
                    String coinstr = position ==0?item.getCoin()>0?"+"+ UserManager.getInstance().getRealMoneyStr(item.getCoin()):UserManager.getInstance().getRealMoneyStr(item.getCoin()):item.getCoin()+" {钻石}";
                    Spanny spanny = new Spanny(coinstr);
                    spanny.findAndSpan("{钻石}", () -> new CenterImageSpan(getContext(), R.drawable.tab_icon_jewel));
                    helper.setText(R.id.item_accountparticulars_tv_type, Check.checkReplace(item.getDesc()))
                    .setText(R.id.item_accountparticulars_tv_time,Check.checkReplace(item.getDate()))
                            .setVisible(R.id.item_accountparticulars_v_line,helper.getAdapterPosition()!=getItemCount()-1)
                    .setText(R.id.item_accountparticulars_tv_price,spanny);
                }
            };
        mFragmentApRv.setAdapter(mAdapter);
        mAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.base_list_empty,mFragmentApRv,false));
        mAdapter.setEnableLoadMore(true);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setOnLoadMoreListener(() -> {
                loadData(mPage.nextpage(),mDate);
            },mFragmentApRv);
    }

    /**
     * 添加新数据
     * @param cashListEntity
     */
    public void addData(BillDetialEntity cashListEntity) {
        mAdapter.addData(cashListEntity.getItems());
    }

    /**
     * 设置新数据
     * @param cashListEntity
     */
    public void setNewData(BillDetialEntity cashListEntity) {
        mAdapter.setNewData(cashListEntity.getItems());
    }

    /**
     * 加载更多数据
     * @param cashListEntity
     */
    public void loadMore(BillDetialEntity cashListEntity){
        if (cashListEntity.getItems()!=null){
                mAdapter.addData(cashListEntity.getItems());
        }
        if (cashListEntity.getTotalpage() ==cashListEntity.getPage()){
            mAdapter.loadMoreEnd();
        }else {
            mAdapter.loadMoreComplete();
        }
    }

    /**
     * 加载更多出错
     */
    public void loadMoreError(){
        mAdapter.loadMoreFail();
        mPage.setCurrentPage(mPage.getCurrentPage()-1);
    }

    public void setDate(String date) {
        mDate = date;
        mPage = new PageBean();
        loadData(mPage.nextpage(),mDate);
    }
}
