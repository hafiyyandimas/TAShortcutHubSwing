import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

public class ShortcutHub extends JFrame {

    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private HashMap<String, String> shortcuts;

    private static final int CELL_WIDTH = 150;
    private static final int CELL_HEIGHT = 150;

    public ShortcutHub() {   //constructor buat bikin inisialisasi kode gui
        setTitle("Shortcut Hub"); //setter buat konfigurasi GUI
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

        applyModernScrollBars();

        add(scrollPane, BorderLayout.CENTER);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //THANK U INDIAN MAN
        } catch (Exception e) {
            // TODO: handle exception}
        }
    }

    private void applyModernScrollBars() {
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        JScrollBar horizontal = scrollPane.getHorizontalScrollBar();

        vertical.setUI(new ModernScrollBarUI());
        horizontal.setUI(new ModernScrollBarUI());
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

        Image image = ((ImageIcon) icon).getImage();
        BufferedImage resizedImage = resizeImage(image, 80, 80);
        iconLabel.setIcon(new ImageIcon(resizedImage));

        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);


        iconLabel.setPreferredSize(new Dimension(80, 80));


        iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "gif"));
                int result = fileChooser.showOpenDialog(panel);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Resize and update the icon
                    ImageIcon newIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    Image resizedNewIcon = newIcon.getImage();
                    BufferedImage finalResizedImage = resizeImage(resizedNewIcon, 80, 80);
                    iconLabel.setIcon(new ImageIcon(finalResizedImage));


                    shortcuts.put(name, selectedFile.getAbsolutePath());
                    saveShortcuts();
                }
            }
        });

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

    private BufferedImage resizeImage(Image originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
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

