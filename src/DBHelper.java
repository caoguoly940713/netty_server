import java.sql.*;

/**
 * mysql连接池，在使用crud操作之前必须调用connect()
 */
public class DBHelper {

    static Connection connection;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
        connection = DriverManager.getConnection(url, "root", "root");
    }

    public static void createTable() throws ClassNotFoundException, SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS `user`(\n" +
                "   `id` INT UNSIGNED AUTO_INCREMENT,\n" +
                "   `userName` VARCHAR(40) NOT NULL,\n" +
                "   `passWord` VARCHAR(40) NOT NULL,\n" +
                "   PRIMARY KEY ( `id` )\n" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public static void addUser(String name, String pass) throws SQLException {
        String sql = "INSERT INTO user(userName, passWord) VALUES('" + name + "','" + pass + "')";
//        System.out.println("sql:" + sql);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public static boolean queryUser(String name) throws SQLException {

        boolean exist = false;
        String sql = "select * from user where userName = '" + name +"'";
//        System.out.println("sql:" + sql);

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        exist = resultSet.next();
        preparedStatement.close();
        return exist;
    }

    public static void close(String name) throws SQLException {
        connection.close();
    }

//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        connect();
//        addUser("44","44");
//    }
}