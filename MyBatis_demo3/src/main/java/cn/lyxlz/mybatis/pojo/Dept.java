package cn.lyxlz.mybatis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dept {

    private Integer did;
    private String deptName;

    public Dept(String deptName) {
        this.deptName = deptName;
    }
}
