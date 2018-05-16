package filedownloader.abs.co.jp.tetlis_test1;

import android.graphics.Canvas;
import android.graphics.Point;

public class Game {
    public static final int WIDTH = 9;
    public static final int HEIGHT = 20;

    private interface Transformable{
        void transform();
    }

    enum GameMode{
        NEW,
        ACTIVE,
        GAME_OVER
    }

    private GameMode m_mode;
    private Board m_board;
    private Board m_mini;
    private Block m_currentBlock;
    private Block m_nextBlock;

    public Game(){
        m_mode = GameMode.NEW;
        m_board = new Board(new Point(WIDTH, HEIGHT), new Point(10, 10));
        m_mini = new Board(new Point(5, 5), new Point(200, 10));

        loadNextBlock();
        loadNewBlock();
    }

    public void draw(Canvas canvas){
        m_board.draw(canvas);
        m_board.drawBlock(canvas, m_currentBlock);
        m_mini.draw(canvas);
    }

    public boolean loadNextBlock(){
        m_nextBlock = new Block();
        m_nextBlock.moveTo(m_mini.getSize(). x / 2, m_mini.getSize(). y -3);

        m_mini.clear();
        if(!m_mini.isBlockInBoundary(m_nextBlock) || !m_mini.isBlockCollide(m_nextBlock)){
            return false;
        }//if
        return m_mini.load(m_nextBlock);
    }

    public boolean loadNewBlock(){
        m_currentBlock = m_nextBlock;
        loadNextBlock();
        m_currentBlock.moveTo(m_board.getSize(). x / 2, m_board.getSize(). y - 3);

        if(!m_board.isBlockInBoundary(m_currentBlock) || !m_board.isBlockCollide(m_currentBlock)){
            m_mode = GameMode.GAME_OVER;
            return false;
        }//if
        return m_board.load(m_currentBlock);
    }

    public boolean moveBy(final int x, final int y){
        if (m_mode == GameMode.NEW){
            m_mode = GameMode.ACTIVE;
        }//if

        return transform(new Transformable(){
           @Override
           public void transform(){
               m_currentBlock.moveBy(x,y);
           }
        });
    }

    public boolean rotate(){
        return transform(new Transformable(){
            @Override
            public void transform() {
                m_currentBlock.rotate(Math.PI / 2.0);
            }
        });
    }

    private boolean transform(Transformable transformable){
        if(m_mode != GameMode.ACTIVE){
            return false;
        }//if

        boolean retval = true;
        Block temp = new Block(m_currentBlock);
        m_board.unload(m_currentBlock);
        transformable.transform();

        if(!m_board.isBlockInBoundary(m_currentBlock) || !m_board.isBlockCollide(m_currentBlock)){
            m_currentBlock = temp;
            retval = false;
        }//if

        m_board.load(m_currentBlock);
        return retval;
    }

    public boolean tick(){
        if(m_mode != GameMode.ACTIVE){
            return false;
        }//if

        if(moveBy(0, -1)){
            return true;
        }//if

        m_board.checkRows();
        return loadNewBlock();
    }

    public boolean drop(){
        while (moveBy(0, -1)){
        }
        return false;
    }
}
