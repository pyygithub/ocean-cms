package cn.com.thtf.service.impl;

import cn.com.thtf.common.Constants;
import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.mapper.ApplicationMapper;
import cn.com.thtf.mapper.TokenMapper;
import cn.com.thtf.model.Application;
import cn.com.thtf.model.Token;
import cn.com.thtf.service.ApplicationService;
import cn.com.thtf.utils.RSAUtil;
import cn.com.thtf.utils.SnowflakeId;
import cn.com.thtf.utils.UrlUtil;
import cn.com.thtf.vo.ApplicationSaveOrUpdateVO;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;

/**
 * ========================
 * 系统应用service实现类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：14:09
 * Version: v1.0
 * ========================
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApplicationServiceImpl implements ApplicationService {
    private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Value("${cas-server.client-add-url}")
    private String clientAddUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenMapper tokenMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    /**
     * 系统应用添加
     * @param applicationSaveOrUpdateVO
     */
    @Override
    public void add(ApplicationSaveOrUpdateVO applicationSaveOrUpdateVO) {
        //分别取出主页地址和注销地址中的IP或域名 + 端口
        String link = applicationSaveOrUpdateVO.getLink();
        String logoutUrl = applicationSaveOrUpdateVO.getLogoutUrl();
        //判断主页地址和注销地址IP和端口是否相同
        if (!UrlUtil.getIpAndPort(link).equals(UrlUtil.getIpAndPort(logoutUrl))) {
            log.error("### 主页地址和注销地址IP不一致. link={}, logouUrl={} ###", link, logoutUrl);
            throw new CustomException(ResultCode.DIFFERENT_IP);
        }

        //保存应用到CAS Server
        String ipAndPort = UrlUtil.getIpAndPort(link);
        JSONObject jsonObject = restTemplate.postForObject(clientAddUrl, null, JSONObject.class, ipAndPort, logoutUrl);
        if (jsonObject == null || jsonObject.getInteger("code") != 200) {
            log.error("### 注册CAS client 错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }

        //保存应用
        Application application = new Application();
        String applicationId = SnowflakeId.getId() + "";
        application.setId(applicationId);
        BeanUtils.copyProperties(applicationSaveOrUpdateVO, application);
        application.setStatus(Constants.DISABLE);
        application.setCreateUserCode(applicationSaveOrUpdateVO.getUserId());
        application.setCreateUserName(applicationSaveOrUpdateVO.getUsername());
        application.setCreateTime(new Timestamp(new Date().getTime()));
        application.setDeletedFlag(Constants.UN_DELETED);
        int appCount = applicationMapper.insert(application);
        if (appCount != 1) {
            log.error("### 保存应用错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }

        //保存应用到第三方系统：存储应用IP和秘钥信息
        Token token = new Token();
        token.setId(SnowflakeId.getId() + "");
        token.setIp(UrlUtil.getIp(link));
        token.setApplicationId(applicationId);
        token.setPublicKey(RSAUtil.getPublicKey());
        token.setPrivateKey(RSAUtil.getPrivateKey());
        token.setAuthLevel(applicationSaveOrUpdateVO.getAuthLevel() == null ? Constants.AUTH_LEVEL_BASE : applicationSaveOrUpdateVO.getAuthLevel());
        int tokenCount = tokenMapper.insert(token);
        if (tokenCount != 1) {
            log.error("### 保存应用到第三方系统错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
    }

}
