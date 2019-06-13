package cn.com.thtf;

import net.unicon.cas.client.configuration.EnableCasClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextListener;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Date;

@SpringBootApplication
@EnableCasClient//开启cas
@MapperScan(basePackages = "cn.com.thtf.mapper")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class OceanCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanCmsApplication.class, args);
    }
}
