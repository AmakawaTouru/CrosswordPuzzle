public class Item {

    //单词
    public String word;
    //单词的起始行位置
    public int startI;
    //单词的起始列位置
    public int startJ;
    //单词是否是行
    public Boolean isRow;
    //单词的解释
    public String hint;

    public Item() {
    }

    public Item(String word, int startI, int startJ, Boolean isRow) {
        this.word = word;
        this.startI = startI;
        this.startJ = startJ;
        this.isRow = isRow;
    }
}
