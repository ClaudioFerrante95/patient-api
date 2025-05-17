package org.cgmtest.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.cgmtest.domain.entity.Patient;

import java.util.List;

@ApplicationScoped
public class PatientRepository implements PanacheRepository<Patient> {

    public Uni<List<Patient>> search(String firstName, String lastName, int page, int size) {
        return find("firstName LIKE ?1 AND lastName LIKE ?2",
                "%" + firstName + "%", "%" + lastName + "%")
                .page(Page.of(page, size))
                .list();
    }
}
