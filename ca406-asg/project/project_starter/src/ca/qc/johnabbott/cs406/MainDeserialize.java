package ca.qc.johnabbott.cs406;

import ca.qc.johnabbott.cs406.serialization.io.BufferedChannel;
import ca.qc.johnabbott.cs406.serialization.SerializationException;
import ca.qc.johnabbott.cs406.serialization.Serializer;
import ca.qc.johnabbott.cs406.serialization.util.Box;
import ca.qc.johnabbott.cs406.serialization.util.SInteger;
import ca.qc.johnabbott.cs406.serialization.util.SString;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Deserialize example.
 */
public class MainDeserialize {
    public static void main(String arg[]) throws IOException, SerializationException {

        Serializer serializer = new Serializer(
                new BufferedChannel(
                        new RandomAccessFile("foo.bin", "rw").getChannel()
                        , BufferedChannel.Mode.READ
                ), null);

        SInteger i = (SInteger) serializer.deserialize();
        System.out.println(serializer.deserialize());
        SString s = (SString) serializer.deserialize();
        System.out.println(serializer.deserialize());
        Box<SString> bs = (Box<SString>) serializer.deserialize();
        System.out.println(serializer.deserialize());

        System.out.println(i);
        System.out.println(s);
        System.out.println(bs);

        serializer.close();

    }
}
