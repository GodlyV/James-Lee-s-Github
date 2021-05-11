package ca.qc.johnabbott.cs406.serialization.util;

import ca.qc.johnabbott.cs406.serialization.Serializable;
import ca.qc.johnabbott.cs406.serialization.SerializationException;
import ca.qc.johnabbott.cs406.serialization.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a grade.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2018-04-29
 */
public class Grade implements Serializable {
    public static final byte SERIAL_ID = 0x05;
    // optimization, use a static ByteBuffer to avoid extra allocations on each (de)serialize operation.
    private static final ByteBuffer buffer;

    // ininitalize a static field in a static initialization block.
    static {
        byte[] bytes = new byte[Integer.BYTES];
        buffer = ByteBuffer.wrap(bytes);
    }

    private Grade value;

    private String name;
    private int result;
    private Date date;

    public Grade(String name, int result, Date date) {
        this.name = name;
        this.result = result;
        this.date = date;
    }

    public Grade(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "name='" + name + '\'' +
                ", result=" + result +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return result == grade.result &&
                Objects.equals(name, grade.name) &&
                Objects.equals(date, grade.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, result, date);
    }

    @Override
    public byte getSerialId() {
        return SERIAL_ID;
    }

    @Override
    public void writeTo(Serializer s) throws IOException {
        // clear the buffer in case of previous use.
        buffer.clear();
        byte[] bytes = new byte[(name.length() * Character.BYTES + Integer.BYTES)];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.putInt(name.length());
        for(int i=0; i<name.length();i++) {
            buffer.putChar(name.charAt(i));
        }
        long newDate = date.getTime();
        buffer.putInt(result);
        buffer.putLong(newDate);
        buffer.flip();
        s.write(buffer.array());

    }

    @Override
    public void readFrom(Serializer s) throws IOException, SerializationException {
        byte [] bytes = new byte[Integer.BYTES];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        s.read(buffer);
        buffer.flip();

        int length = buffer.getInt();

        bytes = new byte[length * Character.BYTES];
        buffer = ByteBuffer.wrap(bytes);
        s.read(buffer);
        buffer.flip();

        char[] cs = new char[length];
        for(int i=0; i<length;i++){
            cs[i]= buffer.getChar();
        }
        String string = new String(cs);
        System.out.println(string);
    }

    @Override
    public boolean immutable() {
        return false;
    }
}
