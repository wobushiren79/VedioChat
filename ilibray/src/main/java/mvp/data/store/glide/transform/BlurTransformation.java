package mvp.data.store.glide.transform; //-----------------------------图片模糊----------------------------------

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Type;
import android.support.annotation.RequiresApi;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * 图片模糊
 */
public class BlurTransformation extends BitmapTransformation {
    private static final String ID = "mvp.data.cache.glide.GlideUtils$GlideAppCircleTransform";
    private final int mRadius;
    private final int mScaledSize;
    private RenderScript rs;

    public BlurTransformation(Context context, int radius) {
        this(context, radius, 1);
    }

    public BlurTransformation(Context context, int radius, int scaledSize) {
        rs = RenderScript.create(context);
        this.mRadius = radius;
        this.mScaledSize = scaledSize;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {

        int USAGE_SHARED = 0x0080;
        Bitmap blurredBitmap = toTransform.copy( Bitmap.Config.ARGB_8888, true);
        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(
                rs,
                blurredBitmap,
                Allocation.MipmapControl.MIPMAP_FULL,
                USAGE_SHARED
        );
        Allocation output = Allocation.createTyped(rs, input.getType());

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);

        // Set the blur radius
        script.setRadius(mRadius);

        // Start the ScriptIntrinisicBlur
        script.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(blurredBitmap);
//            toTransform.recycle();
        return blurredBitmap;

//        Bitmap tempBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);
//        Bitmap blurredBitmap = Bitmap.createScaledBitmap(tempBitmap,
//                tempBitmap.getWidth() / mScaledSize,
//                tempBitmap.getHeight() / mScaledSize,
//                false);
//
//        // 创建一个模糊效果的RenderScript的工具对象，第二个参数Element相当于一种像素处理的算法，高斯模糊的话用这个就好
//        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//
//        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
//        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
//        Allocation input = Allocation.createFromBitmap(rs, blurredBitmap);
//        // 创建相同类型的Allocation对象用来输出
//        Type type = input.getType();
//        Allocation output = Allocation.createTyped(rs, type);
//
//        // 设置渲染的模糊程度, 25f是最大模糊度
//        blurScript.setRadius(mRadius);
//        // 设置blurScript对象的输入内存
//        blurScript.setInput(input);
//        // 将输出数据保存到输出内存中
//        blurScript.forEach(output);
//        // 将数据填充到bitmap中
//        output.copyTo(blurredBitmap);
//
//        // 销毁它们释放内存
//        input.destroy();
//        output.destroy();
//        blurScript.destroy();
//        rs.destroy();
//        type.destroy();
//
//        return blurredBitmap;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID.getBytes(CHARSET));
    }
}
  