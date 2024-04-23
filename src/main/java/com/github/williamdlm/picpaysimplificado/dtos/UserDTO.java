package com.github.williamdlm.picpaysimplificado.dtos;

import com.github.williamdlm.picpaysimplificado.domain.user.User;
import com.github.williamdlm.picpaysimplificado.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String document,
                      BigDecimal balance, String email, String password, UserType userType) {

    public User toModel() {
        return new User(firstName, lastName, document, balance, email, password, userType);
    }
}
