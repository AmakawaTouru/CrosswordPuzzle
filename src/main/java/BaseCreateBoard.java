import java.util.*;

public class BaseCreateBoard {


    /**
     * 在拼图上添加某一单词。
     * @param record
     * @param curWord
     * @param rowStartIndex
     * @param colStartIndex
     * @param isRow
     */
    private void setBoard(CRecord record,Word curWord,int rowStartIndex,int colStartIndex,Boolean isRow){
        int L = curWord.length();
        char[] curWordChs = curWord.word.toCharArray();
        for(int i = 0; i<L; i++) {
            int ci = rowStartIndex;
            int cj = colStartIndex;
            if (isRow) {
                cj = cj + i;
                record.dirFlag[ci][cj] |= 0x01;
            } else {
                ci = ci + i;
                record.dirFlag[ci][cj] |= 0x02;
            }
            record.board[ci][cj] = curWordChs[i];
            //获取该字母的index列表，并把当前行索引，列索引进行记录
            int alpha = curWordChs[i] - 'a';
            ArrayList<Index> indexList = record.indexTable.get(alpha);
            indexList.add(new Index(ci, cj));
        }
        //在item下添加记录
        record.items.add(new Item(curWord.word, rowStartIndex, colStartIndex, isRow));
    }


    /**
     * 判断此位置是否合法，是否可以放下整个单词
     * @param record 拼图记录
     * @param curWord 当前单词
     * @param rowStartIndex 单词摆放的起始行索引
     * @param colStartIndex 单词摆放的起始列索引
     * @param isRow 是否是横向摆放
     * @return
     */
    private Boolean vaildCheck(CRecord record,Word curWord,int rowStartIndex,int colStartIndex,Boolean isRow){

        int L = curWord.length();
        int board_size = record.BOARD_SIZE;

        if(rowStartIndex < 0 || colStartIndex < 0){
            return false;
        }
        if(isRow && colStartIndex + L -1 >= board_size){
            return false;
        }
        if(!isRow && rowStartIndex + L -1 >= board_size){
            return false;
        }
        if(isRow && colStartIndex-1 >= 0 && record.board[rowStartIndex][colStartIndex-1] != '-'){
            return false;
        }
        if(!isRow && rowStartIndex-1 >= 0 && record.board[rowStartIndex-1][colStartIndex] != '-'){
            return false;
        }
        if(isRow && colStartIndex+L < board_size && record.board[rowStartIndex][colStartIndex+L] != '-'){
            return false;
        }
        if(!isRow && rowStartIndex+L < board_size && record.board[rowStartIndex+L][colStartIndex] != '-'){
            return false;
        }
        char[] curWordChs = curWord.word.toCharArray();
        for(int i = 0; i<L; i++){
            int ci = rowStartIndex;
            int cj = colStartIndex;
            if(isRow){
                cj = cj + i;
            }else{
                ci = ci + i;
            }
            if (record.board[ci][cj] != '-' && record.board[ci][cj] != curWordChs[i]){
                return false;
            }
            if(record.board[ci][cj] == '-'){
                if(isRow && ci-1>=0 && record.board[ci-1][cj] != '-' && (record.dirFlag[ci-1][cj] & 0x02) != 0){
                    return false;
                }
                if(isRow && ci+1<board_size && record.board[ci+1][cj] != '-' && (record.dirFlag[ci+1][cj] & 0x02) != 0){
                    return false;
                }
                if(!isRow && cj-1 >=0 && record.board[ci][cj-1]!='-' && (record.dirFlag[ci][cj-1] & 0x01) != 0){
                    return false;
                }
                if(!isRow && cj+1 < board_size && record.board[ci][cj+1]!='-' && (record.board[ci][cj+1]&0x01)!=0 ){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 计算当前数据书否可以填入该单词
     * @param record
     * @param curWord
     * @param rowStartIndex
     * @param colStartIndex
     * @param isRow
     * @return
     */
    private Boolean calculate(CRecord record,Word curWord,int rowStartIndex,int colStartIndex,Boolean isRow){
        if(vaildCheck(record,curWord,rowStartIndex,colStartIndex,isRow)){
            setBoard(record,curWord,rowStartIndex,colStartIndex,isRow);
            return true;
        }
        return false;
    }


    /**
     * 随机生成一个布尔数组，里面的true和flase的元素排列是随机的
     * @return 长度为2的布尔数组
     */
    private Boolean[] randomSort(){
        List<Boolean> l = new ArrayList<>();
        l.add(true);
        l.add(false);
        Collections.shuffle(l);
        return l.toArray(new Boolean[l.size()]);
    }

    /**
     * 重载，随机打乱一个indexTable的位置，以便乱序
     * @param indexTable
     * @return
     */
    private Index[] randomSort(ArrayList<Index> indexTable){
        Collections.shuffle(indexTable);
        return indexTable.toArray(new Index[indexTable.size()]);
    }


    /**
     * 迭代计算该单词应该放的位置
     * 如果是第一个单词，就放在拼图的正中间
     * 如果不是，就逐个查找字母，查找可以交叉的地方，进行插入
     * 其中，单词是横向排列的还是竖向排列的，是随机产生的。
     * @param record 拼图记录
     */
    private void iter(CRecord record){
        Queue<Word> wordpool = record.wordpool;
        int board_size = record.BOARD_SIZE;

        Word curWord = wordpool.poll();
        Boolean[] randomSort = randomSort();
        if(wordpool.size() == record.basePoolSize-1){
            for(int i = 0; i<2 ;i++){
                Boolean isRow = randomSort[i];
                int rowStartIndex;
                int colStartIndex;
                if(isRow){
                    //横向排列
                    rowStartIndex = board_size/2;
                    colStartIndex = (board_size - curWord.length())/2;
                }else{
                    //纵向排列
                    rowStartIndex = (board_size - curWord.length())/2;
                    colStartIndex = board_size/2;
                }
                //如果为真，说明已经找到位置，并插入，跳出循环
                if(calculate(record,curWord,rowStartIndex,colStartIndex,isRow)){
                    break;
                }
            }
        }else{
            int L = curWord.length();
            char[] curWordChs = curWord.word.toCharArray();
            Boolean breakFlag = false;
            for(int i = 0; i<L;i++){
                ArrayList<Index>  indexTables = record.indexTable.get(curWordChs[i] - 'a');
                Index[]  indexs= randomSort(indexTables);
                for(int j = 0;j<indexs.length;j++){
                    for(int k = 0;k<2;k++){
                        Boolean isRow = randomSort[k];
                        int rowStartIndex;
                        int colStartIndex;
                        if(isRow){
                            //横向排列
                            rowStartIndex = indexs[j].i;
                            colStartIndex = indexs[j].j-i;
                        }else{
                            //纵向排列
                            rowStartIndex = indexs[j].i-i;
                            colStartIndex = indexs[j].j;
                        }
                        //如果为真，说明已经找到位置，并插入，跳出循环
                        if(calculate(record,curWord,rowStartIndex,colStartIndex,isRow)){
                            breakFlag = true;
                            break;
                        }
                    }
                    if(breakFlag){
                        break;
                    }
                }
                if (breakFlag){
                    break;
                }
            }
            //如果在此处，标志仍然是错的，那么说明没有找到合适的位置放入此单词，减少生命周期后再放入队列
            //相当于重新给一次机会
            if(!breakFlag && curWord.livecycle == 1){
                curWord.livecycle = 0;
                wordpool.offer(curWord);
            }
        }
    }


    /**
     * 未解决的问题：
     * 问题1：100个单词有可能有部分单词没有塞下去拼图中
     * 问题2：如果单词小量，拼出来的图案有可能不一定好看
     * 问题3：空出空格之后，单词不一定是唯一解，因此要给出提示（单词提示可以是中文或者英文）
     * 问题4：所有单词必须转换成小写
     *
     * @param args
     */
    public static void main(String[] args) {
        BaseCreateBoard baseStart = new BaseCreateBoard();
        //暂时取这些单词来进行复现
//        String wordstring = "buckles,killed,rubato,collect,woodier,potluck,daubers,bloom,learned,honchos,euro,wisp,hexes,earl,mass,flanged,wads,hoods,cravens,tocsins,frown,satori,gasping,tritium,finesse,macho,sharked,torsion,serest,easels,layers,ganging,amuses,rusty,encored,ditties,wombs,unzips,subset,viewed,galls,gambles,gnostic,trots,avidly,loosely,sprint,gobs,dropsy,timothy,gowning,raged,cankers,fetus,thyself,portico,muckier,daring,only,chewy,colors,person,twinkly,chumps,filming,insofar,spore,remind,despot,soother,whiles,vastly,sprouts,bicycle,users,galling,clear,keener,baling,giro,bestows,termite,decking,tankers,gerunds,boxes,news,peso,outlay,theory,repulse,bocks,junks,dopers,icicle,accrue,fatter,flower,shank,keepers";
        String wordstring = "telegram,blue,doctor,picture,red";
        String[] WORDPOOL = wordstring.split(",");
        int BOARD_SIZE = 10;
        CRecord record = new CRecord(BOARD_SIZE,WORDPOOL);
        while(!record.wordpool.isEmpty()){
            baseStart.iter(record);
//            //输出整个拼图表
//            for(int i = 0; i< BOARD_SIZE;i++){
//                for(int j = 0;j<BOARD_SIZE;j++){
//                    System.out.print(record.board[i][j]);
//                }
//                System.out.println();
//            }
//            System.out.println();
        }
//        输出整个拼图表
        for(int i = 0; i< BOARD_SIZE;i++){
            for(int j = 0;j<BOARD_SIZE;j++){
                System.out.print(record.board[i][j]);
            }
            System.out.println();
        }
        for(int i = 0; i< BOARD_SIZE;i++){
            for(int j = 0;j<BOARD_SIZE;j++){
                if(record.dirFlag[i][j] == 0){
                    System.out.print(".");
                }else if(record.dirFlag[i][j] == 1){
                    System.out.print("-");
                }else if(record.dirFlag[i][j] == 2){
                    System.out.print("|");
                }else{
                    System.out.print("+");
                }
            }
            System.out.println();
        }
        System.out.println("拼图表中一共有多少个单词："+record.items.size());
        System.out.println("单词表中一共有多少个单词:"+WORDPOOL.length);
    }


}
