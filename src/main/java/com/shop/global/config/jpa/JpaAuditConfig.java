package com.shop.global.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

/*@Configuration
@EnableJpaAuditing*/
public class JpaAuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwareCreateBy();
    }
}