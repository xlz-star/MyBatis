package cn.lyxlz.mybatis.mapper;

import cn.lyxlz.mybatis.pojo.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptMapper {

    Dept getEmpAndDeptByStepTwo(@Param("did") Integer did);

    List<Dept> getDeptAndEmp();

    List<Dept> getDeptAndEmpByStep();
}
