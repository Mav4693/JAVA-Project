package airlinemanagementsystem;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.*;

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
        add(heading);

        // SOURCE
        JLabel flightSource = new JLabel("Source");
        flightSource.setBounds(50, 100, 100, 25);
        flightSource.setFont(new Font("Tahoma", Font.PLAIN, 16));
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
        add(date);

        dcdate = new JDateChooser();
        dcdate.setBounds(180, 220, 150, 30);
        add(dcdate);

        // BUTTON
        submit = new JButton("Search");
        submit.setBounds(150, 300, 120, 35);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);

        submit.addActionListener(this);

        add(submit);

        // FRAME SETTINGS
        setTitle("Flight Search");

        setSize(450, 450);

        setLocation(500, 200);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        String source = (String) Source.getSelectedItem();

        String destination = (String) Destination.getSelectedItem();

        String ddate = ((JTextField) dcdate.getDateEditor()
                .getUiComponent()).getText();

        // DIALOG BOX
        JOptionPane.showMessageDialog(
                null,
                "Source : " + source +
                        "\nDestination : " + destination +
                        "\nDate : " + ddate);

        // CLOSE CURRENT WINDOW
        setVisible(false);

        // OPEN FLIGHT INFO WINDOW
        new FlightInfo();
    }

    public static void main(String[] args) {

        new Source_Destination();
    }
}