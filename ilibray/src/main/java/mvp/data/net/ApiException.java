package mvp.data.net;

//ApiException.java
public class ApiException extends RuntimeException {
    public static int NULL =-10000;//注意：服务器错误码不会超过5位，因此错误码只能重10000开始使用
    private int mErrorCode;

    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    public boolean isTokenExpried() {
        return mErrorCode == HttpConstants.TOKEN_EXPRIED;
    }
}