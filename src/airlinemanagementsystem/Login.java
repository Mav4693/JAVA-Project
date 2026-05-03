package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {

    JButton submit, reset, close;
    JTextField tfusername;
    JPasswordField tfpassword;

    public Login() {

        setLayout(null);

        JLabel heading = new JLabel("Login - Air India");
        heading.setBounds(0, 40, 800, 40); // Centered across the new 800 width
        heading.setFont(new Font("Tahoma", Font.BOLD, 30));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setForeground(Color.WHITE);
        add(heading);

        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(100, 150, 100, 25);
        lblusername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblusername.setForeground(Color.WHITE);
        add(lblusername);

        tfusername = new JTextField();
        tfusername.setBounds(220, 150, 200, 30);
        add(tfusername);

        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(100, 200, 100, 25);
        lblpassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblpassword.setForeground(Color.WHITE);
        add(lblpassword);

        tfpassword = new JPasswordField();
        tfpassword.setBounds(220, 200, 200, 30);
        add(tfpassword);

        reset = new JButton("Reset");
        reset.setBounds(100, 280, 100, 35);
        reset.addActionListener(this);
        add(reset);

        submit = new JButton("Submit");
        submit.setBounds(220, 280, 100, 35);
        submit.addActionListener(this);
        add(submit);

        close = new JButton("Close");
        close.setBounds(340, 280, 100, 35);
        close.addActionListener(this);
        add(close);

        setSize(800, 500);
        setLocation(400, 150);
        getContentPane().setBackground(new Color(15, 15, 25)); // Matches ThemeManager

        // Add Logo to the right side
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/intro.jpeg"));
        Image i2 = i1.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(420, 50, 300, 300);
        add(image);

        ThemeManager.applyThemeToFrame(this);

        // Ensure background is set correctly after theme application
        getContentPane().setBackground(ThemeManager.BACKGROUND_COLOR);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == submit) {
            String username = tfusername.getText();
            String password = String.valueOf(tfpassword.getPassword());

            try {
                Conn c = new Conn();

                String query = "select * from login where username = '"
                        + username + "' and password = '"
                        + password + "'";

                ResultSet rs = c.s.executeQuery(query);

                if (rs.next()) {
                    new Home();
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == close) {
            setVisible(false);
        } else if (ae.getSource() == reset) {
            tfusername.setText("");
            tfpassword.setText("");
        }

    }

    public static void main(String[] args) {
        new Login();
    }
}