package com.test.driveanddeliver.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(@Value("${appconfig.cache.disabled:false}") boolean cacheDisabled) {
        // Disable caching when running tests
        if (cacheDisabled) {
            return new NoOpCacheManager();
        }
        return new ConcurrentMapCacheManager("deliveryMethods", "deliveryTimeSlots");
    }
}
