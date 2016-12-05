/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import sax.DBConnection;

/**
 *
 * @author saxcr
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DBConnection conection = new DBConnection(3306, "localhost", "agenda", "root", "toor");

        conection.executeQuery("Select * from contactos;");

        while (conection.moveNext()) {
            System.out.println(conection.getString("nombre"));
            System.out.println(conection.getString("telefono"));
            System.out.println(conection.getString("email"));
        }
        
        /*
            PreparedStatement for protect SQL injection
        */

        String sql = "SELECT * FROM contactos WHERE nombre = ?;";
        conection.prepareStatement(sql);
        conection.setPreparedStatement(1, "Dejah Thoris");
        conection.executePreparedStatement();
        while (conection.moveNext()) {
            System.out.println(conection.getString("nombre"));
            System.out.println(conection.getString("telefono"));
            System.out.println(conection.getString("email"));
        }

    }
}
