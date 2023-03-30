package com.beerkhaton.mealtrackerapi.dto.output;


import com.beerkhaton.mealtrackerapi.dto.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BasicResponseDTO extends StandardResponseDTO {

    @JsonProperty
    private Object data;

    public BasicResponseDTO() {}

    public BasicResponseDTO(Status status) {
        super(status);
    }

    public BasicResponseDTO(Status status, Object data) {
        this.status = status;
        this.data = data;
    }

}
