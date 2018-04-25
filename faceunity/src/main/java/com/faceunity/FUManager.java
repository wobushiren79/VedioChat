package com.faceunity;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import com.faceunity.wrapper.faceunity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 核心类，封装nama底层API
 * Created by Administrator on 2017/4/5.
 */

public class FUManager {

    //道具资源数组
//    private final static String[] ITEM_NAMES = {
//            "", "item0204.bundle", "bg_seg.bundle", "fu_zh_duzui.mp3", "yazui.bundle", "mask_matianyu.bundle", "lixiaolong.bundle", "Mood.bundle", "gradient.bundle", "yuguan.bundle"
//    };

    //滤镜名称数组
    final static String[] FILTERS = {"nature", "delta", "electric", "slowlived", "tokyo", "warm"};

    private static volatile Context context;

    private static Handler handler;
    private static volatile Map<String, Integer> effectItem = new HashMap<>();
    private static int faceBeautyItem;

    private static int frameId;

    static volatile boolean creatingItem;

    private static volatile int rotation;
    private static BeautyConfig mBeautyConfig;
    private static FUManager instance;
    private static int[] mPagePosition;//当前选择的道具索引

    public static FUManager getInstance(Context context) {
        FUManager.context = context;
        if (instance == null) {
            instance = new FUManager(context);
        }
        return instance;
    }

    public static boolean hasInstance() {
        if (instance == null) {
            return false;
        } else {
            return true;
        }
    }

