package users.rishik.BlogPlatform.Projections;

public interface PostView {
    long getId();
    String getHeader();
    String getBody();
    UserSummary getUser();

    interface UserSummary {
        String getUsername();
    }
}
