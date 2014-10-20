package cn.edu.fudan.se.dal;

import cn.edu.fudan.se.util.CommUtil;
import cn.edu.fudan.se.util.FileHelper;
import cn.edu.fudan.se.util.INIHelper;

import java.io.File;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBHelper {
    private static Connection conn;
    private static Statement stmt;
    private static String projectName;
    public static final String CLASS_TABLE_NAME = "classinfo";
    public static final String METHOD_TABLE_NAME = "methodinfo";
    public static final String FIELD_TABLE_NAME = "fieldinfo";
    public static final String CLASSRELATION_TABLE_NAME = "classRelationinfo";
    public static final String METHODINVOCATION_TABLE_NAME = "methodinvocationinfo";
    public static final String VARIABLE_TABLE_NAME = "variableinfo";
    public static final String TOKEN_TABLE_NAME = "tokeninfo";
    public static final String PROJECT_LANGUAGE = "projectLanguage";
    public static final String DOCUMENT_TABLE_NAME = "document";
    public static final String WORD_TABLE_NAME = "word";
    public static final String TOPIC_DOCUMENT_TABLE_NAME = "topicToDocument";
    public static final String TOPIC_WORDS_TABLE_NAME = "topicToWord";
    public static final String WORD_DOCUMENT_TABLE_NAME = "wordToDocument";
    public static final String STEM_TABLE_NAME = "stem";
    public static final String TOKEN_IN_METHOD = "tokenInMethod";

    public static Connection getConn() {
        return conn;
    }

    private static void initialDB() {
        try {
            stmt.executeUpdate(FileHelper.getContent(CommUtil.getCurrentProjectPath() +
                    "\\createDB.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static Connection openConn() {
        INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath() +
                "\\conf.ini");
        String JDBC_DRIVER = iniHelper.getValue("DB", "JDBC_DRIVER", "");
        String DB_URL = iniHelper.getValue("DB", "DB_URL", "");
        String DB_USER = iniHelper.getValue("DB", "DB_USER", "");
        String DB_PASSWORD = iniHelper.getValue("DB", "DB_PASSWORD", "");
        String DB_FILENAME = CommUtil.getCurrentProjectPath() + "\\" +
            iniHelper.getValue("DB", "DB_FILENAME", "");

        try {
            File file = new File(DB_FILENAME);

            if (file.exists() == false) {
                file.createNewFile();
            }

            Class<Driver> jdbcDriver = (Class<Driver>) Class.forName(JDBC_DRIVER);
            DriverManager.registerDriver(jdbcDriver.newInstance());

            if (JDBC_DRIVER.contains("mysql")) {
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } else {
                conn = DriverManager.getConnection(DB_URL + DB_FILENAME);
            }

            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            initialDB();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        return conn;
    }

    public static void closeConn() {
        try {
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String sql) {
        try {
            stmt.executeUpdate(sql);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public static ResultSet executeQuery(String sql) {
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        return rs;
    }

    public static String getCurrentProjectName() {
        return projectName;
    }

    public static void setCurrentProjectName(String _projectName) {
        projectName = _projectName;
    }
}
