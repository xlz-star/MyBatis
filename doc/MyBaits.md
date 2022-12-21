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

> 分步查询的优点：
>
> 可以实现延迟加载，但是必须在核心配置文件中设置
>
> lazyLoadingEnabled:  延迟加载开关，当开启时，所有关联对象都会延迟加载
>
> aggressiveLazyLoading: 当开启时，任意方法的调用都会加载该对象的所有属性。否则，每个属性按需加载
>
> 此时就可以实现按需加载，获取的数据是什么，就会执行相应的SQL。此时可通过association和collection中的fetchType属性设置当前分布查询是否使用延迟加载，fetchType="lazy(延迟加载)|eager(立即加载)"

### 2、一对多

在`“多”`中设置对应实体类对象的`集合`

```java
public class Dept {
    private Integer did;
    private String deptName;
    private List<Emp> emp;
}
```

#### 使用collection标签

```xml
<resultMap id="DeptAndEmpResultMap" type="Dept">
    <id property="did" column="did"/>
    <result property="deptName" column="dept_name" />
    <!--
		collection:
			property: 对应的属性
			ofType: 集合内对应的实体类
	-->
    <collection property="emps" ofType="Emp">
        <id property="eid" column="eid" />
        <result property="empName" column="emp_name" />
        <result property="age" column="age" />
        <result property="sex" column="sex" />
        <result property="email" column="email" />
    </collection>
</resultMap>

<select id="getDeptAndEmp" resultMap="DeptAndEmpResultMap">
    select * from t_dept d left join t_emp e on e.did = d.did
</select>
```

#### 分步骤查询

由于前面已经写过获取所有Emp的代码，这里直接调用`EmpMapper.getAllEmp`方法

```xml
<resultMap id="DeptAndEmpByStepResultMap" type="Dept">
    <id property="did" column="did"/>
    <result property="deptName" column="dept_name" />
    <association property="emps"
                 select="cn.lyxlz.mybatis.mapper.EmpMapper.getAllEmp"
                 column="did"
    />
</resultMap>

<select id="getDeptAndEmpByStep" resultMap="DeptAndEmpByStepResultMap">
    select * from t_dept
</select>
```

## 八、动态SQL

> MyBatis框架的动态SQL技术是一种根据特定条件动态拼装SQL语句的功能，它存在的意义是为了解决拼接SQL语句字符串时的痛点问题。

### 1、if

在编写SQL时，当某些条件缺失或不满足规则时，我们不需要执行where后的语句，这时我们可以使用`if`标签让MyBatis帮我们自动判断是否要拼接SQL条件。

如当某些字段为null和空时，不拼接条件。这里的`1=1`是为了避免所有的if标签都不满足，单独出现where造成SQL语法错误。

```xml
<select id="getEmpByCondition" resultType="cn.lyxlz.mybatis.pojo.Emp">
        select * from t_emp where 1=1
        <if test="empName != null and empName != ''">
            emp_name = #{empName}
        </if>
        <if test="age != null and age != ''">
            and age = #{age}
        </if>
        <if test="sex != null and sex != ''">
            and sex = #{sex}
        </if>
        <if test="email != null and email != ''">
            and email = #{email}
        </if>
</select>
```

### 2、where

为了解决上面提到的where关键字问题，MyBatis提供了`where`标签来动态的拼接where关键字

> 注意：where标签中的`and`和`or`关键字，只能写在条件语句前，写在后面无法对多余的and和or进行删除

```xml
<select id="getEmpByCondition" resultType="cn.lyxlz.mybatis.pojo.Emp">
    select * from t_emp
    <where>
        <if test="empName != null and empName != ''">
            emp_name = #{empName}
        </if>
        <if test="age != null and age != ''">
            and age = #{age}
        </if>
        <if test="sex != null and sex != ''">
            and sex = #{sex}
        </if>
        <if test="email != null and email != ''">
            and email = #{email}
        </if>
    </where>
</select>
```

### 3、trim

为了解决where标签只能处理and和or在语句前的情况，MyBatis提供了`trim`标签来处理前后指定字符

>trim:
>
>​	prefix | suffix: 将trim标签中内容前面或后面`添加`指定内容
>
>​	suffixOverrides | prefixOverrides: 将trim标签中内容前面或后面`删除`指定内容
>
>​	若trim中的条件一个都不满足，则trim不会做任何处理

```xml
<select id="getEmpByCondition" resultType="cn.lyxlz.mybatis.pojo.Emp">
    select * from t_emp
    <!--语句前添加where，语句后的and和or去掉-->
    <trim prefix="where" suffixOverrides="and|or">
        <if test="empName != null and empName != ''">
            emp_name = #{empName}
        </if>
        <if test="age != null and age != ''">
            age = #{age} and 
        </if>
        <if test="sex != null and sex != ''">
            sex = #{sex} and 
        </if>
        <if test="email != null and email != ''">
            email = #{email}
        </if>
    </trim>
</select>
```

