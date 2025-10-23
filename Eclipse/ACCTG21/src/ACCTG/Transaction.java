package ACCTG;

public class Transaction {
    private String date;
    private String description;
    private String debitAccount;
    private String creditAccount;
    private double amount;

    public Transaction(String date, String description, String debitAccount, String creditAccount, double amount) {
        this.date = date;
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
    }

    // Getters
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getDebitAccount() { return debitAccount; }
    public String getCreditAccount() { return creditAccount; }
    public double getAmount() { return amount; }

    // Setters (if needed)
    public void setDate(String date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
    public void setDebitAccount(String debitAccount) { this.debitAccount = debitAccount; }
    public void setCreditAccount(String creditAccount) { this.creditAccount = creditAccount; }
    public void setAmount(double amount) { this.amount = amount; }
}
