package airlinemanagementsystem;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Source_Destination extends JFrame implements ActionListener {

    JComboBox<String> Source, Destination;
    JButton submit;
    JDateChooser dcdate;

    public Source_Destination() {

        setLayout(null);

        // HEADING
        JLabel heading = new JLabel("Search Flight");
        heading.setBounds(120, 20, 300, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        heading.setForeground(Color.WHITE);
        add(heading);

        // SOURCE
        JLabel flightSource = new JLabel("Source");
        flightSource.setBounds(50, 100, 100, 25);
        flightSource.setFont(new Font("Tahoma", Font.PLAIN, 16));
        flightSource.setForeground(Color.WHITE);
        add(flightSource);

        String citiesSource[] = {
                "Delhi",
                "Mumbai"
        };

        Source = new JComboBox<>(citiesSource);
        Source.setBounds(180, 100, 150, 30);
        add(Source);

        // DESTINATION
        JLabel flightDestination = new JLabel("Destination");
        flightDestination.setBounds(50, 160, 100, 25);
        flightDestination.setFont(new Font("Tahoma", Font.PLAIN, 16));
        flightDestination.setForeground(Color.WHITE);
        add(flightDestination);

        String citiesDesti[] = {
                "Mumbai",
                "Goa",
                "Chennai",
                "Amritsar",
                "Ayodhya"
        };

        Destination = new JComboBox<>(citiesDesti);
        Destination.setBounds(180, 160, 150, 30);
        add(Destination);

        // DATE
        JLabel date = new JLabel("Date");
        date.setBounds(50, 220, 100, 25);
        date.setFont(new Font("Tahoma", Font.PLAIN, 16));
        date.setForeground(Color.WHITE);
        add(date);

        dcdate = new JDateChooser();
        dcdate.setBounds(180, 220, 150, 30);
        add(dcdate);

        // BUTTON
        submit = new JButton("Search Flights");
        submit.setBounds(150, 300, 150, 38);
        submit.addActionListener(this);
        add(submit);

        // FRAME SETTINGS
        setTitle("Search Flight");
        setSize(450, 420);
        setLocation(500, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ThemeManager.applyThemeToFrame(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        String source      = (String) Source.getSelectedItem();
        String destination = (String) Destination.getSelectedItem();
        String ddate       = ((JTextField) dcdate.getDateEditor().getUiComponent()).getText();

        if (ddate == null || ddate.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a travel date.",
                    "Date Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if a flight exists for this route
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery(
                    "SELECT f_name FROM flight WHERE source = '" + source +
                    "' AND destination = '" + destination + "' LIMIT 1");
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "No flights available for " + source + " → " + destination,
                        "No Flights Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Open FlightInfo with the chosen route & date
        setVisible(false);
        new FlightInfo(source, destination, ddate);
    }

    public static void main(String[] args) {

        new Source_Destination();
    }
}