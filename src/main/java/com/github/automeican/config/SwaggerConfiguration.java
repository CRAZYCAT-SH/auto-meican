package com.github.automeican.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @ClassName SwaggerConfiguration
 * @Deacription
 * @Author liyongbing
 * @Date 2021/1/25 13:42
 * @Version 1.0
 **/
@EnableSwagger2WebMvc
@Configuration
public class SwaggerConfiguration {
    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //描述字段支持Markdown语法
                        .description("# 美餐预定 RESTful APIs")
                        .termsOfServiceUrl("https://doc.xiaominfo.com/")
                        .contact(new Contact("liyongbing","https://github.com/CREAZYCAT","602642037@qq.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("默认")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.github.automeican.api"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
