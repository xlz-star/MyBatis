## 一、MyBatis快速入门

### 1、开发环境

IDE：idea

构建工具：maven

MySQL：MySQL  8.0.31

### 2、maven 依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.9</version>
    </dependency>

    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.29</version>
    </dependency>
</dependencies>
```

### 3、创建MyBatis核心配置文件

>习惯上命名为mybatis-config.xml，在Springboot中这个配置文件可以省略，该文件放在`src/main/resoures`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--配置连接数据库的环境-->
    <environments default="development">
        <environment id="development">
            <!--事务处理-->
            <transactionManager type="JDBC"/>
            <!--数据源-->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useUnicode=true&amp;characterEncoding=utf-8"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>

    <!--引入映射文件-->
    <mappers>
        <mapper resource="mappers/UserMapper.xml"/>
    </mappers>
</configuration>
```

### 4、创建mapper接口

> MyBatis的mapper接口相当于dao。但是mapper仅提供接口，不需要提供实现。

#### 创建表
```sql
CREATE TABLE `t_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
```

#### 创建实体类
```java
package cn.lyxlz.mybatis.pojo;

import lombok.Data;

@Data
public class User {

    private Integer id;

    private String username;

    private String password;

    private Integer age;

    private String sex;

    private String email;

}
```

#### 创建接口
```java
public interface UserMapper {

    /**
     * 添加用户信息
     *
     * @return int
     */
    int insertUser();
    
}
```

### 5、创建MyBatis映射文件

> 1、映射文件命名规则：
>
> 表所对应的实体类的类名+Mapper.xml
>
> 一个映射文件对应一个实体类，对应一张表操作
>
> 2、映射文件放在`src/java/resoures/mappers`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.lyxlz.mybatis.mapper.UserMapper">

    <insert id="insertUser">
        insert into t_user values(null, 'admin', '123456', 23, '男', '123456@qq.com')
    </insert>
    
</mapper>
```

注意：

- mapper标签中的`namespace`要和`映射接口全类名`**保持一致**
- insert等标签的`id`要和`映射接口中的方法名`**保持一致**
- 最后要在`核心配置文件`中引入该映射文件。

### 6、测试添加功能

```java
public class MyBaitsTest {

    @Test
    public void testMyBatis() throws IOException {
        // 加载核心配置文件
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        // 获取SqlSessionFactoryBuilder
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        // 获取SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        // 获取SqlSession，boolean类型的参数表示自动提交事务
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int result = userMapper.insertUser();
        System.out.println("结果：" + result);
    }

}
```

### 7、加入日志功能

#### 导入依赖

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

#### 创建配置文件: log4j.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration STSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
    <appender name="STDOUT" class="org.apache.log4j.ConsoleAppender">
        <param name="Encoding" value="UTF-8" />
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%-5p %d{MM-dd HH:mm:ss,SSS} %m(%F:%L) \n" />
        </layout>
    </appender>
    <logger name="java.sql">
        <level value="debug" />
    </logger>
    <logger name="org.apache.ibatis">
        <level value="info" />
    </logger>
    <root>
        <level value="debug" />
        <appender-ref ref="STDOUT" />
    </root>
</log4j:configuration>
```

> **日志的级别**
>
> FATAL(致命) > ERROR(错误) > WARN(警告) > INFO(信息) > DEBUG(调试)



## 二、核心配置文件

### 1、mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd" >
<configuration>
    <!--
        MyBatis核心配置文件中，标签的顺序：
        properties
        settings
        typeAliases
        typeHandlers
        objectFactory
        objectWrapperFactory
        reflectorFactory
        plugins
        environments
        databaseIdProvider
        mappers
    -->
    <!--
        properties: 引入properties文件
        属性:
            resource=要引入的properties文件
    -->
    <properties resource="jdbc.properties"/>
    <!--
        typeAliases: 设置类型别名
    -->
    <typeAliases>
        <!--
            typeAlias
            属性:
                type=要设置别名的全类名
                alias=别名
        -->
        <typeAlias type="cn.lyxlz.mybatis.pojo.User" alias="User" />
        <!--
            package: 以包为单位，将包下所有的类名都以类名作为别名，不区分大小写
            属性:
                name=要设置别名的包
        -->
        <package name="cn.lyxlz.mybatis.pojo"/>
    </typeAliases>
    <!--
        environments: 配置多个连接数据库的环境
        属性：
            default: 默认激活的数据库连接
    -->
    <environments default="development">
        <!--
            environment: 配置某个具体的环境
            属性:
                id: 表示环境的唯一标识
        -->
        <environment id="development">
            <!--
                transactionManager: 配置事务管理方式
                属性:
                    type="JDBC | MANAGED"
                        JDBC: 表示当前环境中，执行SQL看时，使用的是JDBC中原生的事务管理方式，事务的提交或回滚需要手动处理
                        MANAGED: 被管理，例如Spring
            -->
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <!--
                      dataSource: 配置数据源
                      属性:
                          type: 设置数据源的类型
                          type="POOLED | UNPOOLED | JNDI"
                              POOLED: 表示使用数据库连接池魂村数据库连接
                              UNPOOLED: 表示不使用数据库连接池
                              JNDI: 表示使用上下文中的数据源
                  -->
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>

    <!--引入映射文件-->
    <mappers>
        <!--<mapper resource="mappers/UserMapper.xml"/>-->
        <!--
            package: 以包为单位引入映射文件
            要求:
                mapper接口所在的包要和映射文件所在的包一致
                mapper接口要和映射文件名字一致
        -->
        <package name="cn.lyxlz.mybatis.mapper"/>
    </mappers>
</configuration>

```



