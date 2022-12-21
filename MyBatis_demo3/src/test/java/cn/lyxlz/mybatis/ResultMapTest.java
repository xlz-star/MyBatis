package cn.lyxlz.mybatis;

import cn.lyxlz.mybatis.mapper.DeptMapper;
import cn.lyxlz.mybatis.mapper.EmpMapper;
import cn.lyxlz.mybatis.pojo.Dept;
import cn.lyxlz.mybatis.pojo.Emp;
import cn.lyxlz.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ResultMapTest {

    SqlSession sqlSession;
    EmpMapper empMapper;

    DeptMapper deptMapper;

    @Before
    public void setSqlSession() {
        sqlSession = SqlSessionUtils.getSqlSession();
        empMapper = sqlSession.getMapper(EmpMapper.class);
        deptMapper = sqlSession.getMapper(DeptMapper.class);
    }


    @Test
    public void testGetAllEmp() {
        List<Emp> allEmp = empMapper.getAllEmp();
        allEmp.forEach(System.out::println);
    }

    @Test
    public void testGetEmpAndDept() {
        List<Emp> empAndDept = empMapper.getEmpAndDept();
        empAndDept.forEach(System.out::println);
    }

    @Test
    public void testGetEmpAndDeptByStep() {
        Emp emp = empMapper.getEmpAndDeptByStepOne(3);
//        System.out.println(emp);
        System.out.println(emp.getEmpName());
    }

    @Test
    public void testGetDeptAndEmp() {
        List<Dept> deptAndEmp = deptMapper.getDeptAndEmp();
        System.out.println(deptAndEmp);
    }

    @Test
    public void testGetDeptAndEmpByStep() {
        List<Dept> deptAndEmpByStep = deptMapper.getDeptAndEmpByStep();
        System.out.println(deptAndEmpByStep);
    }

    @After
    public void close() {
        sqlSession.close();
    }

}
