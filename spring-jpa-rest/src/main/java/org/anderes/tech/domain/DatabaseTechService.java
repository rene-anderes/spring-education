package org.anderes.tech.domain;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component
public class DatabaseTechService {

    @Inject
    private DataSource dataSource;
    
    public DatabaseInfo getDatabasename() throws Exception {
        final String databaseProductName = dataSource.getConnection().getMetaData().getDatabaseProductName();
        final String databaseProductVersion = dataSource.getConnection().getMetaData().getDatabaseProductVersion();
        final String driverName = dataSource.getConnection().getMetaData().getDriverName();
        final String driverVersion = dataSource.getConnection().getMetaData().getDriverVersion();
        final String databaseUrl = dataSource.getConnection().getMetaData().getURL();
        final DatabaseInfo databaseInfo = DatabaseInfo.build().setProductName(databaseProductName)
                        .setProductVersion(databaseProductVersion).setDriverName(driverName)
                        .setDriverVersion(driverVersion).setUrl(databaseUrl);
        return databaseInfo;
    }
}
