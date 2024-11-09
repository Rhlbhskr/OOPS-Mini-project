// fund transfers
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;

    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        } else {
            System.out.println("Insufficient balance or invalid amount.");
            return false;
        }
    }

    public boolean transferFunds(BankAccount toAccount, double amount) {
        if (this.withdraw(amount)) {
            toAccount.deposit(amount);
            System.out.println("Transfer successful! Transferred " + amount + " to " + toAccount.getAccountHolder());
            return true;
        } else {
            System.out.println("Transfer failed. Insufficient balance.");
            return false;
        }
    }
}

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
        double transferAmount = scanner.nextDouble();

        // Perform the transfer
        if (transferAmount > 0) {
            System.out.println("\nAttempting to transfer $" + transferAmount + " from " + senderAccount.getAccountHolder()
                    + " to " + receiverAccount.getAccountHolder() + "...");
            senderAccount.transferFunds(receiverAccount, transferAmount);

            // Display final balances
            System.out.println("\nFinal Balances:");
            System.out.println(senderAccount.getAccountHolder() + "'s Balance: $" + senderAccount.getBalance());
            System.out.println(receiverAccount.getAccountHolder() + "'s Balance: $" + receiverAccount.getBalance());
        } else {
            System.out.println("Invalid transfer amount.");
        }
        
        scanner.close();
    }
}
