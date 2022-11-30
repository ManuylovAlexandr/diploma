package com.project;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.springframework.http.CacheControl.maxAge;
import static org.springframework.http.CacheControl.noCache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.EncodedResourceResolver;

import com.project.application.ObjectMapperBuilder;

@EnableConfigurationProperties
@EnableSpringDataWebSupport
@EnableAsync(proxyTargetClass = true)
@SpringBootApplication
public class ProjectApplication
{
    private static final Integer ASYNC_WORKER_POOL_SIZE = 4;

    public static void main(String[] args)
    {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Configuration
    public static class ResourcesConfiguration implements WebMvcConfigurer
    {

        @Bean
        @ResourceObjectMapper
        public ObjectMapper resourcesObjectMapper()
        {
            return ObjectMapperBuilder.builder()
                .withAllModules()
                .useFields()
                .build();
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry)
        {
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry)
        {
            registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(noCache())
                .resourceChain(true)
                .addResolver(new EncodedResourceResolver());
            registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCacheControl(maxAge(365, DAYS));
        }

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
        {
            converters.stream()
                .filter(MappingJackson2HttpMessageConverter.class::isInstance)
                .map(MappingJackson2HttpMessageConverter.class::cast)
                .forEach(converter -> converter.setObjectMapper(resourcesObjectMapper()));
        }

        @Retention(RUNTIME)
        @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
        @Qualifier
        public @interface ResourceObjectMapper
        {

        }
    }

    @Configuration
    @EnableTransactionManagement(order = TransactionManagementConfiguration.TRANSACTION_INTERCEPTOR_ORDER)
    public static class TransactionManagementConfiguration
    {

        public static final int TRANSACTION_INTERCEPTOR_ORDER = 0;
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "asyncWorkerTaskExecutor")
    public TaskExecutor asyncWorkerTaskExecutor()
    {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-Worker-");
        threadPoolTaskExecutor.setCorePoolSize(ASYNC_WORKER_POOL_SIZE);
        threadPoolTaskExecutor.setMaxPoolSize(ASYNC_WORKER_POOL_SIZE);
        threadPoolTaskExecutor.setQueueCapacity(50);
        threadPoolTaskExecutor.afterPropertiesSet();
        return threadPoolTaskExecutor;
    }
}
