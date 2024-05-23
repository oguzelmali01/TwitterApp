import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
public class LoginSystemTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final String testUsername = "testUser";
    private final String testPassword = "testPassword";
    private final String testFriendUsername = "testFriend";
    private final String testTweetContent = "Test tweet";

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testLoginSystemSignup() {
        LoginSystem loginSystem = LoginSystem.getInstance();
        loginSystem.signup(testUsername, testPassword);
        assertEquals("Kullanıcı başarıyla kaydedildi!\n", outContent.toString());
    }

    @Test
    public void testLoginSystemLogin() {
        LoginSystem loginSystem = LoginSystem.getInstance();
        loginSystem.signup(testUsername, testPassword);
        loginSystem.login(testUsername, testPassword);
        assertEquals("Giriş başarılı!\n", outContent.toString());
    }

    @Test
    public void testUserAddFriend() {
        User user = new User(testUsername, testPassword, new ArrayList<>(), new ArrayList<>());
        User friend = new User(testFriendUsername, "password", new ArrayList<>(), new ArrayList<>());
        user.addFriend(friend);
        assertEquals(1, user.getFriends().size());
    }

    @Test
    public void testUserPostTweet() {
        User user = new User(testUsername, testPassword, new ArrayList<>(), new ArrayList<>());
        user.postTweet(testTweetContent);
        assertEquals(1, user.getTweets().size());
    }


}
