package com.shop.doubleu.product.crawl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
@Getter
@Setter
public class DataSourceProperties {
    private String url;
    private String username;
    private String password;

}
