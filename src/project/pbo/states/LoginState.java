package project.pbo.states;

import project.pbo.Handler;
import project.pbo.account.User;
import project.pbo.gfx.Assets;
import project.pbo.gfx.Text;
import project.pbo.input.MouseManager;
import project.pbo.window.SIZE;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoginState extends State implements SIZE, Pesan {

    private final JTextField jUsername = new JTextField();
    private final JPasswordField jPassword = new JPasswordField();
    private final Rectangle loginBtn = new Rectangle(280,370,110,50);
    private final Rectangle registBtn = new Rectangle(20,370, 150, 50 );
    private final MouseManager mouseManager;
    private final ArrayList<User> users;
    private Clip clip;

    public LoginState(Handler handler) {
        super(handler);
        jUsername.setBounds(20, 215, 370, 30);
        jUsername.setMargin(new Insets(0, 5, 0, 0));
        jUsername.setFont(new Font("SansSerif", Font.BOLD, 20));
        jPassword.setBounds(20, 295, 370, 30);
        jPassword.setMargin(new Insets(0, 5, 0, 0));
        jPassword.setFont(new Font("SansSerif", Font.BOLD, 20));
        handler.getGame().getWindow().getLayeredPane().add(jUsername,0);
        handler.getGame().getWindow().getLayeredPane().add(jPassword,0);
        this.mouseManager = handler.getMouseManager();
        this.users = handler.getDb().getUsers();

    }

    @Override
    public void tick() {
        if ((mouseManager.isLeftPressed() || mouseManager.isRightPressed()) && loginBtn.contains(mouseManager.getMouseX(), mouseManager.getMouseY())){
            String username = jUsername.getText();
            String password = String.valueOf(jPassword.getPassword());

            boolean ada = false;
            for (User user : users){
                if (user.getUsername().equals(username) && user.getPassword().equals(password)){
                    ada = true;
                    clip.stop();
                    handler.getGame().getWindow().getLayeredPane().remove(jUsername);
                    handler.getGame().getWindow().getLayeredPane().remove(jPassword);
                    setCurrentState(new LoadingState(handler, new MainMenu(handler, user)));
                }
            }

            if (!ada){
                buatPesan("Username/Password tidak cocok!", JOptionPane.ERROR_MESSAGE, handler);
            }
            mouseManager.setLeftPressed(false);
            mouseManager.setRightPressed(false);

        }
        if ((mouseManager.isLeftPressed() || mouseManager.isRightPressed()) && registBtn.contains(mouseManager.getMouseX(), mouseManager.getMouseY())){
            JTextField u = new JTextField();
            JPasswordField p = new JPasswordField();
            JPasswordField c = new JPasswordField();
            JPanel jPanel = new JPanel(new GridLayout(0,1));
            jPanel.add(new JLabel("Username: "));
            jPanel.add(u);
            jPanel.add(new JLabel("Password: "));
            jPanel.add(p);
            jPanel.add(new JLabel("Confirm Password: "));
            jPanel.add(c);
            int result = JOptionPane.showConfirmDialog(handler.getGame().getWindow(), jPanel, "Register",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {

                String username = u.getText();
                String password = String.valueOf(p.getPassword());
                String confirm = String.valueOf(c.getPassword());

                boolean ada = false;
                for (User user : users){
                    if (user.getUsername().equals(username)){
                        ada = true;
                        break;
                    }
                }

                if (username.isEmpty() || password.isEmpty()) buatPesan("Field tidak boleh kosong!", JOptionPane.ERROR_MESSAGE, handler);
                else {
                    if (ada) buatPesan("Username sudah terdaftar!", JOptionPane.ERROR_MESSAGE, handler);
                    else {
                        if (password.equals(confirm)) {
                            users.add(new User(username, password));
                            buatPesan("User " + username + " berhasil didaftarkan!", JOptionPane.INFORMATION_MESSAGE, handler);
                            handler.saveFile();
                        } else
                            buatPesan("Password dan Confirm Password tidak cocok!", JOptionPane.ERROR_MESSAGE, handler);
                    }
                }

            }

            mouseManager.setLeftPressed(false);
            mouseManager.setRightPressed(false);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.loginBG, 0, 0, width, height, null);
        g.setColor(new Color(0,0,0,0.5f));
        g.fillRect(10, 150, 430, 300);
        Text.drawString(g, "Username: ", 20, 200, false, Color.WHITE, Assets.regulerFont);
        Text.drawString(g, "Password: ", 20, 280, false, Color.WHITE, Assets.regulerFont);
        g.setColor(Color.red);
        ((Graphics2D) g).fill(loginBtn);
        ((Graphics2D) g).fill(registBtn);
        Text.drawString(g, "Register", 38, 400, false, Color.WHITE, Assets.regulerFont);
        Text.drawString(g, "Login", 300, 400, false, Color.WHITE, Assets.regulerFont);

    }

    @Override
    public void playMusic() {
        clip = Assets.audioLogin;
        clip.start();
        handler.setVol(clip);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
