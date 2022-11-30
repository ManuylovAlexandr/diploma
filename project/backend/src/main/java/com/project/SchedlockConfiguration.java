package com.project;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedlockConfiguration
{

    @Bean
    public LockProvider jdbcTemplateLockProvider(@Nonnull DataSource dataSource)
    {
        return new JdbcTemplateLockProvider(dataSource);
    }
}
