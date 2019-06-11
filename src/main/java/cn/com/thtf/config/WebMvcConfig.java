package cn.com.thtf.config;

import org.apache.ibatis.type.JdbcType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import tk.mybatis.mapper.autoconfigure.ConfigurationCustomizer;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 解决Mybatis通用Mapper updateByPrimarykey()和insert()方法报错，提示jdbctype错误
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return new MybatisPlusCustomizers();
    }

    class MybatisPlusCustomizers implements ConfigurationCustomizer {

        @Override
        public void customize(org.apache.ibatis.session.Configuration configuration) {
            configuration.setJdbcTypeForNull(JdbcType.NULL);
        }
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
}
