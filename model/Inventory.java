package kee.wkeec482.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The type Inventory.
 * @author williamkee
 */
public class Inventory {
    /**
     * Observable list holding all the parts in inventory.
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    /**
     * Observable list holding all the products in inventory.
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Add part.
     *
     * @param newPart the new part
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Add product.
     *
     * @param newProduct the new product
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Lookup part.
     *
     * @param partId the part id
     * @return the part
     */
    public static Part lookupPart(int partId) {
        ObservableList<Part> parts = Inventory.getAllParts();
        for (Part part : parts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * Lookup product.
     *
     * @param productId the product id
     * @return the product
     */
    public static Product lookupProduct(int productId) {
        ObservableList<Product> products = Inventory.getAllProducts();
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Lookup part observable list.
     *
     * @param partName the part name
     * @return the observable list
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> searchResults = FXCollections.observableArrayList();

        for(Part part: allParts) {
            if(part.getName().contains(partName)) {
                searchResults.add(part);
            }
        }

        return searchResults;
    }

    /**
     * Lookup product observable list.
     *
     * @param productName the product name
     * @return the observable list
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for(Product product: allProducts) {
            if(product.getName().contains(productName)) {
                searchResults.add(product);
            }
        }

        return searchResults;
    }

    /**
     * Update part.
     *
     * @param index        the index
     * @param selectedPart the selected part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Update product.
     *
     * @param index           the index
     * @param selectedProduct the selected product
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Delete part boolean.
     *
     * @param selectedPart the selected part
     * @return the boolean
     */
    public static boolean deletePart(Part selectedPart) {
        if(allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Delete product boolean.
     *
     * @param selectedProduct the selected product
     * @return the boolean
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if(allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Gets all parts.
     *
     * @return the all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
