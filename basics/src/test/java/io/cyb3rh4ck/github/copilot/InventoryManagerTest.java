package io.cyb3rh4ck.github.copilot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryManagerTest {

    private InventoryManager inventoryManager;
    private Product laptop;
    private Product smartphone;
    private Product tablet;

    @BeforeEach
    void setUp() {
        inventoryManager = new InventoryManager();
        laptop = new Product(1, "Laptop", 10);
        smartphone = new Product(2, "Smartphone", 20);
        tablet = new Product(3, "Tablet", 2);
    }

    @Nested
    @DisplayName("Add Product Tests")
    class AddProductTests {

        @Test
        @DisplayName("Should add product successfully")
        void testAddProduct() {
            inventoryManager.addProduct(laptop);
            
            assertEquals(1, inventoryManager.getTotalProducts());
            assertTrue(inventoryManager.productExists(1));
            assertEquals(laptop, inventoryManager.getProduct(1));
        }

        @Test
        @DisplayName("Should throw exception when adding null product")
        void testAddNullProduct() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, 
                () -> inventoryManager.addProduct(null)
            );
            assertEquals("Product cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should update existing product when adding with same ID")
        void testAddExistingProduct() {
            inventoryManager.addProduct(laptop);
            Product updatedLaptop = new Product(1, "Gaming Laptop", 15);
            
            inventoryManager.addProduct(updatedLaptop);
            
            assertEquals(1, inventoryManager.getTotalProducts());
            assertEquals("Gaming Laptop", inventoryManager.getProduct(1).getName());
            assertEquals(15, inventoryManager.getProduct(1).getQuantity());
        }
    }

    @Nested
    @DisplayName("Remove Product Tests")
    class RemoveProductTests {

        @Test
        @DisplayName("Should remove existing product")
        void testRemoveExistingProduct() {
            inventoryManager.addProduct(laptop);
            
            boolean removed = inventoryManager.removeProduct(1);
            
            assertTrue(removed);
            assertEquals(0, inventoryManager.getTotalProducts());
            assertFalse(inventoryManager.productExists(1));
            assertNull(inventoryManager.getProduct(1));
        }

        @Test
        @DisplayName("Should return false when removing non-existent product")
        void testRemoveNonExistentProduct() {
            boolean removed = inventoryManager.removeProduct(999);
            
            assertFalse(removed);
            assertEquals(0, inventoryManager.getTotalProducts());
        }
    }

    @Nested
    @DisplayName("Update Product Tests")
    class UpdateProductTests {

        @Test
        @DisplayName("Should update existing product")
        void testUpdateExistingProduct() {
            inventoryManager.addProduct(laptop);
            Product updatedLaptop = new Product(1, "Updated Laptop", 25);
            
            inventoryManager.updateProduct(updatedLaptop);
            
            assertEquals(1, inventoryManager.getTotalProducts());
            assertEquals("Updated Laptop", inventoryManager.getProduct(1).getName());
            assertEquals(25, inventoryManager.getProduct(1).getQuantity());
        }

        @Test
        @DisplayName("Should add product when updating non-existent product")
        void testUpdateNonExistentProduct() {
            inventoryManager.updateProduct(laptop);
            
            assertEquals(1, inventoryManager.getTotalProducts());
            assertTrue(inventoryManager.productExists(1));
        }

        @Test
        @DisplayName("Should throw exception when updating with null product")
        void testUpdateWithNullProduct() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryManager.updateProduct(null)
            );
            assertEquals("Product cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Update Quantity Tests")
    class UpdateQuantityTests {

        @Test
        @DisplayName("Should update quantity of existing product")
        void testUpdateProductQuantity() {
            inventoryManager.addProduct(laptop);
            
            boolean updated = inventoryManager.updateProductQuantity(1, 15);
            
            assertTrue(updated);
            assertEquals(15, inventoryManager.getProduct(1).getQuantity());
        }

        @Test
        @DisplayName("Should return false when updating quantity of non-existent product")
        void testUpdateQuantityNonExistentProduct() {
            boolean updated = inventoryManager.updateProductQuantity(999, 10);
            
            assertFalse(updated);
        }

        @Test
        @DisplayName("Should throw exception when setting negative quantity")
        void testUpdateNegativeQuantity() {
            inventoryManager.addProduct(laptop);
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryManager.updateProductQuantity(1, -5)
            );
            assertEquals("Quantity cannot be negative", exception.getMessage());
        }

        @Test
        @DisplayName("Should allow setting quantity to zero")
        void testUpdateQuantityToZero() {
            inventoryManager.addProduct(laptop);
            
            boolean updated = inventoryManager.updateProductQuantity(1, 0);
            
            assertTrue(updated);
            assertEquals(0, inventoryManager.getProduct(1).getQuantity());
        }
    }

    @Nested
    @DisplayName("Query Tests")
    class QueryTests {

        @BeforeEach
        void setUpProducts() {
            inventoryManager.addProduct(laptop);
            inventoryManager.addProduct(smartphone);
            inventoryManager.addProduct(tablet);
        }

        @Test
        @DisplayName("Should return correct product by ID")
        void testGetProduct() {
            Product retrievedProduct = inventoryManager.getProduct(1);
            
            assertNotNull(retrievedProduct);
            assertEquals("Laptop", retrievedProduct.getName());
            assertEquals(10, retrievedProduct.getQuantity());
        }

        @Test
        @DisplayName("Should return null for non-existent product")
        void testGetNonExistentProduct() {
            Product product = inventoryManager.getProduct(999);
            
            assertNull(product);
        }

        @Test
        @DisplayName("Should check if product exists")
        void testProductExists() {
            assertTrue(inventoryManager.productExists(1));
            assertTrue(inventoryManager.productExists(2));
            assertFalse(inventoryManager.productExists(999));
        }

        @Test
        @DisplayName("Should return correct total number of products")
        void testGetTotalProducts() {
            assertEquals(3, inventoryManager.getTotalProducts());
        }

        @Test
        @DisplayName("Should return correct total quantity")
        void testGetTotalQuantity() {
            // laptop(10) + smartphone(20) + tablet(2) = 32
            assertEquals(32, inventoryManager.getTotalQuantity());
        }

        @Test
        @DisplayName("Should list all products")
        void testListProducts() {
            Collection<Product> products = inventoryManager.listProducts();
            
            assertEquals(3, products.size());
            assertTrue(products.contains(laptop));
            assertTrue(products.contains(smartphone));
            assertTrue(products.contains(tablet));
        }
    }

    @Nested
    @DisplayName("Low Stock Tests")
    class LowStockTests {

        @BeforeEach
        void setUpProducts() {
            inventoryManager.addProduct(laptop);      // quantity: 10
            inventoryManager.addProduct(smartphone);  // quantity: 20
            inventoryManager.addProduct(tablet);      // quantity: 2
        }

        @Test
        @DisplayName("Should find products with low stock")
        void testGetLowStockProducts() {
            List<Product> lowStockProducts = inventoryManager.getLowStockProducts(5);
            
            assertEquals(1, lowStockProducts.size());
            assertEquals(tablet, lowStockProducts.get(0));
        }

        @Test
        @DisplayName("Should find multiple products with low stock")
        void testGetMultipleLowStockProducts() {
            List<Product> lowStockProducts = inventoryManager.getLowStockProducts(15);
            
            assertEquals(2, lowStockProducts.size());
            assertTrue(lowStockProducts.contains(laptop));
            assertTrue(lowStockProducts.contains(tablet));
        }

        @Test
        @DisplayName("Should return empty list when no products have low stock")
        void testNoLowStockProducts() {
            List<Product> lowStockProducts = inventoryManager.getLowStockProducts(0);
            
            assertTrue(lowStockProducts.isEmpty());
        }

        @Test
        @DisplayName("Should include products with exact threshold quantity")
        void testLowStockWithExactThreshold() {
            List<Product> lowStockProducts = inventoryManager.getLowStockProducts(2);
            
            assertEquals(1, lowStockProducts.size());
            assertEquals(tablet, lowStockProducts.get(0));
        }
    }

    @Nested
    @DisplayName("Clear Inventory Tests")
    class ClearInventoryTests {

        @Test
        @DisplayName("Should clear all products from inventory")
        void testClearInventory() {
            inventoryManager.addProduct(laptop);
            inventoryManager.addProduct(smartphone);
            assertEquals(2, inventoryManager.getTotalProducts());
            
            inventoryManager.clearInventory();
            
            assertEquals(0, inventoryManager.getTotalProducts());
            assertEquals(0, inventoryManager.getTotalQuantity());
            assertTrue(inventoryManager.listProducts().isEmpty());
        }

        @Test
        @DisplayName("Should handle clearing empty inventory")
        void testClearEmptyInventory() {
            assertEquals(0, inventoryManager.getTotalProducts());
            
            inventoryManager.clearInventory();
            
            assertEquals(0, inventoryManager.getTotalProducts());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complex inventory operations")
        void testComplexInventoryOperations() {
            // Add products
            inventoryManager.addProduct(laptop);
            inventoryManager.addProduct(smartphone);
            inventoryManager.addProduct(tablet);
            assertEquals(3, inventoryManager.getTotalProducts());
            assertEquals(32, inventoryManager.getTotalQuantity());

            // Update quantities
            assertTrue(inventoryManager.updateProductQuantity(1, 5));
            assertTrue(inventoryManager.updateProductQuantity(2, 25));
            assertEquals(32, inventoryManager.getTotalQuantity()); // 5 + 25 + 2 = 32

            // Find low stock (threshold = 10)
            List<Product> lowStock = inventoryManager.getLowStockProducts(10);
            assertEquals(2, lowStock.size()); // laptop(5) and tablet(2)

            // Remove one product
            assertTrue(inventoryManager.removeProduct(3));
            assertEquals(2, inventoryManager.getTotalProducts());
            assertEquals(30, inventoryManager.getTotalQuantity()); // 5 + 25 = 30

            // Verify final state
            assertTrue(inventoryManager.productExists(1));
            assertTrue(inventoryManager.productExists(2));
            assertFalse(inventoryManager.productExists(3));
        }
    }
}
