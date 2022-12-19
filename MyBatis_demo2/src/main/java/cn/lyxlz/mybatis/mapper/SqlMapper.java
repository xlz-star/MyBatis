package cn.lyxlz.mybatis.mapper;

import cn.lyxlz.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SqlMapper {

    List<User> getUserByLike(@Param("username") String username);

    int deleteMore(@Param("ids") String ids);

    List<User> getUserByTable(@Param("tablename") String tablename);

    void insertUser(User user);
}
