/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author si-mexon
 */
public class LogInDetails {

    String name;
    private static String email;
    int userID; //which is automatically generated by the software
    String password;

    //check if the name is atleast one character
    //when the email text field appears, get the string from the field and run email check on it 
    //when the password field appears, get the char(s) from the field and run password check on it and also check if "password" and re-enter password" matches
    //after successful registration, generate a unique userId (100,000 *(cast to int)) and display for the user
    public static boolean isValidDetails() {
        //this function checks if: Email Address is vaild, if password is atleast four characters, 
        return false;

    }

    public static boolean isLogInDetails(int userId, String password) {
        //this function checks if the details supplied matches the database's stored details
        //database snippet: sets the cursor to the userId(parameter) and check if "password"(parameter) matches the stored database password for that userId

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/POS_System", "root", "");

            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("select user_ID, password from Permissions"); //which highlights or selects onnly the Product_id column

            while (rs.next()) {
                if (rs.getInt("User_ID") == userId) {             //if the userID matches with the database user_id    
                    return rs.getString("password") == password;
                }
                //remember to throw an exception ie userId is not found (user does not exist)        
            }
            rs.close();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException ex) {
            System.out.println("Log In failed: An Exception has occurred! " + ex);
        }
        //Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }

    //function to add the details to the database (register a user)
    public static void addDetailsToDatabase(String name, String email, int userId, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/POS_System", "root", "");

            //Database code to add P to products table
            Statement insertStatement = dbConnection.createStatement();
            ResultSet rs = insertStatement.executeQuery("select * from Permissions");

            rs.moveToInsertRow();
            rs.updateString("Name", name);
            rs.updateString("E-mail", email);
            rs.updateInt("User_ID", userId);
            rs.updateString("password", password);
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

    public static boolean isValidEmailCheck(String email) {
        // call this function and add the email to the database is this function returns true
        char[] email_check = {'@', '.'}; //declare variables to check for email address validity

        email = LogInDetails.email; //variable for e-mail address
        int result1;
        int result2;

        result1 = email.indexOf(email_check[0]);

        if (result1 < 1) {
            System.out.println(email + "is an invalid Email"); //throw an exception to *maybe re-enter email address* or  display invalid email
            return false;
        }

        result2 = email.indexOf(email_check[1]);

        if (result1 + 1 >= result2) {
            System.out.println(email + "is an invalid Email");//throw an exception to *maybe re-enter email address* or  display invalid email
            return false;
        }

        System.out.println("\nValid Email Address");
        return true;
    }
    
    public static boolean isValidPasswordCheck(String password){
        //this function checks if the password is atleast four characters
        
        return false;
         
                
    }

}
