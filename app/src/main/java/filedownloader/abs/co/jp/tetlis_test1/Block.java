package filedownloader.abs.co.jp.tetlis_test1;

import android.graphics.Point;
import android.graphics.PointF;

public class Block {

    public static int NUM_OF_CELLS = 4;
    private PointF m_pos;
    private BlockType m_type;
    private PointF [] m_locals;
    private Point [] m_cells;

    public Block(){
        m_pos = new PointF();
        m_pos.x = 0;
        m_pos.y = 0;
        m_type = BlockType.random();

        initPoints();
        update();
    }

    public Block(Block block){
        m_pos = new PointF();
        m_pos.set(block.m_pos);
        m_type = block.m_type;
        m_locals = new PointF[NUM_OF_CELLS];
        m_cells = new Point[NUM_OF_CELLS];

        for (int i = 0; i < NUM_OF_CELLS; i++){
            m_locals[i] = new PointF();
            m_locals[i].set(block.m_locals[i]);
            m_cells[i] = new Point(block.m_cells[i]);
        }//for
    }

    private void initPoints(){
        m_locals = new PointF[NUM_OF_CELLS];
        m_cells = new Point[NUM_OF_CELLS];

        for (int i = 0; i < NUM_OF_CELLS; i++){
            m_locals[i] = new PointF();
            m_locals[i].set(m_type.getPoint(i));
            m_cells[i] = new Point(0, 0);
        }//for
    }

    private void update(){
        for (int i = 0; i < NUM_OF_CELLS; i++){
            m_cells[i].x = Math.round(m_locals[i].x + m_pos.x);
            m_cells[i].y = Math.round(m_locals[i].y + m_pos.y);
        }//for
    }

    public BlockType getType(){
        return m_type;
    }

    public Point [] getCells(){
        return m_cells;
    }

    public void moveBy(int x, int y){
        m_pos.x += x;
        m_pos.y += y;
        update();
    }

    public void moveTo(int x, int y){
        m_pos.x += x;
        m_pos.y += y;
        update();
    }

    public void rotate(double theta){
        for (PointF local : m_locals){
            float x = local.x;
            float y = local.y;

            local.x = (float)(x * Math.cos(theta) - y * Math.sin(theta));
            local.y = (float)(x * Math.sin(theta) + y * Math.cos(theta));
        }//for
        update();
    }
}
