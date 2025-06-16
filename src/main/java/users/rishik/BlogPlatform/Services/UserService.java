package users.rishik.BlogPlatform.Services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import users.rishik.BlogPlatform.Dtos.LoginUserDto;
import users.rishik.BlogPlatform.Dtos.UpdateUserDto;
import users.rishik.BlogPlatform.Dtos.UserDto;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Enums.Roles;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Mappers.UserMapper;
import users.rishik.BlogPlatform.Projections.UserView;
import users.rishik.BlogPlatform.Repositories.UserRepository;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       JwtService jwtService, AuthenticationManager authManager){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public UserView addUser(UserDto userDto){
        if (userDto.getRoles() == null || userDto.getRoles().isEmpty()){
            userDto.setRoles(Set.of(Roles.VIEWER));
        }
        if (userDto.getRoles().equals(Set.of(Roles.MOD)) || userDto.getRoles().equals(Set.of(Roles.ADMIN)))
            throw new IllegalArgumentException("Cannot add user with this role. Ask admin for permission");
        if (this.userRepository.existsByEmail(userDto.getEmail()))
            throw new IllegalArgumentException("Email already exists");
        userDto.setPwd(encoder.encode(userDto.getPwd()));
        User user = this.userMapper.UserDtoToUser(userDto);
        this.userRepository.save(user);
        return this.userRepository.findUserById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + user.getId()));
    }

    public UserView getUser(long id){
        return this.userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    public List<UserView> getAllUsers(){
        return this.userRepository.findAllBy();
    }

    public UserView updateUser(long id, UpdateUserDto userDto){
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        if (userDto.getRoles() != null &&
                userDto.getRoles().stream().anyMatch(role -> role == Roles.MOD || role == Roles.ADMIN)) {
            throw new IllegalArgumentException("Cannot assign elevated roles");
        }
        this.userMapper.updateFromDto(userDto, user);
        this.userRepository.save(user);
        return this.userRepository.findUserById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + user.getId()));
    }

    public void deleteUser(long id){
        this.userRepository.deleteById(id);
    }

    public String verify(LoginUserDto user){
        Authentication auth = this.authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPwd()));
        if (auth.isAuthenticated()){
            User existingUser = this.userRepository.findByEmail(user.getEmail())
                                    .orElseThrow(() -> new NotFoundException("User not found with email: " + user.getEmail()));
            return this.jwtService.generateToken(existingUser);
        } else
            return "Login Failed. Please try again";
    }

}
