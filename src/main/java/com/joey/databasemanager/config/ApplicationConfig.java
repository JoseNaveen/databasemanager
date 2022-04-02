package com.joey.databasemanager.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.XmlViewResolver;

import com.joey.databasemanager.converters.StringToEnumConvertor;
import com.joey.databasemanager.dto.NSEContent;
import com.joey.databasemanager.dto.OptionChain;
import com.joey.databasemanager.interceptors.LoggingInterceptors;
import com.joey.databasemanager.options.OptionsPersistentWriter;
import com.joey.databasemanager.options.OptionsProcessor;
import com.joey.databasemanager.options.OptionsReader;
import com.joey.databasemanager.unitconverter.StringToWeightUnitConverter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@ComponentScan(basePackages = "com.joey.databasemanager")
@EnableMongoRepositories(basePackages="com.joey.databasemanager.repository.nosql")
public class ApplicationConfig extends WebMvcConfigurationSupport {
	@Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("css/**", "images/**","javascript/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/images/","classpath:/static/javascript/");
    }

    @Bean
    public InternalResourceViewResolver jspViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }
    
    @Override
    protected void addFormatters(FormatterRegistry registry) {
    	registry.addConverter(new StringToEnumConvertor());
    	registry.addConverter(new StringToWeightUnitConverter());
    }
    
    @Override
    protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    	configurer.setDefaultTimeout(5000);
    	configurer.setTaskExecutor(getTaskExecutor());
    }
    
    
    @Bean
    @Primary
    public ThreadPoolTaskExecutor getTaskExecutor() {
    	final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    	threadPoolTaskExecutor.setThreadNamePrefix("jose-thread-");
		return threadPoolTaskExecutor;
    }
    
    
    @Bean
    public XmlViewResolver getXmlViewResolver() {
    	XmlViewResolver viewResolver = new XmlViewResolver();
    	viewResolver.setLocation(new ClassPathResource("views.xml"));
    	return viewResolver;
    }
    
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new LoggingInterceptors()).addPathPatterns("/*");
    }
    
    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/react-auth-db");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
          .applyConnectionString(connectionString)
          .build();
        
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), "react-auth-db");
    }
    
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Bean
    public ItemReader<NSEContent> optionsReader() {
    	return new OptionsReader();
    }
    
    @Bean
    public ItemProcessor<NSEContent,OptionChain> optionsProcessor() {
    	return new OptionsProcessor();
    }
    
    @Bean
    public ItemWriter<Object> optionsWriter() {
    	return new OptionsPersistentWriter();
    }
    
    @Bean
    public Job importUserJob(Step step1) {
        return jobBuilderFactory.get("importUserJob")
          .start(step1)
          .build();
    }

    @Bean
    public Step step1(ItemReader<NSEContent> reader,
    		ItemProcessor<NSEContent,OptionChain> processor,
    		ItemWriter<Object> optionsWriter) {
        return stepBuilderFactory.get("step1").<NSEContent,OptionChain> chunk(10)
        		.reader(optionsReader())
        		.processor(processor)
        		.writer(optionsWriter)
        		.build();
    }
    
    @Autowired
    private PlatformTransactionManager jobTransactionManager;
    
    
    
	/*
	 * @Bean public BatchConfigurer batchConfigurer() { return new
	 * DefaultBatchConfigurer() {
	 * 
	 * @Override protected JobRepository createJobRepository() throws Exception {
	 * JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
	 * factory.setDataSource(jobDataSource());
	 * factory.setTransactionManager(jobTransactionManager);
	 * factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ"); return
	 * factory.getObject(); } }; }
	 */
}
