package com.company;

public class Main {

    public static void main(String[] args) {
	    String word="hi";
	    Trie<Character> yikes = new Trie<>();
	    yikes.add(word);
	    yikes.contains("hi");
    }
}
