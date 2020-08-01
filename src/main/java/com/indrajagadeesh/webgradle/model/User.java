package com.indrajagadeesh.webgradle.model;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * user pojo for database table mapping (ORM)
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    private String username;
    @NonNull
    private String password;
    @NonNull
    private int role;
}
