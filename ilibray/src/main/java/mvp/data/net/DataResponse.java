package mvp.data.net;

import com.google.gson.annotations.SerializedName;

//DataResponse.java
public class DataResponse<T> {
    @SerializedName("code")
    private int mCode;
    @SerializedName("msg")
    private String mMessage;
    @SerializedName("data")
    private T mResult;
    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public T getResult() {
        return mResult;
    }

    public void setCode(int code) {
        mCode = code;
    }


    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        return mCode != 0;
    }
}