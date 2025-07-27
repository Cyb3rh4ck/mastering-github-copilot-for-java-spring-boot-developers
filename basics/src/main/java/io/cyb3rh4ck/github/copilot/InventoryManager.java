package io.cyb3rh4ck.github.copilot;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

// InventoryManager class that uses a map and add, removes, list and update products
// Add @Slf4j and replace all System.out with log.info

@Slf4j
public class InventoryManager {

    private Map<Integer, Product> productMap = new HashMap<>();

    public void addProduct(Product product) {
        if (product == null) {
            log.warn("Cannot add null product to inventory");
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (productMap.containsKey(product.getId())) {
            log.warn("Product with ID {} already exists. Updating existing product.", product.getId());
        }
        productMap.put(product.getId(), product);
        log.info("Product added/updated: {}", product);
    }

    public boolean removeProduct(int productId) {
        Product removedProduct = productMap.remove(productId);
        if (removedProduct != null) {
            log.info("Product removed: {}", removedProduct);
            return true;
        } else {
            log.warn("Cannot remove product with ID {}. Product not found.", productId);
            return false;
        }
    }

    public void updateProduct(Product product) {
        if (product == null) {
            log.warn("Cannot update with null product");
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!productMap.containsKey(product.getId())) {
            log.warn("Product with ID {} does not exist. Adding as new product.", product.getId());
        }
        productMap.put(product.getId(), product);
        log.info("Product updated: {}", product);
    }

    //update product quantity given product id and new quantity
    public boolean updateProductQuantity(int productId, int newQuantity) {
        if (newQuantity < 0) {
            log.warn("Cannot set negative quantity {} for product ID {}", newQuantity, productId);
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        Product product = productMap.get(productId);
        if (product != null) {
            int oldQuantity = product.getQuantity();
            product.setQuantity(newQuantity);
            log.info("Product ID {} quantity updated from {} to {}", productId, oldQuantity, newQuantity);
            return true;
        } else {
            log.warn("Cannot update quantity. Product with ID {} not found.", productId);
            return false;
        }
    }

    // get product by id
    public Product getProduct(int productId) {
        return productMap.get(productId);
    }

    // list all products
    public java.util.Collection<Product> listProducts() {
        return productMap.values();
    }

    // check if product exists
    public boolean productExists(int productId) {
        return productMap.containsKey(productId);
    }

    // get total number of products
    public int getTotalProducts() {
        return productMap.size();
    }

    // get total inventory value (assuming products have a price - this is a placeholder)
    public int getTotalQuantity() {
        return productMap.values().stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    // find products with low stock
    public java.util.List<Product> getLowStockProducts(int threshold) {
        return productMap.values().stream()
                .filter(product -> product.getQuantity() <= threshold)
                .toList();
    }

    // clear all inventory
    public void clearInventory() {
        int size = productMap.size();
        productMap.clear();
        log.info("Inventory cleared. {} products removed.", size);
    }

    // main method for testing
    public static void main(String[] args) {
        InventoryManager inventoryManager = new InventoryManager();
        
        // Create some products
        Product product1 = new Product(1, "Laptop", 10);
        Product product2 = new Product(2, "Smartphone", 20);
        Product product3 = new Product(3, "Tablet", 2); // Low stock item
        
        // Add products to inventory
        inventoryManager.addProduct(product1);
        inventoryManager.addProduct(product2);
        inventoryManager.addProduct(product3);
        
        // Display inventory statistics
        log.info("Total products in inventory: {}", inventoryManager.getTotalProducts());
        log.info("Total quantity in inventory: {}", inventoryManager.getTotalQuantity());
        
        // List all products
        log.info("Products in inventory:");
        for (Product product : inventoryManager.listProducts()) {
            log.info(product.toString());
        }
        
        // Update product quantity
        boolean updated = inventoryManager.updateProductQuantity(1, 15);
        log.info("Quantity update successful: {}", updated);
        
        // Get and display updated product
        log.info("Updated product: {}", inventoryManager.getProduct(1));
        
        // Find low stock products
        java.util.List<Product> lowStockProducts = inventoryManager.getLowStockProducts(5);
        log.info("Low stock products (threshold=5): {}", lowStockProducts);
        
        // Try to remove a product
        boolean removed = inventoryManager.removeProduct(2);
        log.info("Product removal successful: {}", removed);
        
        // Try to remove non-existent product
        boolean removedNonExistent = inventoryManager.removeProduct(999);
        log.info("Non-existent product removal result: {}", removedNonExistent);
        
        log.info("Final inventory size: {}", inventoryManager.getTotalProducts());
    }

}
