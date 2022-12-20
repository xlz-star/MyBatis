package cn.lyxlz.mybatis;

import cn.lyxlz.mybatis.mapper.EmpMapper;
import cn.lyxlz.mybatis.pojo.Emp;
import cn.lyxlz.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ResultMapTest {

    SqlSession sqlSession;
    EmpMapper mapper;

    @Before
    public void setSqlSession() {
        sqlSession = SqlSessionUtils.getSqlSession();
        mapper = sqlSession.getMapper(EmpMapper.class);
    }


    @Test
    public void testGetAllEmp() {
        List<Emp> allEmp = mapper.getAllEmp();
        allEmp.forEach(System.out::println);
    }

    @Test
    public void testGetEmpAndDept() {
        List<Emp> empAndDept = mapper.getEmpAndDept();
        empAndDept.forEach(System.out::println);
    }

    @Test
    public void testGetEmpAndDeptByStep() {
        Emp emp = mapper.getEmpAndDeptByStepOne(3);
        System.out.println(emp);
    }

    @After
    public void close() {
        sqlSession.close();
    }

}
