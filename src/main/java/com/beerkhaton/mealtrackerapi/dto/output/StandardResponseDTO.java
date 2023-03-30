package com.beerkhaton.mealtrackerapi.dto.output;

import com.beerkhaton.mealtrackerapi.dto.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class StandardResponseDTO implements Serializable {


    @JsonProperty
    protected Status status;

    public StandardResponseDTO() {
    }
    public StandardResponseDTO(Status status) {
        this.status = status;
    }
}
