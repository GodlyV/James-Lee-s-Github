package ca.qc.johnabbott.cs406.collections;

import ca.qc.johnabbott.cs406.serialization.Serializable;
import ca.qc.johnabbott.cs406.serialization.SerializationException;
import ca.qc.johnabbott.cs406.serialization.Serializer;

import java.io.IOException;

/**
 * Represents one of two possible options.
 *
 * Create an Either object through its static methods. Ex:
 *
 * Either<String, Integer> e1 = Either.left("abc");
 * Either<String, Integer> e2 = Either.right(1234);
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @author TODO
 */
public interface Either<S, T> extends Serializable {

    enum Type { LEFT, RIGHT }

    /**
     * Determine if the object is a left or right Either.
     * @return
     */
    Type getType();

    /**
     * Get the left value of the either.
     * @return
     */
    S getLeft();

    /**
     * Get the right value of the either.
     * @return
     */
    T getRight();


    /**
     * Generate a left Either object.
     * @param val
     * @param <S>
     * @param <T>
     * @return
     */
    static <S,T> Either<S,T> left(S val) {
        return new LeftEither<>(val);
    }

    static <S,T> Either<S,T> right(T val) {
        return new RightEither<>(val);
    }

    /**
     * Class to store left Either values.
     * TODO: should be private but Java won't let me. Update to anonymous inner class next semester ;)
     * @param <S>
     * @param <T>
     */
    class LeftEither<S,T> implements Either<S,T> {

        private S val;

        public LeftEither(S val) {
            this.val = val;
        }

        public LeftEither() { }

        @Override
        public Type getType() {
            return Type.LEFT;
        }

        @Override
        public S getLeft() {
            return val;
        }

        @Override
        public T getRight() {
            throw new RuntimeException();
        }

        @Override
        public byte getSerialId() {
            return 0;
        }

        @Override
        public void writeTo(Serializer s) throws IOException {

        }

        @Override
        public void readFrom(Serializer s) throws IOException, SerializationException {

        }

        @Override
        public boolean immutable() {
            return false;
        }
    }

    /**
     * Class to store right Either values.
     * TODO: should be private but Java won't let me. Update to anonymous inner class next semester ;)
     * @param <S>
     * @param <T>
     */
    class RightEither<S,T> implements Either<S,T> {

        private T val;

        public RightEither(T val) {
            this.val = val;
        }

        public RightEither() {
        }

        @Override
        public Type getType() {
            return Type.RIGHT;
        }

        @Override
        public S getLeft() {
            throw new RuntimeException();
        }

        @Override
        public T getRight() {
            return val;
        }

        @Override
        public byte getSerialId() {
            return 0;
        }

        @Override
        public void writeTo(Serializer s) throws IOException {

        }

        @Override
        public void readFrom(Serializer s) throws IOException, SerializationException {

        }

        @Override
        public boolean immutable() {
            return false;
        }
    }
}
