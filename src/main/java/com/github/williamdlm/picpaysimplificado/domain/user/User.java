package com.github.williamdlm.picpaysimplificado.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "users")
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String document;
    @Column(unique = true)
    private String email;
    private String password;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(String firstName, String lastName, String document, BigDecimal balance,
                String email, String password, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.document = document;
        this.balance = balance;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
}
