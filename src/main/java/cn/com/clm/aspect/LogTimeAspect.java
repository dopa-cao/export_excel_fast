package cn.com.clm.aspect;

import cn.com.clm.error.ErrorEnum;
import cn.com.clm.exception.CommonException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 打印日志的切面
 */
@Component
@Aspect
public class LogTimeAspect {
    private static final Logger LOG = LoggerFactory.getLogger(LogTimeAspect.class);

    @Pointcut(value = "@within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut(){
    }

    @Around(value = "pointcut()")
//    @Around(value = "@annotation(permission) && pointcut()")
//    public Object around(ProceedingJoinPoint joinPoint, LogPermission permission) throws Throwable {
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        checkPermission(permission);

        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String clsName = method.getDeclaringClass().getName();
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long useTimes = System.currentTimeMillis() - start;
        String result = JSONObject.toJSONString(proceed, SerializerFeature.WriteMapNullValue);
        LOG.info("======================================================================");
        LOG.info("==> 接口：{}, {} , 参数 {}", clsName, method.getName(), StringUtils.join(args, " : "));
        LOG.info("==> 耗时：{} ms", useTimes);
        LOG.info("==> 结果：{} ", result);
        LOG.info("======================================================================");
        return proceed;
    }

    private void checkPermission(LogPermission permission) throws Throwable {
        if (null == permission) {
            throw new CommonException(ErrorEnum.UNKNOWN_ERROR, "日志权限不足");
        }
        String s = permission.logPre();
        if (!s.startsWith("log")) {
            throw new CommonException(ErrorEnum.UNKNOWN_ERROR, "日志权限不足");
        }
    }


}

























