package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trie <T extends Comparable<T>> implements Lexicon {
    private static class TrieNode<T> {
        public char element;
        public TrieNode<T> child;
        public HashMap<Character,TrieNode<T>> children = new HashMap<>();
        public boolean isWord;

        public TrieNode(char currentLetter) {
            element=currentLetter;
        }

        public TrieNode() {

        }
    }
    private TrieNode<Character> root;
    private int size;
    @Override
    public void add(String word) {
        char[] letters = new char[word.length()];
        HashMap<Character, HashMap<Character, TrieNode>> main = null;
        //root.isWord = false;
        for (int i =0;i<word.length();i++){
            char currentLetter=word.charAt(i);
            letters[i] = currentLetter;
        }
        add(word,main,0);
    }
    public void add(String word, HashMap<Character, HashMap<Character, TrieNode>> map, int pos){

        if(pos == word.length()){
            root.isWord=true;
            return;
        }
        if(root == null){
            return;
        }
        root.children.putIfAbsent(word.charAt(pos), new TrieNode(word.charAt(pos)));
        size++;

        add(word,map,pos+1);
    }

    @Override
    public boolean contains(String word) {
        char[] letters = new char[word.length()];
        HashMap<Character, HashMap<Character, TrieNode>> main = null;
        for (int i =0;i<word.length();i++){
            char currentLetter=word.charAt(i);
            letters[i] = currentLetter;
        }

        contains(word,letters,0,root.children.get(word.charAt(0)));
        return false;

        //return contains(root,);
    }
    public boolean contains(String word,char [] letters,int pos,TrieNode curr){
        if (pos < word.length()){
            if(root == null){
                return false;
            }

            else{
                contains(word,letters,pos+1,root.children.get(letters[pos+1]));
            }
            if(curr.element == letters[pos]){
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> complete(String prefix, int limit) {
        char[] letters = new char[prefix.length()];
        ArrayList completed = new ArrayList(limit);
        for (int i =0;i<prefix.length();i++){
            char currentLetter=prefix.charAt(i);
            letters[i] = currentLetter;
        }
        return complete(prefix,limit,0,root.children.get(prefix.charAt(0)),completed,prefix,0);
    }
    public List<String> complete(String prefix, int limit,int pos,TrieNode curr,List completed,String word,int i){
        if(root == null){
            return null;
        }
        if(pos <prefix.length()){
            complete(prefix,limit,pos+1,root.children.get(prefix.charAt(pos+1)),completed,word,i);
        }
        for(Object c : curr.children.keySet()){
            //if()
        }
        for(int j =0;j<pos;j++){
            if(curr!=null){
                word +=curr.element;
            }
            complete(prefix,limit,pos,root.children.get(prefix.charAt(pos+1)),completed,word,i);
            completed.add(word);
        }
        return completed;
    }
}
