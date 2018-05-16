package filedownloader.abs.co.jp.tetlis_test1;

import android.graphics.PointF;

import java.util.Random;

public enum  BlockType {

    T(0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f),
    X(-0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f),
    L(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 1.0f, -1.0f),
    J(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, -1.0f, -1.0f),
    B(0.0f, -1.5f, 0.0f, -0.5f, 0.0f, 0.5f, 0.0f, 1.5f),
    S(-0.5f, 0.0f, 0.5f, 0.0f, -0.5f, 1.0f, 0.5f, -1.0f),
    Z(-0.5f, 0.0f, 0.5f, 0.0f, -0.5f, -1.0f, 0.5f, 1.0f),
    EMPTY(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

    private static int NUM_OF_BLOCKTYPES = 7;
    private static Random rand = new Random();

    public static BlockType random(){
        return valueOf(rand.nextInt(NUM_OF_BLOCKTYPES));
    }

    public static BlockType valueOf(int value){
        switch (value){
            case 0: return T;
            case 1: return X;
            case 2: return L;
            case 3: return J;
            case 4: return B;
            case 5: return S;
            case 6: return Z;
        }//switch
        return EMPTY;
    }

    private PointF [] m_points;

    private BlockType(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3){
        m_points = new PointF[Block.NUM_OF_CELLS];
        m_points[0] = new PointF(x0, y0);
        m_points[1] = new PointF(x1, y1);
        m_points[2] = new PointF(x2, y2);
        m_points[3] = new PointF(x3, y3);
    }

    public PointF getPoint(int i){
        return m_points[i];
    }
}
