package cn.lyxlz.mybaits.test;

import cn.lyxlz.mybatis.mapper.SqlMapper;
import cn.lyxlz.mybatis.pojo.User;
import cn.lyxlz.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;

public class SqlMapperTest {

    SqlSession sqlSession;
    SqlMapper mapper;

    @Before
    public void setSqlSession() {
        sqlSession = SqlSessionUtils.getSqlSession();
        mapper = sqlSession.getMapper(SqlMapper.class);
    }

    @Test
    public void testGetUserByLike() {
        List<User> user = mapper.getUserByLike("张");
        System.out.println(user);
    }

    @Test
    public void testDelectMore() {
        int i = mapper.deleteMore("5,6");
        System.out.println(i);
    }

    @Test
    public void testGetUserByTable() {
        List<User> user = mapper.getUserByTable("t_user");
        System.out.println(user);
    }

    @Test
    public void testJDBC() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybatis?useSSL=false", "root", "123456");
        PreparedStatement ps = conn.prepareStatement("insert into t_user values()", Statement.RETURN_GENERATED_KEYS);
        ps.executeQuery();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        ps.close();
        conn.close();
    }

    @Test
    public void testInsertUser() {
        User user = new User("李四", "123456", 21, "男", "123@qq.com");
        mapper.insertUser(user);
        System.out.println(user);

    }

    @After
    public void close() {
        sqlSession.close();
    }
}
