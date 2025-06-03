package users.rishik.BlogPlatform.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import users.rishik.BlogPlatform.Enums.Roles;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String username;

    @Column(name = "mail_id", unique = true)
    private String email;

    @Column(name = "password")
    private String pwd;

    @Column(name = "bio")
    private String bio;

    @Column(name = "Role")
    private Roles role;

}
