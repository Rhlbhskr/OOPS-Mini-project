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

        // Take transfer amount input with error handling
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

        scanner.close();
    }
}

// BankAccount class with basic structure
class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;

    public BankAccount(String accountNumber, String accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
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
        } else {
            System.out.println("Insufficient funds for transfer.");
        }
    }
}
