package com.jc.java.assist.classloading;

import static java.util.Objects.nonNull;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlServerClassLoader extends ClassLoader {

    private ClassLoader parent;
    private String connectionString;

    public MySqlServerClassLoader(String connectionString) {
        this.parent = ClassLoader.getSystemClassLoader();
        this.connectionString = connectionString;
    }

    public MySqlServerClassLoader(ClassLoader parent, String connectionString) {
        this.parent = parent;
        this.connectionString = connectionString;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        try {
            clazz = super.findClass(name);
        } catch (ClassNotFoundException cnfe) {
            byte[] bytes = new byte[0];
            try {
                bytes = loadClassFromDatabase(name);
            } catch (SQLException sqle) {
                throw new ClassNotFoundException("Unable to load class", sqle);
            }
            return defineClass(name, bytes, 0, bytes.length);
        }
        return clazz;
    }

    private byte[] loadClassFromDatabase(String name) throws SQLException {
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
            String sql = "SELECT class from CLASSES where classname= ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Blob blob = rs.getBlob(1);
                byte[] data = blob.getBytes(1, (int) blob.length());
                return data;
            }
        } catch (SQLException sqlException) {
            System.out.println("SQL Exception: " + sqlException.getMessage());
        } catch (Exception e) {
            System.out.println("Runtime Exception: " + e.getMessage());
        } finally {
            if (nonNull(pstmt)) {
                pstmt.close();
            }
            if(nonNull(connection)) {
                connection.close();
            }
        }
        return null;
    }
}
