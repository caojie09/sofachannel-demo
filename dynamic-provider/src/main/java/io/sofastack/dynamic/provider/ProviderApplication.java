package io.sofastack.dynamic.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * dynamic-provider 启动类
 *
 * @author caojie.cj@antfin.com
 * @since 2020/2/11
 */
@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }


    @Configuration
    public static class DemoAutoConfiguration {

        @Value("${demo.value}")
        private String demoValue;

        @Bean
        public String demoValue() {
            System.out.println(""
                    + demoValue
                    + "注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!注意我!!!"
            );
            return demoValue;
        }

    }
}
