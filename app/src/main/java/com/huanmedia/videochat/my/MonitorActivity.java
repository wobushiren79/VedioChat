package com.huanmedia.videochat.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.applecoffee.devtools.custom.layout.ptr.PtrRecyclerView;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;
import com.huanmedia.videochat.mvp.presenter.chat.ITalkRoomListPresenter;
import com.huanmedia.videochat.mvp.presenter.chat.TalkRoomListPresenterImpl;
import com.huanmedia.videochat.mvp.view.chat.ITalkRoomListView;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MonitorActivity extends BaseActivity implements ITalkRoomListView {

    @BindView(R.id.ptr_recycle)
    PtrLayout mListView;

    private BaseRCAdapter adapter;
    private ITalkRoomListPresenter talkRoomListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, MonitorActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_monitor;
    }

    @Override
    protected void initView() {
        super.initView();
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setCallBack(new PtrLayout.PtrHandleCallBack() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                talkRoomListPresenter.getTalkRoomList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                talkRoomListPresenter.getTalkRoomList();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        adapter = new BaseRCAdapter<TalkRoomListResults.Item>(this, R.layout.item_adapter_monitor) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, TalkRoomListResults.Item item, int i) {
                TextView roomId = baseViewHolder.getView(R.id.item_tv_room_id);
                TextView roomInfo = baseViewHolder.getView(R.id.item_tv_room_info);

                roomId.setText("房间号：" + item.getRoomid());
                roomInfo.setText("发起人：" + item.getUid1() + " 接收人：" + item.getUid2());

                baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNavigator().navtoMonitorVideo(MonitorActivity.this, item.getRoomid(), item.getUid1(), item.getUid2());
                    }
                });
            }
        };
        mListView.setAdapter(adapter);
        talkRoomListPresenter = new TalkRoomListPresenterImpl(this);
        RxCountDown.delay2(500).subscribe(integer -> {
            talkRoomListPresenter.getTalkRoomList();
        });
    }


    @Override
    public void getTalkRoomListSuccess(List<TalkRoomListResults.Item> listData) {

    }

    @Override
    public void getTalkRoomListFail(String msg) {
        showToast(msg);
    }

    @Override
    public PtrLayout getPtrLayout() {
        return mListView;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShort(this, toast);
    }
}