### 4、choose、when、otherwise

效果同if标签，但是效果同java中的`if-else if-else`，当某个判断分支成立时，`不再`继续判断

```xml
<select id="getEmpByChoose" resultType="cn.lyxlz.mybatis.pojo.Emp">
    select * from t_emp
    <where>
        <choose>
            <when test="empName != null and empName != ''">
                emp_name = #{empName}
            </when>
            <when test="age != null and age != ''">
                age = #{age}
            </when>
            <when test="sex != null and sex != ''">
                sex = #{sex}
            </when>
            <when test="email != null and email != ''">
                email = #{email}
            </when>
            <otherwise>
                did = 1
            </otherwise>
        </choose>
    </where>
</select>
```

### 5、foreach

用于在处理传入参数为`数组`时的数据摆放问题

例如：批量删除

**法一**：

```java
int deleteMoreByArray(@Param("eids") Integer[] eids);
```

```xml
<delete id="deleteMoreByArray">
    delete from t_emp where eid in
    <!--
       foreach:
        collection: 要循环的数组
        item: 每次循环出来的元素
        separator: 分隔符
		open: 循环内容以什么开始(所有内容)
		close: 循环内容以什么结束(所有内容)
      -->
    <foreach collection="eids" item="eid" separator="," open="(" close=")">
        #{eid}
    </foreach>
</delete>
```

**法二**：

```xml
<delete id="deleteMoreByArray">
    delete from t_emp where
    <foreach collection="eids" item="eid" separator="or">
        eid = #{eid}
    </foreach>
</delete>
```

例如：批量添加

```java
int insertMoreByArray(@Param("emps") List<Emp> emps);
```

```xml
<insert id="insertMoreByArray">
    insert into t_emp values
    <foreach collection="emps" item="emp" separator=",">
        (null, #{emp.empName}, #{emp.age}, #{emp.sex}, #{emp.email}, null)
    </foreach>
</insert>
```

### 6、sql

用于定义一些常用sql代码段，如大段的列名

```xml
<sql id="empCol">eid, emp_name, age, sex, email</sql>
```

调用时使用`include`标签即可

```xml
select <include refid="empCol" /> from t_emp
```

## 九、MyBatis缓存

### 1、一级缓存

一级缓存是SqlSession级别的，通过同一个SqlSeesion查询的数据会被缓存，下次查询相同的数据，就会从缓存中直接获取，不会从数据库重新访问

使一级缓存失效的四种情况：

1. 同的SqlSession对应不同的一级缓存
2. 同一个SqlSession但是查询条件不一样
3. 同一个SqlSession两次查询期间执行了任何一次增删改操作
4. 同一个SqlSession两次查询期间手动清空了缓存(sqlSession.clearCache())

### 2、二级缓存

二级缓存是SqlSessionFactory级别，通过同一个SqlSessionFactory创建的SqlSession查询的结果会被缓存；此后再次执行相同的查询语句，结果就会从缓存中获取

二级缓存开启的条件：

1. 在核心配置文件中，设置全局配置属性cacheEnabled="true"，默认为true
2. 在映射文件中设置标签<cache />
3. 二级缓存必须在SqlSession关闭或提交之后有效
4. 查询的数据所转换的实体类类型必须实现序列化接口

二级缓存失效的情况

两次查询之间执行任意增删改，都会使一二级缓存失效。

### 3、二级缓存相关配置

在mapper配置文件中添加cache标签可以设置一些属性：

- eviction属性：缓存回收策略

​		LRU（Least Recently Used）- 最近最少使用的：移除最长时间不被使用的对象

​		FIFO（First in First out）- 先入先出：按对象进入缓存的顺序移除

​		SOFT - 软引用：移除基于垃圾回收器状态和软引用规则的对象

​		WEAK - 弱引用：更积极的移除基于垃圾回收器状态和弱引用规则的对象

- flushInterval属性：刷新间隔，单位为秒

​		默认不设置，缓存仅仅是在调用语句时刷新

- size属性：引用数目：正整数

​		代表缓存最多可以存储多少个对象，太大容易OOM

- readOnly属性：只读，true/false

​		true：只读缓存：会给调用者返回缓存对象的相同实例。因此这些对象不可修改。拥有很高的性能

​		false：读写缓存：会返回缓存对象的拷贝（序列化）。性能会受影响，但安全，默认为false

### 4、MyBatis缓存查询的顺序

先查二级缓存，因为二级缓存中可能有其他SqlSession已经查出来的数据，可以拿来直接使用

如果二级缓存没有，再查询一级缓存

如果一级缓存没有，则查询数据库

SqlSession关闭后，一级缓存中的数据会写入二级缓存

### 5、整合第三方缓存EHCache

#### 添加依赖

