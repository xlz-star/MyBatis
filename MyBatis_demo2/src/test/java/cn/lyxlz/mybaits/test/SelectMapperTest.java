package cn.lyxlz.mybaits.test;

import cn.lyxlz.mybatis.mapper.SelectMapper;
import cn.lyxlz.mybatis.pojo.User;
import cn.lyxlz.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SelectMapperTest {

    SqlSession sqlSession;

    @Before
    public void setSqlSession() {
        sqlSession = SqlSessionUtils.getSqlSession();
    }

    @Test
    public void testGetAllUserToMapKey() {
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        Map<String, Object> allUserToMapKey = mapper.getAllUserToMapKey();
        System.out.println(allUserToMapKey);
    }

    @Test
    public void testGetAllUserToMap() {
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        List<Map<String, Object>> allUserToMap = mapper.getAllUserToMap();
        System.out.println(allUserToMap);
    }

    @Test
    public void testGetUserByIdToMap() {
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        Map<String, Object> userByIdToMap = mapper.getUserByIdToMap(4);
        System.out.println(userByIdToMap);
    }

    @Test
    public void testGetCount() {
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        Integer count = mapper.getCount();
        System.out.println(count);
    }

    @Test
    public void testGetUserById() {
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        User user = mapper.getUserById(4);
        System.out.println(user);
    }

    @Test
    public void testGetAllUser() {
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        List<User> user = mapper.getAllUser();
        System.out.println(user);
    }

    @After
    public void closeSqlSession() {
        sqlSession.close();
    }

}
