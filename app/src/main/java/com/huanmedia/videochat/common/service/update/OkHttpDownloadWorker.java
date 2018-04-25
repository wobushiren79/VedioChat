/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huanmedia.videochat.common.service.update;

import android.text.TextUtils;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.DownloadWorker;
import org.lzh.framework.updatepluginlib.impl.HttpException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 默认的apk下载任务。若需定制，则可通过{@link UpdateBuilder#setDownloadWorker(Class)}或者{@link UpdateConfig#setDownloadWorker(Class)}进行定制使用
 *
 * <p>此默认下载任务。支持断点下载功能。
 *
 * @author haoge
 */
public class OkHttpDownloadWorker extends DownloadWorker {

    private File original;
    private File bak;
    private long contentLength;
    private static final OkHttpClient sOkClient;

    static {
        sOkClient = new OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MINUTES)
                .build();
    }

    private Response mResponse;

    @Override
    protected void download(String url, File target) throws Exception{
        original = target;
        URL httpUrl = new URL(url);
        Request.Builder builder = new Request.Builder().url(httpUrl);
        Request request = builder.get()
                .addHeader("Accept-Encoding","identity")
                .build();
        Call call = sOkClient.newCall(request);
        target.delete();
        mResponse = call.execute();

        int responseCode = mResponse.code();
        if (responseCode < 200 || responseCode >= 300) {
            mResponse.close();
            throw new HttpException(responseCode,mResponse.message());
        }

        contentLength = mResponse.body().contentLength();
        // 使用此bak文件进行下载。辅助进行断点下载。
        if (checkIsDownAll()) {
            mResponse.close();
            mResponse = null;
            // notify download completed
            sendDownloadComplete(original);
            return;
        }

        createBakFile();
        FileOutputStream writer = supportBreakpointDownload();

        long offset = bak.length();
        InputStream inputStream = mResponse.body().byteStream();
        byte[] buffer = new byte[8 * 1024];
        int length;
        long start = System.currentTimeMillis();
        while ((length = inputStream.read(buffer)) != -1) {
            writer.write(buffer, 0, length);
            offset += length;
            long end = System.currentTimeMillis();
            if (end - start > 1000) {
                sendDownloadProgress(offset,contentLength);
                start = System.currentTimeMillis();
            }
        }

        inputStream.close();
        writer.close();
        inputStream = null;

        // notify download completed
        renameAndNotifyCompleted();
    }

    private boolean checkIsDownAll() {
        return original.length() == contentLength
                && contentLength > 0;
    }

    private FileOutputStream supportBreakpointDownload() throws IOException {

        String range = mResponse.header("Accept-Ranges");
        if (TextUtils.isEmpty(range) || !range.startsWith("bytes")) {
            bak.delete();
            return new FileOutputStream(bak, false);
        }

        long length = bak.length();

        mResponse.close();
        Request.Builder bulide = mResponse.request().newBuilder();
        bulide.addHeader("RANGE", "bytes=" + length + "-" + contentLength);
        Call call = sOkClient.newCall(bulide.build());
        mResponse = call.execute();

        int responseCode = mResponse.code();
        if (responseCode < 200 || responseCode >= 300) {
            throw new HttpException(responseCode,mResponse.message());
        }

        return new FileOutputStream(bak, true);
    }

    // 创建bak文件。
    private void createBakFile() {
        bak = new File(String.format("%s_%s", original.getAbsolutePath(), contentLength));
    }

    private void renameAndNotifyCompleted() {
        original.delete();
        bak.renameTo(original);
        sendDownloadComplete(original);
    }
}
