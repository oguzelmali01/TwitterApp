import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<User> friends;
    private List<Tweet> tweets;

    public User(String username, String password, List<User> friends, List<Tweet> tweets) {
        this.username = username;
        this.password = password;
        this.friends = friends;
        this.tweets = tweets;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }

    public void removeFriend(User friend) {
        friends.remove(friend);
    }

    public void addTweet(Tweet tweet) {
        tweets.add(tweet);
    }

    public void postTweet(String content) {
        Tweet tweet = new Tweet(this, content);
        this.addTweet(tweet);
        System.out.println("Tweet atıldı: " + content);
    }

    public void viewTweets() {
        List<Tweet> friendTweets = new ArrayList<>();
        for (User friend : friends) {
            friendTweets.addAll(friend.getTweets());
        }

        if (friendTweets.isEmpty()) {
            System.out.println("Henüz hiç arkadaşınız tweet atmamış.");
        } else {
            System.out.println("----- Arkadaşlarınızın Tweetleri -----");
            for (Tweet tweet : friendTweets) {
                System.out.println("[" + tweet.getAuthor().getUsername() + "] " + tweet.getContent());
            }
        }
    }
}