```xml
<dependency>
    <groupId>org.mybatis.caches</groupId>
    <artifactId>mybatis-ehcache</artifactId>
    <version>1.2.3</version>
</dependency>

<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.5</version>
</dependency>
```

#### 创建EHCache配置文件

ehcache.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd">

  <!-- 磁盘缓存位置 -->
  <diskStore path="java.io.tmpdir/ehcache"/>

  <!-- 默认缓存 -->
  <defaultCache
          maxEntriesLocalHeap="10000"
          eternal="false"
          timeToIdleSeconds="120"
          timeToLiveSeconds="120"
          maxEntriesLocalDisk="10000000"
          diskExpiryThreadIntervalSeconds="120"
          memoryStoreEvictionPolicy="LRU">
    <persistence strategy="localTempSwap"/>
  </defaultCache>

  <!-- helloworld缓存 -->
  <cache name="HelloWorldCache"
         maxElementsInMemory="1000"
         eternal="false"
         timeToIdleSeconds="5"
         timeToLiveSeconds="5"
         overflowToDisk="false"
         memoryStoreEvictionPolicy="LRU"/>
</ehcache>
```

#### 指定二级缓存类型

mapper映射文件中

```xml
<cache type="org.mybatis.caches.ehcache.EhcacheCache" />
```

### 配置logback日志

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration debug="true">
    <appender name="STDOUT"
        class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>[%d{HH:mm:ss.SSS}] [%-5level] [%thread] [%logger] [%msg]%n</pattern>
        </encoder>
    </appender>

    <root level="DEBUG">
        <appender-ref ref="STDOUT" />
    </root>
    
    <logger name="cn.lyxlz.mybatis.mapper" level="DEBUG" />
</configuration>
```

## 十、MyBatis逆向工程

- 正向工程：先创建Java实体类，有框架根据实体类生成数据库表，Hibernate是支持正向工程的。
- 逆向工程：先创建数据库表，由框架根据数据库表，反向生成如下资源：
  - java实体类
  - Mapper接口
  - Mapper映射文件

### 1、创建逆向工程的步骤

#### 添加依赖和插件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.lyxlz.mybatis</groupId>
    <artifactId>MyBatis_MBG</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.9</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.0</version>
                <dependencies>
                    <dependency>
                        <groupId>org.mybatis.generator</groupId>
                        <artifactId>mybatis-generator-core</artifactId>
                        <version>1.3.2</version>
                    </dependency>
                    <dependency>
                        <groupId>com.mchange</groupId>
                        <artifactId>c3p0</artifactId>
                        <version>0.9.2</version>
                    </dependency>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.29</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>

</project>
```

#### 创建MyBatis核心配置文件

#### 创建逆向工程配置文件

文件名必须是：generatorConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--
        targetRuntime: 执行逆向工程的版本
            MyBatis3: 带注释和条件的CRUD
            MyBatis3Simple: 生成基本的CRUD
    -->
    <context id="DB2Tables"  targetRuntime="MyBatis3">

        <!-- 数据库连接驱动类,URL，用户名、密码 -->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true&amp;characterEncoding=UTF-8"
                        userId="root"
                        password="123456">
        </jdbcConnection>

        <!-- java类型处理器：处理DB中的类型到Java中的类型 -->
        <javaTypeResolver type="org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl">
            <!-- 是否有效识别DB中的BigDecimal类型 -->
            <property name="forceBigDecimals" value="true"/>
        </javaTypeResolver>

        <!-- 生成Domain模型：包名(targetPackage)、位置(targetProject) -->
        <javaModelGenerator targetPackage="cn.lyxlz.mybatis.pojo" targetProject="./src/main/java">
            <!-- 在targetPackage的基础上，根据数据库的schema再生成一层package，最终生成的类放在这个package下，默认为false -->
            <property name="enableSubPackages" value="true"/>
            <!-- 设置是否在getter方法中，对String类型字段调用trim()方法-->
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>

        <!-- 生成xml映射文件：包名(targetPackage)、位置(targetProject) -->
        <sqlMapGenerator targetPackage="cn.lyxlz.mybatis.mapper" targetProject="./src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>

        <!-- 生成DAO接口：包名(targetPackage)、位置(targetProject) -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="cn.lyxlz.mybatis.mapper" targetProject="./src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>

        <!-- 要生成的表：tableName - 数据库中的表名或视图名，domainObjectName - 实体类名 -->
        <table tableName="t_emp" domainObjectName="Emp" />
        <table tableName="t_dept" domainObjectName="Dept" />
    </context>
</generatorConfiguration>
```

#### 使用插件生成代码

![image-20221221200902432](https://img-1304774017.cos.ap-nanjing.myqcloud.com/img/image-20221221200902432.png)

## 十一、分页插件

### 1、使用步骤

#### 添加依赖

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.3.1</version>
</dependency>
```

#### 配置分页插件

mybatis-config.xml

```xml
<plugins>
    <plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
</plugins>
```
