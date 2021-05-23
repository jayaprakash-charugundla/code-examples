package com.jc.spring.batch.config.db;

import lombok.Data;

@Data
public class DatasourceProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
