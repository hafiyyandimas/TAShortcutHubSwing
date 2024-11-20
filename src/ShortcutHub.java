import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;

public class ShortcutHub extends JFrame {

    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private HashMap<String, String> shortcuts;

    public ShortcutHub() {
        // Window setup
        setTitle("Shortcut Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Layout setup
        setLayout(new BorderLayout());

        // Initialize shortcuts
        shortcuts = new HashMap<>();

        // Create a grid panel to display the shortcuts
        gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        add(gridPanel, BorderLayout.CENTER);

        // Add "Add Shortcut" button
        JButton addButton = new JButton("+ Add Shortcut");
        addButton.addActionListener(e -> addShortcut());
        add(addButton, BorderLayout.SOUTH);

        // Load shortcuts (hardcoded for now)
        loadShortcutsToUI();
    }

    private void loadShortcutsToUI() {
        gridPanel.removeAll();
        shortcuts.forEach((name, path) -> {
            JButton button = new JButton(name);
            gridPanel.add(button);
        });
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void addShortcut() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            shortcuts.put(selectedFile.getName(), selectedFile.getAbsolutePath());
            loadShortcutsToUI();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ShortcutHub().setVisible(true);
        });
    }
}
