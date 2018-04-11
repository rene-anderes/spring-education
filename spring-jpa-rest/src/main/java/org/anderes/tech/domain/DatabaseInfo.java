package org.anderes.tech.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatabaseInfo {

    private String productName;
    private String productVersion;
    private String driverName;
    private String driverVersion;
    private String url;
    
    public static DatabaseInfo build() {
        return new DatabaseInfo();
    }

    @JsonProperty("Product-Version")
    public String getProductVersion() {
        return productVersion;
    }

    @JsonProperty("Driver-Name")
    public String getDriverName() {
        return driverName;
    }

    @JsonProperty("Driver-Version")
    public String getDriverVersion() {
        return driverVersion;
    }
    @JsonProperty("URL")
    public String getUrl() {
        return url;
    }
    
    @JsonProperty("Product-Name")
    public String getProductName() {
        return productName;
    }
    
    public DatabaseInfo setProductName(String databaseProductName) {
        this.productName = databaseProductName;
        return this;
    }

    public DatabaseInfo setProductVersion(String databaseProductVersion) {
        this.productVersion = databaseProductVersion;
        return this;
    }

    public DatabaseInfo setDriverName(String driverName) {
        this.driverName = driverName;
        return this;
    }

    public DatabaseInfo setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
        return this;
    }

    public DatabaseInfo setUrl(String databaseUrl) {
        this.url = databaseUrl;
        return this;
    }

}
