package users.rishik.BlogPlatform.Services;

import org.springframework.stereotype.Service;
import users.rishik.BlogPlatform.Dtos.UpdateUserDto;
import users.rishik.BlogPlatform.Dtos.UserDto;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Mappers.UserMapper;
import users.rishik.BlogPlatform.Projections.UserView;
import users.rishik.BlogPlatform.Repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User addUser(UserDto userDto){
        User user = this.userMapper.UserDtoToUser(userDto);
        return this.userRepository.save(user);
    }

    public UserView getUser(long id){
        return this.userRepository.findUserById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    public List<UserView> getAllUsers(){
        return this.userRepository.findAllBy();
    }

    public User updateUser(long id, UpdateUserDto userDto){
        User user = this.userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        this.userMapper.updateFromDto(userDto, user);
        return this.userRepository.save(user);
    }

    public void deleteUser(long id){
        this.userRepository.deleteById(id);
    }

}
