package com.beerkhaton.mealtrackerapi.dto.input;

import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

@GroupSequence({
        UserInputDTO.First.class,
        UserInputDTO.Second.class,
        UserInputDTO.Third.class,
        UserInputDTO.class
})
@Data
public class UserInputDTO {

    @NotNull(message = "{user.email.notEmpty}", groups = UserInputDTO.First.class)
    private String name;

    @NotNull(message = "{user.email.notEmpty}", groups = UserInputDTO.First.class)
    private String department;

    @NotNull(message = "{user.email.notEmpty}", groups = UserInputDTO.First.class)
    private String email;

    @NotNull(message = "{user.email.notEmpty}", groups = UserInputDTO.First.class)
    private String gender;

    interface First {
    }

    interface Second {
    }

    interface Third {
    }
}