## 三、MyBatis获取参数值的两种方式

```java
/**
 * Mybatis获取参数值的方式有两种：
 *  ${} 本质是字符串的拼接
 *  #{} 本质是占位符赋值
 *  MyBatis获取参数值的各种情况
 *      1、mapper接口方法的参数为单个的字面量类型
 *          可以直接通过${}和#{}获取，字段名可取任意，注意${}需要加单引号
 *      2、mapper接口方法的参数有多个时
 *          仍然使用${}和#{}，但字段名需对应参数位置，MyBatis会将数据存在一个map集合中，用arg0， arg1或param1，param2获取
 *      3、手动指定多个参数的字段名
 *          仍然使用${}和#{}，但接口方法需使用Map集合，且调用时传入的键需和#{}中的字段名对应
 *      4、mapper接口方法参数为实体类对象
 *          直接通过属性名访问
 *      5、使用@Param注解命名参数
 *          在mapper接口的方法参数上使用@Param注解直接命名字段名
 */
```

### 1、mapper接口方法的参数为单个的字面量类型

```xml
<!--List<User> getAllUser();-->
<select id="getAllUser" resultType="User">
    select * from t_user
</select>
```

### 2、mapper接口方法的参数有多个时

```xml
<!--User checkLogin(String username, String password);-->
<select id="checkLogin" resultType="User">
    select * from t_user where username = #{arg0} and password = #{arg1}
</select>
```

### 3、手动指定多个参数的字段名

```xml
<!--User checkLoginByMap(Map<String, Object> map);-->
<select id="checkLoginByMap" resultType="User">
    select * from t_user where username = #{username} and password = #{password}
</select>
```

### 4、mapper接口方法参数为实体类对象

```xml
<!--User insertUser(User user);-->
<insert id="insertUser">
    insert into t_user values (null, #{username}, #{password}, #{age}, #{sex}, #{email})
</insert>
```

### 5、使用@Param注解命名参数

```xml
<!--User checkLoginByParam(@Param("username") String username, @Param("password") String password);-->
<select id="checkLoginByParam" resultType="User">
    select * from t_user where username = #{username} and password = #{password}
</select>
```

## 四、MyBatis中的各种查询功能

### 1、查询一个实体类对象

直接使用实体类作为方法返回值

```java
User getUserById(@Param("id") Integer id);
```

### 2、查询多个实体类对象

使用集合接收多个实体类对象

```java
List<User> getAllUser();
```

### 3、查询一条数据

MyBatis中自带对基本数据类型和集合类型的别名

- int -> int、_int
- Integer -> inetget、int
- Map -> map
- List -> list

```java
// 获取所有字段的数量
Integer getCount();
```

### 4、查询一条数据为map集合

```java
// 根据id查找用户，并将每一个字段名设为map的key
Map<String, Object> getUserByIdToMap(@Param("id") Integer id);
```

### 5、查询多条数据为map集合

方法一

```java
// 查询所有用户，并将每个用户赋给map
List<Map<String, Object>> getAllUserToMap();
```

方法二

```java
// 查询所有用户，并按照id为键，其他属性为map的方式存放
@MapKey("id")
Map<String, Object> getAllUserToMapKey();
```

## 五、特殊SQL执行

### 1、模糊查询

SQL中like后的匹配字符需要加`'%%'`，#{}占位符解析时会被当做字符串，导致无法设置字段名，可以使用以下几种方式

```java
List<User> getUserByLike(@Param("username") String username);
```

```xml
<select id="getUserByLike" resultType="cn.lyxlz.mybatis.pojo.User">
    <!--select * from t_user where username like '%${username}%'-->
    <!--select * from t_user where username like concat('%', #{username}, '%')-->
    select * from t_user where username like "%"#{username}"%"
</select>
```

### 2、批量删除

由于`#{}`会自动为字段添加单引号，所以在删除操作中，只能使用`${}`

```java
int deleteMore(@Param("ids") String ids);
```

```xml
<delete id="deleteMore">
    delete from t_user where id in (${ids})
</delete>
```

### 3、动态设置表名

只能使用`${}`理由同上

```java
List<User> getUserByTable(@Param("tablename") String tablename);
```

