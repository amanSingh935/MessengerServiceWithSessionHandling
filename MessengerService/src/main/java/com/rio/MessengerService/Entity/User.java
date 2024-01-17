package com.rio.MessengerService.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Column(nullable = false)
    @Id
    private String username;

    @Column(nullable = false)
    private String passwd;
    private String email;
    private String roles;
    private String name;
}
