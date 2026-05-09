package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddCustomer extends JFrame implements ActionListener {

    JTextField tfname, tfphone, tfaadhar, tfnationality, tfaddress;
    JRadioButton rbmale, rbfemale;

    // ── Layout constants ────────────────────────────────────────────────────
    private static final int LBL_X     = 60;    // label left edge
    private static final int FLD_X     = 220;   // field left edge
    private static final int FLD_W     = 200;   // field width
    private static final int ROW_H     = 28;    // field / label height
    private static final int ROW_GAP   = 55;    // vertical gap between rows
    private static final int START_Y   = 90;    // first row Y

    public AddCustomer() {

        setLayout(null);
        setTitle("Add Customer Details");

        // ── HEADING ──────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Add Customer Details");
        heading.setBounds(LBL_X, 20, 500, 42);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 30));
        heading.setForeground(new Color(0, 150, 255));   // accent blue
        add(heading);

        // ── helper to position a label ───────────────────────────────────────
        // Row 0 — Name
        int y = START_Y;
        JLabel lblname = fieldLabel("Name");
        lblname.setBounds(LBL_X, y, 150, ROW_H);
        add(lblname);
        tfname = styledField();
        tfname.setBounds(FLD_X, y, FLD_W, ROW_H);
        add(tfname);

        // Row 1 — Nationality
        y += ROW_GAP;
        JLabel lblnationality = fieldLabel("Nationality");
        lblnationality.setBounds(LBL_X, y, 150, ROW_H);
        add(lblnationality);
        tfnationality = styledField();
        tfnationality.setBounds(FLD_X, y, FLD_W, ROW_H);
        add(tfnationality);

        // Row 2 — Aadhar Number
        y += ROW_GAP;
        JLabel lblaadhar = fieldLabel("Aadhar Number");
        lblaadhar.setBounds(LBL_X, y, 150, ROW_H);
        add(lblaadhar);
        tfaadhar = styledField();
        tfaadhar.setBounds(FLD_X, y, FLD_W, ROW_H);
        add(tfaadhar);

        // Row 3 — Address
        y += ROW_GAP;
        JLabel lbladdress = fieldLabel("Address");
        lbladdress.setBounds(LBL_X, y, 150, ROW_H);
        add(lbladdress);
        tfaddress = styledField();
        tfaddress.setBounds(FLD_X, y, FLD_W, ROW_H);
        add(tfaddress);

        // Row 4 — Gender (radio buttons, properly spaced)
        y += ROW_GAP;
        JLabel lblgender = fieldLabel("Gender");
        lblgender.setBounds(LBL_X, y, 150, ROW_H);
        add(lblgender);

        ButtonGroup genderGroup = new ButtonGroup();

        rbmale = new JRadioButton("Male");
        rbmale.setBounds(FLD_X, y, 90, ROW_H);
        rbmale.setBackground(new Color(15, 15, 25));
        rbmale.setForeground(Color.WHITE);
        rbmale.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rbmale.setFocusPainted(false);
        add(rbmale);

        rbfemale = new JRadioButton("Female");
        rbfemale.setBounds(FLD_X + 100, y, 95, ROW_H);
        rbfemale.setBackground(new Color(15, 15, 25));
        rbfemale.setForeground(Color.WHITE);
        rbfemale.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rbfemale.setFocusPainted(false);
        add(rbfemale);

        genderGroup.add(rbmale);
        genderGroup.add(rbfemale);
        rbmale.setSelected(true);   // default selection

        // Row 5 — Phone
        y += ROW_GAP;
        JLabel lblphone = fieldLabel("Phone");
        lblphone.setBounds(LBL_X, y, 150, ROW_H);
        add(lblphone);
        tfphone = styledField();
        tfphone.setBounds(FLD_X, y, FLD_W, ROW_H);
        add(tfphone);

        // ── SAVE BUTTON — centred below all fields ───────────────────────────
        y += ROW_GAP + 10;   // a little extra breathing room
        JButton save = new JButton("SAVE CUSTOMER");
        save.setBounds(FLD_X, y, 200, 40);
        save.setBackground(new Color(0, 150, 255));
        save.setForeground(Color.WHITE);
        save.setFont(new Font("Segoe UI", Font.BOLD, 14));
        save.setFocusPainted(false);
        save.setCursor(new Cursor(Cursor.HAND_CURSOR));
        save.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        save.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { save.setBackground(new Color(0, 180, 255)); }
            public void mouseExited(MouseEvent e)  { save.setBackground(new Color(0, 150, 255)); }
        });
        save.addActionListener(this);
        add(save);

        // ── DECORATIVE IMAGE (right side) ────────────────────────────────────
        try {
            ImageIcon image = new ImageIcon(
                getClass().getResource("/airlinemanagementsystem/icons/ashokstambh.png")
            );
            Image img = image.getImage().getScaledInstance(280, 400, Image.SCALE_SMOOTH);
            JLabel lblimage = new JLabel(new ImageIcon(img));
            lblimage.setBounds(490, 70, 280, 400);
            lblimage.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 80), 2));
            add(lblimage);
        } catch (Exception e) {
            System.out.println("Image not found.");
        }

        setSize(860, 580);
        setLocation(300, 150);
        ThemeManager.applyThemeToFrame(this);
        setVisible(true);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        l.setForeground(new Color(180, 190, 210));
        return l;
    }

    private JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(25, 25, 45));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(new Color(0, 150, 255));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 80), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return tf;
    }

    // ── Action ───────────────────────────────────────────────────────────────

    public void actionPerformed(ActionEvent ae) {
        String name        = tfname.getText().trim();
        String nationality = tfnationality.getText().trim();
        String phone       = tfphone.getText().trim();
        String address     = tfaddress.getText().trim();
        String aadhar      = tfaadhar.getText().trim();
        String gender      = rbmale.isSelected() ? "Male" : "Female";

        if (name.isEmpty() || aadhar.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Name and Aadhar Number are required.",
                "Incomplete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Conn conn = new Conn();
            String query = "insert into passenger values('" + name + "','" + nationality
                    + "', '" + phone + "','" + address + "','" + aadhar + "','" + gender + "')";
            conn.s.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Customer Details Added Successfully");
            setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AddCustomer();
    }
}
