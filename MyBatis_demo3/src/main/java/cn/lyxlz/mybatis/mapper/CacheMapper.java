package cn.lyxlz.mybatis.mapper;

import cn.lyxlz.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CacheMapper {
    List<Emp> getEmpByEid(@Param("eid") Integer eid);
}
