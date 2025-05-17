package org.cgmtest.service;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.cgmtest.domain.entity.Patient;
import org.cgmtest.domain.model.PatientDTO;
import org.cgmtest.repository.PatientRepository;

import java.util.List;

@ApplicationScoped
public class PatientService {

    @Inject
    PatientRepository repository;

    //per un codice più completo si utilizza un mapperToDTO o mapperToEntity ma qui data la semplicità
    //ho effettuato direttemente la trasformazione

    @WithTransaction
    public Uni<Patient> create(PatientDTO dto) {
        Patient p = new Patient();
        p.firstName = dto.firstName;
        p.lastName = dto.lastName;
        p.email = dto.email;
        p.fiscalCode = dto.fiscalCode;
        p.phoneNumber = dto.phoneNumber;
        return repository.persistAndFlush(p);
    }

    @WithTransaction
    public Uni<Patient> update(Long id, PatientDTO dto) {
        return repository.findById(id)
                .onItem().ifNotNull().invoke(p -> {
                    p.firstName = dto.firstName;
                    p.lastName = dto.lastName;
                    p.email = dto.email;
                    p.fiscalCode = dto.fiscalCode;
                    p.phoneNumber = dto.phoneNumber;
                });
    }

    @WithTransaction
    public Uni<Boolean> delete(Long id) {
        return repository.deleteById(id);
    }

    @WithSession
    public Uni<Patient> findById(Long id) {
        return repository.findById(id);
    }

    @WithSession
    public Uni<List<Patient>> search(String firstName, String lastName, int page, int size) {
        return repository.search(firstName, lastName, page, size);
    }
}
