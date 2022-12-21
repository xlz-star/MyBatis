package cn.lyxlz.mybatis.test;

import cn.lyxlz.mybatis.mapper.EmpMapper;
import cn.lyxlz.mybatis.pojo.Emp;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PageHelperTest {

    /**
     * limit: index, pageSize
     *  index: 当前页的起始页
     *  pageSize: 每页显示的条数
     *  pageName: 当前页的页码
     */
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
    public void testPageHelper() {
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        Page<Object> page = PageHelper.startPage(1, 4);
        List<Emp> emps = mapper.selectByExample(null);
        emps.forEach(System.out::println);
        System.out.println(page);
    }

    @After
    public void after() {
        sqlSession.close();
    }
}
