package com.company;

import java.util.List;

/**
 * Represents a lexicon.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
  */
public interface Lexicon {

    /**
     * Add a word to the lexicon.
     * @param word the word to add to the lexicon.
     */
    void add(String word);


    /**
     * Test if a word is in the lexicon.
     * @param word the word to check.
     * @return true if the word is in the lexicon, false otherwise.
     */
    boolean contains(String word);

    /**
     * Generate a list of lexicon words that have the prefix specified. An "autocomplete".
     * @param prefix the word prefix to complete.
     * @param limit the maximum number of lexicon words to find.
     * @return a list of lexicon words.
     */
    List<String> complete(String prefix, int limit);

}
