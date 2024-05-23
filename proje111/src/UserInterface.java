import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class UserInterface extends JFrame {
    private final LoginSystem loginSystem;
    private List<User> users;
    private User currentUser;
    private JButton viewTweetsButton;

    private static final String USERS_FILE = "users.txt";
    private static final String FRIENDS_FILE = "friends.txt";
    private static final String TWEETS_FILE = "tweets.txt";
    private static LoginSystem instance;

    public UserInterface(LoginSystem loginSystem) {
        super("Kullanıcı Arayüzü");
        this.loginSystem = loginSystem;
        initializeUI();
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.write(user.getUsername() + "#" + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFriendsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FRIENDS_FILE))) {
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

    private void saveTweetsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TWEETS_FILE))) {
            for (User user : users) {
                for (Tweet tweet : user.getTweets()) {
                    writer.write(user.getUsername() + "#" + tweet.getContent());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Ekranın ortasında açılmasını sağlar

        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Giriş Yap veya Kayıt Ol");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        loginPanel.add(titleLabel, gbc);

        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx++;
        JTextField usernameField = new JTextField(15);
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Şifre:");
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx++;
        JPasswordField passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JButton loginButton = new JButton("Giriş Yap");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (loginSystem.login(username, password)) {
                    showMainMenu();
                } else {
                    JOptionPane.showMessageDialog(UserInterface.this, "Giriş başarısız. Lütfen kullanıcı adı ve şifrenizi kontrol edin.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginPanel.add(loginButton, gbc);

        gbc.gridx++;
        JButton signupButton = new JButton("Kayıt Ol");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (!username.isEmpty() && !password.isEmpty()) {
                    loginSystem.signup(username, password);
                    JOptionPane.showMessageDialog(UserInterface.this, "Kullanıcı başarıyla kaydedildi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(UserInterface.this, "Kullanıcı adı ve şifre boş olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginPanel.add(signupButton, gbc);

        add(loginPanel);
    }

    private void showMainMenu() {
        MainMenu mainMenu = new MainMenu(loginSystem);
        mainMenu.setVisible(true);
        setVisible(false); // Kullanıcı arayüzünü gizle
    }

    private void displayTweets() {
        loginSystem.viewTweets();
    }

    public static void main(String[] args) {
        LoginSystem loginSystem = LoginSystem.getInstance();
        UserInterface ui = new UserInterface(loginSystem);
        ui.setVisible(true); // Kullanıcı arayüzünü göster
    }
}
