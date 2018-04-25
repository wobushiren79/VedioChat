package com.huanmedia.videochat.common.service.socket;

import com.huanmedia.ilibray.utils.Installation;
import com.huanmedia.ilibray.utils.data.cipher.MD5Cipher;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.repository.net.NetConstants;
import com.huanmedia.videochat.repository.net.codes;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Socket配置
 * Create by Administrator
 * time: 2017/11/23.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class WSConfig {
    private String mHost;
    private Map<String,String> parms=new HashMap<>();
    private int conntimeOut;

    public WSConfig(Bulide bulide) {
        this.mHost = bulide.mHost;
        this.parms = bulide.parms;
        this.conntimeOut = bulide.conntimeOut;
    }

    public int getConntimeOut() {
        return conntimeOut;
    }

    public URI getHost() {
        Map<String,String> parms=getParms();
        StringBuffer urlBuf = new StringBuffer(mHost);
        urlBuf.append("/?");
        for (String key :parms.keySet()){
            urlBuf.append(key);
            urlBuf.append("=");
            urlBuf.append( parms.get(key));
            urlBuf.append("&");
        }
        return URI.create(urlBuf.toString());
    }

    protected Map<String, String> getParms() {
        if (parms.get("sign")==null){
            parms.put("sign",getHandshakePrams(parms));
        }
        return parms;
    }

    protected   String getHandshakePrams(Map<String,String> parms) {
        String signHandshake = "";
        try {
            String password = NetConstants.S;
            String timeTen= parms.get("time");
            String token=parms.get("token");
            String deviceid=parms.get("deviceid");
            signHandshake= new String(new MD5Cipher().Md5((password + timeTen + token + deviceid).getBytes()));
        }catch (Exception e){
            throw new RuntimeException("握手参数异常");
        }
        return signHandshake;
    }

    public Bulide newBulede() {
      return new Bulide().setConntimeOut(this.conntimeOut)
              .setHost(this.mHost)
              .setParms(this.parms);
    }

    public  static class Bulide{
        private String mHost;
        private Map<String,String> parms;
        private int conntimeOut;

        public Bulide setConntimeOut(int conntimeOut) {
            this.conntimeOut = conntimeOut;
            return this;
        }

        public Bulide setParms(Map<String, String> parms) {
            this.parms = parms;
            return this;
        }

        public Bulide setHost(String hostAndProt) {
            mHost = hostAndProt;
            return this;
        }
        public WSConfig bulide(){
            return new WSConfig(this);
        }
    }

}
