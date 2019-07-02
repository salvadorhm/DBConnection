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
 * @author Salvador Hernández Mendoza - salvadorhm@gmail.com
 * @version 4.5 - 14/Jun/2017
 *
 *
 *
 */
public class DBConnection extends Exception {

    private Connection connection;
    private Statement st;
    private ResultSet rs;
    private PreparedStatement ps;

    private String db;

    private DefaultTableModel tableModel;
    private HashMap<Object, Object> tableList = new HashMap<>();
    private HashMap<Object, Object> fieldList = new HashMap<>();
    private HashMap<Object, Object> allRow = new HashMap<>();

    private String version = "01072019";

    /**
     *
     * @param host Host direction
     * @param port port of mysql service
     * @param db Database to connect
     * @param user User for Database connect
     * @param password Password for Database connect
     * @throws sax.DBConnection
     */
    public DBConnection(int port, String host, String db, String user, String password) throws DBConnection {
        try {
            this.db = db;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, "'" + user + "'", password);
            st = connection.createStatement();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException err) {
            throw new DBConnection("\nDBConnectionException: DBConnection Error 100: " + err.getMessage());
        }
    }

    public DBConnection(String message) {
        super(message);
    }

    /**
     *
     * @return HashMap with Database Tables List
     * @throws sax.DBConnection
     */
    public HashMap getTableList() throws DBConnection {
        String sql = "SHOW TABLE STATUS FROM " + db;
        this.executeQuery(sql);
        tableList.clear();
        int p = 0;
        do {
            try {
                tableList.put(p++, rs.getString("name"));

            } catch (SQLException | NullPointerException err) {
                throw new DBConnection("\nDBConnectionException: getTableList error 101: " + err.getMessage());
            }
        } while (moveNext());
        return tableList;
    }

    /**
     *
     * @param table Table to show Table fields
     * @return HashMap with Field List
     * @throws sax.DBConnection
     */
    public HashMap getFieldList(String table) throws DBConnection {
        String sql = "SHOW COLUMNS FROM " + table + ";";
        this.executeQuery(sql);
        fieldList.clear();
        int p = 0;
        do {
            try {
                fieldList.put(p++, rs.getString("field"));
            } catch (SQLException | NullPointerException err) {
                throw new DBConnection("\nDBConnectionException: getFieldList error 102: " + err.getMessage());
            }
        } while (moveNext());
        return fieldList;
    }

    /**
     *
     * @return True if move previous
     * @throws sax.DBConnection
     */
    public boolean movePrevious() throws DBConnection {
        boolean result = false;

        try {
            if (!rs.isFirst()) {
                if (rs.previous() == true) {
                    result = true;
                } else {
                    result = false;
                }

            }
        } catch (SQLException | NullPointerException err) {
            result = false;
            throw new DBConnection("\nDBConnectionException: movePrevious 103: " + err.getMessage());
        }
        return result;
    }

    /**
     *
     * @return True is move next
     * @throws sax.DBConnection
     */
    public boolean moveNext() throws DBConnection {
        boolean result = false;
        try {
            if (!rs.isLast()) {
                if (rs.next() == true) {
                    result = true;
                } else {
                    result = false;
                }
            }
        } catch (SQLException | NullPointerException err) {
            result = false;
            throw new DBConnection("\nDBConnectionException: moveNext 104: " + err.getMessage());
        }
        return result;
    }

    /**
     *
     * @return True if move first
     * @throws sax.DBConnection
     */
    public boolean moveFirst() throws DBConnection {
        boolean result = false;
        try {
            if (rs.first() == true) {
                result = true;
            } else {
                result = false;
            }
        } catch (SQLException | NullPointerException err) {
            result = false;
            throw new DBConnection("\nDBConnectionException: MoveFirst 105: " + err.getMessage());
        }
        return result;
    }

    /**
     *
     * @return True if move last
     * @throws sax.DBConnection
     */
    public boolean moveLast() throws DBConnection {
        boolean result = false;
        try {
            if (rs.last() == true) {
                result = true;
            } else {
                result = false;
            }
        } catch (SQLException | NullPointerException err) {
            result = false;
            throw new DBConnection("\nDBConnectionException: moveLast 106: " + err.getMessage());
        }
        return result;
    }

    /**
     *
     * @param sqlQuery SQL query to execute
     * @throws sax.DBConnection
     */
    public void executeQuery(String sqlQuery) throws DBConnection {
        try {
            rs = st.executeQuery(sqlQuery);
        } catch (SQLException | NullPointerException err) {
            throw new DBConnection("\nDBConnectionException: Execute query " + sqlQuery + " error 107: " + err.getMessage());
        }
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
     * @return
     * @throws sax.DBConnection
     */
    public boolean executeUpdate(String sqlQuery) throws DBConnection {
        boolean result = false;
        try {
            st.executeUpdate(sqlQuery);
            result = true;
        } catch (SQLException | NullPointerException err) {
            //System.err.println("Execute update " + sqlQuery + " error 108: " + err.getMessage());
            throw new DBConnection("\nDBConnectionException: Execute update " + sqlQuery + " error 108: " + err.getMessage());
        }
        return result;
    }

    /**
     *
     * @param sqlQuery SQL query for show in table
     * @return True if update table
     * @throws sax.DBConnection
     */
    public boolean updateTable(String sqlQuery) throws DBConnection {
        boolean result = false;
        try {
            rs = st.executeQuery(sqlQuery);
            tableModel = (DefaultTableModel) DbUtils.resultSetToTableModel(rs);
            //System.out.println("table model ready");
            result = true;
        } catch (SQLException | NullPointerException | NoClassDefFoundError err) {
            result = false;
            throw new DBConnection("\nDBConnectionException: Execute update " + sqlQuery + " error 108: " + err.getMessage());
        }
        return result;
    }

    /**
     *
     * @param field Table field to show
     * @return field value in String format
     * @throws sax.DBConnection
     */
    public String getString(String field) throws DBConnection {
        String result = null;
        try {
            result = rs.getString(field);
        } catch (SQLException | NullPointerException err) {
            result = null;
            throw new DBConnection("\nDBConnectionException: Get String " + field + " error 110: " + err.getMessage());
        }
        return result;

    }

    /**
     *
     * @param field Table field to show
     * @return field value in String format
     * @throws sax.DBConnection
     */
    public int getInteger(String field) throws DBConnection {
        int result = 0;
        try {
            result = rs.getInt(field);
        } catch (SQLException | NullPointerException err) {
            //System.err.println("Get Integer " + field + " error 111: " + err.getMessage());
            throw new DBConnection("\nDBConnectionException: Get Integer " + field + " error 111: " + err.getMessage());
        }
        return result;
    }

    /**
     *
     * @param field Table field to show
     * @return field value in float format
     * @throws sax.DBConnection
     */
    public float getFloat(String field) throws DBConnection {
        float result = 0.0f;
        try {
            result = rs.getFloat(field);
        } catch (SQLException | NullPointerException err) {
            //System.err.println("Get Float " + field + " error 112: " + err.getMessage());
            throw new DBConnection("\nDBConnectionException: Get Float " + field + " error 112: " + err.getMessage());
        }
        return result;
    }

    /**
     *
     * @param field Table field to show
     * @return field value in double format
     * @throws sax.DBConnection
     */
    public double getDouble(String field) throws DBConnection {
        double result = 0.0;
        try {
            result = rs.getDouble(field);
        } catch (SQLException | NullPointerException err) {
            //System.err.println("Get Double " + field + " error 113: " + err.getMessage());
            throw new DBConnection("\nDBConnectionException: Get Double " + field + " error 113: " + err.getMessage());
        }
        return result;
    }

    /**
     *
     * @throws sax.DBConnection
     */
    public void executePreparedStatement() throws DBConnection {
        try {
            ps.executeUpdate();
        } catch (SQLException err) {
            throw new DBConnection("\nDBConnectionException: Execute Prepared Statement error 114: " + err.getMessage());
        }
    }

    /**
     *
     * @param sql Query to prepare Statement
     * @throws sax.DBConnection
     */
    public void prepareStatement(String sql) throws DBConnection {
        try {
            ps = connection.prepareStatement(sql);
        } catch (SQLException err) {
            throw new DBConnection("\nDBConnectionException: Prepare Statement " + sql + " error 115: " + err.getMessage());
        }
    }

    /**
     *
     * @param index index of prepared statement
     * @param value Object value
     * @throws sax.DBConnection
     */
    public void setPreparedStatement(int index, Object value) throws DBConnection {
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
        } catch (NullPointerException | SQLException err) {
            //System.err.println("Prepared Statement error 116: " + err.getMessage());
            throw new DBConnection("\nDBConnectionException: Prepared Statement error 116: " + err.getMessage());
        }
    }

    /**
     *
     * @param table Database Table to be mapped
     * @param rows Number of rows to show
     * @return HashMap with all the fields in the Database Table
     * @throws sax.DBConnection
     */
    public HashMap<Object, Object> getAllRow(String table, int rows) throws DBConnection {
        try {
            String sql = "SELECT * FROM " + table + " LIMIT " + rows + ";";
            executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            int index = 0;
            while (rs.next()) {
                Map<String, Object> columns = new HashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    columns.put(metaData.getColumnLabel(i), rs.getObject(i));
                }

                allRow.put(index++, columns);
            }
        } catch (NullPointerException | SQLException err) {
            //System.err.println("getAllRow Error 117: " + err.getMessage());
            throw new DBConnection("\nDBConnectionException: getAllRow Error 117: " + err.getMessage());
        }
        return allRow;
    }

    public void version() {
        System.out.println("Salvador Hernández Mendoza\nsalvadorhm@gmail.com\nversión " + this.version);
    }
}
