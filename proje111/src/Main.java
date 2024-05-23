import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LoginSystem loginSystem = LoginSystem.getInstance();
        boolean isLoggedIn = false;

        while (true) {
            if (!isLoggedIn) {
                System.out.println("1. Login");
                System.out.println("2. Signup");
                System.out.println("3. Exit");
                System.out.print("Seçenek girin: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.print("Kullanıcı adı: ");
                        String username = scanner.nextLine();
                        System.out.print("Şifre: ");
                        String password = scanner.nextLine();
                        isLoggedIn = loginSystem.login(username, password);
                        break;
                    case "2":
                        System.out.print("Yeni kullanıcı adı: ");
                        String newUsername = scanner.nextLine();
                        System.out.print("Yeni şifre: ");
                        String newPassword = scanner.nextLine();
                        loginSystem.signup(newUsername, newPassword);
                        break;
                    case "3":
                        System.out.println("Programdan çıkılıyor...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Geçersiz seçenek!");
                }
            } else {
                System.out.println("1. Arkadaş Ekle");
                System.out.println("2. Arkadaş Çıkar");
                System.out.println("3. Twit At");
                System.out.println("4. Twitlere Bak");
                System.out.println("5. Logout");
                System.out.print("Seçenek girin: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.print("Eklenecek arkadaşın kullanıcı adı: ");
                        String friendUsername = scanner.nextLine();
                        User friend = loginSystem.findUserByUsername(friendUsername);
                        if (friend != null) {
                            loginSystem.addFriend(friend);
                        } else {
                            System.out.println("Kullanıcı bulunamadı.");
                        }
                        break;
                    case "2":
                        System.out.print("Çıkarılacak arkadaşın kullanıcı adı: ");
                        String exFriendUsername = scanner.nextLine();
                        User exFriend = loginSystem.findUserByUsername(exFriendUsername);
                        if (exFriend != null) {
                            loginSystem.removeFriend(exFriend);
                        } else {
                            System.out.println("Kullanıcı bulunamadı.");
                        }
                        break;
                    case "3":
                        System.out.print("Twit içeriği: ");
                        String tweetContent = scanner.nextLine();
                        loginSystem.postTweet(tweetContent);
                        break;
                    case "4":
                        loginSystem.viewTweets();
                        break;
                    case "5":
                        isLoggedIn = false;
                        System.out.println("Çıkış yapıldı.");
                        break;
                    default:
                        System.out.println("Geçersiz seçenek!");
                }
            }
        }
    }
}
