package airlinemanagementsystem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class FlightInfo extends JFrame {

    // ── flight details (populated from DB) ──────────────────────────────────
    private String flightName = "", flightCode = "", depTime = "";
    private final String travelSource, travelDest, travelDate;

    // ── shared PNR for this entire booking session ───────────────────────────
    private final String pnr;

    // ── track confirmed passengers ───────────────────────────────────────────
    private final ArrayList<String[]> confirmedPassengers = new ArrayList<>();
    // each entry: { ticketId, name, gender, age, aadhar }

    // ── UI references ────────────────────────────────────────────────────────
    private JLabel lblFlightName, lblFlightCode, lblDepTime;
    private JPanel passengersContainer;
    private JScrollPane scrollPane;
    private JButton btnBook;
    private int passengerCount = 0;

    // ── colours (match ThemeManager) ────────────────────────────────────────
    private static final Color BG       = new Color(15, 15, 25);
    private static final Color CARD_BG  = new Color(25, 25, 45);
    private static final Color ACCENT   = new Color(0, 150, 255);
    private static final Color HOVER    = new Color(0, 180, 255);
    private static final Color BORDER   = new Color(50, 50, 80);
    private static final Color SUCCESS  = new Color(0, 200, 100);
    private static final Color TEXT     = Color.WHITE;
    private static final Color SUBTEXT  = new Color(180, 190, 210);

    // ─────────────────────────────────────────────────────────────────────────
    public FlightInfo(String source, String destination, String date) {
        this.travelSource = source;
        this.travelDest   = destination;
        this.travelDate   = date;
        this.pnr          = "PNR-" + String.format("%06d", new Random().nextInt(1000000));

        setTitle("Flight Booking — " + pnr);
        setLayout(null);
        setSize(950, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG);

        buildUI();
        loadFlightFromDB();
        setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    private void buildUI() {

        // ── PAGE TITLE ───────────────────────────────────────────────────────
        JLabel title = new JLabel("✈  FLIGHT BOOKING");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(ACCENT);
        title.setBounds(30, 18, 500, 40);
        add(title);

        JLabel pnrLabel = new JLabel("PNR:  " + pnr);
        pnrLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnrLabel.setForeground(SUCCESS);
        pnrLabel.setBounds(30, 58, 400, 22);
        add(pnrLabel);

        // ── FLIGHT INFO CARD ─────────────────────────────────────────────────
        JPanel flightCard = createCard(30, 90, 888, 160);
        add(flightCard);

        JLabel secTitle1 = sectionTitle("FLIGHT INFORMATION");
        secTitle1.setBounds(15, 10, 400, 22);
        flightCard.add(secTitle1);

        JSeparator sep1 = new JSeparator();
        sep1.setBounds(15, 36, 855, 1);
        sep1.setForeground(BORDER);
        flightCard.add(sep1);

        // Row 1 — Flight Name & Source
        addCardRow(flightCard, "Flight Name",  30,  55); lblFlightName = addCardValue(flightCard, 180,  55);
        addCardRow(flightCard, "Source",       30,  90); addCardValue2(flightCard, travelSource,   180,  90);

        // Row 2 — Flight Code & Destination
        addCardRow(flightCard, "Flight Code",  460,  55); lblFlightCode = addCardValue(flightCard, 610,  55);
        addCardRow(flightCard, "Destination",  460,  90); addCardValue2(flightCard, travelDest,    610,  90);

        // Row 3 — Departure & Date
        addCardRow(flightCard, "Departure",    30, 125); lblDepTime   = addCardValue(flightCard, 180, 125);
        addCardRow(flightCard, "Date",         460, 125); addCardValue2(flightCard, travelDate,    610, 125);

        // ── PASSENGERS SECTION HEADER ────────────────────────────────────────
        JLabel secTitle2 = sectionTitle("PASSENGERS");
        secTitle2.setBounds(30, 268, 300, 22);
        add(secTitle2);

        JButton btnAdd = accentButton("Add Passenger");
        btnAdd.setBounds(720, 262, 200, 32);
        btnAdd.addActionListener(e -> addPassengerPanel());
        add(btnAdd);

        // ── SCROLLABLE PASSENGER AREA ────────────────────────────────────────
        passengersContainer = new JPanel();
        passengersContainer.setLayout(new BoxLayout(passengersContainer, BoxLayout.Y_AXIS));
        passengersContainer.setBackground(BG);

        scrollPane = new JScrollPane(passengersContainer);
        scrollPane.setBounds(30, 298, 888, 340);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getViewport().setBackground(BG);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        // ── BOOK FLIGHT BUTTON ───────────────────────────────────────────────
        btnBook = new JButton("BOOK FLIGHT");
        btnBook.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBook.setBackground(SUCCESS);
        btnBook.setForeground(Color.WHITE);
        btnBook.setFocusPainted(false);
        btnBook.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBook.setBounds(330, 650, 290, 44);
        btnBook.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnBook.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnBook.setBackground(new Color(0, 230, 120)); }
            public void mouseExited(MouseEvent e)  { btnBook.setBackground(SUCCESS); }
        });
        btnBook.addActionListener(e -> bookFlight());
        add(btnBook);

        // hint label
        JLabel hint = new JLabel("Add and confirm all passengers before booking");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(SUBTEXT);
        hint.setBounds(0, 700, 950, 20);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        add(hint);
    }

    // ─────────────────────────────────────────────────────────────────────────
    private void loadFlightFromDB() {
        try {
            Conn conn = new Conn();
            String q = "SELECT f_name, f_code, dep_time FROM flight WHERE source = '"
                    + travelSource + "' AND destination = '" + travelDest + "' LIMIT 1";
            ResultSet rs = conn.s.executeQuery(q);
            if (rs.next()) {
                flightName = rs.getString("f_name");
                flightCode = rs.getString("f_code");
                depTime    = rs.getString("dep_time");
                lblFlightName.setText(flightName);
                lblFlightCode.setText(flightCode);
                lblDepTime.setText(depTime);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No flight found for " + travelSource + " → " + travelDest,
                        "No Flight", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    private void addPassengerPanel() {
        passengerCount++;
        int idx = passengerCount;

        // ── outer card panel ─────────────────────────────────────────────────
        JPanel card = new JPanel(null);
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, ACCENT),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setPreferredSize(new Dimension(860, 120));

        // title
        JLabel lbl = new JLabel("Passenger " + idx);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(ACCENT);
        lbl.setBounds(0, 2, 200, 20);
        card.add(lbl);

        // status indicator (updated on confirm)
        JLabel statusLbl = new JLabel("● Not confirmed");
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLbl.setForeground(new Color(255, 100, 100));
        statusLbl.setBounds(700, 2, 160, 20);
        card.add(statusLbl);

        // ── fields row ──────────────────────────────────────────────────────
        // Name
        JLabel lName = fieldLabel("Name *");
        lName.setBounds(0, 30, 70, 22);
        card.add(lName);

        JTextField tfName = styledField();
        tfName.setBounds(70, 28, 150, 28);
        card.add(tfName);

        // Gender
        JLabel lGender = fieldLabel("Gender *");
        lGender.setBounds(235, 30, 60, 22);
        card.add(lGender);

        JComboBox<String> cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cbGender.setBounds(300, 28, 100, 28);
        styleCombo(cbGender);
        card.add(cbGender);

        // Age
        JLabel lAge = fieldLabel("Age *");
        lAge.setBounds(415, 30, 40, 22);
        card.add(lAge);

        JTextField tfAge = styledField();
        tfAge.setBounds(460, 28, 60, 28);
        card.add(tfAge);

        // Aadhar
        JLabel lAadhar = fieldLabel("Aadhar No *");
        lAadhar.setBounds(535, 30, 90, 22);
        card.add(lAadhar);

        JTextField tfAadhar = styledField();
        tfAadhar.setBounds(628, 28, 155, 28);
        card.add(tfAadhar);

        // ── Confirm button — right-aligned ──────────────────────────────────
        JButton btnConfirm = accentButton("Confirm Passenger");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConfirm.setBounds(608, 72, 200, 32);
        card.add(btnConfirm);

        // ticket label — left of confirm button
        JLabel ticketLbl = new JLabel("");
        ticketLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ticketLbl.setForeground(SUCCESS);
        ticketLbl.setBounds(0, 78, 600, 22);
        card.add(ticketLbl);

        // ── confirm action ───────────────────────────────────────────────────
        btnConfirm.addActionListener(e -> {
            String name   = tfName.getText().trim();
            String gender = (String) cbGender.getSelectedItem();
            String age    = tfAge.getText().trim();
            String aadhar = tfAadhar.getText().trim();

            if (name.isEmpty() || age.isEmpty() || aadhar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields for Passenger " + idx,
                        "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!age.matches("\\d{1,3}")) {
                JOptionPane.showMessageDialog(this, "Age must be a number.", "Invalid Age", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String ticketId = "TIC-" + String.format("%06d", new Random().nextInt(1000000));

            try {
                Conn conn = new Conn();
                // Insert into reservation table
                // Columns: PNR, TICKET, aadhar, name, flightname, flightcode, src, des, ddate, gender, age
                // We insert what we know; nationality field left as empty string if not available
                String query = "INSERT INTO reservation (PNR, TICKET, aadhar, name, nationality, "
                        + "flightname, flightcode, src, des, ddate) VALUES ('"
                        + pnr + "', '" + ticketId + "', '" + aadhar + "', '" + name + "', '', '"
                        + flightName + "', '" + flightCode + "', '" + travelSource + "', '"
                        + travelDest + "', '" + travelDate + "')";
                conn.s.executeUpdate(query);

                // Mark confirmed
                confirmedPassengers.add(new String[]{ticketId, name, gender, age, aadhar});

                // Update UI
                statusLbl.setText("✔ Confirmed");
                statusLbl.setForeground(SUCCESS);
                ticketLbl.setText("Ticket ID: " + ticketId);
                btnConfirm.setEnabled(false);
                btnConfirm.setBackground(new Color(50, 50, 70));
                tfName.setEditable(false);
                cbGender.setEnabled(false);
                tfAge.setEditable(false);
                tfAadhar.setEditable(false);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        passengersContainer.add(card);
        passengersContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        passengersContainer.revalidate();
        passengersContainer.repaint();

        // Auto-scroll to the new panel
        SwingUtilities.invokeLater(() -> {
            JScrollBar vsb = scrollPane.getVerticalScrollBar();
            vsb.setValue(vsb.getMaximum());
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    private void bookFlight() {
        if (confirmedPassengers.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please add and confirm at least one passenger before booking.",
                    "No Passengers", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ── Build popup ──────────────────────────────────────────────────────
        JDialog popup = new JDialog(this, "✈ Booking Confirmed!", true);
        popup.setSize(850, 560);
        popup.setLocationRelativeTo(this);
        popup.setLayout(null);
        popup.getContentPane().setBackground(BG);

        // ── Header ──────────────────────────────────────────────────────────
        JLabel hTitle = new JLabel("✈  BOOKING CONFIRMED");
        hTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        hTitle.setForeground(SUCCESS);
        hTitle.setBounds(20, 15, 500, 36);
        popup.add(hTitle);

        // ── Flight Summary Box ───────────────────────────────────────────────
        JPanel summaryBox = new JPanel(null);
        summaryBox.setBounds(20, 65, 810, 130);
        summaryBox.setBackground(CARD_BG);
        summaryBox.setBorder(BorderFactory.createLineBorder(BORDER));
        popup.add(summaryBox);

        // Row 1
        addPopupRow(summaryBox, "PNR",         pnr,         20,  15, ACCENT);
        addPopupRow(summaryBox, "Flight",       flightName,  20,  42, TEXT);
        addPopupRow(summaryBox, "Flight Code",  flightCode,  20,  69, TEXT);

        addPopupRow(summaryBox, "Source",       travelSource, 430, 15,  TEXT);
        addPopupRow(summaryBox, "Destination",  travelDest,   430, 42,  TEXT);
        addPopupRow(summaryBox, "Date",         travelDate,   430, 69,  TEXT);
        addPopupRow(summaryBox, "Departure",    depTime,      430, 96,  TEXT);

        // ── Passenger Table ───────────────────────────────────────────────────
        JLabel pTitle = new JLabel("PASSENGERS");
        pTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pTitle.setForeground(ACCENT);
        pTitle.setBounds(20, 210, 200, 22);
        popup.add(pTitle);

        String[] cols = {"Ticket ID", "Name", "Gender", "Age", "Aadhar No"};
        Object[][] data = new Object[confirmedPassengers.size()][5];
        for (int i = 0; i < confirmedPassengers.size(); i++) {
            data[i] = confirmedPassengers.get(i);
        }

        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setBackground(CARD_BG);
        table.setForeground(TEXT);
        table.setGridColor(BORDER);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setBackground(new Color(20, 20, 40));
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionBackground(new Color(40, 60, 100));
        table.setSelectionForeground(TEXT);

        JScrollPane tsp = new JScrollPane(table);
        tsp.setBounds(20, 238, 810, 230);
        tsp.setBorder(BorderFactory.createLineBorder(BORDER));
        tsp.getViewport().setBackground(CARD_BG);
        popup.add(tsp);

        // ── Close button ─────────────────────────────────────────────────────
        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClose.setBackground(ACCENT);
        btnClose.setForeground(TEXT);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setBounds(360, 488, 120, 35);
        btnClose.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnClose.addActionListener(e -> {
            popup.dispose();
            dispose(); // close FlightInfo window after booking
        });
        popup.add(btnClose);

        popup.setVisible(true);
    }

    // ── UI Helper Methods ────────────────────────────────────────────────────

    private JPanel createCard(int x, int y, int w, int h) {
        JPanel p = new JPanel(null);
        p.setBounds(x, y, w, h);
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createLineBorder(BORDER));
        return p;
    }

    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(ACCENT);
        return l;
    }

    private void addCardRow(JPanel p, String label, int x, int y) {
        JLabel l = new JLabel(label + ":");
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(SUBTEXT);
        l.setBounds(x, y, 150, 22);
        p.add(l);
    }

    private JLabel addCardValue(JPanel p, int x, int y) {
        JLabel l = new JLabel("—");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(TEXT);
        l.setBounds(x, y, 250, 22);
        p.add(l);
        return l;
    }

    private void addCardValue2(JPanel p, String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(TEXT);
        l.setBounds(x, y, 250, 22);
        p.add(l);
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(SUBTEXT);
        return l;
    }

    private JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(18, 18, 35));
        tf.setForeground(TEXT);
        tf.setCaretColor(ACCENT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        return tf;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setBackground(new Color(18, 18, 35));
        cb.setForeground(TEXT);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    private JButton accentButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT);
        btn.setForeground(TEXT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (btn.isEnabled()) btn.setBackground(HOVER); }
            public void mouseExited(MouseEvent e)  { if (btn.isEnabled()) btn.setBackground(ACCENT); }
        });
        return btn;
    }

    private void addPopupRow(JPanel p, String label, String value, int x, int y, Color valColor) {
        JLabel lbl = new JLabel(label + ":  ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(SUBTEXT);
        lbl.setBounds(x, y, 110, 22);
        p.add(lbl);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        val.setForeground(valColor);
        val.setBounds(x + 115, y, 280, 22);
        p.add(val);
    }

    public static void main(String[] args) {
        new FlightInfo("Delhi", "Mumbai", "15/05/2026");
    }
}
