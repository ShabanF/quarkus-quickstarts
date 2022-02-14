package org.acme.getting.started.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.acme.getting.started.models.Country;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    @JsonProperty("_id")
    private Object id;
    private String name;
    private List<Country> visitedCountries;
}
