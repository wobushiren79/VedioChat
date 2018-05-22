package com.huanmedia.videochat.common.widget.dialog.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.ViewFindUtils;
import com.huanmedia.videochat.repository.entity.ItemMenuEntity;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelAdapter;

/**
 * 兴趣、家乡、行业
 */
public class CitysWheelAdapter extends AbstractWheelAdapter {
    private final LayoutInflater inft;
    List<ItemMenuEntity> entities = new ArrayList<>();

    public CitysWheelAdapter(Context context) {
        inft = LayoutInflater.from(context);
    }

    public List<ItemMenuEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<ItemMenuEntity> entities) {
        this.entities = entities;
    }

    public CitysWheelAdapter(Context context, List<? extends ItemMenuEntity> entities) {
        inft = LayoutInflater.from(context);
        this.entities = (List<ItemMenuEntity>) entities;
    }

    @Override
    public int getItemsCount() {
        return entities.size();
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inft.inflate(android.R.layout.simple_list_item_1, null);
            ((TextView) convertView).setGravity(Gravity.CENTER);
        }
        TextView tv = ViewFindUtils.find(convertView, android.R.id.text1);
        tv.setText(entities.get(index).getName());
        return convertView;
    }
}