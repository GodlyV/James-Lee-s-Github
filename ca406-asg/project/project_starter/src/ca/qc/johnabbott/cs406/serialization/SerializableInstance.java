package ca.qc.johnabbott.cs406.serialization;

import ca.qc.johnabbott.cs406.serialization.util.*;

/**
 * Utility class use to create instances of serializables.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @author TODO
 */
public class SerializableInstance {

    public static final byte SERIAL_NULL_ID = 0x00;

    /* Utility class: hide constructor to make it uninstantable */
    private SerializableInstance() {
    }

    /**
     * Create an instance of the object given by the provided serial ID.
     * @param id the serial ID.
     * @return the object.
     * @throws SerializationException
     */
    public static Serializable byId(byte id) throws SerializationException {

        // TODO: add a case to this switch for each new serializable.

        switch (id) {
            case SERIAL_NULL_ID:
                return null;
            case SInteger.SERIAL_ID:
                return new SInteger();
            case SString.SERIAL_ID:
                return new SString();
            case Box.SERIAL_ID:
                return new Box();
            case SDate.SERIAL_ID:
                return new SDate();
            case Grade.SERIAL_ID:
                return new Grade();
                default:
                throw new SerializationException("Unknown serial ID.");
        }
    }

}