```xml
<select id="getUserByTable" resultType="cn.lyxlz.mybatis.pojo.User">
    select * from ${tablename}
</select>
```

### 4、获取添加自增功能的主键

```java
void insertUser(User user);
```

```xml
<!--
    userGenerateKeys：设置当前标签中sql使用了的自增标签
    keyProperty：将自增主键作为属性返回
-->
<insert id="insertUser" useGeneratedKeys="true" keyProperty="id">
    insert into t_user values (null, #{username}, #{password}, #{age}, #{sex}, #{email})
</insert>
```

## 六、字段名和属性名不一致问题

开发中实体类（驼峰）与mysql字段名（下划线）的命名规则不一样，MyBatis处理映射时无法正确获取数据，可以使用以下几种方式解决

### 1、字段别名

```xml
<select id="getAllEmp" resultType="cn.lyxlz.mybatis.pojo.Emp">
    select eid, emp_name as empName, age, sex, email, did from t_emp;
</select>
```

### 2、MyBatis全局配置

**mybatis-config.xml**

```xml
<!--设置MyBatis的全局配置-->
<settings>
    <!--设置下划线转驼峰-->
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

### 3、resultMap

**EmpMapper.xml**

```xml
<resultMap id="empResultMap" type="Emp">
    <!--设置主键-->
    <id property="eid" column="eid"/>
    <!--设置其他字段-->
    <result property="empName" column="emp_name" />
    <result property="age" column="age" />
    <result property="sex" column="sex" />
    <result property="email" column="email" />
</resultMap>

<select id="getAllEmp" resultMap="empResultMap">
    select * from t_emp
</select>
```

## 七、复杂映射关系

### 1、多对一

#### 通过级联属性赋值

在`“一”`中设置对应实体类的`对象`，在`“多”`中设置对应实体类对象的`集合`

```java
public class Emp {
    private Integer eid;
    private String empName;
    private Integer age;
    private char sex;
    private String email;
    private Dept dept;
}
```

```java
public class Dept {
    private Integer did;
    private String deptName;
    private List<Emp> emp;
}
```

在xml映射文件中，需要使用resultMap设置字段对应，对于对象字段的属性需要用`".字段"`设置

```xml
<resultMap id="empAndDeptResultMap" type="Emp">
    <!--设置主键-->
    <id property="eid" column="eid"/>
    <!--设置其他字段-->
    <result property="empName" column="emp_name" />
    <result property="age" column="age" />
    <result property="sex" column="sex" />
    <result property="email" column="email" />
    <result property="dept.did" column="did" />
    <result property="dept.deptName" column="dept_name" />
</resultMap>

<select id="getEmpAndDept" resultMap="empAndDeptResultMap">
    SELECT
        e.eid,
        e.emp_name,
        e.age,
        e.sex,
        e.email,
        e.did,
        d.dept_name
    FROM
        t_emp e
    JOIN t_dept d ON e.did = d.did
</select>
```

#### 使用association标签

**EmpMapper**

```xml
<resultMap id="empAndDeptResultMapTwo" type="Emp">
    <!--设置主键-->
    <id property="eid" column="eid"/>
    <!--设置其他字段-->
    <result property="empName" column="emp_name" />
    <result property="age" column="age" />
    <result property="sex" column="sex" />
    <result property="email" column="email" />
    <!--
        association:
           property: 映射字段
           javaType: 映射的实体类
    -->
    <association property="dept" javaType="Dept">
        <!--主键-->
        <id property="did" column="did" />
        <!--其他字段-->
        <result property="deptName" column="dept_name" />
    </association>
</resultMap>
```

#### 分步骤查询

首先查询员工表中的数据

```java
Emp getEmpAndDeptByStepOne(@Param("eid") Integer eid);
```

```xml
<resultMap id="empAndDeptByStepResultMap" type="Emp">
    <id property="eid" column="eid"/>
    <result property="empName" column="emp_name" />
    <result property="age" column="age" />
    <result property="sex" column="sex" />
    <result property="email" column="email" />
    <!--
		 property: 对应的实体类属性
		 select  : 下一步查询方法
		 column  : 对应的字段（类似于join on中的on）
	-->
    <association 
                 property="dept"
                 select="cn.lyxlz.mybatis.mapper.DeptMapper.getEmpAndDeptByStepTwo"
    			 column="did" />
    
</resultMap>

<select id="getEmpAndDeptByStepOne" resultMap="empAndDeptByStepResultMap">
    select * from t_emp where eid = #{eid}
</select>
```

再查询对应部门表中的数据

```java
Dept getEmpAndDeptByStepTwo(@Param("did") Integer did);
```

```xml
<select id="getEmpAndDeptByStepTwo" resultType="cn.lyxlz.mybatis.pojo.Dept">
    select * from t_dept where did = #{did}
</select>
```

测试时调用第一步即可

```java
@Test
public void testGetEmpAndDeptByStep() {
    Emp emp = mapper.getEmpAndDeptByStepOne(3);
    System.out.println(emp);
}
```
