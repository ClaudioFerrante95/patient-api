package org.cgmtest.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cgmtest.domain.entity.Patient;
import org.cgmtest.domain.model.PatientDTO;
import org.cgmtest.service.PatientService;

import java.util.List;

//i controller con l'annotazione PATH non necessiotano di ApplicationScoped in quanto sono gestiti dal framework
//Quarkus tratta i controller REST come "per-request":
//    1) Una nuova istanza viene creata per ogni richiesta HTTP.
//    2) L'istanza viene distrutta dopo la risposta.
@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientResource {

    //equivalente di Autowired in Spring
    @Inject
    PatientService service;

    //Quarkus usa Mutiny, una libreria reattiva simile a Project Reactor (usato da Spring WebFlux), ma con un'API più espressiva e user-friendly.
    //Uni Rappresenta un singolo valore asincrono o nessun valore.
    //Multi Rappresenta uno stream asincrono di valori (0, 1 o molti). È usato per risultati multipli, ad esempio una lista di pazienti o notifiche in tempo reale.
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> create(@Valid PatientDTO dto) {
        return service.create(dto)
                .onItem().transform(p -> Response.status(Response.Status.CREATED).entity(p).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> update(@PathParam("id") Long id, @Valid PatientDTO dto) {
        return service.update(id, dto)
                .onItem().ifNull().failWith(() -> new WebApplicationException("Patient not found", 404))
                .onItem().transform(p -> Response.ok(p).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return service.delete(id)
                .onItem().transform(deleted -> deleted ? Response.noContent().build() : Response.status(404).build());
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") Long id) {
        return service.findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException("Not found", 404))
                .onItem().transform(p -> Response.ok(p).build());
    }

    @GET
    public Uni<List<Patient>> search(@QueryParam("firstName") @DefaultValue("") String firstName,
                                     @QueryParam("lastName") @DefaultValue("") String lastName,
                                     @QueryParam("page") @DefaultValue("0") int page,
                                     @QueryParam("size") @DefaultValue("10") int size) {
        return service.search(firstName, lastName, page, size);
    }
}

