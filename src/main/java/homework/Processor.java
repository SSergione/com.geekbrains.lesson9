package homework;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Processor {
    public static String prSt;

    public static void main(String[] args) {
        Students st1 = new Students(1, "Ivan", 2);
        Students st2 = new Students(4, "Petr", 6);

        try {
            connect(getPrSt(st2));
            //dropTableEx();
            //buildTable(Students.class);
            addObject(st2);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;

    public static void connect(String prSt) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite://Users/sergeysemenyuk/Documents/Test/main.db");
            statement = connection.createStatement();
            preparedStatement = connection.prepareStatement(prSt);
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException("Unable to connect");
        }
    }

    public static void buildTable(Class cl) throws SQLException {
        if (!cl.isAnnotationPresent(Table.class)) {
            throw new RuntimeException("@Table missed");
        }
        Map<Class, String> map = new HashMap<>();
        map.put(int.class, "INTEGER");
        map.put(String.class, "TEXT");
        // CREATE TABLE students (id INTEGER, name TEXT, score INTEGER);
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE ");
        // 'CREATE TABLE '
        stringBuilder.append(((Table) cl.getAnnotation(Table.class)).title());
        // 'CREATE TABLE students'
        stringBuilder.append(" (");
        // 'CREATE TABLE students ('
        Field[] fields = cl.getDeclaredFields();
        for (Field o : fields) {
            if (o.isAnnotationPresent(Column.class)) {
                stringBuilder.append(o.getName())
                        .append(" ")
                        .append(map.get(o.getType()))
                        .append(", ");
            }
        }
        // CREATE TABLE students (id INTEGER, name TEXT, score INTEGER,
        stringBuilder.setLength(stringBuilder.length() - 2);
        // CREATE TABLE students (id INTEGER, name TEXT, score INTEGER
        stringBuilder.append(");");
        // CREATE TABLE students (id INTEGER, name TEXT, score INTEGER);
        statement.executeUpdate(stringBuilder.toString());
    }

    public static void addObject(Object obj) throws SQLException, IllegalAccessException {
        if (!obj.getClass().isAnnotationPresent(Table.class)) {
            throw new RuntimeException("@Table missed");
        }
        Map<Class, String> map = new HashMap<>();
        map.put(int.class, "INTEGER");
        map.put(String.class, "TEXT");
        // INSERT INTO students (id, name, score) VALUES (?, ?, ?);
        StringBuilder stb = new StringBuilder();
        stb.append("INSERT INTO ");
        // INSERT INTO
        stb.append(((Table) obj.getClass().getAnnotation(Table.class)).title());
        stb.append(" (");
        // INSERT INTO students (
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field o : fields) {
            if (o.isAnnotationPresent(Column.class)) {
                stb.append(o.getName() + " ,");
            }
        }
        // INSERT INTO students (id, name, score,
        stb.setLength(stb.length() - 2);
        stb.append(") VALUES (");
        // INSERT INTO students (id, name, score) VALUES (
        for (Field o : fields) {
            if (o.isAnnotationPresent(Column.class)) {
                stb.append("?, ");
            }
        }
        // INSERT INTO students (id, name, score) VALUES (?, ?, ?,
        stb.setLength(stb.length() - 2);
        // INSERT INTO students (id, name, score) VALUES (?, ?, ?);
        stb.append(");");
        //prSt = stb.toString();
        stb.setLength(stb.length() - 9);
        // INSERT INTO students (id, name, score) VALUES (
        for (Field o : fields) {
            if (o.isAnnotationPresent(Column.class)) {
                if (o.getType().getSimpleName().equals("String")) {
                    stb.append("'")
                            .append(o.get(obj))
                            .append("'")
                            .append(", ");
                } else
                    stb.append(o.get(obj)).append(", ");
            }
        }
        stb.setLength(stb.length() - 2);
        stb.append(");");
        statement.executeUpdate(stb.toString());
    }

    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void dropTableEx() throws SQLException {
        statement.executeUpdate("DROP TABLE students;");
    }

    private static String getPrSt(Object obj) throws SQLException, IllegalAccessException {
        if (!obj.getClass().isAnnotationPresent(Table.class)) {
            throw new RuntimeException("@Table missed");
        }
        Map<Class, String> map = new HashMap<>();
        map.put(int.class, "INTEGER");
        map.put(String.class, "TEXT");
        // INSERT INTO students (id, name, score) VALUES (?, ?, ?);
        StringBuilder stb = new StringBuilder();
        stb.append("INSERT INTO ");
        // INSERT INTO
        stb.append(((Table) obj.getClass().getAnnotation(Table.class)).title());
        stb.append(" (");
        // INSERT INTO students (
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field o : fields) {
            if (o.isAnnotationPresent(Column.class)) {
                stb.append(o.getName() + " ,");
            }
        }
        // INSERT INTO students (id, name, score,
        stb.setLength(stb.length() - 2);
        stb.append(") VALUES (");
        // INSERT INTO students (id, name, score) VALUES (
        for (Field o : fields) {
            if (o.isAnnotationPresent(Column.class)) {
                stb.append("?, ");
            }
        }
        // INSERT INTO students (id, name, score) VALUES (?, ?, ?,
        stb.setLength(stb.length() - 2);
        // INSERT INTO students (id, name, score) VALUES (?, ?, ?);
        stb.append(");");
        return stb.toString();
    }
}

