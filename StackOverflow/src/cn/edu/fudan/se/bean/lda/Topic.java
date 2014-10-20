package cn.edu.fudan.se.bean.lda;

import java.util.ArrayList;
import java.util.List;


public class Topic {
    List<String> relatedDocs;
    List<String> topWords;
    int index;

    public Topic() {
        relatedDocs = new ArrayList<String>();
        topWords = new ArrayList<String>();
    }

    public List<String> getRelatedDocs() {
        return relatedDocs;
    }

    public void setRelatedDocs(List<String> relatedDocs) {
        this.relatedDocs = relatedDocs;
    }

    public List<String> getTopWords() {
        return topWords;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTopWords(List<String> topWords) {
        this.topWords = topWords;
    }

    public void addTopWord(String word) {
        topWords.add(word);
    }
}
