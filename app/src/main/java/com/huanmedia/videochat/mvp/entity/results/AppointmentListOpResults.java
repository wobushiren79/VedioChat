package com.huanmedia.videochat.mvp.entity.results;

import java.util.List;

public class AppointmentListOpResults extends ListDataResults {
    private List<AppointmentDataOpResults> items;

    public List<AppointmentDataOpResults> getItems() {
        return items;
    }

    public void setItems(List<AppointmentDataOpResults> items) {
        this.items = items;
    }
}
