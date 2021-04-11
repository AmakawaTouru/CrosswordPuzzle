public class Word {

    public String word;
    public Boolean isVisited;
    public int livecycle;

    /**
     * 生命周期默认为1
     * isVisited代表是否已经访问过了
     * @param word 必须要有参构造，传入一个单词
     */
    public Word(String word) {
        this.word = word;
        this.livecycle = 1;
        this.isVisited = false;
    }

    public int length(){
        return word.length();
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", isVisited=" + isVisited +
                ", livecycle=" + livecycle +
                '}';
    }
}
