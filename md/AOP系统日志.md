# AOP系统日志

本文介绍SSM项目中以自定义注解结合AOP形式实现系统日志功能的方式

## 导入包

在pom.xml文件中导入包，可以根据自己项目spring的版本选择合适的版本

```
<properties>   
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>    			  			 				<spring.version>4.3.7.RELEASE</spring.version>   
</properties>
<dependency>
	<groupId>org.aspectj</groupId>
	<artifactId>aspectjweaver</artifactId>
	<version>1.8.10</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-aop</artifactId>
	<version>${spring.version}</version>
</dependency>
```

##  修改配置文件

修改Spring-mvc.xml文件，添加如下配置，开启对@AspectJ注解的支持，及对我们放自定义注解包的位置的扫描，因为后面保存日志的操作是用多线程的方式是实现的所以还配置了线程池

```
<!--  启动自定义注解包的位置的扫描 -->
<context:component-scan base-package="com.ktdream.server.util.annotation" />
<!--  启动对@AspectJ注解的支持 -->
<aop:aspectj-autoproxy/>
<!--线程池-->
<bean id="threadPoolTaskExecutor" class="org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor">
	<property name="corePoolSize" value="5" />
	<property name="maxPoolSize" value="10" />
</bean>
```

## 数据库建表

数据库种建表存放用户名，id,请求的接口，ip,请求参数等信息

### 建表sql

```
CREATE TABLE `his_operate_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `logFunction` varchar(255) DEFAULT NULL COMMENT '接口',
  `logFocus` varchar(255) DEFAULT NULL COMMENT '日志主诉',
  `logSummary` varchar(1000) DEFAULT NULL COMMENT '日志概要(暂存Ip)',
  `logRecord` longtext COMMENT '请求参数记录',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=547606 DEFAULT CHARSET=utf8 COMMENT='his操作日志表';


```

![image-20200813153029888](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\image-20200813153029888.png)

## bean

建立对应的实体类

```
package com.ktdream.server.persistence.beans.operatelog;

import java.util.Date;

@SuppressWarnings("serial")
public class OperateLog {
	
	private int id;
	
	private int userId;
	
	private String userName;
	
	private String logFunction;//日志调用功能函数
	
	private String logFocus;//日志主诉
	
	private String logSummary;//日志概要
	
	private String logRecord;//操作请求数据
	
	private Date createDate;
	
}

```

## mapper

```
package com.ktdream.server.persistence.mapper.operatelog;

import com.ktdream.server.persistence.beans.operatelog.OperateLog;

public interface OperatelogMapper {

	void insertOperatelog(OperateLog operateLog);

}

```

```
<insert id="insertOperatelog"
		parameterType="com.ktdream.server.persistence.beans.operatelog.OperateLog"
		useGeneratedKeys="true" keyProperty="id">
	        INSERT INTO his_operate_log (
	        	userId,	userName,   createDate,	logFunction,	logFocus,	logSummary,logRecord	        
	        ) VALUES (
	        	#{userId},#{userName}, #{createDate},#{logFunction},#{logFocus},#{logSummary}
	        	,#{logRecord}
	        )
</insert>

```

## service

```
package com.ktdream.server.service.operatelog;

import com.ktdream.server.persistence.beans.operatelog.OperateLog;

public  interface OperatelogService {

	void insertOperatelog(OperateLog operateLog);
}

```

```
package com.ktdream.server.service.operatelog.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdream.server.persistence.beans.operatelog.OperateLog;
import com.ktdream.server.persistence.mapper.operatelog.OperatelogMapper;
import com.ktdream.server.service.operatelog.OperatelogService;
import com.ktdream.server.util.exception.ServiceException;

@Service
public class OperatelogServiceImpl implements OperatelogService {
	@Autowired
	private OperatelogMapper operatelogMapper;

	@Override
	public void insertOperatelog(OperateLog operateLog) {
		operatelogMapper.insertOperatelog(operateLog);		
	}

}

```

### 自定义注解

```
package com.ktdream.server.util.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;

@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Operation {
    String value() default "用户操作";
    String data() default "";
}
```

### 切面类

```java
package com.ktdream.server.util.annotation;

import com.ktdream.server.controller.IndexController;
import com.ktdream.server.persistence.beans.HisUser;
import com.ktdream.server.persistence.beans.operatelog.OperateLog;
import com.ktdream.server.service.HisUserService;
import com.ktdream.server.service.operatelog.OperatelogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * AOP 切面编程，结合日志框架
 *
 */
 
@Component
@Aspect
public class OperationAOP {
	@Autowired
	private HisUserService hisUserService;
	@Autowired
	private OperatelogService operatelogService;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	private Logger logger = LoggerFactory.getLogger(OperationAOP.class);

