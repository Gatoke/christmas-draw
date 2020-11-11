package io.github.gatoke.christmasdraw.port.adapter.configuration;

import org.springframework.web.reactive.config.WebFluxConfigurationSupport;

//@Configuration
//@EnableSwagger2WebFlux
public class SwaggerConfiguration extends WebFluxConfigurationSupport {

//    @Bean
//    Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2).select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    @Bean
//    public Jackson2JsonEncoder jackson2JsonEncoder(final ObjectMapper mapper) {
//        return new Jackson2JsonEncoder(mapper);
//    }
//
//    @Bean
//    public Jackson2JsonDecoder jackson2JsonDecoder(final ObjectMapper mapper) {
//        return new Jackson2JsonDecoder(mapper);
//    }
//
//    @Override
//    protected void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:META-INF/resources/webjars/")
//                .resourceChain(true)
//                .addResolver(new WebJarsResourceResolver());
//    }
}
