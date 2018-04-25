package com.huanmedia.hmalbumlib;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.huanmedia.hmalbumlib.engine.HM_ImageEngine;
import com.huanmedia.hmalbumlib.extar.HM_PhotoEntity;
import com.huanmedia.hmalbumlib.ui.HM_PhotoSeePagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yb on 2016/6/23.
 */
public class HM_StartSeePhoto {
    private final static String Extar = "extra_photosee_";
    public final static String EXTRA_PHOTOSEE_DATA = Extar + "data";
    public final static String EXTRA_PHOTOSEE_CURRENTSELECT = Extar + "currentselect";
    protected HM_StartSeePhoto(Context mContext, Bulide bulide) {
        Intent intent = new Intent(mContext, HM_PhotoSeePagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (bulide.list() != null && bulide.list().size() > 0) {
            intent.putParcelableArrayListExtra(EXTRA_PHOTOSEE_DATA, (ArrayList<? extends Parcelable>) bulide.list());
            if (bulide.currentSelect() != 0)
                intent.putExtra(EXTRA_PHOTOSEE_CURRENTSELECT, bulide.currentSelect());
                mContext.startActivity(intent);
            }
            else
             (mContext).startActivity(intent);
        }

    public static class Bulide {
        private Context mContext;
        private List<?extends HM_PhotoEntity> list;
        private int currentSelect;
        public Bulide(Context context, HM_ImageEngine engine) {
            HM_AlbumConifg.getInstance().setEngine(engine);
            this.mContext = context;
        }
        public List<? extends HM_PhotoEntity> list() {
            return list;
        }

        public Bulide setList(List<?extends HM_PhotoEntity> list) {
            this.list = list;
            return this;
        }


        public int currentSelect() {
            return currentSelect;
        }

        public Bulide setCurrentSelect(int currentSelect) {
            this.currentSelect = currentSelect;
            return this;
        }

        public HM_StartSeePhoto bulide() {
            return new HM_StartSeePhoto(mContext, this);
        }
    }
}
