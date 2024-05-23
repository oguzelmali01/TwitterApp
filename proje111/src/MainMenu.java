import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {
    private LoginSystem loginSystem;

    public MainMenu(LoginSystem loginSystem) {
        super("Ana Menü");
        this.loginSystem = loginSystem;
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Ekranın ortasında açılmasını sağlar

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 5, 5));

        JButton addFriendButton = new JButton("Arkadaş Ekle");
        addFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String friendUsername = JOptionPane.showInputDialog(MainMenu.this, "Eklemek istediğiniz arkadaşın kullanıcı adını girin:");
                User friend = loginSystem.findUserByUsername(friendUsername);
                if (friend != null) {
                    loginSystem.addFriend(friend);
                    JOptionPane.showMessageDialog(MainMenu.this, friend.getUsername() + " arkadaş olarak eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(MainMenu.this, "Kullanıcı bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(addFriendButton);

        JButton removeFriendButton = new JButton("Arkadaş Çıkar");
        removeFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String friendUsername = JOptionPane.showInputDialog(MainMenu.this, "Çıkarmak istediğiniz arkadaşın kullanıcı adını girin:");
                User friend = loginSystem.findUserByUsername(friendUsername);
                if (friend != null) {
                    loginSystem.removeFriend(friend);
                    JOptionPane.showMessageDialog(MainMenu.this, friend.getUsername() + " arkadaş listesinden çıkarıldı.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(MainMenu.this, "Kullanıcı bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(removeFriendButton);

        JButton postTweetButton = new JButton("Tweet At");
        postTweetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tweetContent = JOptionPane.showInputDialog(MainMenu.this, "Tweet içeriğini girin:");
                loginSystem.postTweet(tweetContent);
            }
        });
        mainPanel.add(postTweetButton);

        JButton viewTweetsButton = new JButton("Tweetlere Bak");
        viewTweetsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginSystem.viewTweets();
            }
        });
        mainPanel.add(viewTweetsButton);

        add(mainPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                loginSystem.saveUsersToFile();
                loginSystem.saveFriendsToFiles();
                loginSystem.saveTweetsToFile();
                dispose(); // Pencereyi kapat
            }
        });
    }
}
