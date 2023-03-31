package com.beerkhaton.mealtrackerapi.dto.output;


import com.beerkhaton.mealtrackerapi.dto.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmployeeHistoryResponseDTO extends StandardResponseDTO {

    @JsonProperty
    private Object data;

    private long count;

    public EmployeeHistoryResponseDTO() {}

    public EmployeeHistoryResponseDTO(Status status) {
        super(status);
    }

    public EmployeeHistoryResponseDTO(Status status, Object data) {
        this.status = status;
        this.data = data;
    }

    public EmployeeHistoryResponseDTO(Status status, Object data, long count) {
        this.status = status;
        this.data = data;
        this.count = count;
    }

}
