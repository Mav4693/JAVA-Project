package airlinemanagementsystem;
import javax.swing.*;

import net.proteanit.sql.DbUtils;

import java.awt.*;
import java.sql.*;
public class FlightInfo extends JFrame{
      
public FlightInfo(){

        setLayout(null);

        JTable table = new JTable();
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("select * from flight");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 0, 800, 500);
        add(jsp);
        setSize(800, 500);
        setLocation(400, 200);
        ThemeManager.applyThemeToFrame(this);
        setVisible(true);
}
public static void main( String [] args){
    new FlightInfo();

}
}
