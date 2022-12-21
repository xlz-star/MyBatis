package cn.lyxlz.mybatis;

import cn.lyxlz.mybatis.mapper.DeptMapper;
import cn.lyxlz.mybatis.mapper.DynamicSQLMapper;
import cn.lyxlz.mybatis.mapper.EmpMapper;
import cn.lyxlz.mybatis.pojo.Dept;
import cn.lyxlz.mybatis.pojo.Emp;
import cn.lyxlz.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DynamicSQLTest {

    SqlSession sqlSession;
    DynamicSQLMapper dynamicSQLMapper;

    @Before
    public void setSqlSession() {
        sqlSession = SqlSessionUtils.getSqlSession();
        dynamicSQLMapper= sqlSession.getMapper(DynamicSQLMapper.class);
    }

    @Test
    public void testGetEmpByCondition() {
        List<Emp> empByCondition = dynamicSQLMapper.getEmpByCondition(new Emp("张三", null, '男', null));
        System.out.println(empByCondition);
    }

    @Test
    public void testGetEmpByChoose() {
        List<Emp> empByChoose = dynamicSQLMapper.getEmpByChoose(new Emp());
        empByChoose.forEach(System.out::println);
    }

    @Test
    public void testDeleteMoreByArray() {
        int i = dynamicSQLMapper.deleteMoreByArray(new Integer[]{4});
        System.out.println(i);
    }

    @Test
    public void testInsertMoreByArray() {
        List<Emp> emps = Arrays.asList(
                new Emp("李四", 22, '男', "123@qq.com"),
                new Emp("王五", 33, '男', "987@qq.com")
        );
        int i = dynamicSQLMapper.insertMoreByArray(emps);
        System.out.println(i);
    }

    @After
    public void close() {
        sqlSession.close();
    }

}
