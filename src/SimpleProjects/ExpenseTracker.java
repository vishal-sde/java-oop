package SimpleProjects;

import java.util.ArrayList;
import java.util.Scanner;

public class ExpenseTracker {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Wallet wallet = new Wallet();
        int choice = 0;

        do {
            System.out.println("\n---- Expense Tracker ----");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. Show All Transactions");
            System.out.println("4. Show Current Balance");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            if (!input.hasNextInt()) {
                System.out.println("Enter a valid number.");
                input.next();
                continue;
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1, 2 -> {
                    System.out.print("Enter Transaction ID: ");
                    int id = input.nextInt();
                    input.nextLine();

                    System.out.print("Enter Title: ");
                    String title = input.nextLine();

                    System.out.print("Enter Amount: ");
                    double amount = input.nextDouble();

                    if (choice == 1) {
                        wallet.addTransaction(new Income(id, title, amount));
                    } else {
                        wallet.addTransaction(new Expense(id, title, amount));
                    }
                }

                case 3 -> wallet.showTransactions();

                case 4 -> wallet.showBalance();

                case 5 -> System.out.println("Exiting...");

                default -> System.out.println("Invalid choice, try again.");
            }

        } while (choice != 5);

        input.close();
    }
}

class Wallet {
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private double balance = 0;

    void addTransaction(Transaction transaction) {
        if (!transaction.apply(this)) {
            return;
        }

        transactions.add(transaction);
        System.out.println("Transaction added successfully.");
    }

    void showTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n---- All Transactions ----");
        for (Transaction t : transactions) {
            t.showDetails();
        }
    }

    void showBalance() {
        System.out.println("Current Balance: ₹" + balance);
    }

    double getBalance() {
        return balance;
    }

    void addToBalance(double amount) {
        balance += amount;
    }

    void deductFromBalance(double amount) {
        balance -= amount;
    }
}

abstract class Transaction {
    private final int transactionId;
    private final String title;
    protected final double amount;

    Transaction(int transactionId, String title, double amount) {
        this.transactionId = transactionId;
        this.title = title;
        this.amount = amount;
    }

    abstract boolean apply(Wallet wallet);

    abstract String getType();

    void showDetails() {
        System.out.println(
                getType() +
                        " | ID: " + transactionId +
                        " | Title: " + title +
                        " | Amount: ₹" + amount
        );
    }
}

class Income extends Transaction {
    Income(int id, String title, double amount) {
        super(id, title, amount);
    }

    @Override
    boolean apply(Wallet wallet) {
        if (amount <= 0) {
            System.out.println("Income amount must be greater than 0.");
            return false;
        }

        wallet.addToBalance(amount);
        return true;
    }

    @Override
    String getType() {
        return "INCOME";
    }
}

class Expense extends Transaction {
    Expense(int id, String title, double amount) {
        super(id, title, amount);
    }

    @Override
    boolean apply(Wallet wallet) {
        if (amount <= 0) {
            System.out.println("Expense amount must be greater than 0.");
            return false;
        }

        if (amount > wallet.getBalance()) {
            System.out.println("Insufficient balance for this expense.");
            return false;
        }

        wallet.deductFromBalance(amount);
        return true;
    }

    @Override
    String getType() {
        return "EXPENSE";
    }
}
