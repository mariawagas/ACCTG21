package ACCTG;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class GeneralJournal extends JPanel {
    private JTable journalTable;
    private DefaultTableModel journalModel;
    private final Color LINE_COLOR = new Color(0, 150, 150);
    private final Color HEADER_COLOR = new Color(0, 170, 170);

    public GeneralJournal() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);

     // ===== HEADER BAR =====
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 150, 150)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // spacing from edges

        // 🔹 LEFT SIDE (Title + Subtitle)
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("General Journal");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0, 150, 150));

        JLabel subtitle = new JLabel("Chronological record of all journal entries");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        // 🔹 RIGHT SIDE (Print Button)
        ImageIcon printIcon = new ImageIcon(getClass().getResource("/icons/printer.png"));
        Image scaledPrintImage = printIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        RoundedButton printButton = new RoundedButton(" Print", new ImageIcon(scaledPrintImage));
        printButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        printButton.setForeground(Color.WHITE);
        printButton.setBackground(new Color(0, 170, 170));
        printButton.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printButton.setFocusable(false);

        // ===== ADD TO TOP PANEL =====

        // Left (title + subtitle)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        topPanel.add(titlePanel, gbc);

        // Right (print button, parallel aligned)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        topPanel.add(printButton, gbc);

        add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"Date", "Description", "Account", "Debit", "Credit"};
        journalModel = new DefaultTableModel(columns, 0);
        journalTable = createStyledTable(journalModel);

        JScrollPane scrollPane = new JScrollPane(journalTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
     // ===== WHITE MARGIN WRAPPER AROUND TABLE =====
        JPanel marginPanel = new JPanel(new BorderLayout());
        marginPanel.setBackground(Color.WHITE);
        marginPanel.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16)); // top, left, bottom, right
        marginPanel.add(scrollPane, BorderLayout.CENTER);

        add(marginPanel, BorderLayout.CENTER);
    }

    // ===== Add new entry method (IMPORTANT) =====
    public void addEntry(String accountName, double amount) {
        // Just a sample: you can replace with your actual data
        journalModel.addRow(new Object[]{
            "2025-10-27",  // Sample date
            "Transaction recorded", 
            accountName, 
            String.format("%.2f", amount),
            "" // leave credit empty for now
        });
    }

    // ===== TABLE DESIGN =====
    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);

        // keep grid but we'll also add custom borders for a consistent look
        table.setShowGrid(true);
        table.setGridColor(LINE_COLOR);
        table.setIntercellSpacing(new Dimension(1, 1)); // 1px grid gaps
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);

        // ===== CUSTOM CELL RENDERER =====
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel cell = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                cell.setHorizontalAlignment(SwingConstants.LEFT);
                cell.setOpaque(true);

                // For the leftmost column, draw a left vertical line as well
                if (column == 0) {
                    // top, left, bottom, right
                    cell.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, LINE_COLOR));
                } else {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, LINE_COLOR));
                }

                cell.setBackground(isSelected ? new Color(220, 250, 250) : Color.WHITE);
                return cell;
            }
        });

        // ===== HEADER STYLE =====
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel headerLabel = new JLabel(value == null ? "" : value.toString(), SwingConstants.CENTER);
                headerLabel.setOpaque(true);
                headerLabel.setBackground(HEADER_COLOR);
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

                // add left border for the first header cell so the top-left vertical line is present
                if (column == 0) {
                    headerLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, LINE_COLOR));
                } else {
                    headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, LINE_COLOR));
                }
                return headerLabel;
            }
        });

        return table;
    }
}