package com.project.port.adapter.semantic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "project.semantic-engine")
public class SemanticEngineConfig
{

    private String storeUrl;

    public String getStoreUrl()
    {
        return this.storeUrl;
    }

    public void setStoreUrl(String storeUrl)
    {
        this.storeUrl = storeUrl;
    }

}
