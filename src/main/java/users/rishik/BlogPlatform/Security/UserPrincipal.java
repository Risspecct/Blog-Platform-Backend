package users.rishik.BlogPlatform.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import users.rishik.BlogPlatform.Entities.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(User user){
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !user.isBanned();
    }

    public Long getUserId() {
        return user.getId();
    }
}
