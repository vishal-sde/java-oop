package SimpleProjects;

import java.util.ArrayList;
import java.util.Scanner;

public class InventoryManage {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Store store = new Store();
        int choice;

        do {
            System.out.println("\n----- INVENTORY MANAGEMENT -----");
            System.out.println("1. Add Perishable Product");
            System.out.println("2. Add Non-Perishable Product");
            System.out.println("3. Show All Products");
            System.out.println("4. Add Stock");
            System.out.println("5. Sell Stock");
            System.out.println("6. Show Total Inventory Value");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            choice = in.nextInt();
            in.nextLine(); // clear buffer

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Product ID: ");
                    int id = in.nextInt();
                    in.nextLine();

                    System.out.print("Enter Product Name: ");
                    String name = in.nextLine();

                    System.out.print("Enter Price: ");
                    double price = in.nextDouble();

                    System.out.print("Enter Quantity: ");
                    int quantity = in.nextInt();

                    System.out.print("Enter Expiry Days: ");
                    int expiryDays = in.nextInt();

                    store.addProduct(new PerishableProduct(id, name, price, quantity, expiryDays));
                }

                case 2 -> {
                    System.out.print("Enter Product ID: ");
                    int id = in.nextInt();
                    in.nextLine();

                    System.out.print("Enter Product Name: ");
                    String name = in.nextLine();

                    System.out.print("Enter Price: ");
                    double price = in.nextDouble();

                    System.out.print("Enter Quantity: ");
                    int quantity = in.nextInt();

                    store.addProduct(new NonPerishableProduct(id, name, price, quantity));
                }

                case 3 -> store.showProducts();

                case 4 -> {
                    System.out.print("Enter Product ID: ");
                    int id = in.nextInt();

                    System.out.print("Enter Quantity to Add: ");
                    int qty = in.nextInt();

                    store.addStock(id, qty);
                }

                case 5 -> {
                    System.out.print("Enter Product ID: ");
                    int id = in.nextInt();

                    System.out.print("Enter Quantity to Sell: ");
                    int qty = in.nextInt();

                    store.sellStock(id, qty);
                }

                case 6 -> System.out.println("Total Inventory Value: ₹" + store.getTotalValue());

                case 7 -> System.out.println("Exiting...");

                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 7);

        in.close();
    }
}

class Store {
    private final ArrayList<Product> products = new ArrayList<>();

    void addProduct(Product product) {
        if (findProduct(product.getProductId()) != null) {
            System.out.println("Product already exists.");
            return;
        }

        products.add(product);
        System.out.println("Product added successfully.");
    }

    Product findProduct(int id) {
        for (Product product : products) {
            if (product.getProductId() == id) {
                return product;
            }
        }
        return null;
    }

    void addStock(int id, int quantity) {
        Product product = findProduct(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        product.addStock(quantity);
    }

    void sellStock(int id, int quantity) {
        Product product = findProduct(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        product.sellStock(quantity);
    }

    void showProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        for (Product product : products) {
            product.showDetails();
        }
    }

    double getTotalValue() {
        double total = 0;

        for (Product product : products) {
            total += product.calculateValue();
        }

        return total;
    }
}

abstract class Product {
    private final int productId;
    private final String productName;
    private final double price;
    protected int quantity;

    Product(int productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    int getProductId() {
        return productId;
    }

    double calculateValue() {
        return price * quantity;
    }

    void addStock(int amount) {
        if (amount <= 0) {
            System.out.println("Invalid stock amount.");
            return;
        }

        quantity += amount;
        System.out.println("Stock added successfully.");
    }

    void sellStock(int amount) {
        if (amount <= 0) {
            System.out.println("Invalid stock amount.");
            return;
        }

        if (amount > quantity) {
            System.out.println("Not enough stock available.");
            return;
        }

        quantity -= amount;
        System.out.println("Stock sold successfully.");
    }

    abstract String getType();

    void showDetails() {
        System.out.println("----------------------------------");
        System.out.println("Type      : " + getType());
        System.out.println("Product ID: " + productId);
        System.out.println("Name      : " + productName);
        System.out.println("Price     : ₹" + price);
        System.out.println("Quantity  : " + quantity);
        System.out.println("Value     : ₹" + calculateValue());
    }
}

class PerishableProduct extends Product {
    private final int expiryDays;

    PerishableProduct(int id, String name, double price, int quantity, int expiryDays) {
        super(id, name, price, quantity);
        this.expiryDays = expiryDays;
    }

    @Override
    String getType() {
        return "Perishable Product";
    }

    @Override
    void sellStock(int amount) {
        if (expiryDays <= 0) {
            System.out.println("Cannot sell expired product.");
            return;
        }

        super.sellStock(amount); // normal selling logic
    }

    @Override
    void showDetails() {
        super.showDetails();
        System.out.println("Expiry In : " + expiryDays + " days");
    }
}

class NonPerishableProduct extends Product {

    NonPerishableProduct(int id, String name, double price, int quantity) {
        super(id, name, price, quantity);
    }

    @Override
    String getType() {
        return "Non-Perishable Product";
    }
}
