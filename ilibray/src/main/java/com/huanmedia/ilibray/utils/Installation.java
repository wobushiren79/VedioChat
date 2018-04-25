package com.huanmedia.ilibray.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * @description 用于创建设备安装标识（唯一标识）<br/>
 * @author Eric<br/>
 * @since <br/>
 * @version <br/>
 * @email yb498869020@hotmail.com<br/>
 * Create by ericYang on 2017/11/15.
 */
public class Installation {
    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";
//    protected static final String PREFS_FILE = "device_id.xml";
//    protected static final String PREFS_DEVICE_ID = "device_id";
//    protected static UUID uuid;

//    public UUID id(Context context) {
//        if (uuid == null) {
//            synchronized (Installation.class) {
//                if (uuid == null) {
//                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
//                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
//                    if (id != null) {
//                        // Use the ids previously computed and stored in the prefs file
//                        uuid = UUID.fromString(id);
//                    } else {
//                        @SuppressLint("HardwareIds")
//                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
//                        // unless it's not available, then fallback on a random number which we store
//                        // to a prefs file
//                        try {
//                            if (!"9774d56d682e549c".equals(androidId)) {
//                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
//                            } else {
//                                    @SuppressLint({"HardwareIds", "MissingPermission"})
//                                    final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//                                    uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
//                            }
//                        } catch (UnsupportedEncodingException e) {
//                            throw new RuntimeException(e);
//                        }
//                        // Write the value out to the prefs file
//                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString() ).commit();
//                    }
//                }
//            }
//        }
//        return uuid;
//    }
    public synchronized static String id(Context context) {
        if (sID == null) {  
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID =readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}