package com.huanmedia.videochat.repository.net;

import com.huanmedia.videochat.common.manager.UserManager;
import com.orhanobut.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public final class codes {

	public static String SHA256Encrypt(String orignal) {
        MessageDigest md = null;
        try { 
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
			Logger.e(e.getMessage());
        }
        if (null != md) { 
            byte[] origBytes = orignal.getBytes(); 
            md.update(origBytes); 
            byte[] digestRes = md.digest(); 
            String digestStr = getDigestStr(digestRes);
            return digestStr; 
        }

        return null; 
    }
	 private static String getDigestStr(byte[] origBytes) {
	        String tempStr = null;
	        StringBuilder stb = new StringBuilder();
	        for (int i = 0; i < origBytes.length; i++) { 
	            tempStr = Integer.toHexString(origBytes[i] & 0xff);
	            if (tempStr.length() == 1) { 
	                stb.append("0"); 
	            } 
	            stb.append(tempStr);

	        } 
	        return stb.toString(); 
	    }
	    private static char[] base64EncodeChars = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/'};
	    public static String encode(byte[] data) {
	        StringBuffer sb = new StringBuffer();
	        int len = data.length;
	        int i = 0;
	        int b1, b2, b3;
	        while (i < len) {
	            b1 = data[i++] & 0xff;
	            if (i == len) {
	                sb.append(base64EncodeChars[b1 >>> 2]);
	                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
	                sb.append("==");
	                break;
	            }
	            b2 = data[i++] & 0xff;
	            if (i == len) {
	                sb.append(base64EncodeChars[b1 >>> 2]);
	                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
	                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
	                sb.append("=");
	                break;
	            }
	            b3 = data[i++] & 0xff;
	            sb.append(base64EncodeChars[b1 >>> 2]);
	            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
	            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
	            sb.append(base64EncodeChars[b3 & 0x3f]);
	        }
	        return sb.toString();
	    }

	public static String getRandom(){
		StringBuffer sbs=new StringBuffer();
		Random random = new Random();
		for(int i=0;i<6;i++){
			sbs.append(random.nextInt(10));
		}
		return sbs.toString();
	}
	public static String getTimeTen(){
		long time= System.currentTimeMillis();
		return String.valueOf(time).substring(0, 10);
	}

	public static String getTime(){
		long time= System.currentTimeMillis();
		return String.valueOf(time);
	}
}
