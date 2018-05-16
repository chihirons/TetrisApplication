package filedownloader.abs.co.jp.tetlis_test1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

public class Board {
    public static final int CELL_WIDTH = 18;
    public static final int CELL_PADDING = 2;
    private static String TAG = "Board";
    private static Paint s_framePaint = null;
    private static Paint s_blockPaint = null;
    private static Paint s_resPaint = null;

    private static Paint getFramePaint() {
        if (s_framePaint == null) {
            s_framePaint = new Paint();
            s_framePaint.setAntiAlias(true);
            s_framePaint.setARGB(255, 100, 100, 100);
            s_framePaint.setStyle(Paint.Style.STROKE);
        }//if
        return s_framePaint;
    }

    private static Paint getBlockPin() {
        if (s_blockPaint == null) {
            s_blockPaint = new Paint();
            s_blockPaint.setAntiAlias(true);
            s_blockPaint.setARGB(255, 100, 100, 100);
        }//if
        return s_blockPaint;
    }

    private static Paint getRedPain() {
        if (s_resPaint == null) {
            s_resPaint = new Paint();
            s_resPaint.setAntiAlias(true);
            s_resPaint.setARGB(255, 100, 100, 100);
        }//if
        return s_resPaint;
    }

    private Point m_location;
    private Point m_size;
    private int m_cellCount;
    private ArrayList<Rect> m_rectangles;
    private BlockType[] m_cells;
    private Rect m_frame;

    public Board(Point size, Point location) {
        m_size = size;
        m_location = location;
        m_cellCount = size.x * size.y;

        initRects();
        m_cells = new BlockType[m_cellCount];
        clear();
    }

    private void initRects() {
        m_rectangles = new ArrayList<Rect>();
        for (int y = 0; y < m_size.y; y++) {
            for (int x = 0; x < m_size.x; x++) {
                Point coord = toCoord(x, y);
                m_rectangles.add(new Rect(coord.x, coord.y, coord.x + CELL_WIDTH, coord.y + CELL_WIDTH));
            }//for x
        }//for y

        Point coord = toCoord(m_size.x, -1);
        m_frame = new Rect(m_location.x, m_location.y, coord.x, coord.y);
    }

    private Point toCoord(int x, int y) {
        return new Point(x * (CELL_WIDTH + CELL_PADDING) + CELL_PADDING + m_location.x,
                (m_size.y - y - 1) * (CELL_WIDTH + CELL_PADDING) + CELL_PADDING + m_location.y);
    }

    private boolean isCellEmpty(int i) {
        return m_cells[i] == BlockType.EMPTY;
    }

    private boolean isCellEmpty(Point pos) {
        return getCell(pos) == BlockType.EMPTY;
    }

    private boolean isCellEmpty(int x, int y) {
        return m_cells[y * m_size.x + x] == BlockType.EMPTY;
    }

    private BlockType getCell(Point pos) {
        return m_cells[pos.y * m_size.x + pos.x];
    }

    private boolean isCellInBoundary(Point pos) {
        return (pos.x >= 0 && pos.x < m_size.x && pos.y >= 0 && pos.y < m_size.y);
    }

    private void setCell(Point pos, BlockType value) {
        m_cells[pos.y * m_size.x + pos.x] = value;
    }

    public void clear() {
        for (int i = 0; i < m_cellCount; i++) {
            m_cells[i] = BlockType.EMPTY;
        }//for i
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < m_cellCount; i++) {
            if (!isCellEmpty(i)) {
                canvas.drawRect(m_rectangles.get(i), getBlockPin());

            }//if
        }//for
        canvas.drawRect(m_frame, getFramePaint());
    }

    public void drawBlock(Canvas canvas, Block block) {
        for (Point cell : block.getCells()) {
            canvas.drawRect(m_rectangles.get(cell.y * m_size.x + cell.x), getRedPain());
        }//for
    }

    public boolean load(Block block) {
        for (Point cell : block.getCells()) {
            if (!isCellEmpty(cell)) {
                return false;
            }//if
            setCell(cell, block.getType());
        }//for
        return true;
    }

    public boolean unload(Block block) {
        for (Point cell : block.getCells()) {
            setCell(cell, BlockType.EMPTY);
        }//for
        return true;
    }

    public boolean isBlockInBoundary(Block block) {
        for (Point cell : block.getCells()) {
            if (!isCellInBoundary(cell)) {
                return false;
            }//if
        }//for
        return true;
    }

    public boolean isBlockCollide(Block block) {
        for (Point cell : block.getCells()) {
            if (!isCellEmpty(cell)) {
                return false;
            }//if
        }//for
        return true;
    }

    public void checkRows() {
        for (int y = m_size.y - 1; y >= 0; y--) {
            if (isRowFilled(y)) {
                removeRow(y);
            }//if
        }//for
    }

    private boolean isRowFilled(int y) {
        for (int x = 0; x < m_size.x; x++) {
            if (isCellEmpty(x, y)) {
                return false;
            }//if
        }//for
        return true;
    }

    private void removeRow(int y) {
        Log.i(TAG, "About to remove row" + Integer.toString(y));

        for (int i = y * m_size.x; i < m_cellCount - m_size.x; i++) {
            m_cells[i] = m_cells[i + m_size.x];
        }//for

        for (int i = m_cellCount - m_size.x; i < m_cellCount; i++) {
            m_cells[i] = BlockType.EMPTY;
        }///for

    }

    public Point getSize() {
        return m_size;
    }
}
