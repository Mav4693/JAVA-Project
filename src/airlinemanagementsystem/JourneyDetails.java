package airlinemanagementsystem;

import javax.swing.*;

import net.proteanit.sql.DbUtils;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class JourneyDetails extends JFrame implements ActionListener {
    JTable table;
    JTextField pnr;
    JButton show;

    public JourneyDetails() {

        setLayout(null);

        JLabel lblpnr = new JLabel("PNR Details");
        lblpnr.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblpnr.setBounds(50, 50, 100, 25);
        add(lblpnr);

        pnr = new JTextField();
        pnr.setBounds(160, 50, 100, 25);
        add(pnr);

        show = new JButton("Show Details");
        show.setBounds(290, 50, 120, 25);
        show.addActionListener(this);
        add(show);

        table = new JTable();

        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 800, 150);
        add(jsp);
        setSize(800, 600);
        setLocation(400, 150);

        // Decorative Image at the bottom
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/chakra.png"));
            Image i2 = i1.getImage().getScaledInstance(600, 250, Image.SCALE_SMOOTH);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel image = new JLabel(i3);
            image.setBounds(100, 250, 600, 250);
            add(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ThemeManager.applyThemeToFrame(this);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("select * from reservation where PNR = '" + pnr.getText() + "'");

            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "No Information Found");
                return;
            }
            table.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new JourneyDetails();

    }
}
