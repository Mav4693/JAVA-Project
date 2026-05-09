package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BoardingPass extends JFrame implements ActionListener {

    JTextField tfticket;
    JLabel tfname, tfnationality, lblsrc, lbldest, labelfname, labelfcode, labeldate, labeltime;
    JButton fetchButton;

    public BoardingPass() {

        setLayout(null);

        // Fonts
        Font headingFont = new Font("Tahoma", Font.BOLD, 30);
        Font subHeadingFont = new Font("Tahoma", Font.BOLD, 22);
        Font labelFont = new Font("Tahoma", Font.BOLD, 16);
        Font valueFont = new Font("Tahoma", Font.PLAIN, 16);

        // Heading
        JLabel heading = new JLabel("AIR INDIA");
        heading.setBounds(350, 10, 400, 40);
        heading.setFont(headingFont);
        heading.setForeground(Color.WHITE);
        add(heading);

        JLabel subheading = new JLabel(" BOARDING  PASS");
        subheading.setBounds(320, 50, 400, 30);
        subheading.setFont(subHeadingFont);
        subheading.setForeground(Color.WHITE);
        add(subheading);

        // Ticket ID
        JLabel lblticket = new JLabel("Ticket ID");
        lblticket.setBounds(50, 100, 100, 25);
        lblticket.setFont(labelFont);
        lblticket.setForeground(Color.WHITE);
        add(lblticket);

        tfticket = new JTextField();
        tfticket.setBounds(150, 100, 150, 25);
        tfticket.setFont(valueFont);
        add(tfticket);

        fetchButton = new JButton("Enter");
        fetchButton.setBounds(320, 100, 100, 25);
        fetchButton.setFont(labelFont);
        fetchButton.addActionListener(this);
        add(fetchButton);

        // LEFT SIDE
        JLabel lbl1 = new JLabel("Name");
        lbl1.setBounds(50, 150, 120, 25);
        lbl1.setFont(labelFont);
        lbl1.setForeground(Color.WHITE);
        add(lbl1);

        tfname = new JLabel();
        tfname.setBounds(150, 150, 200, 25);
        tfname.setFont(valueFont);
        tfname.setForeground(Color.WHITE);
        add(tfname);

        JLabel lbl2 = new JLabel("Nationality");
        lbl2.setBounds(50, 190, 120, 25);
        lbl2.setFont(labelFont);
        lbl2.setForeground(Color.WHITE);
        add(lbl2);

        tfnationality = new JLabel();
        tfnationality.setBounds(150, 190, 200, 25);
        tfnationality.setFont(valueFont);
        tfnationality.setForeground(Color.WHITE);
        add(tfnationality);

        JLabel lbl3 = new JLabel("Source");
        lbl3.setBounds(50, 230, 120, 25);
        lbl3.setFont(labelFont);
        lbl3.setForeground(Color.WHITE);
        add(lbl3);

        lblsrc = new JLabel();
        lblsrc.setBounds(150, 230, 200, 25);
        lblsrc.setFont(valueFont);
        lblsrc.setForeground(Color.WHITE);
        add(lblsrc);

        JLabel lbl4 = new JLabel("Destination");
        lbl4.setBounds(50, 270, 120, 25);
        lbl4.setFont(labelFont);
        lbl4.setForeground(Color.WHITE);
        add(lbl4);

        lbldest = new JLabel();
        lbldest.setBounds(150, 270, 200, 25);
        lbldest.setFont(valueFont);
        lbldest.setForeground(Color.WHITE);
        add(lbldest);

        // RIGHT SIDE
        JLabel lbl5 = new JLabel("Flight Name");
        lbl5.setBounds(400, 150, 120, 25);
        lbl5.setFont(labelFont);
        lbl5.setForeground(Color.WHITE);
        add(lbl5);

        labelfname = new JLabel();
        labelfname.setBounds(520, 150, 200, 25);
        labelfname.setFont(valueFont);
        labelfname.setForeground(Color.WHITE);
        add(labelfname);

        JLabel lbl6 = new JLabel("Flight Code");
        lbl6.setBounds(400, 190, 120, 25);
        lbl6.setFont(labelFont);
        lbl6.setForeground(Color.WHITE);
        add(lbl6);

        labelfcode = new JLabel();
        labelfcode.setBounds(520, 190, 200, 25);
        labelfcode.setFont(valueFont);
        labelfcode.setForeground(Color.WHITE);
        add(labelfcode);

        JLabel lbl7 = new JLabel("Date");
        lbl7.setBounds(400, 230, 120, 25);
        lbl7.setFont(labelFont);
        lbl7.setForeground(Color.WHITE);
        add(lbl7);

        labeldate = new JLabel();
        labeldate.setBounds(520, 230, 200, 25);
        labeldate.setFont(valueFont);
        labeldate.setForeground(Color.WHITE);
        add(labeldate);

        JLabel lbl8 = new JLabel("Time");
        lbl8.setBounds(400, 270, 120, 25);
        lbl8.setFont(labelFont);
        lbl8.setForeground(Color.WHITE);
        add(lbl8);

        labeltime = new JLabel();
        labeltime.setBounds(520, 270, 200, 25);
        labeltime.setFont(valueFont);
        labeltime.setForeground(Color.WHITE);
        add(labeltime);

        // Image
        try {
            ImageIcon img = new ImageIcon(
                    ClassLoader.getSystemResource("airlinemanagementsystem/icons/intro.jpeg"));
            Image i = img.getImage().getScaledInstance(280, 180, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(i));
            image.setBounds(650, 100, 250, 200);
            image.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            add(image);
        } catch (Exception e) {
            System.out.println("Image not found");
        }

        setSize(1000, 450);
        setLocation(300, 150);
        ThemeManager.applyThemeToFrame(this);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == fetchButton) {

            String ticket = tfticket.getText().trim();

            if (ticket.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Ticket ID.",
                        "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Conn conn = new Conn();

                String query = "SELECT * FROM reservation WHERE TICKET = '" + ticket + "'";
                ResultSet rs = conn.s.executeQuery(query);

                if (rs.next()) {
                    tfname.setText(rs.getString("name"));
                    tfnationality.setText(rs.getString("nationality"));
                    lblsrc.setText(rs.getString("src"));
                    lbldest.setText(rs.getString("des"));
                    labelfname.setText(rs.getString("flightname"));
                    labelfcode.setText(rs.getString("flightcode"));
                    labeldate.setText(rs.getString("ddate"));

                    // Fetch departure time from flight table
                    String fcode = rs.getString("flightcode");
                    ResultSet rs2 = conn.s.executeQuery(
                            "SELECT dep_time FROM flight WHERE f_code = '" + fcode + "'");
                    if (rs2.next()) {
                        labeltime.setText(rs2.getString("dep_time"));
                    } else {
                        labeltime.setText("N/A");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Ticket ID",
                            "Not Found", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new BoardingPass();
    }
}