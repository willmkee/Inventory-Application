package kee.wkeec482.model;

/**
 * The type Outsourced.
 * Extends Part class.
 * @author williamkee
 */
public class Outsourced extends Part{
    /**
     * The company name.
     */
    private String companyName;

    /**
     * Instantiates a new Outsourced.
     *
     * @param id          the id
     * @param name        the name
     * @param price       the price
     * @param stock       the stock
     * @param min         the min
     * @param max         the max
     * @param companyName the company name
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Sets company name.
     *
     * @param companyName the company name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Gets company name.
     *
     * @return the company name
     */
    public String getCompanyName() {
        return companyName;
    }
}