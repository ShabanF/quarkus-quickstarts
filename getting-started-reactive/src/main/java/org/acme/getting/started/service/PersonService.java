package org.acme.getting.started.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Uni;
import org.acme.getting.started.dto.PersonDto;
import org.acme.getting.started.models.Country;
import org.acme.getting.started.models.Person;

@ApplicationScoped
public class PersonService {

    private static final List<Person> PERSONS = new ArrayList<>();

    static {
        PERSONS.add(
                Person.builder().id(1).name("Person 1").visitedCountries(
                        Arrays.asList(Country.builder().name("Country 1").build(),
                                Country.builder().name("Country 2").build(),
                                Country.builder().name("Country 3").build())
                ).build());

        PERSONS.add(Person.builder().id(2).name("Person 2").visitedCountries(Arrays.asList(
                Country.builder().name("Country 21").build(),
                Country.builder().name("Country 22").build(),
                Country.builder().name("Country 23").build())).build());


        PERSONS.add(Person.builder().id(3).name("Person 3").visitedCountries(Arrays.asList(
                Country.builder().name("Country 31").build(),
                Country.builder().name("Country 32").build(),
                Country.builder().name("Country 33").build()
        )).build());

        PERSONS.add(Person.builder().id(4).name("Person 4").visitedCountries(Arrays.asList(
                Country.builder().name("Country 41").build(),
                Country.builder().name("Country 42").build(),
                Country.builder().name("Country 43").build()
        )).build());

    }

    public Uni<List<Person>> getByName(String name) {

        Uni<List<Person>> filteredPersons = Uni.createFrom().emitter(emitter -> {
            List<Person> filteredItems = PERSONS.stream().filter(person -> person.getName().equals(name)).collect(Collectors.toList());
            emitter.complete(filteredItems);
        });

        return filteredPersons;
    }

    public Uni<Person> getOne(int id) {

        Uni<Person> personById = Uni.createFrom().emitter(emitter -> {
            Optional<Person> filteredItems = PERSONS.stream().filter(person -> person.getId() == id).findFirst();
            emitter.complete(filteredItems.get());
        });

        return personById;
    }

    public Uni<List<Person>> add(Person person) {

        Uni<List<Person>> allPersons = Uni.createFrom().item(PERSONS).onItem().transform(people -> {
                    people.add(person);
                    return people;
                })
                .onFailure().invoke(t -> System.out.println("Unable to create new person: " + t.getMessage()));

        return allPersons;
    }

    public Uni<List<Person>> update(int id, PersonDto person) {

        Uni<Person> personUni = Uni.createFrom().emitter(emitter -> {

            Optional<Person> firstPerson = PERSONS.stream().filter(p -> p.getId() == id).findFirst();
            firstPerson.get().setName(person.getName());

            emitter.complete(firstPerson.get());

        });

        Uni<List<Person>> allPersons = personUni.onItem().transform(person1 -> {
            int index = PERSONS.indexOf(person1);
            PERSONS.set(index, person1);

            return PERSONS;
        });

        return allPersons;
    }

    public Uni<List<Person>> delete(int id) {


        return null;
    }
}
