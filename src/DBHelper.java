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

        String user = "CREATE TABLE IF NOT EXISTS `user`(\n" +
                "   `id` INT UNSIGNED AUTO_INCREMENT,\n" +
                "   `userName` VARCHAR(40) NOT NULL,\n" +
                "   `passWord` VARCHAR(40) NOT NULL,\n" +
                "   PRIMARY KEY ( `id` )\n" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        String version = "CREATE TABLE IF NOT EXISTS `version`(\n" +
                "   `id` INT UNSIGNED AUTO_INCREMENT,\n" +
                "   `versionName` VARCHAR(40) NOT NULL,\n" +
                "   `versionCode` VARCHAR(40) NOT NULL,\n" +
                "   PRIMARY KEY ( `id` )\n" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        PreparedStatement ps1 = connection.prepareStatement(user);
        ps1.execute();
        PreparedStatement ps2 = connection.prepareStatement(version);
        ps2.execute();
        ps1.close();
        ps2.close();
    }

    public static void addUser(String name, String pass) throws SQLException {
        String sql = "INSERT INTO user(userName, passWord) VALUES('" + name + "','" + pass + "')";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public static boolean queryUser(String name) throws SQLException {

        boolean exist = false;
        String sql = "select * from user where userName = '" + name + "'";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        exist = resultSet.next();
        preparedStatement.close();
        return exist;
    }

    public static Version queryVersion() throws SQLException {

        String sql = "select * from version";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        Version version = new Version();
        while (resultSet.next()) {
            String versionName = resultSet.getString(2);
            String versionCode = resultSet.getString(3);
            version.setVersionCode(versionCode);
            version.setVersionName(versionName);
        }
        System.out.println(version);

        preparedStatement.close();
        return version;
    }

    public static void close(String name) throws SQLException {
        connection.close();
    }
}