package users.rishik.BlogPlatform.Services;

import org.springframework.stereotype.Service;
import users.rishik.BlogPlatform.Dtos.PostDto;
import users.rishik.BlogPlatform.Dtos.UpdatePostDto;
import users.rishik.BlogPlatform.Entities.Post;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Exceptions.UnauthorizedException;
import users.rishik.BlogPlatform.Mappers.PostMapper;
import users.rishik.BlogPlatform.Repositories.PostRepository;
import users.rishik.BlogPlatform.Repositories.UserRepository;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    PostService(PostRepository postRepository, PostMapper postMapper, UserRepository userRepository){
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
    }

    public Post addPost(PostDto postDto){
        Post post = this.postMapper.fromDtoToPost(postDto, this.userRepository);
        return this.postRepository.save(post);
    }

    public Post getPost(long id){
        return this.postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post unavailable"));
    }

    public List<Post> getByUserId(long userId){
        return this.postRepository.findAllByUserId(userId).orElseThrow(() -> new NotFoundException("No posts found under this user id"));
    }

    public Post updatePost(long userId, long postId, UpdatePostDto postDto){
        Post post = this.validate(userId, postId);
        post = this.postMapper.UpdateFromDto(postDto, post);
        return this.postRepository.save(post);
    }

    public void deletePost(long userId, long postId){
        this.validate(userId, postId);
        this.postRepository.deleteById(postId);
    }

    public Post validate(long userId, long postId){
        Post post = this.getPost(postId);
        if (userId != post.getUser().getId()) throw new UnauthorizedException("You are not the author of this post");
        return post;
    }
}
