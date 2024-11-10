import java.util.*;

public class BankManagementSystem {
    private static Map<String, BankAccount> accounts = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Sample accounts
        accounts.put("12345", new BankAccount("12345", "Alice", 1000.00));
        accounts.put("67890", new BankAccount("67890", "Bob", 500.00));

        // Take sender account input
        System.out.print("Enter Sender Account Number: ");
        String senderAccountNumber = scanner.nextLine();
        BankAccount senderAccount = accounts.get(senderAccountNumber);

        if (senderAccount == null) {
            System.out.println("Sender account not found.");
            return;
        }

        // Take receiver account input
        System.out.print("Enter Receiver Account Number: ");
        String receiverAccountNumber = scanner.nextLine();
        BankAccount receiverAccount = accounts.get(receiverAccountNumber);

        if (receiverAccount == null) {
            System.out.println("Receiver account not found.");
            return;
        }

        // Take transfer amount input
        System.out.print("Enter Amount to Transfer: ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a numeric value.");
            return;
        }
        double transferAmount = scanner.nextDouble();

        // Check transfer amount and sufficient balance
        if (transferAmount <= 0) {
            System.out.println("Invalid transfer amount.");
        } else if (senderAccount.getBalance() < transferAmount) {
            System.out.println("Insufficient funds in sender's account.");
        } else {
            System.out.println("\nAttempting to transfer $" + transferAmount + " from " + senderAccount.getAccountHolder()
                    + " to " + receiverAccount.getAccountHolder() + "...");
            senderAccount.transferFunds(receiverAccount, transferAmount);

            // Display final balances
            System.out.println("\nFinal Balances:");
            System.out.println(senderAccount.getAccountHolder() + "'s Balance: $" + senderAccount.getBalance());
            System.out.println(receiverAccount.getAccountHolder() + "'s Balance: $" + receiverAccount.getBalance());
        }

        // Display transaction history for sender and receiver accounts
        System.out.println("\nTransaction History for " + senderAccount.getAccountHolder() + ":");
        senderAccount.displayTransactionHistory();

        System.out.println("\nTransaction History for " + receiverAccount.getAccountHolder() + ":");
        receiverAccount.displayTransactionHistory();

        scanner.close();
    }
}

// Transaction class to store transaction details
class Transaction {
    private Date date;
    private String type; // "Deposit", "Withdrawal", or "Transfer"
    private double amount;
    private String details;

    public Transaction(String type, double amount, String details) {
        this.date = new Date();
        this.type = type;
        this.amount = amount;
        this.details = details;
    }

    @Override
    public String toString() {
        return date + " - " + type + ": $" + amount + " (" + details + ")";
    }
}

// BankAccount class with transaction history
class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;
    private List<Transaction> transactionHistory;

    public BankAccount(String accountNumber, String accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public void transferFunds(BankAccount receiver, double amount) {
        if (balance >= amount) {
            this.balance -= amount;
            receiver.balance += amount;

            // Record transaction history
            transactionHistory.add(new Transaction("Transfer Out", amount, "To " + receiver.getAccountHolder()));
            receiver.transactionHistory.add(new Transaction("Transfer In", amount, "From " + this.getAccountHolder()));
        } else {
            System.out.println("Insufficient funds for transfer.");
        }
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add(new Transaction("Deposit", amount, "Deposit to account"));
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            transactionHistory.add(new Transaction("Withdrawal", amount, "Withdrawal from account"));
        } else {
            System.out.println("Insufficient funds for withdrawal.");
        }
    }

    public void displayTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }
}
