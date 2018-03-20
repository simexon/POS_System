package pos.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author SI-MEXON
 */
public class Product {

    //Product properties/attributes
    private String category;
    private String name;
    private int price;
    private double weight;
    private String description;
    private int productID;
    private String expiryDate;
    private static int randNum;

    //product constructor
    Product(String category, String name, double weight, String description, int price, int ID, String expDate)/*Constructor*/ {
        this.category = category;
        this.name = name;
        this.weight = weight;
        this.description = description;
        this.price = price;
        this.productID = ID;
        this.expiryDate = expDate;
    }

    //before adding a product, a unique ProductID is generated for the product automatically by the Software
    private static int GenProductID() {
        //this method generates a [(cast to integer)(random number * 1000)] and checks if the Product
        //has the same ID(from the database). If there is, it generates another number
        //snippet: While ranNum == * (* in the ProductID column), ranNum == (int)randNum * 1000;

        //use resultset.next to go through all the rows in "products" table and use getInt() to get the value from the current row
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/POS_System", "root", "");

            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("select Product_id from products"); //whichh highlights or selects onnly the Product_id column

            randNum = (int) (Math.random() * 1000); //generate a random number * 1000 and cast to int
            while (rs.next()) {                             //
//                int clmVal = rs.getInt("Product_id");       //Check if the generated ramdom number is unique
                if (rs.getInt("Product_id") == randNum) {                    //
                    randNum = (int) (Math.random() * 1000); //generate another random number * 1000 and cast to int
                    rs.beforeFirst(); //goes back to the beginning of the table
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
        return randNum;
    }

    //this function adds a product to the products table (by the admin) 
    //and also adds the product ID to the selected category 
    public static void addProducts(String category, String name, double weight, String description, int price, String expDate) {
        int id = Product.GenProductID();//runs the function 'Generate product id' to generate an ID 

        /*Product P = new Product("Vegetables", "Water Leaf", 0.2, "African Tropical Vegetable", 50, id, "23/8/2019");*/
        //Database code to add P to the products table and to the selected category
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/POS_System", "root", "");

            //Database code to add P to all_products table
            Statement insertStatement = dbConnection.createStatement();
            ResultSet rs = insertStatement.executeQuery("select * from products");

            rs.moveToInsertRow();
            rs.updateString("Category", category);
            rs.updateString("Name", name);
            rs.updateDouble("Weight", weight);
            rs.updateString("Description", description);
            rs.updateInt("Price", price);
            rs.updateInt("ID", id);
            rs.updateString("ExpDate", expDate);
            rs.insertRow();

            rs.close();

            //Database code to add P.stock to its category
            Statement statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs2 = statement.executeQuery("select Name, Stock from " + category); //which highlights or selects Name and Stock column(s) from argumented (parameter) table

            boolean itemExists = false;

            while (rs2.next()) {
                if (rs.getString("Name") == name) { //if the table already has an item with the name, it gets the int value from stock and updates it to (stock + 1) 
                    int numStock = rs.getInt("Stock");
                    numStock += 1; //or numItems
                    rs.updateInt("Stock", numStock);
                    rs.updateRow();

                    itemExists = true;
                }
            }
            statement.close();

            if (!itemExists) {
                // if the table does not have an item with the name, create a row add(Name = name, Stock = 1)
                Statement insertStatement2 = dbConnection.createStatement();
                ResultSet rs3 = insertStatement2.executeQuery("select Name, Stock from " + category);

                rs2.last();
                rs2.moveToInsertRow();
                rs2.updateString("Name", name);
                rs2.updateInt("Stock", 1);
                rs2.insertRow();

                insertStatement.close();
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

    public static void createCategory(String category) {
        //Create a table with the category name
        //Adds "category name" to category table
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/POS_System", "root", "");

            Statement createStatement = dbConnection.createStatement();
            createStatement.executeUpdate("CREATE TABLE " + category + "(Name VARCHAR(10), Price INT, Description VARCHAR(250), Stock INT");//Creates a table with the category name
            createStatement.close();

//            Statement insertStatement = dbConnection.createStatement();
//            insertStatement.executeUpdate("INSERT INTO avail_categories VALUES('" + category + "')"); 
//            insertStatement.close();
            Statement insertStatement = dbConnection.createStatement();
            ResultSet rs = insertStatement.executeQuery("select * from avail_categories");

            rs.last();
            rs.moveToInsertRow();
            rs.updateString("Category_Name", category);//Inserts "String category" into available_categories table 
            rs.insertRow();

            rs.close();

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

}
