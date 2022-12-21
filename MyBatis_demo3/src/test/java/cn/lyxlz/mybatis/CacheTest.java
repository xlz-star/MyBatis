package cn.lyxlz.mybatis;

import cn.lyxlz.mybatis.mapper.CacheMapper;
import cn.lyxlz.mybatis.pojo.Emp;
import cn.lyxlz.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class CacheTest {
    @Test
    public void testGetEmpByEid() {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        CacheMapper mapper = sqlSession.getMapper(CacheMapper.class);
        List<Emp> empByEid = mapper.getEmpByEid(1);
        System.out.println(empByEid);
        List<Emp> empByEid1 = mapper.getEmpByEid(1);
        System.out.println(empByEid1);
    }
}
