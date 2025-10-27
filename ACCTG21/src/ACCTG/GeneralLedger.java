package ACCTG;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class GeneralLedger extends JPanel {
    public void updateLedger(String accountName, double amount) {
        // Update ledger table
    }



//public class GeneralLedger extends JPanel {
    private DefaultTableModel ledgerTableModel;
    private JTable ledgerTable;
    private JComboBox<String> accountDropdown;

    public GeneralLedger() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);

        // ===== UPPER BAR (Title + Account Selector + Search) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 150, 150)));

        // 🔹 Left side: Title
        JLabel title = new JLabel("General Ledger");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0, 150, 150));
        title.setBorder(BorderFactory.createEmptyBorder(3, 13, 3, 0));
        topPanel.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Detailed posting of transactions per account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 13, 4, 0));
        topPanel.add(subtitle, BorderLayout.SOUTH);

        // 🔍 Right side: Account dropdown + Search bar
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        rightPanel.setOpaque(false);

        JLabel accountLabel = new JLabel("Account:");
        accountDropdown = new JComboBox<>(new String[]{
                "Cash [ASSET]",
                "Accounts Payable [LIABILITY]",
                "Owner’s Equity [EQUITY]",
                "Service Revenue [INCOME]",
                "Rent Expense [EXPENSE]"
        });
        accountDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        accountDropdown.setPreferredSize(new Dimension(180, 26));
        
        rightPanel.add(accountLabel);
        rightPanel.add(accountDropdown);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== TABLE SECTION =====
        String[] columnNames = {"Date", "Description", "Debit", "Credit", "Amount", "Balance"};
        ledgerTableModel = new DefaultTableModel(columnNames, 0);
        ledgerTable = new JTable(ledgerTableModel);

        // 🔹 Table appearance
        ledgerTable.setShowGrid(false);
        ledgerTable.setIntercellSpacing(new Dimension(0, 0));
        ledgerTable.setBackground(Color.WHITE);
        ledgerTable.setForeground(Color.BLACK);
        ledgerTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ledgerTable.setRowHeight(25);
        ledgerTable.setFillsViewportHeight(true);

        // 🔹 Custom cell renderer (same as Transactions)
        ledgerTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel cell = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.BLACK);
                cell.setHorizontalAlignment(SwingConstants.LEFT);

                // Add borders — left border on first column
                if (column == 0) {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(0, 150, 150)));
                } else {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 150, 150)));
                }

                if (isSelected) {
                    cell.setBackground(new Color(220, 250, 250));
                }
                return cell;
            }
        });

        // 🔹 Header styling
        JTableHeader header = ledgerTable.getTableHeader();
        header.setBackground(new Color(0, 150, 150));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setOpaque(true);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel headerLabel = new JLabel(value.toString(), SwingConstants.CENTER);
                headerLabel.setOpaque(true);
                headerLabel.setBackground(new Color(0, 170, 170));
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

                // Left border on first column
                if (column == 0) {
                    headerLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(0, 150, 150)));
                } else {
                    headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 150, 150)));
                }
                return headerLabel;
            }
        });
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 150, 150)));

        // ===== ScrollPane with white margins =====
        JScrollPane scrollPane = new JScrollPane(ledgerTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.setOpaque(true);

        add(scrollPane, BorderLayout.CENTER);

        // 🔄 Dropdown listener (changes displayed account)
        accountDropdown.addActionListener(e -> refreshLedgerTable());
    }

    // 🔁 Refresh and filtering logic placeholders
    private void refreshLedgerTable() {
        ledgerTableModel.setRowCount(0);
        // You can replace this with your actual ledger data retrieval logic
        ledgerTableModel.addRow(new Object[]{"2025-10-01", "Opening Balance", "", "Cash", "1000.00", "1000.00"});
        ledgerTableModel.addRow(new Object[]{"2025-10-02", "Service Income", "Cash", "Service Revenue", "500.00", "1500.00"});
    }

    private void filterLedgerTable(String keyword) {
        // You can later replace this with filtering logic if ledger data is dynamic
        refreshLedgerTable(); // Placeholder behavior
    }
}