    private FUManager(final Context context) {
        mBeautyConfig = new BeautyConfig();
        HandlerThread handlerThread = new HandlerThread("FUManager");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    final byte[] authData = authpack.A();
                    if (context == null) return;
                    InputStream is = context.getAssets().open("v3.bundle");
                    faceunity.fuCreateEGLContext();
                    byte[] v3data = new byte[is.available()];
                    is.read(v3data);
                    is.close();
                    //TODO 调用fuSetup执行初始化
                    /*
                     类文件 ：faceunity.java

                     函数原型：
                     int fuSetup(byte[] v3data, byte[] ardata, byte[] authData);

                     参数：
                     v3Data : 人脸识别数据库
                     arData : 不需要，传null即可
                     authData : 鉴权证书authpack
                    */
                    int setup = faceunity.fuSetup(v3data, null, authData);
                    Log.v("this", "fuSetup:" + setup);
                    Log.v("this", "fuSetup fuIsTracking:" + faceunity.fuIsTracking());
                    faceunity.fuSetMaxFaces(4);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static int[] getmPagePosition() {
        return mPagePosition;
    }

    public static synchronized BeautyConfig getmBeautyConfig() {
        return mBeautyConfig;
    }

    public void loadItems() {
        frameId = 0;

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //加载默认道具yuguan.bundle
//                    loadItem("item0204.bundle");
                    if (context == null) return;
                    InputStream is = context.getAssets().open("face_beautification.bundle");
                    byte[] itemData = new byte[is.available()];
                    is.read(itemData);
                    is.close();
                    //TODO 调用fuCreateItemFromPackage，加载美颜道具
                    /*
                     类文件 ：faceunity.java

                     函数原型：
                     int fuCreateItemFromPackage(byte[] itemData);

                     参数：
                     itemData : 道具文件加载到内存中的byte[]
                     */
                    faceBeautyItem = faceunity.fuCreateItemFromPackage(itemData);
                    Log.v("this", "fuCreateItemFromPackage fuIsTracking:" + faceunity.fuIsTracking());
                    //设置美颜参数
                    faceunity.fuItemSetParam(faceBeautyItem, "blur_level", getmBeautyConfig().getBlur_level());
                    faceunity.fuItemSetParam(faceBeautyItem, "color_level", getmBeautyConfig().getColor_level());
                    faceunity.fuItemSetParam(faceBeautyItem, "red_level", getmBeautyConfig().getRed_level());
                    faceunity.fuItemSetParam(faceBeautyItem, "face_shape", getmBeautyConfig().getFace_shape());
                    faceunity.fuItemSetParam(faceBeautyItem, "face_shape_level", getmBeautyConfig().getFace_shape_level());
                    faceunity.fuItemSetParam(faceBeautyItem, "cheek_thinning", getmBeautyConfig().getCheek_thinning());
                    faceunity.fuItemSetParam(faceBeautyItem, "eye_enlarging", getmBeautyConfig().getEye_enlarging());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void renderItemsToYUVFrame(final long yBuffer, final long uBuffer, final long vBuffer, final int yStride, final int uStride, final int vStride, final int w, final int h, final int rotation) {
        if (context == null) {
            return;
        }

        synchronized (FUManager.class) {
            try {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (FUManager.class) {
                            if (FUManager.rotation != rotation) {
                                FUManager.rotation = rotation;
                                setEffectRotation();
                            }
                            int[] itemList = new int[effectItem.size() + 1];
                            int position = 0;
                            for (Map.Entry<String, Integer> entry : effectItem.entrySet()) {
                                itemList[position] = entry.getValue();
                                position++;
                            }
                            itemList[effectItem.size()] = faceBeautyItem;
                            faceunity.fuRenderToYUVImage(yBuffer, uBuffer, vBuffer, yStride, uStride, vStride, w, h, frameId++, itemList);

                            FUManager.class.notifyAll();
                        }
                    }
                });

                FUManager.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void destroyItems() {
        context = null;

        handler.post(new Runnable() {
            @Override
            public void run() {
                //TODO 调用fuDestroyAllItems，销毁所有道具（特效和美颜）
                /*
                 类文件 ：faceunity.java

                 函数原型：
                 void fuDestroyAllItems();
                 */
                faceunity.fuDestroyAllItems();
                effectItem.clear();
                faceBeautyItem = 0;
                faceunity.fuOnDeviceLost();
                handler.removeCallbacksAndMessages(null);
            }
        });
    }

    private static int createEffectItem(String name) {
        try {
            InputStream is = new FileInputStream(name);
            byte[] itemData = new byte[is.available()];
            is.read(itemData);
            is.close();
            int effectId = faceunity.fuCreateItemFromPackage(itemData);
            effectItem.put(name, effectId);
            faceunity.fuItemSetParam(effectId, "isAndroid", 1);
            setEffectRotation();
            return effectId;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据图片（yuv数组）朝向设置道具朝向
     */
    private static void setEffectRotation() {
        for (Map.Entry<String, Integer> entry : effectItem.entrySet()) {
            faceunity.fuItemSetParam(entry.getValue(), "default_rotation_mode", (rotation == 270) ? 1 : 3);
            faceunity.fuItemSetParam(entry.getValue(), "rotationAngle", (rotation == 270) ? 90 : 270);
        }
    }

    private static void destroyEffectItem(int effectItem) {
        if (effectItem != 0) {
            faceunity.fuDestroyItem(effectItem);
        }
    }


    /**
     * 清空面具
     */
    public static void clearMask() {
        for (Map.Entry<String, Integer> entry : effectItem.entrySet()) {
            destroyEffectItem(entry.getValue());
        }
        effectItem.clear();
        getmBeautyConfig().clearMask();
    }

    /**
     * 根据面具名字列表设置面具
     *
     * @param nameList
     * @param pagePosition
     * @param position
     */
    public static void setMaskByNameList(final List<String> nameList, int pagePosition, int position) {
        if (nameList == null) return;
        if (nameList.size() == 0) {
            mPagePosition = null;
        } else {
            mPagePosition = new int[]{pagePosition, position};
        }
        clearMask();
        creatingItem = true;
        for (final String name : nameList) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //TODO 调用FUManager封装的loadItem，加载道具数组ITEM_NAMES中对应位置道具，实现UI交互道具切换
                    setMaskByName(name);
                    creatingItem = false;
                }
            }).start();
        }
    }

    /**
     * 根据面具名字设置面具
     *
     * @param name
     */
    public static void setMaskByName(String name) {
        getmBeautyConfig().addMask(name);
        createEffectItem(name);
    }

