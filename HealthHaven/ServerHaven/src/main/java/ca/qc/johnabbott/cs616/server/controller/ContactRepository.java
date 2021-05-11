package ca.qc.johnabbott.cs616.server.controller;

import ca.qc.johnabbott.cs616.server.model.Contact;
import ca.qc.johnabbott.cs616.server.model.Log;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "contact")
public interface ContactRepository extends CrudRepository<Contact, String> {
}
