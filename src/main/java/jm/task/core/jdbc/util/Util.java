/**
 * How to load and register JDBC-Driver?
 *
 * 1) Class.forName("com.mysql.cj.jdbc.Driver"); -> throws ClassNotFoundException
 * 2)  Driver d = new com.mysql.cj.jdbc.Driver();
 *     DriverManager.registerDriver(d);
 * 3) Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); -> throws
 *      ClassNotFoundException | InstantiationException | IllegalAccessException
 *
 */

package jm.task.core.jdbc.util;

import java.sql.*;

public class Util {
    private static final String db_url = "jdbc:mysql://localhost:3306/preproj_db";
    private static final String user = "root";
    private static final String password = "root";
    private static final String jdbc_driver = "com.mysql.cj.jdbc.Driver";

    private static Connection con;

    public static Connection getConnection() throws SQLException {
        if (con == null) {
            try {
                Class.forName(jdbc_driver);
                con = DriverManager.getConnection(db_url, user, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
       return con;
    }

    public static void closeConnection() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }

}
