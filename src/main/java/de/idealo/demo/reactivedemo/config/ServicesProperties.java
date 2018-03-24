package de.idealo.demo.reactivedemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = ServicesProperties.PREFIX)
public class ServicesProperties {

    public static final String PREFIX = "services";

    private ServiceConfig metaDataService;
    private ServiceConfig exportService;

    @Getter
    @Setter
    public static class ServiceConfig {
        private Integer threadCount;
        private Boolean externalAPI;
    }
}
