package cn.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients(basePackages = "cn.mall.clients")
@EnableSwagger2
@MapperScan("cn.mall.mapper")
public class MallEvaluationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallEvaluationApplication.class, args);
    }

}
