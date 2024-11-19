import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ShortcutHub extends JFrame {

    private JPanel gridPanel;
    private JScrollPane scrollPane;

    public ShortcutHub() {
        setTitle("Shortcut Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDarkTheme();

        gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        add(gridPanel, BorderLayout.CENTER);
        JButton addButton = new JButton("Add Shortcut");
        addButton.addActionListener(e -> addShortcut());
        gridPanel.add(addButton);
    }

    private void setDarkTheme() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("control", new Color(48, 48, 48));
            UIManager.put("nimbusBase", new Color(18, 30, 49));
            UIManager.put("nimbusLightBackground", new Color(48, 48, 48));
            UIManager.put("text", new Color(230, 230, 230));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addShortcut() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Added: " + selectedFile.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShortcutHub().setVisible(true));
    }
}
