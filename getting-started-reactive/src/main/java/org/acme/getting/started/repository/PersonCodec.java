package org.acme.getting.started.repository;

import com.mongodb.MongoClientSettings;
import org.acme.getting.started.dto.PersonDto;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.UUID;

public class PersonCodec implements CollectibleCodec<PersonDto> {

    private final Codec<Document> documentCodec;

    public PersonCodec(Codec<Document> documentCodec) {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public PersonDto generateIdIfAbsentFromDocument(PersonDto personDto) {
        if (!documentHasId(personDto)) {
            personDto.setId(UUID.randomUUID().toString());
        }
        return personDto;
    }

    @Override
    public boolean documentHasId(PersonDto personDto) {
        return personDto.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(PersonDto personDto) {
        return new BsonString((String) personDto.getId());
    }

    @Override
    public PersonDto decode(BsonReader reader, DecoderContext decoderContext) {

        Document document = documentCodec.decode(reader, decoderContext);
        PersonDto personDto = PersonDto.builder().build();
        if (document.getString("id") != null) {
            personDto.setId(document.getString("id"));
        }
        personDto.setName(document.getString("name"));
        //fruit.setDescription(document.getString("description"));
        return personDto;
    }

    @Override
    public void encode(BsonWriter writer, PersonDto personDto, EncoderContext encoderContext) {
        Document doc = new Document();
        doc.put("name", personDto.getName());

        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Class<PersonDto> getEncoderClass() {
        return PersonDto.class;
    }
}
