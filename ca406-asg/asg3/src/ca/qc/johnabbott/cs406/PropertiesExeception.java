package ca.qc.johnabbott.cs406;

public class PropertiesExeception extends Exception {

    public PropertiesExeception() {
        super();
    }

    public PropertiesExeception(String message) {
        super(message);
    }

    public PropertiesExeception(Exception e) {
        super(e);
    }
}
