package com.jc.soda;

import com.jc.soda.repository.EmployeeRepository;
import java.sql.SQLException;
import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;
import oracle.soda.OracleCollection;
import oracle.soda.OracleDatabase;
import oracle.soda.OracleDatabaseAdmin;
import oracle.soda.OracleException;
import oracle.soda.rdbms.OracleRDBMSClient;
import oracle.soda.rdbms.OracleRDBMSMetadataBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OracleSodaConfig {

    @Value("${oracle.soda.collection}")
    private String collection;

    @Bean
    public DataSource dataSource(@Value("${oracle.datasource.username}") String username,
                                 @Value("${oracle.datasource.password}") String password,
                                 @Value("${oracle.datasource.url}") String url) throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setURL(url);
        dataSource.setImplicitCachingEnabled(true);
        dataSource.setFastConnectionFailoverEnabled(true);
        return dataSource;
    }

    @Bean
    public OracleRDBMSClient oracleRDBMSClient() {
        return new OracleRDBMSClient();
    }

    @Bean
    public OracleDatabase oracleDatabase(DataSource dataSource,
                                         OracleRDBMSClient oracleRDBMSClient) throws OracleException, SQLException {
        return oracleRDBMSClient.getDatabase(dataSource.getConnection());
    }

    @Bean
    OracleDatabaseAdmin oracleDatabaseAdmin(OracleDatabase oracleDatabase) {
        return oracleDatabase.admin();
    }

    @Bean
    OracleRDBMSMetadataBuilder oracleRDBMSMetadataBuilder(OracleRDBMSClient oracleRDBMSClient) throws OracleException {
        return oracleRDBMSClient.createMetadataBuilder().keyColumnAssignmentMethod("client");
    }

    @Bean
    OracleCollection oracleCollection(OracleDatabase oracleDatabase,
                                      OracleRDBMSMetadataBuilder oracleRDBMSMetadataBuilder) throws OracleException {
        return oracleDatabase.admin().createCollection(collection,
            oracleRDBMSMetadataBuilder.build());
    }

    @Bean
    EmployeeRepository employeeRepository(OracleDatabase oracleDatabase) throws OracleException {
        return new EmployeeRepository(oracleDatabase, collection);
    }

}
