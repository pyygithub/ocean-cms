package cn.com.thtf.config;

import cn.com.thtf.web.filter.UrlFilter;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class WebFilterConfig {

    /**
     * 不需要认证的接口
     */
    @Value(value = "${ignore-pattern.paths}")
    private String IGNORE_PATTERN;

    /**
     * 系统登出后跳转路径
     */
    @Value(value = "${login.out.path}")
    private String LOGIN_URL;

    /**
     * CAS 服务器前缀
     */
    @Value(value = "${cas.server-url-prefix}")
    private String CAS_SERVER_URL_PREFIX;

    /**
     * CAS 服务器登陆页面地址
     */
    @Value(value = "${cas.server-login-url}")
    private String CAS_SERVER_LOGIN_URL;

    /**
     * CAS 服务器地址
     */
    @Value(value = "${cas.client-host-url}")
    private String SERVER_NAME;

    /**
     * 自定义过滤器
     */
    @Bean
    public FilterRegistrationBean<UrlFilter> abcFilter() {
        List<String> urls = Arrays.asList(this.IGNORE_PATTERN);
        FilterRegistrationBean<UrlFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new UrlFilter(urls, LOGIN_URL));
        filterRegBean.addUrlPatterns("/*");
        filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegBean;
    }

    /**
     * CAS 单点登出过滤器
     */
    @Bean
    public FilterRegistrationBean<SingleSignOutFilter> filterSingleRegistration() {
        FilterRegistrationBean<SingleSignOutFilter> registration = new FilterRegistrationBean<SingleSignOutFilter>();
        registration.setFilter(new SingleSignOutFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", CAS_SERVER_URL_PREFIX);
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registration;
    }

    /**
     * CAS 授权过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        Map<String,String> initParameters = new HashMap<>();
        initParameters.put("casServerLoginUrl", CAS_SERVER_LOGIN_URL);
        initParameters.put("serverName", SERVER_NAME);
        initParameters.put("ignorePattern", IGNORE_PATTERN);
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 3);
        return registration;
    }
}
