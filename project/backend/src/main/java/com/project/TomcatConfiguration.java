package com.project;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration
{

    @Bean
    public WebServerFactoryCustomizer containerCustomizer()
    {
        return (WebServerFactoryCustomizer<TomcatServletWebServerFactory>) factory -> factory.addConnectorCustomizers(
            connector -> connector.setProperty("relaxedQueryChars", "[]"));
    }
}
