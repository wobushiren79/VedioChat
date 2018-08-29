package com.huanmedia.videochat.common.widget;

import android.media.audiofx.Visualizer;

public abstract class WaveformVisualizer extends Visualizer {
    Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener() {

        //捕获波形数据
        @Override
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                          int samplingRate) {
            // updateVisualizer(bytes);

        }

        //捕获傅里叶数据
        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                     int samplingRate) {
            byte[] model = new byte[bytes.length / 2 + 1];
            model[0] = (byte) Math.abs(bytes[1]);
            int j = 1;
            for (int i = 2; i < 18; ) {
                model[j] = (byte) Math.hypot(bytes[i], bytes[i + 1]);
                i += 2;
                j++;
            }
            updateVisualizer(model);
        }
    };

    public WaveformVisualizer(int audioSession) throws UnsupportedOperationException, RuntimeException {
        super(audioSession);
        setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
        setDataCaptureListener
                (captureListener, Visualizer.getMaxCaptureRate() / 2, true, true);
    }

    public abstract void updateVisualizer(byte[] bytes);
}
