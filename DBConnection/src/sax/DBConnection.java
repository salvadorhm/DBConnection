/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sax;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * Library to easy connect to MySQL Database, use the MySQL JDBC Driver
 *
 * @author salvadorhm@gmail.com
 * @version 1.2
 *
 */
public class DBConnection {

    private Connection connection;
    private Statement st;
    private ResultSet rs;
    private PreparedStatement ps;
    
    private String version = "1.2";

    private String db;

    private DefaultTableModel tableModel;
    private HashMap<Object, Object> tableList = new HashMap<>();
    private HashMap<Object, Object> fieldList = new HashMap<>();
    private HashMap<Object, Object> allRow = new HashMap<>();

    /**
     * 
     * @param host Host direction 
     * @param port port of mysql service 
     * @param db Database to connect
     * @param user User for Database connect
     * @param password Password for Database connect
     */
    public DBConnection(int port, String host, String db, String user, String password){
        try {
            this.db = db;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, "'" + user + "'", password);
            st = connection.createStatement();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException err) {
            System.err.println("Connection Error 100: " + err.getMessage());
        }
    }

    /**
     *
     * @return HashMap with Database Tables List
     */
    public HashMap getTableList(){
        String sql = "SHOW TABLE STATUS FROM " + db;
        this.executeQuery(sql);
        tableList.clear();
        int p = 0;
        do {
            try {
                tableList.put(p++, rs.getString("name"));
            } catch (SQLException | NullPointerException err) {
                System.err.println("Get tables error 101: " + err.getMessage());
            }
        } while (moveNext());
        return tableList;
    }

    /**
     *
     * @param table Table to show Table fields
     * @return HashMap with Field List
     */
    public HashMap getFieldList(String table){
        String sql = "SHOW COLUMNS FROM " + table + ";";
        this.executeQuery(sql);
        fieldList.clear();
        int p = 0;
        do {
            try {
                fieldList.put(p++, rs.getString("field"));
            } catch (SQLException | NullPointerException err) {
                System.err.println("Get fields error 102: " + err.getMessage());
            }
        } while (moveNext());
        return fieldList;
    }
    /**
     * 
     * @return boolean if can move previous
     */
    public boolean movePrevious() {
        try {
            if (!rs.isFirst()) {
                rs.previous();
                return true;
            }
        } catch (SQLException | NullPointerException err) {
            System.err.println("Previous row error 103: " + err.getMessage());
        }
        return false;
    }
    /**
     * 
     * @return boolean if can move next
     */
    public boolean moveNext(){
        try {
            if (!rs.isLast()) {
                rs.next();
                return true;
            }
        } catch (SQLException | NullPointerException err) {
            System.err.println("Next row error 104: " + err.getMessage());
        }
        return false;
    }
    /**
     * 
     * @return boolean if can move first
     */
    public boolean moveFirst() {
        try {
            rs.first();
            return true;
        } catch (SQLException | NullPointerException err) {
            System.err.println("Fisrt row error 105: " + err.getMessage());
        }
        return false;
    }
    /**
     * 
     * @return boolean if can move last
     */
    public boolean moveLast(){
        try {
            rs.last();
            return true;
        } catch (SQLException | NullPointerException err) {
            System.err.println("Last row error 106: " + err.getMessage());
        }
        return false;
    }

    /**
     *
     * @param sqlQuery SQL query to execute
     */
    public boolean executeQuery(String sqlQuery){
        try {
            rs = st.executeQuery(sqlQuery);
            return true;
        } catch (SQLException | NullPointerException err) {
            System.err.println("Execute query " + sqlQuery + " error 107: " + err.getMessage());
        }
        return false;
    }

    /**
     *
     * @return TableModel with fields and data
     */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    /**
     *
     * @param sqlQuery SQL query for update data
     */
    public boolean executeUpdate(String sqlQuery){
        try {
            st.executeUpdate(sqlQuery);
            return true;
        } catch (SQLException | NullPointerException err) {
            System.err.println("Execute update " + sqlQuery + " error 108: " + err.getMessage());
        }
        return false;
    }

    /**
     *
     * @param sqlQuery SQL query for show in table
     */
    public boolean updateTable(String sqlQuery){
        try {
            rs = st.executeQuery(sqlQuery);
            tableModel = (DefaultTableModel) DbUtils.resultSetToTableModel(rs);
            System.out.println("table model ready");
            return true;
        } catch (SQLException | NullPointerException | NoClassDefFoundError err) {
            System.err.println("Udapte Table " + sqlQuery + " error 109: " + err.getMessage());
        }
        return false;
    }

    /**
     *
     * @param field Table field to show
     * @return field value in String format
     */
    public String getString(String field){
        try {
            return rs.getString(field);
        } catch (SQLException | NullPointerException err) {
            System.err.println("Get String " + field + " error 110: " + err.getMessage());
        }
        return null;
    }
    /**
     *
     * @param field Table field to show
     * @return field value in String format
     */
    public int getInteger(String field){
        try {
            return rs.getInt(field);
        } catch (SQLException | NullPointerException err) {
            System.err.println("Get Integer " + field + " error 111: " + err.getMessage());
        }
        return 0;
    }
    /**
     *
     * @param field Table field to show
     * @return field value in float format
     */
    public float getFloat(String field){
        try {
            return rs.getInt(field);
        } catch (SQLException | NullPointerException err) {
            System.err.println("Get Float " + field + " error 112: " + err.getMessage());
        }
        return 0;
    }
    /**
     *
     * @param field Table field to show
     * @return field value in double format
     */
    public double getDouble(String field){
        try {
            return rs.getInt(field);
        } catch (SQLException | NullPointerException err) {
            System.err.println("Get Double " + field + " error 113: " + err.getMessage());
        }
        return 0;
    }
    /**
     * 
     */
    public boolean executePreparedStatement() {
        try {
            rs= ps.executeQuery();
            return true;
        } catch (SQLException err) {
            System.err.println("Execute Prepared Statement error 114: " + err.getMessage());
        }
        return false;
    }
    
      /**
     * 
     */
    public boolean executeUpdatePreparedStatement() {
        try {
            ps.executeUpdate();
            return true;
        } catch (SQLException err) {
            System.err.println("Execute Prepared Statement error 114: " + err.getMessage());
        }
        return false;
    }
    /**
     * 
     * @param sql Query to prepare Statement
     */
    public boolean prepareStatement(String sql){
        try {
            ps = connection.prepareStatement(sql);
            return true;
        } catch (SQLException err) {
            System.err.println("Prepare Statement " + sql + " error 115: " + err.getMessage());
        }
        return false;
    }
    /**
     * 
     * @param index index of prepared statement
     * @param value Object value
     */
    public boolean setPreparedStatement(int index, Object value){
        try {
            if (value instanceof String) {
                ps.setString(index, (String) value);
            } else if (value instanceof Integer) {
                ps.setInt(index, (Integer) value);
            } else if (value instanceof Float) {
                ps.setFloat(index, (Float) value);
            } else if (value instanceof Double) {
                ps.setDouble(index, (Double) value);
            } else if (value instanceof Date) {
                ps.setDate(index, (Date) value);
            }
            return true;
        } catch (NullPointerException | SQLException err) {
            System.err.println("Prepared Statement error 116: " + err.getMessage());
        }
        return false;
    }
    /**
     * 
     * @param table Database Table to be mapped
     * @param rows Number of rows to show
     * @return  HashMap with all the fields in the Database Table
     */
    public HashMap<Object, Object> getAllRow(String table, int rows){
        try {
            String sql = "SELECT * FROM " + table + " LIMIT " + rows + ";";
            executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            int index=0;
            while (rs.next()) {
                Map<String, Object> columns = new HashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    columns.put(metaData.getColumnLabel(i), rs.getObject(i));
                }
                
                allRow.put(index++,columns);
            }
        } catch (NullPointerException | SQLException err) {
            System.err.println("getAllRow Error 117: " + err.getMessage());
        }
        return allRow;
    }
    
    
    /**
     * Set the value of version
     *
     * @param version new value of version
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
