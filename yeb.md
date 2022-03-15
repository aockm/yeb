

<img src="E:\blog\yeb\assets\yeb\image-20220126223924071.png" alt="image-20220126223924071" style="zoom:67%;" />



### 1、



```xml
<!-- web依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!-- lombok依赖 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<!-- mysql依赖 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
	<scope>runtime</scope>
</dependency>
<!-- mybatis-plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.3.1.tmp</version>
</dependency>
<!-- swagger2 依赖 -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.7.0</version>
</dependency>
<!--swagger第三方ui依赖-->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>swagger-bootstrap-ui</artifactId>
    <version>1.9.6</version>
</dependency>

```







```yml
server:
  port: 8081

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: "jdbc:mysql://localhost:3306/yeb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai"
    username: root
    password: 123456
  	hikari:
      pool-name: DataHikariCP
      minimum-idle: 5
      idle-timeout: 1800000
      maximum-pool-size: 10
      auto-commit: true
      max-lifetime: 1800000
      connection-timeout: 30000
      connection-test-query: SELECT 1


mybatis-plus:
  mapper-locations: classpath*:/mapper/*Mapper.xml
  #配置mybatis数据返回类型别名
  type-aliases-package: com.tockm.server.pojo
  configuration:
    # 自动驼峰命名
    map-underscore-to-camel-case: false

logging:
  level:
    com.tockm.server.mapper: debug 

```

```yml
jwt:
  # Jwt存储的请求头
  tokenHeader: Authorization
  # Jwt加密秘钥
  secret: yeb-secret
  # Jwt 的超期限时间（60*60）*24
  expiration: 604800
  # Jwt负载中拿到开头
  tokenHead: Bearer
```







```xml
```





#### CodeGenerator代码生成器类

```java
package com.tockm.generctor;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        final String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/yeb-generator/src/main/java");
        gc.setAuthor("tockm"); //作者
        gc.setOpen(false); //是否打开目录
        gc.setBaseResultMap(true);//xml开启BaseResultMap
        gc.setBaseColumnList(true);//xml 开启BaseColumnList
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/yeb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        final PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
        pc.setParent("com.tockm.server")
                .setEntity("pojo")
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl")
                .setController("controller");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/yeb-generator/src/main/resources/mapper/"  + tableInfo.getEntityName() +
                        "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.no_change);
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        //lombok模型
        strategy.setEntityLombokModel(true);
        //生成RestController
        strategy.setRestControllerStyle(true);
        // 公共父类
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        //表前缀
        strategy.setTablePrefix("t_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }


}
```







#### security和jwt依赖



```xml
<!--security 依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<!--JWT依赖-->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.0</version>
</dependency>
```

#### jwt配置

```yml
jwt:
  # Jwt存储的请求头
  tokenHeader: Authorization
  # Jwt加密秘钥
  secret: yeb-secret
  # Jwt 的超期限时间（60*60）*24
  expiration: 604800
  # Jwt负载中拿到开头
  tokenHead: Bearer
```







#### 验证码依赖

```xml
<!--google kaptcha依赖-->
<dependency>
    <groupId>com.github.axet</groupId>
    <artifactId>kaptcha</artifactId>
    <version>0.0.9</version>
</dependency>
```



验证码生成类

```java
 @Bean
    public DefaultKaptcha defaultKaptcha(){
        // 验证码生成器
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        // 配置
        Properties properties = new Properties();
        // 是否有边框
        properties.setProperty("kaptcha.border","yes");
        // 设置边框颜色
        //properties.setProperty("kaptcha.border.color","105,179,90");
        properties.setProperty("kaptcha.border.color","224,224,224");
        // 边框粗细度，默认为1
        properties.setProperty("kaptcha.border.thickness","1");
        // 验证码 session key
        properties.setProperty("kaptcha.session.key", "code");
        // 设置验证码文本字符颜色，默认黑色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 设置字体样式
        properties.setProperty("kaptcha.textproducer.font.names", "微软雅黑");
        //properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "30");


        // 验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 验证码字符间距 默认是2
        //properties.setProperty("kaptcha.textproducer.char.space", "4");
        // 验证码图片宽度，默认为 200
        properties.setProperty("kaptcha.image.width", "100");
        // 验证码图片高度，默认为40
        properties.setProperty("kaptcha.image.height", "40");
        // 没有干扰
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");// 没有干扰

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
```





