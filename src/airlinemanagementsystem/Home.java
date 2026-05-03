package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Home extends JFrame implements ActionListener {

    public Home() {

        setLayout(null);

        JLabel image = new JLabel();
        ImageIcon i = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/airplane.gif"));
        image.setIcon(i);
        image.setBounds(400, 220, 800, 500); // Further decreased size and centered
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setVerticalAlignment(SwingConstants.CENTER);
        image.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        add(image);

        // Professional Smooth Marquee Banner
        class MarqueePanel extends JPanel {
            private String text = "AIR INDIA WELCOMES YOU - EXPLORE THE WORLD WITH US - BOOK YOUR DREAM JOURNEY TODAY - EXPERIENCE LUXURY IN THE SKIES";
            private int x = 1600;

            public MarqueePanel() {
                setLayout(null);
                setOpaque(false);
                setBounds(0, 80, 1600, 120); // Shifted 5px down from 40
            }

            @Override
            protected void paintComponent(Graphics g) {
                // Manually draw the semi-transparent background
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Draw Banner Background
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);

                // Draw Text with Fancy Font
                g2.setFont(new Font("Segoe UI Semibold", Font.ITALIC | Font.BOLD, 48));
                g2.setColor(Color.WHITE);
                g2.drawString(text, x, 75);
            }

            public void step() {
                x -= 3;
                if (x < -3000)
                    x = 1600; // Adjusted for larger text
                repaint();
            }
        }

        MarqueePanel bannerPanel = new MarqueePanel();
        add(bannerPanel);

        ImageIcon logoIcon = new ImageIcon(
                ClassLoader.getSystemResource("airlinemanagementsystem/icons/new_airindia_logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        JLabel topLogo = new JLabel(new ImageIcon(scaledLogo));
        topLogo.setBounds(0, 15, 1600, 45); // Occupies the top space
        topLogo.setHorizontalAlignment(SwingConstants.CENTER);
        add(topLogo);

        Timer marqueeTimer = new Timer(15, e -> bannerPanel.step());
        marqueeTimer.start();

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu details = new JMenu("Details");
        menubar.add(details);

        JMenuItem flightDetails = new JMenuItem("Flight Details");
        flightDetails.addActionListener(this);
        details.add(flightDetails);

        JMenuItem customerDetails = new JMenuItem("Add Customer Details");
        customerDetails.addActionListener(this);
        details.add(customerDetails);

        JMenuItem bookFlight = new JMenuItem("Book Flight");
        bookFlight.addActionListener(this);
        details.add(bookFlight);

        JMenuItem journeyDetails = new JMenuItem("Journey Details");
        journeyDetails.addActionListener(this);
        details.add(journeyDetails);

        JMenuItem ticketCancellation = new JMenuItem("Cancel Ticket");
        ticketCancellation.addActionListener(this);

        details.add(ticketCancellation);

        JMenu ticket = new JMenu("Ticket");
        menubar.add(ticket);

        JMenuItem boardingPass = new JMenuItem("Boarding Pass");
        boardingPass.addActionListener(this);
        ticket.add(boardingPass);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        ThemeManager.applyThemeToFrame(this);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        String text = ae.getActionCommand();
        if (text.equals("Add Customer Details")) {
            new AddCustomer();
        } else if (text.equals("Flight Details")) {
            new FlightInfo();
        } else if (text.equals("Book Flight")) {
            new BookFlight();
        } else if (text.equals("Journey Details")) {
            new JourneyDetails();
        } else if (text.equals("Cancel Ticket")) {
            new Cancel();
        } else if (text.equals("Boarding Pass")) {
            new BoardingPass();
        }
    }

    public static void main(String[] args) {
        new Home();
    }
}