package cn.lyxlz.mybatis.test;

import cn.lyxlz.mybatis.mapper.EmpMapper;
import cn.lyxlz.mybatis.pojo.Emp;
import cn.lyxlz.mybatis.pojo.EmpExample;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MBGTest {

    SqlSession sqlSession;

    @Before
    public void before() {
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
            sqlSession = factory.openSession(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMBG() {
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        EmpExample example = new EmpExample();
        example.createCriteria()
                .andEmpNameEqualTo("李四")    // 姓名为张三
                .andAgeGreaterThan(20); // 年龄大于22
        example.or()
                .andDidIsNotNull();
        List<Emp> emps = mapper.selectByExample(example);
        emps.forEach(System.out::println);
    }

    @After
    public void after() {
        sqlSession.close();
    }
}
