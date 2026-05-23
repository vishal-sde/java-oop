package SimpleProjects;

import java.util.ArrayList;
import java.util.Scanner;


public class BankManagementSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner input = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n--- Welcome to Our Bank ---");
            System.out.println("1. Create Savings Account");
            System.out.println("2. Create Current Account");
            System.out.println("3. Show All Accounts");
            System.out.println("4. Deposit");
            System.out.println("5. Withdraw");
            System.out.println("6. Exit");
            System.out.print("Enter Choice: ");

            try {
                choice = Integer.parseInt(input.nextLine().trim());

                switch (choice) {
                    case 1, 2 -> {
                        System.out.print("Enter Name: ");
                        String name = input.nextLine().trim();
                        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");

                        System.out.print("Enter Account Number: ");
                        String accNo = input.nextLine().trim();
                        if (!accNo.matches("\\d+")) throw new IllegalArgumentException("Account number must be numeric.");

                        System.out.print("Enter Initial Balance: ");
                        double balance = Double.parseDouble(input.nextLine().trim());
                        if (balance < 0) throw new IllegalArgumentException("Initial balance cannot be negative.");

                        if (choice == 1)
                            bank.addAccount(new SavingsAccount(name, accNo, balance));
                        else
                            bank.addAccount(new CurrentAccount(name, accNo, balance));
                    }

                    case 3 -> bank.showAccounts();

                    case 4 -> {
                        System.out.print("Enter Account Number: ");
                        String accNo = input.nextLine().trim();
                        System.out.print("Enter Amount to Deposit: ");
                        double amt = Double.parseDouble(input.nextLine().trim());
                        bank.depositAmount(accNo, amt);
                    }

                    case 5 -> {
                        System.out.print("Enter Account Number: ");
                        String accNo = input.nextLine().trim();
                        System.out.print("Enter Amount to Withdraw: ");
                        double amt = Double.parseDouble(input.nextLine().trim());
                        bank.withdrawAmount(accNo, amt);
                    }

                    case 6 -> System.out.println("Exiting... Thank you!");

                    default -> System.out.println("Invalid choice! Enter 1-6.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                choice = 0; // prevent accidental exit
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
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
        System.out.println("Account created: " + account.getAccountNumber());
    }

    Account findAccount(String number) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(number)) return acc;
        }
        return null;
    }

    void showAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        accounts.forEach(Account::showDetails);
    }

    void depositAmount(String number, double amount) {
        Account acc = findAccount(number);
        if (acc != null) {
            try { acc.depositMoney(amount); }
            catch (IllegalArgumentException e) { System.out.println("Deposit failed: " + e.getMessage()); }
        } else {
            System.out.println("Account not found.");
        }
    }

    void withdrawAmount(String number, double amount) {
        Account acc = findAccount(number);
        if (acc != null) {
            try { acc.withdrawMoney(amount); }
            catch (IllegalArgumentException | InsufficientFundsException e) {
                System.out.println("Withdrawal failed: " + e.getMessage());
            }
        } else {
            System.out.println("Account not found.");
        }
    }
}


class InsufficientFundsException extends Exception {
    InsufficientFundsException(double balance, double requested) {
        super(String.format("Requested ₹%.2f but available balance is ₹%.2f", requested, balance));
    }
}



abstract class Account {
    private final String accountNumber;
    private final String name;
    private double balance;

    Account(String name, String accountNumber, double balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getName()          { return name; }
    public double getBalance()       { return balance; }


    protected void setBalance(double balance) {
        this.balance = balance;
    }

    void depositMoney(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive.");
        balance += amount;
        System.out.printf("Deposited ₹%.2f | New Balance: ₹%.2f%n", amount, balance);
    }

    abstract void withdrawMoney(double amount) throws InsufficientFundsException;

    abstract String getAccountType();

    void showDetails() {
        System.out.printf("[%s] Name: %s | Acc No: %s | Balance: ₹%.2f%n",
                getAccountType(), name, accountNumber, balance);
    }
}



class SavingsAccount extends Account {
    private static final double MIN_BALANCE = 500.0;
    private static final int MAX_WITHDRAWALS_PER_DAY = 3;
    private int withdrawalsToday = 0;

    SavingsAccount(String name, String number, double balance) {
        super(name, number, balance);
        if (balance < MIN_BALANCE)
            throw new IllegalArgumentException(
                    "Savings account requires minimum balance of ₹" + MIN_BALANCE);
    }

    @Override
    public void withdrawMoney(double amount) throws InsufficientFundsException {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive.");
        if (withdrawalsToday >= MAX_WITHDRAWALS_PER_DAY)
            throw new IllegalArgumentException("Daily withdrawal limit reached (max " + MAX_WITHDRAWALS_PER_DAY + ").");
        if (getBalance() - amount < MIN_BALANCE)
            throw new InsufficientFundsException(getBalance() - MIN_BALANCE, amount);

        setBalance(getBalance() - amount);
        withdrawalsToday++;
        System.out.printf("Withdrawn ₹%.2f | New Balance: ₹%.2f%n", amount, getBalance());
    }

    @Override
    public String getAccountType() { return "Savings"; }
}



class CurrentAccount extends Account {
    private final double overdraftLimit;

    CurrentAccount(String name, String number, double balance) {
        this(name, number, balance, 10000.0); // default overdraft
    }

    CurrentAccount(String name, String number, double balance, double overdraftLimit) {
        super(name, number, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdrawMoney(double amount) throws InsufficientFundsException {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive.");
        if (amount > getBalance() + overdraftLimit)
            throw new InsufficientFundsException(getBalance() + overdraftLimit, amount);

        setBalance(getBalance() - amount);
        System.out.printf("Withdrawn ₹%.2f | New Balance: ₹%.2f%n", amount, getBalance());
        if (getBalance() < 0)
            System.out.printf("⚠ Overdraft active. Overdraft used: ₹%.2f%n", Math.abs(getBalance()));
    }

    @Override
    public String getAccountType() { return "Current"; }

    @Override
    void showDetails() {
        super.showDetails();
        System.out.printf("   Overdraft Limit: ₹%.2f%n", overdraftLimit);
    }
}