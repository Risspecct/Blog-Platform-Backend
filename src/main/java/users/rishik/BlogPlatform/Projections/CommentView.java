package users.rishik.BlogPlatform.Projections;

public interface CommentView {
    long getId();
    String getComment();
    PostSummary getPost();
    UserSummary getUser();

    interface PostSummary {
        long getId();
    }

    interface UserSummary {
        long getId();
    }
}
