package org.acme.getting.started;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Uni;
import org.acme.getting.started.dto.PersonDto;
import org.acme.getting.started.service.PersonService;
import org.acme.getting.started.models.Person;
import org.acme.getting.started.service.PersonServiceMongoReactive;

import java.util.List;

@Path("/api/person")
public class PersonResource {

    @Inject
    PersonService personService;

    @Inject
    PersonServiceMongoReactive personServiceMongoReactive;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Uni<List<Person>> all() {
        return personService.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public Uni<List<Person>> personsByName(@PathParam("name") String name) {
        return personService.getByName(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Uni<Person> getPersonById(@PathParam("id") int id) {
        return personService.getOne(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Uni<List<Person>> addPerson(Person person) {
        return personService.add(person);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update/{id}")
    public Uni<List<Person>> updatePerson(@PathParam("id") int id, PersonDto personDto) {
        return personService.update(id, personDto);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/{id}")
    public Uni<List<Person>> deletePerson(@PathParam("id") int id) {
        return personService.delete(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/personMongo")
    public Uni<List<PersonDto>> addPersonToMongo(PersonDto person) {

        return personServiceMongoReactive.add(person);
    }

}
