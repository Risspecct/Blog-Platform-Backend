package users.rishik.BlogPlatform.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Repositories.UserRepository;
import users.rishik.BlogPlatform.Security.UserPrincipal;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public MyUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with the email: " + username));
        return new UserPrincipal(user);
    }
}
