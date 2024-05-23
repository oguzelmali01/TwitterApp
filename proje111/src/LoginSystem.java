import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginSystem {
    private List<User> users;
    private static final String FILE_NAME = "users.txt";
    private static final String FILE_FRIENDS = "friends.txt";
    private static final String FILE_TWITS = "twits.txt";
    private User currentUser;
    private static LoginSystem instance;


    private LoginSystem() {
        users = new ArrayList<>();
        loadUsersFromFile();
    }

    // Singleton tasarım deseni
    public static LoginSystem getInstance() {
        if (instance == null) {
            instance = new LoginSystem();
        }
        return instance;
    }
    public List<Tweet> getCurrentUserAndFriendsTweets() {
        List<Tweet> allTweets = new ArrayList<>();
        if (currentUser != null) {
            allTweets.addAll(currentUser.getTweets());
            for (User friend : currentUser.getFriends()) {
                allTweets.addAll(friend.getTweets());
            }
        }
        return allTweets;
    }


    //Data Access Object (DAO) Tasarım Deseni
    private void loadUsersFromFile() {
        try (BufferedReader userReader = new BufferedReader(new FileReader(FILE_NAME));
             BufferedReader friendsReader = new BufferedReader(new FileReader(FILE_FRIENDS));
             BufferedReader twitReader = new BufferedReader(new FileReader(FILE_TWITS))) {

            String line;

            // Kullanıcıları yükle
            while ((line = userReader.readLine()) != null) {
                String[] parts = line.split("#");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String password = parts[1].trim();
                    users.add(UserFactory.createUser(name, password));
                }
            }

            // Arkadaşları yükle
            while ((line = friendsReader.readLine()) != null) {
                String[] parts = line.split("#");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String[] friendNames = parts[1].trim().split(",");
                    User user = findUserByUsername(username);
                    if (user != null) {
                        for (String friendName : friendNames) {
                            User friend = findUserByUsername(friendName.trim());
                            if (friend != null) {
                                user.addFriend(friend);
                            }
                        }
                    }
                }
            }

            // Tweetleri yükle
            while ((line = twitReader.readLine()) != null) {
                String[] parts = line.split("#");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    User user = findUserByUsername(username);
                    if (user != null) {
                        List<String> tweetList = Arrays.asList(parts[1].trim().split("~"));
                        List<Tweet> twitList = user.getTweets();
                        for (String twitContent : tweetList) {
                            Tweet tweet = TweetFactory.createTweet(user, twitContent.trim());
                            twitList.add(tweet);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Giriş başarılı!");
                return true;
            }
        }
        System.out.println("Yanlış kullanıcı adı veya şifre.");
        return false;
    }

    public void signup(String username, String password) {
        if (findUserByUsername(username) != null) {
            System.out.println("Bu kullanıcı adı zaten mevcut.");
            return;
        }

        User newUser = UserFactory.createUser(username, password);
        users.add(newUser);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + "#" + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Kullanıcı kaydedilemedi, bir hata oluştu.");
            return;
        }

        System.out.println("Kullanıcı başarıyla kaydedildi!");
    }

    public void addFriend(User friend) {
        if (currentUser != null && friend != null) {
            if (!currentUser.getFriends().contains(friend)) { // Arkadaş zaten listede değilse ekle
                currentUser.addFriend(friend);
                friend.addFriend(currentUser); // Yeni arkadaşı ekleyin
                saveFriendsToFile(); // Arkadaş listesini dosyaya kaydet
                System.out.println(friend.getUsername() + " arkadaş olarak eklendi.");
            } else {
                System.out.println("Bu kullanıcı zaten arkadaşınız.");
            }
        } else {
            System.out.println("Arkadaş eklenemedi.");
        }
    }



    public void removeFriend(User friend) {
        if (currentUser != null && friend != null) {
            if (currentUser.getFriends().contains(friend)) { // Eğer arkadaş listesinde ise çıkar
                currentUser.removeFriend(friend);
                friend.removeFriend(currentUser); // Arkadaşın listesinden de çıkar
                saveFriendsToFile(); // Arkadaş listesini dosyaya kaydet
                System.out.println(friend.getUsername() + " arkadaş listesinden çıkarıldı.");
            } else {
                System.out.println("Bu kullanıcı arkadaşınız değil.");
            }
        } else {
            System.out.println("Arkadaş çıkarılamadı.");
        }
    }


    public void postTweet(String content) {
        if (currentUser == null) {
            System.out.println("Önce giriş yapmalısınız.");
            return;
        }

        currentUser.postTweet(content);
        updateTweetFile(currentUser, content); // Yeni tweeti dosyaya kaydet

        System.out.println("Tweet atıldı: " + content);
    }
    //Data Access Object (DAO) Tasarım Deseni
    private void updateTweetFile(User user, String newTweetContent) {
        List<String> lines = new ArrayList<>();

        // Dosyadaki mevcut tweetleri oku
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_TWITS))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // İlk kullanıcıyı bul ve yeni tweeti satırın sonuna ekle
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("#");
            if (parts[0].equals(user.getUsername())) {
                lines.set(i, lines.get(i) + "~" + newTweetContent);
                break;
            }
        }

        // Dosyayı güncelle
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_TWITS))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//OBSERVER DESSIGN PATTERN
    public void viewTweets() {
        if (currentUser == null) {
            System.out.println("Önce giriş yapmalısınız.");
            return;
        }

        List<Tweet> allTweets = new ArrayList<>();
        allTweets.addAll(currentUser.getTweets());

        for (User friend : currentUser.getFriends()) {
            allTweets.addAll(friend.getTweets());
        }

        if (allTweets.isEmpty()) {
            System.out.println("Henüz hiç twit yok.");
        } else {
            System.out.println("----- Twitler -----");
            for (Tweet tweet : allTweets) {
                System.out.println("[" + tweet.getAuthor().getUsername() + "] " + tweet.getContent());
            }
        }
    }
    //Data Access Object (DAO) Tasarım Deseni
    private void saveFriendsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_FRIENDS))) {
            for (User user : users) {
                writer.write(user.getUsername() + "#");
                List<User> friends = user.getFriends();
                for (int i = 0; i < friends.size(); i++) {
                    writer.write(friends.get(i).getUsername());
                    if (i < friends.size() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : users) {
                writer.write(user.getUsername() + "#" + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFriendsToFiles() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_FRIENDS))) {
            for (User user : users) {
                writer.write(user.getUsername() + "#");
                List<User> friends = user.getFriends();
                for (int i = 0; i < friends.size(); i++) {
                    writer.write(friends.get(i).getUsername());
                    if (i < friends.size() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTweetsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_TWITS))) {
            for (User user : users) {
                writer.write(user.getUsername() + "#");
                List<Tweet> tweets = user.getTweets();
                for (int i = 0; i < tweets.size(); i++) {
                    writer.write(tweets.get(i).getContent());
                    if (i < tweets.size() - 1) {
                        writer.write("~");
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public User getCurrentUser() {
        return currentUser;
    }
}
