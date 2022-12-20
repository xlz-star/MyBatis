package cn.lyxlz.mybatis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emp {
    private Integer eid;
    private String empName;
    private Integer age;
    private char sex;
    private String email;
    private Dept dept;

    public Emp(String empName, Integer age, char sex, String email) {
        this.empName = empName;
        this.age = age;
        this.sex = sex;
        this.email = email;
    }
}
