import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;

public class ShortcutHub extends JFrame {

    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private HashMap<String, String> shortcuts; // Store shortcut name and path

    public ShortcutHub() {

        setTitle("Shortcut Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        setDarkTheme();


        shortcuts = new HashMap<>();


        gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        add(gridPanel, BorderLayout.CENTER);


        JButton addButton = new JButton("Add Shortcut");
        addButton.addActionListener(e -> addShortcut());
        add(addButton, BorderLayout.SOUTH);


        loadShortcutsToUI();
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


    private void loadShortcutsToUI() {
        gridPanel.removeAll();  // Remove all existing components in the grid


        shortcuts.forEach((name, path) -> {

            JButton button = new JButton(name);
            button.setToolTipText(path);
            button.addActionListener(e -> openShortcut(path));
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
            String filePath = selectedFile.getAbsolutePath();
            shortcuts.put(selectedFile.getName(), filePath);
            loadShortcutsToUI();
            JOptionPane.showMessageDialog(this, "Added: " + selectedFile.getAbsolutePath());
        }
    }


    private void openShortcut(String path) {
        try {
            Desktop.getDesktop().open(new File(path));  // Open the file associated with the shortcut
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to open the file: " + path, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new ShortcutHub().setVisible(true));
    }
}
