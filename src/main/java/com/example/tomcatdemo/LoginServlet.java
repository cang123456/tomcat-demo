package com.example.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

// 1. 配置Servlet映射：前端请求路径为 "/LoginServlet"（需与前端AJAX的URL一致）
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    // 2. 重写doPost方法（处理前端POST请求）
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 3. 解决中文乱码问题（请求和响应都需设置）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8"); // 响应格式为JSON

        // 4. 获取前端传递的参数（username和password）
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 5. 初始化数据库连接参数（替换为你的MySQL配置）
        String url = "jdbc:mysql://localhost:3306/test_db?useSSL=false&serverTimezone=UTC";
        String dbUsername = "root"; // 你的MySQL用户名（如root）
        String dbPassword = "123456"; // 你的MySQL密码

        // 6. 声明数据库相关对象
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrintWriter out = response.getWriter(); // 用于输出JSON响应

        try {
            // 7. 加载MySQL JDBC驱动（MySQL 8.0+无需显式加载，可省略）
//            Class.forName("com.mysql.cj.jdbc.Driver");

            // 8. 建立数据库连接
            conn = DriverManager.getConnection(url, dbUsername, dbPassword);

            // 9. 编写SQL（查询用户名对应的密码，使用PreparedStatement防止SQL注入）
            String sql = "SELECT password FROM user WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username); // 替换SQL中的?为前端传递的username

            // 10. 执行SQL查询，获取结果集
            rs = pstmt.executeQuery();

            // 11. 验证结果：判断是否存在该用户，且密码一致
            if (rs.next()) { // 存在该用户名
                String dbPwd = rs.getString("password"); // 从数据库获取密码
                if (dbPwd.equals(password)) { // 密码一致 → 登录成功
                    out.write("{\"success\":true, \"msg\":\"登录成功\"}");
                } else { // 密码不一致 → 登录失败
                    out.write("{\"success\":false, \"msg\":\"密码错误\"}");
                }
            } else { // 不存在该用户名 → 登录失败
                out.write("{\"success\":false, \"msg\":\"用户名不存在\"}");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            out.write("{\"success\":false, \"msg\":\"驱动加载失败\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            out.write("{\"success\":false, \"msg\":\"数据库连接失败\"}");
        } finally {
            // 12. 关闭数据库资源（避免内存泄漏，顺序：ResultSet → PreparedStatement → Connection）
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            out.close(); // 关闭输出流
        }
    }

    // 可选：重写doGet方法（若前端用GET请求，逻辑与doPost一致）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}