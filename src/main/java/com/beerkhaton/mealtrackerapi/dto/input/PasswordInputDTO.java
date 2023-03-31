package com.beerkhaton.mealtrackerapi.dto.input;

import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@GroupSequence({
        PasswordInputDTO.First.class,
        PasswordInputDTO.Second.class,
        PasswordInputDTO.Third.class,
        PasswordInputDTO.class
})
@Data
public class PasswordInputDTO {

    @NotNull(message = "{user.password.notEmpty}", groups = PasswordInputDTO.First.class)
    @Size(min = 8, max = 150, message = "{user.password.sizeError}", groups = PasswordInputDTO.Second.class)
    private String password;

    @NotNull(message = "{user.password.notEmpty}", groups = PasswordInputDTO.First.class)
    @Size(min = 8, max = 150, message = "{user.password.sizeError}", groups = PasswordInputDTO.Second.class)
    private String confirmPassword;

    interface First {
    }

    interface Second {
    }

    interface Third {
    }
}
