# IDEA 搭建springBoot项目

## 概念

Spring Boot 是 Spring 开源组织下的子项目，是 Spring 组件一站式解决方案，主要是简化了使用 Spring 的难度，简省了繁重的配置，提供了各种启动器，开发者能快速上手。

### 优点

1. 容易上手，提升开发效率，为 Spring 开发提供一个更快、更广泛的入门体验。
2. 开箱即用，远离繁琐的配置。
3. 提供了一系列大型项目通用的非业务性功能，例如：**内嵌服务器**、安全管理、运行数据监控、运行状况检查和外部化配置等。
4. 没有代码生成，也不需要XML配置。
5. 避免大量的 Maven 导入和各种版本冲突。

## 项目搭建

### 工具

IDEA

## 搭建步骤

### 1   File -->  New -->  Project

![image-20200908163350867](C:\Users\DELL\Desktop\md\md\image-20200908163350867.png)

### 2   Spring Initializr   选择Jdk版本  next 

![image-20200908163612951](C:\Users\DELL\Desktop\md\md\image-20200908163612951.png)



### 3 填写域名  项目名称(全小写)  选择java版本 点next



![image-20200908171038201](C:\Users\DELL\Desktop\md\md\image-20200908171038201.png)



## 4 web-->Spring web  （还可以在这选择其他依赖，数据库 例如mybatis，mysql）  选择Springboot 版本         next



![image-20200908171608786](C:\Users\DELL\Desktop\md\md\image-20200908171608786.png)

### 5  finish

![image-20200908172419792](C:\Users\DELL\Desktop\md\md\image-20200908172419792.png)

### 6 项目搭建完成

![image-20200908172609991](C:\Users\DELL\Desktop\md\md\image-20200908172609991.png)



###  7 标记文件夹  Java文件夹标记为Source Root ,  Resource 文件夹标记为  Resource Root  ,test文件夹标记为 Test Source Root



![image-20200908173009862](C:\Users\DELL\Desktop\md\md\image-20200908173009862.png)

### 8 写一个测试接口

![image-20200908174743504](C:\Users\DELL\Desktop\md\md\image-20200908174743504.png)



###  9 启动  默认8080端口

![image-20200908174518035](C:\Users\DELL\Desktop\md\md\image-20200908174518035.png)



###  10 浏览器访问 测试搭建结果

![image-20200908174640735](C:\Users\DELL\Desktop\md\md\image-20200908174640735.png)

至此 一个简单的springboot项目就搭建完成了搭建



Git地址： https://github.com/codecwf/springbootdemo.git