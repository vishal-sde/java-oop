package SimpleProjects;

import java.util.ArrayList;
import java.util.Scanner;



public class RestaurantOrderManage {
    public static void main(String[] args) {
        Restaurant myRestaurant = new Restaurant();
        Scanner scan = new Scanner(System.in);

        // Initial Menu Setup
        myRestaurant.addItem(new FoodItem("Biriyani", 250, 50));
        myRestaurant.addItem(new FoodItem("Dosa", 40, 100));
        myRestaurant.addItem(new FoodItem("Parota", 15, 200));

        int choice = 0;
        do {
            System.out.println("\n--- Welcome to Hotel ---");
            System.out.println("1. Show Menu\n2. Place Order\n3. Cancel Order\n4. Exit");
            System.out.print("Enter Choice: ");

            if (!scan.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                scan.next(); // Clear invalid input
                continue;
            }

            choice = scan.nextInt();
            scan.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> myRestaurant.showMenu();
                case 2 -> {
                    System.out.print("Enter item name to order: ");
                    myRestaurant.bookOrder(scan.nextLine());
                }
                case 3 -> {
                    System.out.print("Enter item name to cancel: ");
                    myRestaurant.cancelOrder(scan.nextLine());
                }
                case 4 -> System.out.println("Thank you for visiting!");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 4);
        scan.close();
    }
}

class Restaurant {
    private final ArrayList<FoodItem> menu = new ArrayList<>();

    void addItem(FoodItem item) {
        menu.add(item);
    }

    void showMenu() {
        System.out.println("\n--- Current Menu ---");
        for (FoodItem item : menu) {
            System.out.println(item);
        }
    }

    void bookOrder(String orderName) {
        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(orderName)) {
                item.orderItem();
                return;
            }
        }
        System.out.println("Sorry '" + orderName + "' is not available.");
    }

    void cancelOrder(String orderName) {
        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(orderName)) {
                item.cancelItem();
                return;
            }
        }
        System.out.println("Could not find '" + orderName + "' in our records.");
    }
}

class FoodItem {
    private final String name;
    private final int price;
    private int stock;

    FoodItem(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() { return name; }

    void orderItem() {
        if (stock > 0) {
            stock--;
            System.out.println("Success: " + name + " ordered!");
        } else {
            System.out.println("Sorry, " + name + " is out of stock.");
        }
    }

    void cancelItem() {
        stock++;
        System.out.println("Success: " + name + " order cancelled.");
    }

    @Override
    public String toString() {
        return String.format("%-10s | Price: ₹%-5d | Stock: %-4d [%s]",
                name, price, stock, (stock > 0 ? "Available" : "OUT OF STOCK"));
    }
}

class Customer{
    String name;
    int customerId;

    Customer(String name,int customerId){
        this.name = name;
        this.customerId = customerId;
    }
}

