package cn.lyxlz.mybatis.mapper;

import cn.lyxlz.mybatis.pojo.User;

import java.util.List;

public interface UserMapper {

    /**
     * 添加用户信息
     *
     * @return int 受影响的行数
     */
    int insertUser();


    /**
     * 更新用户
     */
    void updateUser();

    /**
     * 删除用户
     */
    void deleteUser();

    /**
     * 根据id查找用户
     *
     * @return {@link User}
     */
    User getUserById();

    /**
     * 查询所有用户
     *
     * @return {@link List}<{@link User}>
     */
    List<User> getAllUser();
}
