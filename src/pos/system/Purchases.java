package pos.system;

import static java.awt.image.ImageObserver.HEIGHT;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SI-MEXON
 */
public class Purchases {

    /* 
     * Create a list (shopping cart) to hold the selected/purchased products
     * To make purchases (1st step), we: 
<>   *  1.  List all the categories(from categories table) and wait for action listener
     *  2.  List all the available products from the selected category
<>   *  3.  Display a pop up window to enter no of stock(numItems) wished to purchase for selected item
     *  4.  Check if the value (int) entered is less than or equal to available stock:
     *      If it is less or equal
     *          >> Add the selected item to the list or Cart (Product ID)
     *      else  
     *          >> Display jOption Pane showing "numItems is more  than available stock"
     *  5.  A function to restore items (is selected) if purchase is unsuccessful 
     * After Successful purchases (2nd step), 
     *  1.  Check for fund or payments (Credit Card Details)
     *  2.  Delete items from the database (product id) (before deleting a product from the table, get the row index(int) of that product)
     *  3.  Print recipt (txt file format) 
     */
    static ArrayList ShoppingCart = new ArrayList();//list(Cart) 

    public static List<String> ListCategories() {
        //this function lists all the category names(String) in the 'avail_Categories' table(which holds all the available categories in the database)
        //as a jButton click event
        //snippet: while (rs.nxt){ getString from 'Category_name' and display jButton with txt CategoryName}
        List<String> categories = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/POS_System", "root", "");

            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("select Category_Name from avail_categories"); //which highlights or selects only the Product_id column

            while (rs.next()) {
                String category_name = rs.getString("Category_Name");
                //missing** code to Display jButton with Category_Name as text on it and i variable name
                System.out.println(category_name);
                categories.add(category_name);
            }

        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Purchases.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categories;
    }

    public static List<String> ListItems(String category) {
        //this function lists all the Product names(String) in the database table(which holds all the available categories in the database)
        //Check For the Item on the database and lists all the available items and returns the ProductID
        //snippet: while(rs.nxt){}
        List<String> products = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/POS_System", "root", "");

            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("select Name, Price, Description from " + category); //which highlights or selects only the Product_id column

            while (rs.next()) {
                String productName = rs.getString("Name");
                int productPrice = rs.getInt("Price");
                String productDescription = rs.getString("Description");
                products.add(productName + "        " + productPrice + "        " + productDescription);
                //missing** code to Display jButton with product: Name\t Price\t \tDesciption on it
            }

        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Purchases.class.getName()).log(Level.SEVERE, null, ex);
        }
        return products;
    }

    public static void AddToShoppingCart(String name) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/POS_System", "root", "");

            Statement statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = statement.executeQuery("select Name, Product_id, isSelected from products"); //which highlights or selects only the Product_id column

            while (rs.next()) {
                if (rs.getString("Name") == name) {
                    if (!rs.getBoolean("isSelected")) {
                        ShoppingCart.add(rs.getInt("Product_id"));//get the Product_ID of the item (by name) in the Products list and adds it to shopping cart 
                        rs.updateBoolean("isSelected", true);//update the field of the column 'isSelected' to true 
                        rs.updateRow();
                    }
                }
            }

        } catch (InstantiationException ex) {
            System.out.println("Log In failed: An Exception has occurred! " + ex);
        } catch (IllegalAccessException ex) {
            System.out.println("Log In failed: An Exception has occurred! " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Log In failed: An Exception has occurred! " + ex);
        } catch (SQLException ex) {
            System.out.println("Log In failed: An Exception has occurred! " + ex);
            //Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static boolean isLessThanOrEqualToStock(String category, String productName, int numStock) {
        //this function checks if the stock entered is less than or equal to the available stock (purchasable stock) in the database
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/POS_System", "root", "");

            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("select Name, Stock from " + category); //which highlights or selects only the Product_id column

            while (rs.next()) {
                if (rs.getString("Name") == productName) {
                    return numStock <= rs.getInt("Stock");
                }
            }

        } catch (InstantiationException ex) {
            System.out.println("Log In failed: An Exception has occurred! " + ex);
        } catch (IllegalAccessException ex) {
            System.out.println("Log In failed: An Exception has occurred! " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Log In failed: An Exception has occurred! " + ex);
        } catch (SQLException ex) {
            System.out.println("Log In failed: An Exception has occurred! " + ex);
            //Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    public static void PrintRecipt() {
//  parameters: Date, Name of P, No of stock, IDs of P          
    }

    public static void RestoreItems(int ProductID) {

    }
}
