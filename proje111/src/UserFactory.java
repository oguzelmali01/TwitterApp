import java.util.ArrayList;
public class UserFactory {
    public static User createUser(String username, String password) {
        return new User(username, password, new ArrayList<>(), new ArrayList<>());
    }
}
