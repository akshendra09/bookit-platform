package com.bookit.catalog.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // Redis auto-configured via spring-boot-starter-data-redis + application.yml
}