    public static void setCurrentFilterByPosition(final int filterPosition) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String filterName = FILTERS[filterPosition];
                FUManager.getmBeautyConfig().setFilter_position(filterPosition);
                faceunity.fuItemSetParam(faceBeautyItem, "filter_name", filterName);
            }
        });
    }

    public static void setBlurLevel(final int level) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getmBeautyConfig().setBlur_level(level);
                faceunity.fuItemSetParam(faceBeautyItem, "blur_level", level);
            }
        });
    }

    public static void setColorLevel(final float level) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getmBeautyConfig().setColor_level(level);
                faceunity.fuItemSetParam(faceBeautyItem, "color_level", level);
            }
        });
    }

    public static void setRedLevel(final float level) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getmBeautyConfig().setRed_level(level);
                faceunity.fuItemSetParam(faceBeautyItem, "red_level", level);
            }
        });
    }

    public static void setFaceShape(final float level) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getmBeautyConfig().setFace_shape(level);
                faceunity.fuItemSetParam(faceBeautyItem, "face_shape", level);
            }
        });
    }

    public static void setFaceShapeLevel(final float level) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getmBeautyConfig().setFace_shape_level(level);
                faceunity.fuItemSetParam(faceBeautyItem, "face_shape_level", level);
            }
        });
    }

    public static void setCheekThinning(final float level) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getmBeautyConfig().setCheek_thinning(level);
                faceunity.fuItemSetParam(faceBeautyItem, "cheek_thinning", level);
            }
        });
    }

    public static void setEyeEnlarging(final float level) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getmBeautyConfig().setEye_enlarging(level);
                faceunity.fuItemSetParam(faceBeautyItem, "eye_enlarging", level);
            }
        });
    }


    public static class BeautyConfig {
        private int blur_level = 3;//磨皮
        private float color_level = 0.5f;//美白
        private float red_level = 0.5f;//红润
        private float face_shape = 3f;//脸型
        private float face_shape_level = 0.5f;//脸型程度
        private float cheek_thinning = 0.0f;//瘦脸
        private float eye_enlarging = 0.0f;//大眼
        private int filter_position = 0;//滤镜
        private List<String> mask = new ArrayList<>();//面具

        public BeautyConfig() {
        }

        public List<String> getMask() {
            return mask;
        }

        public BeautyConfig setMask(List<String> mask) {
            this.mask = mask;
            return this;
        }

        public BeautyConfig addMask(String maskItem) {
            mask.add(maskItem);
            return this;
        }

        public void clearMask() {
            mask.clear();
        }

        public int getFilter_position() {
            return filter_position;
        }

        public BeautyConfig setFilter_position(int filter_position) {
            this.filter_position = filter_position;
            return this;
        }

        public BeautyConfig setBlur_level(int blur_level) {
            this.blur_level = blur_level;
            return this;
        }

        public BeautyConfig setColor_level(float color_level) {
            this.color_level = color_level;
            return this;
        }

        public BeautyConfig setRed_level(float red_level) {
            this.red_level = red_level;
            return this;
        }

        //
        private BeautyConfig setFace_shape(float face_shape) {
            this.face_shape = face_shape;
            return this;
        }

        private BeautyConfig setFace_shape_level(float face_shape_level) {
            this.face_shape_level = face_shape_level;
            return this;
        }

        public BeautyConfig setCheek_thinning(float cheek_thinning) {
            this.cheek_thinning = cheek_thinning;
            return this;
        }

        public BeautyConfig setEye_enlarging(float eye_enlarging) {
            this.eye_enlarging = eye_enlarging;
            return this;
        }

        public int getBlur_level() {
            return blur_level;
        }

        public float getColor_level() {
            return color_level;
        }

        public float getRed_level() {
            return red_level;
        }

        public float getFace_shape() {
            return face_shape;
        }

        public float getFace_shape_level() {
            return face_shape_level;
        }

        public float getCheek_thinning() {
            return cheek_thinning;
        }

        public float getEye_enlarging() {
            return eye_enlarging;
        }
    }


}
