import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
    public static void main(String[] args) {
        // 数据库连接相关信息（请替换为你的实际配置）
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "123456";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 获取数据库连接
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();

            // 查询 emp 表（使用小写表名，与创建时保持一致）
            String sql = "select * from emp";
            resultSet = statement.executeQuery(sql);

            // 遍历结果集（使用实际的字段名 emp_name、emp_sex、emp_age）
            while (resultSet.next()) {
                String name = resultSet.getString("emp_name");  // 修正字段名
                String sex = resultSet.getString("emp_sex");    // 修正字段名
                int age = resultSet.getInt("emp_age");          // 修正字段名，使用int类型

                System.out.print("姓名: " + name);
                System.out.print("性别: " + sex);
                System.out.print("年龄: " + age);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库操作异常");
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}