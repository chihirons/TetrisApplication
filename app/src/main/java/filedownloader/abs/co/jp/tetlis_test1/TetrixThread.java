package filedownloader.abs.co.jp.tetlis_test1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.logging.Handler;

public class TetrixThread extends Thread {

    private final String TAG = "TetrixThread";

    private SurfaceHolder m_holder;
    private Handler m_handler;
    private Context m_context;
    private boolean m_isRunning;
    private Paint m_black;
    private long m_fps;
    private int m_targetFps;
    private int m_frameQuantum;
    private int m_canvasWidth;
    private int m_canvasHeigth;
    private Game m_game;


    public TetrixThread(SurfaceHolder holder, Context context, Handler handler) {
        m_holder = holder;
        m_handler = handler;
        m_context = context;

        m_targetFps = 30;
        m_frameQuantum = 1000 / m_targetFps;

        m_black = new Paint();
        m_black.setAntiAlias(false);
        m_black.setARGB(255, 0, 0, 0);

        m_game = new Game();
    }

    @Override
    public void run() {
        Log.d(TAG, "run");

        long diffs[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        long diff;
        int diffPos = 0;
        int frame = 0;

        while (m_isRunning) {
            long start = System.currentTimeMillis();

            Canvas canvas = null;

            try {
                canvas = m_holder.lockCanvas(null);
                synchronized (m_holder) {
                    if (frame++ == 0) {
                        m_game.tick();
                    } else {
                        if (frame == m_targetFps) {
                            frame = 0;
                        }//if
                    }//else
                    doDraw(canvas);
                }
            } finally {
                if (canvas != null) {
                    m_holder.unlockCanvasAndPost(canvas);
                }//if
            }

            diff = System.currentTimeMillis() - start;
            if (diff < m_frameQuantum && m_isRunning) {
                try {
                    Thread.sleep(m_frameQuantum - diff);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }//if

            diff = System.currentTimeMillis() - start;
            if (++diffPos > 9) {
                diffPos = 0;
            }//if
            diffs[diffPos] = diff;
            long total = 0;
            for (long value : diffs) {
                total += value;
            }//for
            m_fps = 10000 / total;
        }
    }

        private void doDraw(Canvas canvas){
            canvas.drawRect(0, 0, m_canvasHeigth, m_canvasHeigth,m_black);
            m_game.draw(canvas);
        }

        public void setRunning(boolean b){
            m_isRunning = b;
        }

        public void setSurfaceSize(int width, int height){
            synchronized (m_holder){
                m_canvasWidth = width;
                m_canvasHeigth = height;
            }
        }

        public void addFling(float vx, float vy){
            int theta = (int) Math.toDegrees(Math.atan2(vy,vx));
            if(theta < 0){
                theta += 360;
            }//if
            double hypot = Math.hypot(vx, vy);

            Log.i(TAG,"fling: (" + Integer.toString(theta) + "," + Double.toString(hypot) + ")");

            if(theta < 45 || theta >= 315){
                synchronized (m_holder){
                    m_game.moveBy(1,0);
                }
                return;
            }//if

            if(theta >= 45 && theta < 135){
                synchronized (m_holder){
                    m_game.drop();
                }
                return;
            }//if

            if(theta >= 135 && theta < 225){
                synchronized (m_holder){
                    m_game.moveBy(-1, 0);
                }
                return;
            }//if

            if(theta >= 225 && theta < 315){
                synchronized (m_holder){
                    m_game.rotate();
                }
                return;
            }//if
        }
}
