package cn.lyxlz.mybatis.mapper;

import cn.lyxlz.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmpMapper {

    List<Emp> getAllEmp();

    List<Emp> getEmpAndDept();

    Emp getEmpAndDeptByStepOne(@Param("eid") Integer eid);




}
