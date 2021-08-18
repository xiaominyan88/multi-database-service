package data.clickhouse.datasource;

import com.alibaba.druid.pool.DruidDataSource;

import data.clickhouse.interceptor.MapF2FInterceptor;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;


@Configuration
//扫描 Mapper 接口并容器管理
@MapperScan(basePackages = ClickHouseDatasourceConfig.PACKAGE, sqlSessionFactoryRef = "clickHouseSqlSessionFactory")
public class ClickHouseDatasourceConfig {
    private Logger logger = LoggerFactory.getLogger(ClickHouseDatasourceConfig.class);
    /**
     * 精确到 master 目录，以便跟其他数据源隔离
     */
    static final String PACKAGE = "com.hyit.bigdata.clickhouse.dao.clickhouse";
    static final String MAPPER_LOCATION = "classpath:mapper/clickhouse/*.xml";
    private static final String DB_PREFIX = "spring.datasource.clickhouse";
    @Value("${spring.datasource.clickhouse.url}")
    private String url;
    @Value("${spring.datasource.clickhouse.username}")
    private String user;
    @Value("${spring.datasource.clickhouse.password}")
    private String password;
    @Value("${spring.datasource.clickhouse.driver-class-name}")
    private String driverClass;
    @Value("${spring.datasource.clickhouse.maxActive}")
    private Integer maxActive;
    @Value("${spring.datasource.clickhouse.minIdle}")
    private Integer minIdle;
    @Value("${spring.datasource.clickhouse.initialSize}")
    private Integer initialSize;
    @Value("${spring.datasource.clickhouse.maxWait}")
    private Long maxWait;
    @Value("${spring.datasource.clickhouse.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.clickhouse.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${spring.datasource.clickhouse.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${spring.datasource.clickhouse.validationQuery}")
    private String validationQuery;
    @Value("${spring.datasource.clickhouse.testWhileIdle}")
    private Boolean testOnBorrow;
    @Value("${spring.datasource.clickhouse.testOnBorrow}")
    private Boolean testOnReturn;
    @Value("${spring.datasource.clickhouse.poolPreparedStatements}")
    private Boolean poolPreparedStatements;
    @Value("${spring.datasource.clickhouse.maxPoolPreparedStatementPerConnectionSize}")
    private Integer maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.clickhouse.filters}")
    private String filters;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = DB_PREFIX)
    public DataSource clickHouseDataSource() {
        //jdbc配置
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        //连接池配置
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean
    @Primary
    public SqlSessionFactory clickHouseSqlSessionFactory(@Qualifier("clickHouseDataSource") DataSource dataSource) throws Exception {
        org.apache.ibatis.session.Configuration  configuration=new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(true);
        configuration.setLogImpl(Log4j2Impl.class);
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(ClickHouseDatasourceConfig.MAPPER_LOCATION));
        bean.setConfiguration(configuration);
        bean.setPlugins(new Interceptor[]{new MapF2FInterceptor()});
        return bean.getObject();
    }

    @Bean
    @Primary
    public DataSourceTransactionManager clickHouseTransactionManager(@Qualifier("clickHouseDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @Primary
    public SqlSessionTemplate clickHouseSqlSessionTemplate(@Qualifier("clickHouseSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
