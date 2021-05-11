package ca.qc.johnabbott.cs616.server.model;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

/**
 * User class
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
@Entity
@Table(name = "medicalinfo")
public class MedicalInfo {

    public enum Type {
        ALLERGY,
        MEDICAL_CONDITION
    }

    @Id
    @GeneratedValue(generator = UuidGenerator.generatorName)
    @GenericGenerator(name = UuidGenerator.generatorName, strategy = "ca.qc.johnabbott.cs616.server.model.UuidGenerator")
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "type")
    private Type type;

    @Column(name = "name")
    private String name;

    @Column(name = "info")
    private String info;

    public String getUuid() {
        return uuid;
    }

    public MedicalInfo setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}