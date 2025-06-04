package users.rishik.BlogPlatform.Projections;

public interface LikeView {

    PostSummary getPost();
    UserSummary getUser();

    interface PostSummary {
        long getId();
    }

    interface UserSummary {
        long getId();
    }
}
