package ca.qc.johnabbott.cs406.serialization.util;

import ca.qc.johnabbott.cs406.serialization.Serializable;
import ca.qc.johnabbott.cs406.serialization.SerializationException;
import ca.qc.johnabbott.cs406.serialization.Serializer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;


public class SDate<T extends Serializable> implements Serializable {
    public static final byte SERIAL_ID = 0x04;
    private static final ByteBuffer buffer;

    private long value;

    public static void main(String[] args) throws FileNotFoundException {
        RandomAccessFile file = new RandomAccessFile("foo.bin", "rw");
    }
    static{
        byte [] bytes = new byte[Long.BYTES];
        buffer= ByteBuffer.wrap(bytes);
    }
    public SDate(){
        value=0;
    }
    public SDate(long value){
        this.value=value;
    }
    @Override
    public byte getSerialId() {
        return SERIAL_ID;
    }

    @Override
    public void writeTo(Serializer s) throws IOException {
        // clear the buffer in case of previous use.
        buffer.clear();

        //place the date
        buffer.putLong(value);
        s.write(buffer.array());
    }

    @Override
    public void readFrom(Serializer s) throws IOException, SerializationException {
        // clear the buffer in case of previous use.
        buffer.clear();

        // extract the backing byte[] and read into it.
        byte[] bytes = buffer.array();
        s.read(bytes);

        // read the integer value from the ByteBuffer,
        // since the backing byte[] now has the data.
        value = buffer.getLong();
    }

    @Override
    public boolean immutable() {
        return true;
    }
}
