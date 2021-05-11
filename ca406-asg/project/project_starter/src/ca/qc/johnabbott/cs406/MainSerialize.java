package ca.qc.johnabbott.cs406;

import ca.qc.johnabbott.cs406.serialization.io.BufferedChannel;
import ca.qc.johnabbott.cs406.serialization.Serializer;
import ca.qc.johnabbott.cs406.serialization.util.Box;
import ca.qc.johnabbott.cs406.serialization.util.SInteger;
import ca.qc.johnabbott.cs406.serialization.util.SString;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Serialization example.
 */
public class MainSerialize {

    public static void main(String arg[]) throws IOException {

        BufferedChannel channel = new BufferedChannel(new RandomAccessFile("foo.bin", "rw").getChannel(), BufferedChannel.Mode.WRITE);

        Serializer serializer = new Serializer(null, channel);

        SInteger i = new SInteger(123);
        SString s = new SString("hello,");
        Box<SString> bs = new Box<>(new SString("world."));

        serializer.serialize(i);
        serializer.serializeNull();
        serializer.serialize(s);
        serializer.serializeNull();
        serializer.serialize(bs);
        serializer.serializeNull();

        channel.close();

    }
}
