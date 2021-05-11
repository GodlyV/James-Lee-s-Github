package ca.qc.johnabbott.cs616.healthhaven.sqlite;

/**
 * Indicated model classes that have an ID field.
 *
 * @param <I>
 * @param <R>
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public interface Identifiable<I,R> {
    I getId();
    R setId(I id);
}
