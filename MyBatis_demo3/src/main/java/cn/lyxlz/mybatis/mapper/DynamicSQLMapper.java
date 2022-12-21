package cn.lyxlz.mybatis.mapper;

import cn.lyxlz.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DynamicSQLMapper {

    /**
     * 多条件查询
     */
    List<Emp> getEmpByCondition(Emp emp);

    List<Emp> getEmpByChoose(Emp emp);

    int deleteMoreByArray(@Param("eids") Integer[] eids);

    int insertMoreByArray(@Param("emps") List<Emp> emps);
}
