package filedownloader.abs.co.jp.tetlis_test1;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.logging.LogRecord;

public class TetrixView extends SurfaceView implements SurfaceHolder.Callback{

    private TetrixThread m_thread;

    public TetrixView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        m_thread = new TetrixThread(holder, context, new java.util.logging.Handler() {
            @Override
            public void publish(LogRecord logRecord) {
            }
            @Override
            public void flush() {
            }
            @Override
            public void close() throws SecurityException {
            }
        });

        setFocusable(true);
        setLongClickable(true);
        setGesture();
    }

    private void setGesture() {
        final GestureDetector gestureDetector = new GestureDetector(
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        m_thread.addFling(velocityX, velocityY);
                        return true;
                    }
                });

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,int format, int width, int height) {
        m_thread.setSurfaceSize(width,height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_thread.setRunning(true);
        m_thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        m_thread.setRunning(false);
        while (retry){
            try {
                m_thread.join();
                retry = false;
            }catch (InterruptedException e){
            }
        }
    }
}
