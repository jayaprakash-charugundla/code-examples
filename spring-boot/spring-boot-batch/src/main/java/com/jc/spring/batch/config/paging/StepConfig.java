package com.jc.spring.batch.config.paging;

import com.jc.spring.batch.config.PatientPreparedStatementSetter;
import com.jc.spring.batch.config.SamplePartitioner;
import com.jc.spring.batch.domain.Patient;
import com.jc.spring.batch.step.Processor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@ConditionalOnProperty(name = "app.paging.enabled", havingValue = "true")
@Configuration
public class StepConfig {

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    private int startIndex;
    private int endIndex;

    @BeforeStep
    public void initializeValues(StepExecution stepExecution) {
        startIndex = stepExecution.getExecutionContext().getInt("startIndex");
        endIndex = stepExecution.getExecutionContext().getInt("endIndex");

    }

    private static final String QUERY_FIND_PATIENTS =
            "SELECT " +
                    "patient_id, " +
                    "source_id, " +
                    "first_name, " +
                    "middle_initial, " +
                    "last_name, " +
                    "email_address, " +
                    "phone_number, " +
                    "street, " +
                    "city, " +
                    "state, " +
                    "zip_code, " +
                    "birth_date, " +
                    "social_security_number " +
                    "FROM PATIENT " +
                    "ORDER BY patient_id ASC";

    private static final String QUERY_INSERT_PATIENTS = "INSERT " +
            "INTO PATIENT(patient_id, source_id, first_name, middle_initial, last_name, email_address, phone_number, street, city, state, zip_code, birth_date,"
            + " social_security_number) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Bean
    public Step partitionStep(Step step, Partitioner partitioner) {
        return stepBuilderFactory.get("partitionStep").partitioner("step", partitioner).step(step).gridSize(5)
                .taskExecutor(taskExecutor()).build();
    }

    @Bean
    public Step step(ItemReader<Patient> itemReader, ItemWriter<Patient> itemWriter) {
        return stepBuilderFactory.get("step").<Patient, Patient>chunk(100)
                .reader(itemReader).processor(new Processor())
                .writer(itemWriter).build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setQueueCapacity(5);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    @Bean
    public Partitioner partitioner(@Qualifier("postgresJdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new SamplePartitioner(jdbcTemplate, "patient", "patient_id");
    }

    @Bean
    ItemReader<Patient> patientItemReader(@Qualifier("postgresDataSource") DataSource dataSource) {
        JdbcPagingItemReader<Patient> databaseReader = new JdbcPagingItemReader<>();
        databaseReader.setDataSource(dataSource);
        databaseReader.setPageSize(2000);
        PagingQueryProvider queryProvider = createQueryProvider();
        databaseReader.setQueryProvider(queryProvider);
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("startingIndex", 951);
        parameterValues.put("endingIndex", 2950);
        databaseReader.setParameterValues(parameterValues);
        databaseReader.setRowMapper(new BeanPropertyRowMapper<>(Patient.class));
        return databaseReader;
    }

    private PagingQueryProvider createQueryProvider() {
        PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause(
                "SELECT patient_id, source_id, first_name, middle_initial, last_name, email_address, phone_number, street,"
                        + "city, state, zip_code, birth_date, social_security_number");
        queryProvider.setFromClause("FROM patient");
        queryProvider.setWhereClause("where patient_id>= :startingIndex and patient_id<=:endingIndex");
        queryProvider.setSortKeys(sortByPatientId());
        return queryProvider;
    }

    private Map<String, Order> sortByPatientId() {
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("patient_id", Order.ASCENDING);
        return sortConfiguration;
    }

    @Bean
    ItemWriter<Patient> patientItemWriter(@Qualifier("mysqlDataSource") DataSource dataSource,
                                          NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<Patient> databaseItemWriter = new JdbcBatchItemWriter<>();
        databaseItemWriter.setDataSource(dataSource);
        databaseItemWriter.setJdbcTemplate(jdbcTemplate);
        databaseItemWriter.setSql(QUERY_INSERT_PATIENTS);
        ItemPreparedStatementSetter<Patient> valueSetter = new PatientPreparedStatementSetter();
        databaseItemWriter.setItemPreparedStatementSetter(valueSetter);
        return databaseItemWriter;
    }

}