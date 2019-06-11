package cn.com.thtf.aop.aspect;


import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.model.Log;
import cn.com.thtf.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private LogService logService;

    private long currentTime = 0L;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(cn.com.thtf.aop.log.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint){
        Object result = null;
        currentTime = System.currentTimeMillis();
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new CustomException(ResultCode.FAIL);
        }
        Log log = new Log(System.currentTimeMillis() - currentTime);
        logService.save(joinPoint, log);
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        //暂无需求
    }
}
