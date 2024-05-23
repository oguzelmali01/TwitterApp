public class TweetFactory {
    public static Tweet createTweet(User author, String content) {
        return new Tweet(author, content);
    }
}
