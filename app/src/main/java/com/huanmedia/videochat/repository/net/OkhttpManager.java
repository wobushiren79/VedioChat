package com.huanmedia.videochat.repository.net;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.huanmedia.ilibray.BuildConfig;
import com.huanmedia.ilibray.utils.Installation;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.manager.UserManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttp客户端管理类
 * 该类中可以更具需要修改HTTPS的使用方式
 * {@link OkhttpManager#certificateClient(String[])}
 * 和{@link OkhttpManager#certificateClient(Context, int[])}
 * 以及 {@link OkhttpManager#noCertificateClient(String)} }
 * 其中 {@link OkhttpManager#noCertificateClient(String)}} 需要配置证书公钥
 *<p/>
 * Create by Eric<br/>
 * time: 2017/11/10.<br/>
 * Email:eric.yang@huanmedia.com<br/>
 * version: v1.0
 */

public class OkhttpManager {
    private volatile static OkhttpManager INSTANCE;

    private String CER_VIDEOCHAT = "-----BEGIN CERTIFICATE-----\n" +
            "MIICRTCCAa4CCQCgOYKgP4wcAjANBgkqhkiG9w0BAQUFADBnMQswCQYDVQQGEwJD\n" +
            "TjEQMA4GA1UECAwHU2lDaHVhbjEQMA4GA1UEBwwHQ2hlbmdEdTEQMA4GA1UECgwH\n" +
            "cHJvcGhldDEQMA4GA1UECwwHcHJvcGhldDEQMA4GA1UEAwwHcHJvcGhldDAeFw0x\n" +
            "NzAzMjAxMDQ5MzFaFw0yNzAzMTgxMDQ5MzFaMGcxCzAJBgNVBAYTAkNOMRAwDgYD\n" +
            "VQQIDAdTaUNodWFuMRAwDgYDVQQHDAdDaGVuZ0R1MRAwDgYDVQQKDAdwcm9waGV0\n" +
            "MRAwDgYDVQQLDAdwcm9waGV0MRAwDgYDVQQDDAdwcm9waGV0MIGfMA0GCSqGSIb3\n" +
            "DQEBAQUAA4GNADCBiQKBgQC7BiruA/kghnZ+6sCObp1eKpg8ADTZzjAP9yZe7nUr\n" +
            "1/IePL6LAurBehGwMCFSeUE9arqd5Cra6DYRmwdKUUrdcOJKuBlXi/bUy4JkvlEY\n" +
            "f76giaThsiSX9Hejr1stAFIvGX33TXZQqAr5ezv0suGARORaDPeHnJEtqkA6v0u9\n" +
            "aQIDAQABMA0GCSqGSIb3DQEBBQUAA4GBAJ3AvKy8oLJ7r4QN15WVFrRzr7nRlSpu\n" +
            "119x5TCqqbRpcuMX0NV9xfU6X0/JK+aNh4NDEvdeRCQuQNqePtvkVT1B7uzR59He\n" +
            "+XuTDMHNG66Lh0D0oPMG/2w8Rw/QSsuRS95tWNinywHN2ezAgD01WCSzdw30K1CL\n" +
            "nXD9qGptgX7Y\n" +
            "-----END CERTIFICATE-----";

    private OkHttpClient mClient;
    private  OkHttpClient mHttpsClient;

    public final static int HTTP=1;
    public final static int HTTPS=2;
    /**
     * 信任主机列表
     */
   private String[] mVerifierHost=new String[]{HostManager.getDomainName()};

    /**
     * 默认请求头处理
     */
    private Interceptor mDefaulHeader=new Interceptor() {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder newReq = request.newBuilder();
            newReq.addHeader("Authentication",getAuthentication());
            HttpUrl url = request
                    .url()
                    .newBuilder()
                    .addQueryParameter("sId",UserManager.getInstance().getsId()==null?"":UserManager.getInstance().getsId())
                    .build();
            newReq.url(url);
            return chain.proceed(newReq.build());
        }
    };
    public static   String getAuthentication(){
        //"Authentication","appid=1, account=admin, skey=xxx, nonce=xxx, created=xxx ， deviceid=xxxxx"
        String a= codes.getRandom();
        String b= UserManager.getInstance().reviseSyncTime();
        String s=NetConstants.S+a+b;
        String x = codes.encode(codes.SHA256Encrypt(s).getBytes());
        String c = "appid=1,account=admin,skey="+x+",nonce="+a+",created="+b+" ,deviceid="+ Installation.id(FApplication.getApplication());
        return c;
    }
    public static OkhttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (OkhttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OkhttpManager();
                }
            }
        }
        return INSTANCE;
    }
    @IntDef({HTTP, HTTPS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ClientType{
    }
    private OkhttpManager() {
    }

    private  OkHttpClient getClientInst(@ClientType int clientType) {
        switch (clientType){
            case HTTP:
                if (mClient==null)
                mClient = httpClient();
                return mClient;
            case HTTPS:
                if (mHttpsClient==null){
//                mClient =certificateClient(FApplication.getApplication(),new int[]{});
//                mClient =noCertificateClient("");//公共密钥
                    mHttpsClient = certificateClient(new String[]{CER_VIDEOCHAT});// 证书字符串
                }
            return mHttpsClient;
        }
        return null;
    }


    /**
     * 无本地证书方式
     * Create by ericYang on 2017/11/10.
     */
    public OkHttpClient getClient(@ClientType int type) {
        return  getClientInst(type);
    }
    /**
     * 没有证书的OkHttp
     * Create by ericYang on 2017/11/10.
     */
    private OkHttpClient httpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        builder.addInterceptor(mDefaulHeader);
        return builder.build();
    }

    /**
     * 有证书的本地证书
     * Create by ericYang on 2017/11/10.
     */
    @SuppressWarnings("ConstantConditions")
    private OkHttpClient certificateClient(Context context, int[] certificates) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);
        TrustManagerFactory trustManagerFactory = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            //Create a KeyStore containing our trusted CAs
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                //读取本地证书
                InputStream is = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(is));

                if (is != null) {
                    is.close();
                }
            }
            configHttps(builder, keyStore);
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void configHttps(OkHttpClient.Builder builder, KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustManagerFactory trustManagerFactory;
        trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
            builder.sslSocketFactory(sslSocketFactory, trustManager);
        }
        builder.addInterceptor(mDefaulHeader);
        builder.hostnameVerifier(getHostnameVerifier(mVerifierHost));
    }

    /**
     * 有证书的本地证书
     * Create by ericYang on 2017/11/10.
     */
    @SuppressWarnings("ConstantConditions")
    private OkHttpClient certificateClient(String[] certificates) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);
        TrustManagerFactory trustManagerFactory = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            //Create a KeyStore containing our trusted CAs
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                //读取本地证书
                InputStream is =new okio.Buffer()
                        .writeUtf8(certificates[i])
                        .inputStream() ;
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(is));

                if (is != null) {
                    is.close();
                }
            }
            configHttps(builder, keyStore);
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 本地无证书认证公钥
     * Create by ericYang on 2017/11/10.
     */
    private OkHttpClient noCertificateClient(String pub_key) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        builder.addInterceptor(mDefaulHeader);
        Object[] parms;
        try {
            parms = getSSLSocketFactory(pub_key);//TODO 需要填写公共密钥
            builder.sslSocketFactory((SSLSocketFactory) parms[0], (X509TrustManager) parms[1]);
            builder.hostnameVerifier(getHostnameVerifier(mVerifierHost));
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    private Object[] getSSLSocketFactory(String pub_key) throws Exception {
        Object[] sslParms = new Object[2];
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            //证书中的公钥
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {


            }

            //客户端并为对ssl证书的有效性进行校验
            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
                if (chain == null) {
                    throw new IllegalArgumentException("checkServerTrusted:x509Certificate array isnull");
                }

                if (!(chain.length > 0)) {
                    throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
                }

                if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
                    throw new CertificateException("checkServerTrusted: AuthType is not RSA");
                }

                // Perform customary SSL/TLS checks
                try {
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                    tmf.init((KeyStore) null);
                    for (TrustManager trustManager : tmf.getTrustManagers()) {
                        ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                    }
                } catch (Exception e) {
                    throw new CertificateException(e);
                }
                // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
                // with 0×30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0×00 to drop.
                RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();

                String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);
                // Pin it!
                final boolean expected = pub_key.equalsIgnoreCase(encoded);

                if (!expected) {
                    throw new CertificateException("checkServerTrusted: Expected public key: "
                            + pub_key + ", got public key:" + encoded);
                }

            }


            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts,
                new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        sslParms[0] = sslContext
                .getSocketFactory();
        sslParms[1] = trustAllCerts[0];
        return sslParms;
    }
    /**
     * 主机认证
     * Create by ericYang on 2017/11/10.
     */
    private  HostnameVerifier getHostnameVerifier(final String[] hostUrls) {
        return (hostname, session) -> {
            boolean ret = false;
            for (String host : hostUrls) {
                if (host.equalsIgnoreCase(hostname)) {
                    ret = true;
                }
            }
            return ret;
        };
    }
}
