import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;


public class CRecord {

    public int BOARD_SIZE;
    public Queue<Word> wordpool;
    public int basePoolSize;
    public ArrayList<ArrayList<Index>> indexTable;
    public char[][] board;
    public int[][] dirFlag;
    public ArrayList<Item> items;


    public CRecord() {

        BOARD_SIZE = 0;
        basePoolSize = 0;

        wordpool = new ArrayDeque<>();
        indexTable = new ArrayList<>(26);
        items = new ArrayList<>();
    }

    /**
     * 有参构造
     * 初始化
     *
     * wordpool 单词队列，当单词队列为空时，说明已经放满格子
     * indexTable 必须初始化为26大小
     * board 代表图片迷宫,初始化所有位置都是‘-’
     * dirFlag 代表位置标志
     *         初始化所有位置都是0 代表标志.
     *         当为1时代表标志 -
     *         当为2时代表标志 |
     *         当为3时代表标志 +
     * items 所有单词在图中的信息
     *
     * @param BOARD_SIZE 表格长宽大小
     * @param WORDPOOL 单词词汇表
     */
    public CRecord(int BOARD_SIZE,String[] WORDPOOL) {
        //调用无参构造
        this();
        this.BOARD_SIZE = BOARD_SIZE;
        for(int i = 0;i<WORDPOOL.length;i++){
            wordpool.add(new Word(WORDPOOL[i]));
        }
        basePoolSize = WORDPOOL.length;
        for(int i = 0;i<26;i++){
            indexTable.add(new ArrayList<Index>());
        }
        this.board = new char[BOARD_SIZE][BOARD_SIZE];
        for(int i = 0;i<BOARD_SIZE;i++){
            Arrays.fill(board[i],'-');
        }
        this.dirFlag = new int[BOARD_SIZE][BOARD_SIZE];
        for(int i = 0;i<BOARD_SIZE;i++){
            Arrays.fill(dirFlag[i], 0);
        }
    }

}
