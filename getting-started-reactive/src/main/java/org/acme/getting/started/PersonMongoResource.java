package org.acme.getting.started;

import io.smallrye.mutiny.Uni;
import org.acme.getting.started.dto.PersonDto;
import org.acme.getting.started.service.PersonServiceMongoReactive;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/mongo")
public class PersonMongoResource {

    @Inject
    PersonServiceMongoReactive personServiceMongoReactive;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addPerson")
    public Uni<List<PersonDto>> addPersonToMongo(PersonDto person) {
        return personServiceMongoReactive.add(person);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAll")
    public Uni<List<PersonDto>> getAll() {
       return personServiceMongoReactive.list();
    }

}
