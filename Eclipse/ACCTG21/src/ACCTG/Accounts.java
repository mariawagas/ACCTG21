package ACCTG;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Accounts extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public Accounts() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 45)); // dark background

        // Header Label with emoji
        JLabel title = new JLabel("🧾 Accounts");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Account", "Type", "Balance"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
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

        // Load default accounts
        addDefaultAccounts();
    }

    private void addDefaultAccounts() {
        Object[][] defaultAccounts = {
            {"Cash", "ASSET", 0.00},
            {"Accounts Receivable", "ASSET", 0.00},
            {"Inventory", "ASSET", 0.00},
            {"Prepaid Expenses", "ASSET", 0.00},
            {"Equipment", "ASSET", 0.00},
            {"Accounts Payable", "LIABILITY", 0.00},
            {"Notes Payable", "LIABILITY", 0.00},
            {"Owner's Capital", "EQUITY", 0.00},
            {"Sales Revenue", "INCOME", 0.00},
            {"Service Revenue", "INCOME", 0.00},
            {"Cost of Goods Sold", "EXPENSE", 0.00},
            {"Rent Expense", "EXPENSE", 0.00},
            {"Salaries Expense", "EXPENSE", 0.00},
            {"Utilities Expense", "EXPENSE", 0.00}
        };

        for (Object[] row : defaultAccounts) {
            model.addRow(row);
        }
    }
}
