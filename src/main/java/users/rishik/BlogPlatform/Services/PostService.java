package users.rishik.BlogPlatform.Services;

import org.springframework.stereotype.Service;
import users.rishik.BlogPlatform.Dtos.PostDto;
import users.rishik.BlogPlatform.Dtos.UpdatePostDto;
import users.rishik.BlogPlatform.Entities.Post;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Exceptions.UnauthorizedException;
import users.rishik.BlogPlatform.Mappers.PostMapper;
import users.rishik.BlogPlatform.Projections.PostView;
import users.rishik.BlogPlatform.Repositories.UserRepository;

import java.util.List;

@Service
public class PostService {
    private final users.rishik.BlogPlatform.Repositories.PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    PostService(users.rishik.BlogPlatform.Repositories.PostRepository postRepository, PostMapper postMapper, UserRepository userRepository){
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
    }

    public PostView addPost(long userId, PostDto postDto){
        postDto.setUserId(userId);
        Post post = this.postMapper.fromDtoToPost(postDto, this.userRepository);
        this.postRepository.save(post);
        return this.postRepository.findPostById(post.getId()).orElseThrow(() -> new NotFoundException("Post unavailable"));
    }

    public PostView getPost(long id){
        return this.postRepository.findPostById(id).orElseThrow(() -> new NotFoundException("Post unavailable"));
    }

    public List<PostView> getByUserId(long userId){
        return this.postRepository.findAllByUserId(userId).orElseThrow(() -> new NotFoundException("No posts found under this user id"));
    }

    public PostView updatePost(long userId, long postId, UpdatePostDto postDto){
        Post post = this.validate(userId, postId);
        post = this.postMapper.UpdateFromDto(postDto, post);
        this.postRepository.save(post);
        return this.postRepository.findPostById(post.getId()).orElseThrow(() -> new NotFoundException("Post unavailable"));
    }

    public void deletePost(long userId, long postId){
        this.validate(userId, postId);
        this.postRepository.deleteById(postId);
    }

    public Post validate(long userId, long postId){
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post unavailable"));
        if (userId != post.getUser().getId()) throw new UnauthorizedException("You are not the author of this post");
        return post;
    }
}
