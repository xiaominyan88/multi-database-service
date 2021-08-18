package data.clickhouse.datasource;

import com.alibaba.druid.pool.DruidDataSource;

import data.clickhouse.interceptor.MapF2FInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;


@Configuration
//扫描 Mapper 接口并容器管理
@MapperScan(basePackages = HiveDatasourceConfig.PACKAGE, sqlSessionFactoryRef = "hiveSqlSessionFactory")
public class HiveDatasourceConfig {
//    private Logger logger = LoggerFactory.getLogger(HiveDatasourceConfig.class);
    /**
     * 精确到 master 目录，以便跟其他数据源隔离
     */
    static final String PACKAGE = "com.hyit.bigdata.clickhouse.dao.hive";
    static final String MAPPER_LOCATION = "classpath:mapper/hive/*.xml";
    private static final String DB_PREFIX = "spring.datasource.hive";
    @Value("${spring.datasource.hive.url}")
    private String url;
    @Value("${spring.datasource.hive.username}")
    private String user;
    @Value("${spring.datasource.hive.password}")
    private String password;
    @Value("${spring.datasource.hive.driver-class-name}")
    private String driverClass;
    @Value("${spring.datasource.hive.maxActive}")
    private Integer maxActive;
    @Value("${spring.datasource.hive.minIdle}")
    private Integer minIdle;
    @Value("${spring.datasource.hive.initialSize}")
    private Integer initialSize;
    @Value("${spring.datasource.hive.maxWait}")
    private Long maxWait;
    @Value("${spring.datasource.hive.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.hive.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${spring.datasource.hive.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${spring.datasource.hive.validationQuery}")
    private String validationQuery;
    @Value("${spring.datasource.hive.testWhileIdle}")
    private Boolean testOnBorrow;
    @Value("${spring.datasource.hive.testOnBorrow}")
    private Boolean testOnReturn;
    @Value("${spring.datasource.hive.poolPreparedStatements}")
    private Boolean poolPreparedStatements;
    @Value("${spring.datasource.hive.maxPoolPreparedStatementPerConnectionSize}")
    private Integer maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.hive.filters}")
    private String filters;

    @Bean
    @ConfigurationProperties(prefix = DB_PREFIX)
    public DataSource hiveDataSource() {
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
    public SqlSessionFactory hiveSqlSessionFactory(@Qualifier("hiveDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources( HiveDatasourceConfig.MAPPER_LOCATION));
        bean.setPlugins(new Interceptor[]{new MapF2FInterceptor()});
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager hiveTransactionManager(@Qualifier("hiveDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate hiveSqlSessionTemplate(@Qualifier("hiveSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
