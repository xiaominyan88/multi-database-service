package data.clickhouse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    @Value("${swagger2.basePackage}")
    private String swagger2BasePackage;
    @Value("${swagger2.title}")
    private String swagger2Title;
    @Value("${swagger2.api.version}")
    private String apiVersion;
    @Value("${swagger2.status}")
    private boolean enableSwagger;
    @Bean
    public Docket createRestApi() {
        System.out.println(enableSwagger);
        //添加query参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        pars.add(tokenPar.build());
        //添加query参数end
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage(swagger2BasePackage))
                .paths(PathSelectors.any())
                .build()
                .enable(enableSwagger)
                ;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swagger2Title)
                .version(apiVersion)
                .build();
    }

}
