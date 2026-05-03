package airlinemanagementsystem;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThemeManager {
    // Premium Color Palette
    public static final Color BACKGROUND_COLOR = new Color(15, 15, 25); // Deep Midnight
    public static final Color COMPONENT_BG = new Color(25, 25, 45);    // Navy Slate
    public static final Color FOREGROUND_COLOR = Color.WHITE;
    public static final Color ACCENT_COLOR = new Color(0, 150, 255);   // Electric Blue
    public static final Color HOVER_COLOR = new Color(0, 180, 255);    // Bright Blue
    public static final Color BORDER_COLOR = new Color(50, 50, 80);    // Muted Navy

    public static void applyTheme(Container container) {
        container.setBackground(BACKGROUND_COLOR);
        
        for (Component c : container.getComponents()) {
            if (c instanceof JLabel) {
                styleLabel((JLabel) c);
            } else if (c instanceof JTextField || c instanceof JPasswordField) {
                styleTextField((JComponent) c);
            } else if (c instanceof JButton) {
                styleButton((JButton) c);
            } else if (c instanceof JRadioButton || c instanceof JCheckBox) {
                styleToggle((JComponent) c);
            } else if (c instanceof JPanel) {
                applyTheme((Container) c);
            } else if (c instanceof JScrollPane) {
                styleScrollPane((JScrollPane) c);
            } else if (c instanceof JTable) {
                styleTable((JTable) c);
            }
        }
    }

    private static void styleLabel(JLabel lbl) {
        lbl.setForeground(FOREGROUND_COLOR);
    }

    private static void styleTextField(JComponent c) {
        c.setBackground(COMPONENT_BG);
        c.setForeground(Color.WHITE);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (c instanceof javax.swing.text.JTextComponent) {
            ((javax.swing.text.JTextComponent) c).setCaretColor(ACCENT_COLOR);
            ((javax.swing.text.JTextComponent) c).setMargin(new Insets(5, 10, 5, 10));
        }
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private static void styleButton(JButton btn) {
        btn.setBackground(ACCENT_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(HOVER_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ACCENT_COLOR);
            }
        });
    }

    private static void styleToggle(JComponent c) {
        c.setBackground(BACKGROUND_COLOR);
        c.setForeground(FOREGROUND_COLOR);
        c.setFocusable(false);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private static void styleScrollPane(JScrollPane jsp) {
        jsp.setBackground(BACKGROUND_COLOR);
        jsp.getViewport().setBackground(BACKGROUND_COLOR);
        jsp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        applyTheme(jsp.getViewport());
    }

    private static void styleTable(JTable table) {
        table.setBackground(COMPONENT_BG);
        table.setForeground(FOREGROUND_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(40, 60, 100));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        table.getTableHeader().setBackground(new Color(20, 20, 40));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
    }

    public static void applyThemeToFrame(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        applyTheme(frame.getContentPane());
        
        JMenuBar menuBar = frame.getJMenuBar();
        if (menuBar != null) {
            styleMenuBar(menuBar);
        }
    }

    private static void styleMenuBar(JMenuBar menuBar) {
        menuBar.setBackground(new Color(20, 20, 35));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            styleMenu(menuBar.getMenu(i));
        }
    }

    private static void styleMenu(JMenu menu) {
        menu.setForeground(FOREGROUND_COLOR);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menu.getPopupMenu().setBackground(new Color(25, 25, 45));
        menu.getPopupMenu().setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setBackground(new Color(25, 25, 45));
                item.setForeground(FOREGROUND_COLOR);
                item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                item.setMargin(new Insets(8, 15, 8, 15));
                if (item instanceof JMenu) {
                    styleMenu((JMenu) item);
                }
            }
        }
    }
}
