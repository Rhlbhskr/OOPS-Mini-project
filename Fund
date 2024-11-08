// fund transfers
// BankAccount.java

public class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;

    // Constructor
    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    // Deposit method
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    // Withdraw method
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        } else {
            System.out.println("Insufficient balance or invalid amount.");
            return false;
        }
    }

    // Transfer method
    public boolean transferFunds(BankAccount toAccount, double amount) {
        if (this.withdraw(amount)) {
            toAccount.deposit(amount);
            System.out.println("Transfer successful. Transferred " + amount + " to " + toAccount.getAccountHolder());
            return true;
        } else {
            System.out.println("Transfer failed. Insufficient balance.");
            return false;
        }
    }
}

// Main.java

public class Main {
    public static void main(String[] args) {
        // Create two bank accounts
        BankAccount account1 = new BankAccount("12345", "Alice", 1000.00);
        BankAccount account2 = new BankAccount("67890", "Bob", 500.00);

        // Display initial balances
        System.out.println("Initial Balance:");
        System.out.println("Alice's Balance: $" + account1.getBalance());
        System.out.println("Bob's Balance: $" + account2.getBalance());

        // Transfer funds from Alice to Bob
        double transferAmount = 200.00;
        System.out.println("\nAttempting to transfer $" + transferAmount + " from Alice to Bob...");
        boolean success = account1.transferFunds(account2, transferAmount);

        // Display final balances
        System.out.println("\nFinal Balance:");
        System.out.println("Alice's Balance: $" + account1.getBalance());
        System.out.println("Bob's Balance: $" + account2.getBalance());
    }
}
