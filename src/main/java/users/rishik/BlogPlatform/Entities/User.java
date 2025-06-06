package users.rishik.BlogPlatform.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import users.rishik.BlogPlatform.Enums.Roles;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "mail_id")})
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

    @Column(name = "mail_id")
    private String email;

    @Column(name = "password")
    private String pwd;

    @Column(name = "bio")
    private String bio;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "role")
    private Set<Roles> roles = new HashSet<>();

    private boolean banned = false;
}
