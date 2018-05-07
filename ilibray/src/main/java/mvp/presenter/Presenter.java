package mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huanmedia.ilibray.utils.data.assist.Network;

import java.io.InterruptedIOException;
import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import mvp.data.net.ApiException;
import mvp.view.BaseView;

public abstract class Presenter<V extends BaseView> {
    public Presenter() {
    }

    public V getView() {
        if (mWBaseView != null && mWBaseView.get() != null) {
            return mWBaseView.get();
        } else {
            return null;
        }
    }

    public boolean isNullView() {
        return mWBaseView == null || mWBaseView.get() == null;
    }

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private WeakReference<V> mWBaseView;

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onResume() method.
     */
    public void resume() {

    }

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onPause() method.
     */
    public void pause() {

    }

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onDestroy() method.
     */
    public void destroy() {
        dispose();
    }

    public void setView(@NonNull V view) {
        if (mWBaseView != null)
            mWBaseView.clear();
        mWBaseView = new WeakReference<V>(view);
    }

    @Nullable
    public Context getContext() {
        if (mWBaseView != null && mWBaseView.get() != null) {
            return mWBaseView.get().context();
        }
        return null;
    }

    public void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    public void remove(Disposable disposable) {
        mCompositeDisposable.remove(disposable);
    }

    public void dispose() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    public String getGeneralErrorStr(Throwable throwable) {
        if (throwable instanceof ApiException) {
            return throwable.getMessage();
        } else if (getContext() != null && !Network.isConnected(getContext())) {
            return "无网络连接";
        } else if (throwable instanceof InterruptedIOException || throwable instanceof UnknownHostException) {
            return "网络异常";
        }
        return "未知错误";
    }
}