	private static final ThreadLocal<Date> beginTimeThreadLocal =
										new NamedThreadLocal("ThreadLocal beginTime");
	private static final ThreadLocal<OperateLog> logThreadLocal =
										new NamedThreadLocal("ThreadLocal log");
	private static final ThreadLocal<HisUser> currentUser =
										new NamedThreadLocal("ThreadLocal user");
	//定义annotation切点
	@Pointcut("@annotation(com.ktdream.server.util.annotation.Operation)")
	public  void annotationAspect() {
	}

	@Before("annotationAspect()")
	public void doBefore(JoinPoint joinPoint) throws InterruptedException {
		Date beginTime = new Date();
		HttpServletRequest request = getReq();
		beginTimeThreadLocal.set(beginTime);//线程绑定变量（该数据只有当前请求的线程可见）
		if (logger.isDebugEnabled()) {//这里日志级别为debug
			logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(beginTime), request.getRequestURI());
		}
		//读取session中的用户
		//HttpSession session = request.getSession();
		//HisUser user = (HisUser) session.getAttribute("user");
		//获取当前登录用户（方式不限）
		HisUser user = IndexController.getUserByRequest(request,hisUserService);
		//System.out.println("begin");
		currentUser.set(user);
	}

	/**
	 * 用于拦截Controller层记录用户的操作
	 *
	 * @param joinPoint 切点
	 */
	@SuppressWarnings("unchecked")
	@AfterReturning(value = "annotationAspect()",returning = "returnValue")
	public void doAfter(JoinPoint joinPoint,Object returnValue)throws Exception {
		HisUser user = currentUser.get();
		if (user != null) {
			//String remoteAddr = OperationAOP.getIp();//请求的IP
			String requestUri = getReq().getRequestURI();//请求的Uri
            //设置日志对象参数
			OperateLog operateLog = new OperateLog();
			operateLog.setCreateDate(new Date());
			operateLog.setUserId(user.getId());
			operateLog.setUserName(user.getName());
			operateLog.setLogFunction(requestUri);
			String value = getControllerMethodDescription(joinPoint);
			operateLog.setLogSummary(getIp());
			operateLog.setLogFocus(value);
			Map<String, Object> fieldsName = getFieldsName(joinPoint);
			if(fieldsName!=null){
				operateLog.setLogRecord(fieldsName.toString());
			}
			//通过线程池来执行日志保存
			threadPoolTaskExecutor.execute(new SaveLogThread(operateLog, operatelogService));
			//本地存储operateLog
			logThreadLocal.set(operateLog);
		}
	}

	 // 获取请求ip
	public static String getIp()throws Exception {
		InetAddress ia=null;
		ia=ia.getLocalHost();
		String localip=ia.getHostAddress();
		return localip;
	}

	public static String getControllerMethodDescription(JoinPoint joinPoint) throws NoSuchMethodException {
		// 获取当前方法
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		HashMap<String, String> map = new HashMap<>();
		String value =null;
		try {
			Operation annotation = method.getAnnotation(Operation.class);
			value = annotation.value();
		}catch (Exception e){
			e.printStackTrace();
		}
		return value;
	}

	//保存日志线程
	private static class SaveLogThread implements Runnable {
		private OperateLog operateLog;
		private OperatelogService operatelogService;
		public SaveLogThread(OperateLog operateLog, OperatelogService operatelogService) {
			this.operateLog = operateLog;
			this.operatelogService = operatelogService;
		}
		@Override
		public void run() {
			operatelogService.insertOperatelog(operateLog);
		}
	}

	private static HttpServletRequest getReq(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return request;
	}

	private Map<String, Object> getFieldsName(JoinPoint joinPoint) throws Exception {
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			// 对于接受参数中含有MultipartFile，ServletRequest，ServletResponse类型的特殊处理。
			if (arg instanceof MultipartFile || arg instanceof ServletRequest ||
					arg instanceof ServletResponse) {
				return null;
			}
		}
		ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		String[] parameterNames = pnd.getParameterNames(method);
		Map<String, Object> paramMap = new HashMap<>(32);
		for (int i = 0; i < parameterNames.length; i++) {
			paramMap.put(parameterNames[i], args[i]);
		}
		return paramMap;
	}
}

```

### controller层

在controller上加上我们的自定义注解就ok了 ，指哪打哪

```java
	/**
	 * 预约列表
	 * @param
	 * @return
	 * @throws ControllerException
	 */
	@Operation(value = "预约项目列表")
	@RequestMapping(value = "/subscribeList",method = RequestMethod.GET)
	public HisUserResponse subscribeList(HisCheackSubscribe hisCheackSubscribe ) throws ServletException, IOException 	{
		HisUserResponse frontResponse = null;
		try {
			HashMap<String, Object> hashMap = hisProjectService.subscribeList(hisCheackSubscribe);
			frontResponse = new HisUserResponse(1, new Date(), "成功",     		·														hashMap.get("res"),hashMap.get("total"));
		} catch (Exception e) {
			logger.error("--subscribeDelete--", e);
			frontResponse = new HisUserResponse(3, new Date(), "网络异常，请联系管理员",null);
		}
		return frontResponse;
	}
```







