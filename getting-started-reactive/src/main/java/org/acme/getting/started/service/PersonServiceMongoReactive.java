package org.acme.getting.started.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import org.acme.getting.started.dto.PersonDto;
import org.bson.Document;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PersonServiceMongoReactive {

    @Inject
    ReactiveMongoClient mongoClient;

    public Uni<List<PersonDto>> add(PersonDto person) {

        Document personDocument = new Document()
                .append("name", person.getName());

        List<Document> countries = person.getVisitedCountries().stream().map(country -> {
            Document countryDocument = new Document()
                    .append("name", country.getName());

            return countryDocument;
        }).collect(Collectors.toList());

        personDocument.append("visitedCountries", countries);

        Uni<List<PersonDto>> persons = getCollection().insertOne(personDocument)
                .onItem().ignore().andSwitchTo(this::list);

        return persons;
    }

    public Uni<List<PersonDto>> list() {

        Uni<List<PersonDto>> persons = getCollection().find().map(p -> {
            String toJson = p.toJson();

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                return objectMapper.readValue(toJson, new TypeReference<PersonDto>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect().asList();

        return persons;
    }

    private ReactiveMongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("agency").getCollection("person");
    }

}
