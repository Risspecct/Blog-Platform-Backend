package users.rishik.BlogPlatform.Projections;

import users.rishik.BlogPlatform.Enums.Roles;

public interface UserView {
    long getId();
    String getUsername();
    String getBio();
    Roles getRoles();
}
