package cn.lyxlz.mybatis.mapper;

import cn.lyxlz.mybatis.pojo.Dept;
import org.apache.ibatis.annotations.Param;

public interface DeptMapper {

    Dept getEmpAndDeptByStepTwo(@Param("did") Integer did);

}
