package ACCTG;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GeneralJournal extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public GeneralJournal() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 45)); // Dark background

        // Header
        JLabel title = new JLabel("📘 General Journal");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Subtitle
        JLabel subtitle = new JLabel("Chronological debit/credit entries");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 12, 10, 10));
        add(subtitle, BorderLayout.BEFORE_FIRST_LINE);

        // Table columns
        String[] columns = {"Date", "Description", "Account", "Debit", "Credit"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        // Styling table
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(55, 55, 55));
        table.setGridColor(new Color(70, 70, 70));
        table.getTableHeader().setBackground(new Color(35, 35, 35));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(45, 45, 45));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom label
        JLabel status = new JLabel("Ready");
        status.setForeground(Color.GRAY);
        status.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(status, BorderLayout.SOUTH);
    }

    // Optional: method to add entries (for when you later connect transactions)
    public void addJournalEntry(String date, String description, String account, double debit, double credit) {
        model.addRow(new Object[]{date, description, account, debit, credit});
    }
}
