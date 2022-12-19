package cn.lyxlz.mybaits.test;

import cn.lyxlz.mybatis.mapper.ParameterMapper;
import cn.lyxlz.mybatis.pojo.User;
import cn.lyxlz.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

public class ParameterMapperTest {

    /**
     * Mybatis获取参数值的方式有两种：
     *  ${} 本质是字符串的拼接
     *  #{} 本质是占位符赋值
     *  MyBatis获取参数值的各种情况
     *      1、mapper接口方法的参数为单个的字面量类型
     *          可以直接通过${}和#{}获取，字段名可取任意，注意${}需要加单引号
     *      2、mapper接口方法的参数有多个时
     *          仍然使用${}和#{}，但字段名需对应参数位置，MyBatis会将数据存在一个map集合中，用arg0， arg1或param1，param2获取
     *      3、手动指定多个参数的字段名
     *          仍然使用${}和#{}，但接口方法需使用Map集合，且调用时传入的键需和#{}中的字段名对应
     *      4、mapper接口方法参数为实体类对象
     *          直接通过属性名访问
     *      5、使用@Param注解命名参数
     *          在mapper接口的方法参数上使用@Param注解直接命名字段名
     */

    @Test
    public void testCheckLoginByParam() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        HashMap<String, Object> map = new HashMap<>();
        User user = mapper.checkLoginByParam("张三", "123456");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        User user = new User("李四", "123456", 20, "男", "123456@qq.com");
        mapper.insertUser(user);
    }

    @Test
    public void testCheckLoginByMap() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", "123456");
        User user = mapper.checkLoginByMap(map);
        System.out.println(user);
    }

    @Test
    public void testCheckLogin() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        User user = mapper.checkLogin("张三", "123456");
        System.out.println(user);
    }

    @Test
    public void testGetUserByUsername() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        User user = mapper.getUserByUsername("张三");
        System.out.println(user);
    }

    @Test
    public void testgetAllUser() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        List<User> allUser = mapper.getAllUser();
        for (User user : allUser) {
            System.out.println(allUser);
        }
    }

    @Test
    public void testJDBC() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybatis?useSSL=false", "root", "123456");
        PreparedStatement ps = conn.prepareStatement("select * from t_user where username = ?");
        ps.setString(1, "张三");
        ResultSet resultSet = ps.executeQuery();
        resultSet.next();
        int id = resultSet.getInt(1);
        String username = resultSet.getString(2);
        System.out.printf("id=%d username=%s", id, username);
        ps.close();
        conn.close();
    }
}
