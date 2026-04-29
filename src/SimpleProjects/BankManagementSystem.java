package SimpleProjects;

import java.util.ArrayList;
import java.util.Scanner;

public class BankManagementSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner input = new Scanner(System.in);
        int choice=0;

        do {
            System.out.println("\n--- Welcome to Our Bank ---");
            System.out.println("1. Create Savings Account");
            System.out.println("2. Create Current Account");
            System.out.println("3. Show All Accounts");
            System.out.println("4. Deposit");
            System.out.println("5. Withdraw");
            System.out.println("6. Exit");
            System.out.print("Enter Choice: ");

            if (!input.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                input.next();
                continue;
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1, 2 -> {
                    System.out.print("Enter Name: ");
                    String name = input.nextLine();

                    System.out.print("Enter Account Number: ");
                    int number = input.nextInt();

                    System.out.print("Enter Initial Balance: ");
                    double balance = input.nextDouble();

                    if (choice == 1)
                        bank.addAccount(new SavingsAccount(name, number, balance));
                    else
                        bank.addAccount(new CurrentAccount(name, number, balance));
                }

                case 3 -> bank.showAccounts();

                case 4 -> {
                    System.out.print("Enter Account Number: ");
                    int acc = input.nextInt();
                    System.out.print("Enter Amount to Deposit: ");
                    double amt = input.nextDouble();
                    bank.depositAmount(acc, amt);
                }

                case 5 -> {
                    System.out.print("Enter Account Number: ");
                    int acc = input.nextInt();
                    System.out.print("Enter Amount to Withdraw: ");
                    double amt = input.nextDouble();
                    bank.withdrawAmount(acc, amt);
                }

                case 6 -> System.out.println("Exiting... Thank you!");

                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 6);

        input.close();
    }
}

class Bank {
    private final ArrayList<Account> accounts = new ArrayList<>();

    void addAccount(Account account) {
        if (findAccount(account.getAccountNumber()) != null) {
            System.out.println("Account number already exists.");
            return;
        }
        accounts.add(account);
        System.out.println("Account created successfully!");
    }

    Account findAccount(int number) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == number) return acc;
        }
        return null;
    }

    void showAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (Account acc : accounts) {
            acc.showDetails();
        }
    }

    void depositAmount(int number, double amount) {
        Account acc = findAccount(number);
        if (acc != null) acc.depositMoney(amount);
        else System.out.println("Account not found.");
    }

    void withdrawAmount(int number, double amount) {
        Account acc = findAccount(number);
        if (acc != null) acc.withdrawMoney(amount);
        else System.out.println("Account not found.");
    }
}

class Account {
    private final int accountNumber;
    private final String name;
    protected double balance;

    Account(String name, int accountNumber, double balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = Math.max(balance, 0);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    void depositMoney(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid deposit amount.");
            return;
        }

        balance += amount;
        System.out.println("Deposit successful.");
    }

    void withdrawMoney(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
            return;
        }

        if (amount > balance) {
            System.out.println("Insufficient funds.");
            return;
        }

        balance -= amount;
        System.out.println("Withdrawal successful.");
    }

    void showDetails() {
        System.out.println(getClass().getSimpleName() +
                " | Name: " + name +
                " | Acc No: " + accountNumber +
                " | Balance: ₹" + balance);
    }
}

class SavingsAccount extends Account {
    SavingsAccount(String name, int number, double balance) {
        super(name, number, balance);
    }
}

class CurrentAccount extends Account {
    private final double overdraftLimit = 1000;

    CurrentAccount(String name, int number, double balance) {
        super(name, number, balance);
    }

    @Override
    void withdrawMoney(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
            return;
        }

        if (amount > balance + overdraftLimit) {
            System.out.println("Overdraft limit exceeded.");
            return;
        }

        balance -= amount;
        System.out.println("Withdrawal successful.");
    }
}
