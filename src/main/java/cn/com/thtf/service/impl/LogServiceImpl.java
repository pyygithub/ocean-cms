package cn.com.thtf.service.impl;

import cn.com.thtf.common.Constants;
import cn.com.thtf.dao.LogMapper;
import cn.com.thtf.model.Log;
import cn.com.thtf.service.LogService;
import cn.com.thtf.utils.*;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

/**
 * ========================
 * Log日志service实现类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/11
 * Time：10:17
 * Version: v1.0
 * ========================
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    private final String LOGINPATH = "login";

    @Override
    public void save(ProceedingJoinPoint joinPoint, Log log){

        // 获取request
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        cn.com.thtf.aop.log.Log aopLog = method.getAnnotation(cn.com.thtf.aop.log.Log.class);

        // 日志详情
        if (log != null) {
            log.setBusinessCode(aopLog.businessCode());
            log.setOperationDesc(aopLog.operationDesc());
        }

        // 方法路径
        //String methodName = joinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";

        String params = "{";
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        // 用户名
        String username = "";

        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                params += " " + argNames[i] + ": " + JSON.toJSONString(argValues[i]);
            }
        }

        // 获取IP地址
        log.setRequestIp(StringUtils.getIP(request));

        if(!LOGINPATH.equals(signature.getName())){
            username = UserUtil.getUsername();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(argValues[0]);
                username = jsonObject.get("username").toString();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.setOperationContent(params + " }");
        log.setUsername(username);
        log.setCreateTime(new Timestamp(new Date().getTime()));
        log.setDeletedFlag(Constants.UN_DELETED);
        log.setId(SnowflakeId.getId() + "");
        logMapper.insert(log);
    }
}
