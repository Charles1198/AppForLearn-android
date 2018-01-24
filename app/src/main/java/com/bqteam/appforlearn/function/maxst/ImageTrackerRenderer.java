/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */
package com.bqteam.appforlearn.function.maxst;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bqteam.appforlearn.function.maxst.util.BackgroundRenderHelper;
import com.bqteam.appforlearn.util.LogUtil;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


class ImageTrackerRenderer implements Renderer {
    private int surfaceWidth;
    private int surfaceHeight;
    private BackgroundRenderHelper backgroundRenderHelper;

    private OnRecognizeListener onRecognizeListener;

    @SuppressLint("HandlerLeak")
    private
    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (onRecognizeListener != null) {
                onRecognizeListener.recognized(msg.arg1 > 0);
            }
        }
    };

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        backgroundRenderHelper = new BackgroundRenderHelper();
        backgroundRenderHelper.init();

        MaxstAR.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        surfaceWidth = width;
        surfaceHeight = height;

        MaxstAR.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);

        TrackingState state = TrackerManager.getInstance().updateTrackingState();
        TrackingResult trackingResult = state.getTrackingResult();

        backgroundRenderHelper.drawBackground();

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        Message message = new Message();
        message.arg1 = trackingResult.getCount();
        mainHandler.sendMessage(message);
    }

    public interface OnRecognizeListener {
        /**
         * 告诉监听者识别到目标否
         *
         * @param recognized
         */
        void recognized(boolean recognized);
    }

    public void setOnRecognizeListener(OnRecognizeListener listener) {
        onRecognizeListener = listener;
    }
}
