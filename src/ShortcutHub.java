import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class ShortcutHub extends JFrame {

    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private HashMap<String, String> shortcuts;

    private static final int CELL_WIDTH = 150;
    private static final int CELL_HEIGHT = 150;

    public ShortcutHub() {
        setTitle("Shortcut Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setLookAndFeel();
        setResizable(false);

        shortcuts = loadShortcuts();

        gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        loadShortcutsToUI();

        scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadShortcutsToUI() {
        gridPanel.removeAll();
        shortcuts.forEach((name, path) -> {
            gridPanel.add(createShortcutPanel(name, path));
        });
        gridPanel.add(createAddShortcutButton());
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createShortcutPanel(String name, String path) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(48, 48, 48));
        panel.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));

        JLabel iconLabel = new JLabel();
        File file = new File(path);
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
        iconLabel.setIcon(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(48, 48, 48));
        JButton runButton = new JButton("Run");
        JButton deleteButton = new JButton("Delete");

        runButton.addActionListener(e -> runShortcut(path));
        deleteButton.addActionListener(e -> {
            shortcuts.remove(name);
            saveShortcuts();
            loadShortcutsToUI();
        });

        buttonPanel.add(runButton);
        buttonPanel.add(deleteButton);

        panel.add(iconLabel, BorderLayout.CENTER);
        panel.add(nameLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAddShortcutButton() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(48, 48, 48));

        JButton addButton = new JButton("+");
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        addButton.setHorizontalAlignment(SwingConstants.CENTER);
        addButton.addActionListener(e -> addShortcut()); 

        panel.add(addButton, BorderLayout.CENTER);
        return panel;
    }

    private void addShortcut() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();
            String filePath = file.getAbsolutePath();

            shortcuts.put(fileName, filePath);
            saveShortcuts();
            loadShortcutsToUI();
        }
    }

    private void runShortcut(String path) {
        try {
            Desktop.getDesktop().open(new File(path));
            JOptionPane.showMessageDialog(this, "Running: " + path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to run the file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveShortcuts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("shortcuts.dat"))) {
            oos.writeObject(shortcuts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> loadShortcuts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("shortcuts.dat"))) {
            return (HashMap<String, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void setLookAndFeel() {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ShortcutHub().setVisible(true);
        });
    }
}
