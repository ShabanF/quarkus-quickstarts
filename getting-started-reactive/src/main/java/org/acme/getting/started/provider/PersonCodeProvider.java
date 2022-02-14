package org.acme.getting.started.provider;

import org.acme.getting.started.dto.PersonDto;
import org.acme.getting.started.models.Person;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class PersonCodeProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz.equals(PersonDto.class)) {
            return (Codec<T>) PersonDto.builder().build();
        }
        return null;
    }
}
