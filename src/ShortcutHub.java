import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShortcutHub extends JFrame {

    private JScrollPane scrollPane;
    private JPanel gridPanel;

    public ShortcutHub() {
        setTitle("Shortcut Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JButton addButton = new JButton("Add Shortcut");
        addButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Add shortcut functionality"));
        panel.add(addButton);

        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShortcutHub().setVisible(true));
    }
}
