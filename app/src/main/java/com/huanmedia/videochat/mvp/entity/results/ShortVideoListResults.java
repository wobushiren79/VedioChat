package com.huanmedia.videochat.mvp.entity.results;


import java.util.List;

public class ShortVideoListResults extends ListDataResults{

    private List<ShortVideoResults> items;

    public List<ShortVideoResults> getItems() {
        return items;
    }

    public void setItems(List<ShortVideoResults> items) {
        this.items = items;
    }
}
