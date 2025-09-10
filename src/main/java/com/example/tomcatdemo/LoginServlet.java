package com.example.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

// 1. ����Servletӳ�䣺ǰ������·��Ϊ "/LoginServlet"������ǰ��AJAX��URLһ�£�
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    // 2. ��дdoPost����������ǰ��POST����
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 3. ��������������⣨�������Ӧ�������ã�
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8"); // ��Ӧ��ʽΪJSON

        // 4. ��ȡǰ�˴��ݵĲ�����username��password��
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 5. ��ʼ�����ݿ����Ӳ������滻Ϊ���MySQL���ã�
        String url = "jdbc:mysql://localhost:3306/test_db?useSSL=false&serverTimezone=UTC";
        String dbUsername = "root"; // ���MySQL�û�������root��
        String dbPassword = "123456"; // ���MySQL����

        // 6. �������ݿ���ض���
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrintWriter out = response.getWriter(); // �������JSON��Ӧ

        try {
            // 7. ����MySQL JDBC������MySQL 8.0+������ʽ���أ���ʡ�ԣ�
//            Class.forName("com.mysql.cj.jdbc.Driver");

            // 8. �������ݿ�����
            conn = DriverManager.getConnection(url, dbUsername, dbPassword);

            // 9. ��дSQL����ѯ�û�����Ӧ�����룬ʹ��PreparedStatement��ֹSQLע�룩
            String sql = "SELECT password FROM user WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username); // �滻SQL�е�?Ϊǰ�˴��ݵ�username

            // 10. ִ��SQL��ѯ����ȡ�����
            rs = pstmt.executeQuery();

            // 11. ��֤������ж��Ƿ���ڸ��û���������һ��
            if (rs.next()) { // ���ڸ��û���
                String dbPwd = rs.getString("password"); // �����ݿ��ȡ����
                if (dbPwd.equals(password)) { // ����һ�� �� ��¼�ɹ�
                    out.write("{\"success\":true, \"msg\":\"��¼�ɹ�\"}");
                } else { // ���벻һ�� �� ��¼ʧ��
                    out.write("{\"success\":false, \"msg\":\"�������\"}");
                }
            } else { // �����ڸ��û��� �� ��¼ʧ��
                out.write("{\"success\":false, \"msg\":\"�û���������\"}");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            out.write("{\"success\":false, \"msg\":\"��������ʧ��\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            out.write("{\"success\":false, \"msg\":\"���ݿ�����ʧ��\"}");
        } finally {
            // 12. �ر����ݿ���Դ�������ڴ�й©��˳��ResultSet �� PreparedStatement �� Connection��
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            out.close(); // �ر������
        }
    }

    // ��ѡ����дdoGet��������ǰ����GET�����߼���doPostһ�£�
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}