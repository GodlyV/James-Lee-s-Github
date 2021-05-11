package com.company;

import java.util.Collection;
import java.util.Set;

public interface Sequences<T, S extends Collection<T>> {

    /** * Generate all the sequences. * @return the set of all sequences. */
    Set<S> generateAll();
    /** * Generate the first 'n' sequences. * @param limit the number of sequences to generate. * @return the set of sequences. */
    Set<S> generateSome(int limit);
}

