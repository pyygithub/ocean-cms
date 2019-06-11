package cn.com.thtf.service;

import cn.com.thtf.model.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

/**
 * ========================
 * 系统操作日志service接口
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/11
 * Time：10:02
 * Version: v1.0
 * ========================
 */
public interface LogService {
    /**
     * 新增日志
     * @param joinPoint
     * @param log
     */
    //    @Async
    void save(ProceedingJoinPoint joinPoint, Log log);
}
