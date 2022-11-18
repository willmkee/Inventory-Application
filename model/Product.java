package kee.wkeec482.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The type Product.
 * @author williamkee
 */
public class Product {
    /**
     * Holds the associated parts.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * The product id
     */
    private int id;
    /**
     * The product name.
     */
    private String name;
    /**
     * The price of the product
     */
    private double price;
    /**
     * The amount in stock
     */
    private int stock;
    /**
     * The minimum amount
     */
    private int min;
    /**
     * The maximum amount
     */
    private int max;

    /**
     * Instantiates a new Product.
     *
     * @param id    the id
     * @param name  the name
     * @param price the price
     * @param stock the stock
     * @param min   the min
     * @param max   the max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    /* Setters */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets stock.
     *
     * @param stock the stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Sets min.
     *
     * @param min the min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets max.
     *
     * @param max the max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    /* Getters */
    public int getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets stock.
     *
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Gets min.
     *
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Gets max.
     *
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Add associated part.
     *
     * @param part the part
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Delete associated part boolean.
     *
     * @param selectedAssociatedPart the selected associated part
     * @return the boolean
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        if (associatedParts.contains(selectedAssociatedPart)) {
            associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        else
            return false;
    }

    /**
     * Gets all associated parts.
     *
     * @return the all associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