输出验证码流

```java

		// 定义 response 输出类型为 image/jpeg 类型
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
        response.addHeader("Cache-Control", "post-check=0,pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        // 生成验证码 开始
```







### 菜单栏

redis依赖

```xml
<!--spring data redis 依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!--commons-pool2 对象依赖-->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```



redis配置

```yml
redis:
    #超时时间
    timeout: 10000ms
    host: localhost
    port: 6379
    database: 0
    lettuce:
      pool:
        #最大连接数 默认8
        max-active: 1024
        #最大连接阻塞等待时间 默认-1
        max-wait: 10000ms
        #最大空闲连接
        max-idle: 200
        min-idle: 5
```



#### 递归查询



```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.tockm.server.mapper.DepartmentMapper">

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="com.tockm.server.pojo.Department">
        <id column="id" property="id" />
        <result column="name" property="name" />
        <result column="parent_id" property="parentId" />
        <result column="dep_path" property="depPath" />
        <result column="enabled" property="enabled" />
        <result column="is_parent" property="isParent" />
    </resultMap>
    
    <resultMap id="DepartmentWithChildren" type="com.tockm.server.pojo.Department" extends="BaseResultMap">
        <collection property="children" ofType="com.tockm.server.pojo.Department" select="getAllDepartments" column="id">
        </collection>
    </resultMap>

    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        id, name, parent_id, dep_path, enabled, is_parent
    </sql>
    
    
    <!--获取所有部门-->
    <select id="getAllDepartments" resultMap="DepartmentWithChildren" >
      select
      <include refid="Base_Column_List"/>
      from t_department
      where parent_id = #{parentId}
    </select>

</mapper>
```



​	

### 存储过程



存储过程是什么

为什么要使用存储过程

存储过程怎样用



### 导入和导出（Easy POI)

#### Apache POI

开发中涉及到Excel的处理，如导出Excel，导入Excel到数据库中，操作Excel等。

#### Easy POI



 **EasyPoi的主要特点：**

1. 设计精巧,使用简单
2. 接口丰富,扩展简单
3. 默认值多,write less do more
4. AbstractView 支持,web导出可以简单明了



 什么场景该用哪个方法

导入导出Excel



```xml
<!--easy poi 依赖-->
<dependency>
    <groupId>cn.afterturn</groupId>
    <artifactId>easypoi-spring-boot-starter</artifactId>
    <version>4.2.0</version>
</dependency>
```





```java
@ApiOperation(value = "导入员工数据")
    @PostMapping("/inport")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        // 去掉标题行
        params.setTitleRows(1);
        List<Nation> nationList = nationService.list();
        List<Position> positionList = positionService.list();
        List<Department> departmentList = departmentService.list();
        List<Joblevel> joblevelList = joblevelService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            list.forEach(employee -> {
                // 民族id
                Integer nid = nationList.get(nationList.indexOf(new Nation(employee.getNation().getName()))).getId();
                //政治面貌id
                Integer pid = politicsStatusList.get(politicsStatusList.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId();
                // 部门id
                Integer did = departmentList.get(departmentList.indexOf(new Department(employee.getDepartment().getName()))).getId();
                // 职称id
                Integer jid = joblevelList.get(joblevelList.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId();
                // 职位id
                Integer posid = positionList.get(positionList.indexOf(new Position(employee.getPosition().getName()))).getId();
                employee.setNationId(nid);
                employee.setPoliticId(pid);
                employee.setDepartmentId(did);
                employee.setJobLevelId(jid);
                employee.setPosId(posid);
            });
            if (employeeService.saveBatch(list)){
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败！");
    }
```







### 邮件服务



#### 授权码：

WVYGKGWAZSQVZHKT

#### 依赖:

```xml
<!--rabbitmq 依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
<!--mail 依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
<!--thymeleaf 依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
<!--server 依赖-->
<dependency>
    <groupId>com.tockm</groupId>
    <artifactId>yeb-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```





application.yml

