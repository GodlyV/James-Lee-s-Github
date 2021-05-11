package ca.qc.johnabbott.cs616.server.model;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

/**
 * User class
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
@Entity
@Table(name = "userinfo")
public class UserInfo {

    @Id
    @GeneratedValue(generator = UuidGenerator.generatorName)
    @GenericGenerator(name = UuidGenerator.generatorName, strategy = "ca.qc.johnabbott.cs616.server.model.UuidGenerator")
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "userid")
    private String user;

    @Column(name = "medicalinfoid")
    private String medinfo;

    public String getUuid() {
        return uuid;
    }

    public UserInfo setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMedicalInfo() {
        return medinfo;
    }

    public void setMedicalInfo(String medInfo) {
        this.medinfo = medInfo;
    }
}