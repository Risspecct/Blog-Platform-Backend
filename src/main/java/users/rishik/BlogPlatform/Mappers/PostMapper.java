package users.rishik.BlogPlatform.Mappers;

import org.mapstruct.*;
import users.rishik.BlogPlatform.Dtos.PostDto;
import users.rishik.BlogPlatform.Dtos.UpdatePostDto;
import users.rishik.BlogPlatform.Entities.Post;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Repositories.UserRepository;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "userId", target = "user", qualifiedByName = "findById")
    Post fromDtoToPost(PostDto postDto, @Context UserRepository userRepository);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Post UpdateFromDto(UpdatePostDto postDto, @MappingTarget Post post);

    @Named("findById")
    default User findById(long user_id,@Context UserRepository userRepository){
        return userRepository.findById(user_id).orElseThrow(() -> new NotFoundException("User not found with id: " + user_id));
    }

}
