package com.imitate.douyin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.imitate.douyin.R;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import moe.codeest.enviews.ENDownloadView;

import static android.content.ContentValues.TAG;

/***
 * 作者 ： 于德海
 * 时间 ： 2021/3/29 18:47
 * 描述 ： 
 */
public class CustomGsyVideo extends StandardGSYVideoPlayer {
    public CustomGsyVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        init();
    }

    public CustomGsyVideo(Context context) {
        super(context);
        init();
    }

    public CustomGsyVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mNeedShowWifiTip = false;
        getBackButton().setVisibility(View.GONE);
        getFullscreenButton().setVisibility(View.GONE);
        mDismissControlTime = 1000;
        mCurrentTimeTextView.setVisibility(View.GONE);
        mTotalTimeTextView.setVisibility(View.GONE);
        mBottomProgressBar.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.GONE);

    }

    public String getUrl() {
        return mOriginUrl;
    }

    float x1, y1, x2, y2;
    long donwTime;




    @Override
    protected void resolveUIState(int state) {
        Log.i(TAG, "resolveUIState = " + state);
        super.resolveUIState(state);
    }

    @Override
    protected void touchSurfaceDown(float x, float y) {
//        super.touchSurfaceDown(x, y);
        x1 = x;
        x2 = x;
        y1 = y;
        y2 = y;
        donwTime = System.currentTimeMillis();
    }
    @Override
    protected void touchSurfaceMove(float deltaX, float deltaY, float y) {
//        super.touchSurfaceMove(deltaX, deltaY, y);
        x2 = deltaX;
        y2 = deltaY;
    }
    @Override
    protected void touchSurfaceUp() {
        Log.i(TAG, "touchSurfaceUp");
//        super.touchSurfaceUp();
        if (System.currentTimeMillis() - donwTime < 500 && Math.abs(x2 - x1) < 20 && Math.abs(y2 - y1) < 20) {
            clickStartIcon();
        }

    }

    @Override
    public void onClick(View v) {
//        super.onClick(v);
        if (v.getId() == R.id.start) {
            clickStartIcon();
        }
    }

    @Override
    protected void startDismissControlViewTimer() {
        super.startDismissControlViewTimer();
        Log.i(TAG, "startDismissControlViewTimer = ");
    }

    @Override
    protected void cancelDismissControlViewTimer() {
        super.cancelDismissControlViewTimer();
        Log.i(TAG, "cancelDismissControlViewTimer = ");
    }

    @Override
    protected void touchDoubleUp(MotionEvent e) {
//        super.touchDoubleUp(e);
    }

    @Override
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {
//        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
    }

    @Override
    protected void dismissProgressDialog() {
//        super.dismissProgressDialog();
    }

    @Override
    protected void changeUiToNormal() {
        Debuger.printfLog("changeUiToNormal");
        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
        setViewShowState(mThumbImageViewLayout, VISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);
        updateStartImage();
        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
    }

    @Override
    protected void changeUiToPlayingShow() {
        Debuger.printfLog("changeUiToPlayingShow");

        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, VISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
        updateStartImage();
        mThumbImageViewLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mThumbImageViewLayout.setVisibility(View.GONE);
            }
        },200);
    }

    @Override
    protected void changeUiToPreparingShow() {
        Debuger.printfLog("changeUiToPreparingShow");

        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
        setViewShowState(mThumbImageViewLayout, VISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ENDownloadView enDownloadView = (ENDownloadView) mLoadingProgressBar;
            if (enDownloadView.getCurrentState() == ENDownloadView.STATE_PRE) {
                ((ENDownloadView) mLoadingProgressBar).start();
            }
        }
    }

    @Override
    protected void changeUiToPauseShow() {

        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, VISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
        updateStartImage();
        updatePauseCover();
    }

    @Override
    protected void hideAllWidget() {
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
    }
}