```yml
server:
  port: 8082

spring:
  # 邮件配置
  mail:
    host: smtp.163.com
    protocol: smtp
    default-encoding: UTF-8
    password: NZHOSHWRKWQZTXMG
    username: z17631089214@163.com
    port: 25
  rabbitmq:
    username: guest
    password: guest
    host: 39.102.65.157
    port: 5672
    listener:
      simple:
        acknowledge-mode: manual
  redis:
    timeout: 10000ms
    host: 39.102.65.157
    port: 6379
    database: 0 # 选择哪个库，默认0库
    lettuce:
      pool:
        max-active: 1024 # 最大连接数，默认 8
        max-wait: 10000ms # 最大连接阻塞等待时间，单位毫秒，默认 -1
        max-idle: 200 # 最大空闲连接，默认 8
        min-idle: 5

```







```java
// 获取所有员工账套
    @Override
    public RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size) {

        //Page<Employee> page = new Page<>(currentPage,size);
        //IPage<Employee>  employeeIPage = employeeMapper.getEmployeeWithSalary(page);
        //RespPageBean respPageBean = new RespPageBean(employeeIPage.getTotal(), employeeIPage.getRecords());
        //return respPageBean;
        return null;

    }
```

```xml
<!--获取所有员工账套-->
    <!-- <select id="getEmployeeWithSalary" resultMap="EmployeeWithSalary">
        SELECT
            e.*,
            d.`name` AS dname,
            s.id AS sid,
            s.`name` AS sname,
            s.basic_salary AS sba,
            s.bonus AS sb,
            s.lunch_salary AS sls,
            s.traffic_salary AS sta,
            s.all_salary AS sas,
            s.pension_base AS spb,
            s.pension_per AS spp,
            s.medical_base AS smb,
            s.medical_per AS smp,
            s.accumulation_fund_base AS safb,
            s.accumulation_fund_per AS safp
        FROM
            t_employee e
            LEFT JOIN t_salary s ON e.salary_id = s.id
            LEFT JOIN t_department d ON e.department_id = d.id
        ORDER BY
            e.id
    </select>-->

 <resultMap id="EmployeeWithSalary" type="com.tockm.server.pojo.Employee" extends="BaseResultMap">
        <association property="salary" javaType="com.tockm.server.pojo.Salary ">
            <id column="sid" property="id"/>
            <result column="sname" property="name"/>
            <result column="sbs" property="basicSalary"/>
            <result column="sb" property="bonus"/>
            <result column="sls" property="lunchSalary"/>
            <result column="sts" property="trafficSalary"/>
            <result column="sas" property="allSalary"/>
            <result column="spb" property="pensionBase"/>
            <result column="spp" property="pensionPer"/>
            <result column="smb" property="medicalBase"/>
            <result column="smp" property="medicalPer"/>
            <result column="safb" property="accumulationFundBase"/>
            <result column="safp" property="accumulationFundPer"/>
        </association>
        <association property="department" javaType="com.tockm.server.pojo.Department">
            <result column="dname" property="name"/>
        </association>
    </resultMap>
```

```java
@ApiOperation(value = "获取所有员工账套")
    @GetMapping("/")
    public RespPageBean getEmployeeWithSalary(@RequestParam(defaultValue = "1") Integer currentPage,
                                              @RequestParam(defaultValue = "10") Integer size){
        //return employeeService.getEmployeeWithSalary(currentPage,size);
        return null;
    }
```









### 在线聊天 WebSocket





#### 依赖

```xml
<!--webSocket-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```





### 个人中心



#### 更新密码



#### 更新头像



##### FastDFS

##### 依赖

```xml
<!-- https://mvnrepository.com/artifact/org.csource/fastdfs-client-java -->
<dependency>
    <groupId>org.csource</groupId>
    <artifactId>fastdfs-client-java</artifactId>
    <version>1.27-RELEASE</version>
</dependency>
```

```xml
<!-- https://mvnrepository.com/artifact/com.github.tobato/fastdfs-client -->
<dependency>
    <groupId>com.github.tobato</groupId>
    <artifactId>fastdfs-client</artifactId>
    <version>1.26.2</version>
</dependency>
```

配置文件



```conf
#连接超时
connect_timeout = 2
#网络超时
network_timeout = 30
#编码格式
charset = UTF-8
#tracker端口号
#http.tracker_http_port = 8080
#防盗链功能
http.anti_steal_token = no
#秘钥
http.secret_key = FastDFS1234567890
#tracker ip：端口号
tracker_server = 39.102.65.157:22122
#连接池配置
connection_pool.enabled = true
connection_pool.max_count_per_entry = 500
connection_pool.max_idle_time = 3600
connection_pool.max_wait_time_in_ms = 1000
```





**2022_1_26**
