package cn.lyxlz.mybatis.mapper;

import cn.lyxlz.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ParameterMapper {

    /**
     * 验证登录（使用@Param注解命名参数）
     */
    User checkLoginByParam(@Param("username") String username, @Param("password") String password);

    /**
     * 添加用户
     */
    int insertUser(User user);

    /**
     * 通过用户名和密码匹配用户信息（手动指定map）
     */
    User checkLoginByMap(Map<String, Object> map);

    /**
     * 通过用户名和密码匹配用户信息
     */
    User checkLogin(String username, String password);

    /**
     * 查询所有员工的信息
     */
    List<User> getAllUser();

    /**
     * 根据用户名查询用户信息
     */
    User getUserByUsername(String username);
}
