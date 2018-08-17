package com.huanmedia.videochat.my.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.github.chrisbanes.photoview.PhotoView;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.widget.video.EmptyVideoPlayer;
import com.huanmedia.videochat.discover.weight.androidtagview.TagContainerLayout;
import com.huanmedia.videochat.discover.weight.androidtagview.TagView;
import com.huanmedia.videochat.mvp.entity.results.FileHotTagResults;
import com.huanmedia.videochat.mvp.presenter.info.FileHotTagPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.info.IFileHotTagPresenter;
import com.huanmedia.videochat.mvp.view.file.IFileHotTagView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import mvp.data.store.DataKeeper;

@SuppressLint("ValidFragment")
public class FileInfoEditTagFragment extends BaseFragment implements TagView.OnTagClickListener, IFileHotTagView, TextWatcher {
    public final String TAG = "FileInfoEditTagFragment";

    @BindView(R.id.et_tag)
    EditText mETTag;
    @BindView(R.id.view_tag)
    TagContainerLayout mTagView;


    private String mFilePath;
    private IFileHotTagPresenter mFileHotTagPresenter;
    private int mFileType;

    public FileInfoEditTagFragment(String mFilePath, int mFileType) {
        this.mFilePath = mFilePath;
        this.mFileType = mFileType;
    }

    public static Fragment newInstance(String mFilePath, int mFileType) {
        FileInfoEditTagFragment fragment = new FileInfoEditTagFragment(mFilePath, mFileType);
        return fragment;
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mETTag.addTextChangedListener(this);
        mTagView.setOnTagClickListener(this);
        mTagView.setIsTagViewClickable(true);

        String initTag = new DataKeeper(getContext(), DataKeeper.DEFULTFILE).get("FileTag", "");
        if (initTag != null)
            mETTag.setText(initTag);

        mFileHotTagPresenter = new FileHotTagPresenterImpl(this);
        mFileHotTagPresenter.getFileHotTagList();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_file_info_edit_tag;
    }

    public String getTagStr() {
        String tagStr = mETTag.getText().toString();
        if (tagStr != null && tagStr.length() != 0)
            new DataKeeper(getContext(), DataKeeper.DEFULTFILE).put("FileTag", tagStr);
        return tagStr;
    }

    //-------------标签点击事件--------------
    @Override
    public void onTagClick(int position, String text) {
        List<View> listTagView = mTagView.getAllTagView();
        if (listTagView != null) {
            for (int i = 0; i < listTagView.size(); i++) {
                TagView itemTagView = (TagView) listTagView.get(i);
                if (position == i) {
                    itemTagView.setTagTextColor(getResources().getColor(R.color.white));
                    itemTagView.setTagBorderColor(getResources().getColor(R.color.white));
                    itemTagView.setTagBackgroundColor(getResources().getColor(R.color.color_fe6f06));
                } else {
                    itemTagView.setTagTextColor(getResources().getColor(R.color.base_gray));
                    itemTagView.setTagBorderColor(getResources().getColor(R.color.color_e8ebf1));
                    itemTagView.setTagBackgroundColor(getResources().getColor(R.color.white));
                }
                itemTagView.invalidate();
            }
        }
        mETTag.setText(text);
    }

    @Override
    public void onTagLongClick(int position, String text) {

    }

    @Override
    public void onTagCrossClick(int position) {

    }

    //-------------标签获取--------------
    @Override
    public void getFileHotTagListSuccess(List<FileHotTagResults> listData) {

    }

    @Override
    public void getFileHotTagListFail(String msg) {

    }

    @Override
    public void setFileHotTagList(List<String> listData) {
        mTagView.setTags(listData);
        String initTag = mETTag.getText().toString();
        for (int i = 0; i < listData.size(); i++) {
            String item = listData.get(i);
            if (initTag.equals(item)) {
                TagView itemTagView = mTagView.getTagView(i);
                itemTagView.setTagTextColor(getResources().getColor(R.color.white));
                itemTagView.setTagBorderColor(getResources().getColor(R.color.white));
                itemTagView.setTagBackgroundColor(getResources().getColor(R.color.color_fe6f06));
                break;
            }
        }

    }

    @Override
    public void showToast(String toast) {

    }

    //---------输入框监听--------------
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().equals("")) {
            List<View> listTagView = mTagView.getAllTagView();
            if (listTagView != null) {
                for (int f = 0; f < listTagView.size(); f++) {
                    TagView itemTagView = (TagView) listTagView.get(f);
                    itemTagView.setTagTextColor(getResources().getColor(R.color.base_gray));
                    itemTagView.setTagBorderColor(getResources().getColor(R.color.color_e8ebf1));
                    itemTagView.setTagBackgroundColor(getResources().getColor(R.color.white));
                    itemTagView.invalidate();
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
