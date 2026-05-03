package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class Cancel extends JFrame implements ActionListener {

    JTextField tfpnr;
    JLabel tfname, cancellationno, lblfcode, lbldateofTravel;
    JButton fetchButton, cancel;

    public Cancel() {

        setLayout(null);

        Random random = new Random();

        JLabel heading = new JLabel("Cancel Ticket");
        heading.setBounds(180, 20, 500, 35);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 32));
        heading.setForeground(Color.WHITE);
        add(heading);

        ImageIcon il = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/ashokstambh.png"));
        Image i2 = il.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(470, 120, 250, 250);
        image.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        add(image);

        JLabel lblpnr = new JLabel("PNR Number");
        lblpnr.setBounds(60, 80, 150, 25);
        lblpnr.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblpnr.setForeground(Color.WHITE);
        add(lblpnr);

        tfpnr = new JTextField();
        tfpnr.setBounds(220, 80, 150, 25);
        add(tfpnr);

        fetchButton = new JButton("Show Details");
        fetchButton.setBounds(380, 80, 120, 25);
        fetchButton.addActionListener(this);
        add(fetchButton);

        JLabel lblname = new JLabel("Name");
        lblname.setBounds(60, 130, 150, 25);
        lblname.setForeground(Color.WHITE);
        add(lblname);

        tfname = new JLabel();
        tfname.setBounds(220, 130, 150, 25);
        tfname.setForeground(Color.WHITE);
        add(tfname);

        JLabel lblcancel = new JLabel("Cancellation No");
        lblcancel.setBounds(60, 180, 150, 25);
        lblcancel.setForeground(Color.WHITE);
        add(lblcancel);

        cancellationno = new JLabel("" + random.nextInt(1000000));
        cancellationno.setBounds(220, 180, 150, 25);
        cancellationno.setForeground(Color.WHITE);
        add(cancellationno);

        JLabel lblcode = new JLabel("Flight Code");
        lblcode.setBounds(60, 230, 150, 25);
        lblcode.setForeground(Color.WHITE);
        add(lblcode);

        lblfcode = new JLabel();
        lblfcode.setBounds(220, 230, 150, 25);
        lblfcode.setForeground(Color.WHITE);
        add(lblfcode);

        JLabel lbldate = new JLabel("Date");
        lbldate.setBounds(60, 280, 150, 25);
        lbldate.setForeground(Color.WHITE);
        add(lbldate);

        lbldateofTravel = new JLabel();
        lbldateofTravel.setBounds(220, 280, 150, 25);
        lbldateofTravel.setForeground(Color.WHITE);
        add(lbldateofTravel);

        cancel = new JButton("Cancel Ticket");
        cancel.setBounds(220, 330, 150, 25);
        cancel.addActionListener(this);
        add(cancel);

        setSize(800, 450);
        setLocation(350, 150);
        ThemeManager.applyThemeToFrame(this);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == fetchButton) {

            String pnr = tfpnr.getText();

            try {
                Conn conn = new Conn();
                String query = "select * from reservation where PNR = '" + pnr + "'";
                ResultSet rs = conn.s.executeQuery(query);

                if (rs.next()) {
                    tfname.setText(rs.getString("name"));
                    lblfcode.setText(rs.getString("flightcode"));
                    lbldateofTravel.setText(rs.getString("ddate"));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid PNR Number");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (ae.getSource() == cancel) {
            String name = tfname.getText();
            String pnr = tfpnr.getText();
            String cancelno = cancellationno.getText();
            String fcode = lblfcode.getText();
            String date = lbldateofTravel.getText();

            try {
                Conn conn = new Conn();
                String query = "insert into cancel values('" + pnr + "','" + name + "','" + cancelno + "','" + fcode
                        + "','" + date + "')";
                conn.s.executeUpdate(query);
                conn.s.executeUpdate("delete from reservation where PNR = '" + pnr + "'");

                JOptionPane.showMessageDialog(null, "Ticket Cancelled Successfully");
                setVisible(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Cancel();
    }
}