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
@MapperScan(basePackages = MysqlDatasourceConfig.PACKAGE, sqlSessionFactoryRef = "mysqlSqlSessionFactory")
public class MysqlDatasourceConfig {

    static final String PACKAGE = "com.hyit.bigdata.clickhouse.dao.mysql.zhny";
    static final String MAPPER_LOCATION = "classpath:mapper/mysql/zhny/*.xml";
    private static final String DB_PREFIX = "spring.datasource.mysql";
    @Value("${spring.datasource.mysql.url}")
    private String url;
    @Value("${spring.datasource.mysql.username}")
    private String user;
    @Value("${spring.datasource.mysql.password}")
    private String password;
    @Value("${spring.datasource.mysql.driver-class-name}")
    private String driverClass;
    @Value("${spring.datasource.mysql.maxActive}")
    private Integer maxActive;
    @Value("${spring.datasource.mysql.minIdle}")
    private Integer minIdle;
    @Value("${spring.datasource.mysql.initialSize}")
    private Integer initialSize;
    @Value("${spring.datasource.mysql.maxWait}")
    private Long maxWait;
    @Value("${spring.datasource.mysql.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.mysql.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${spring.datasource.mysql.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${spring.datasource.mysql.validationQuery}")
    private String validationQuery;
    @Value("${spring.datasource.mysql.testWhileIdle}")
    private Boolean testOnBorrow;
    @Value("${spring.datasource.mysql.testOnBorrow}")
    private Boolean testOnReturn;
    @Value("${spring.datasource.mysql.poolPreparedStatements}")
    private Boolean poolPreparedStatements;
    @Value("${spring.datasource.mysql.maxPoolPreparedStatementPerConnectionSize}")
    private Integer maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.mysql.filters}")
    private String filters;

    @Bean
    @ConfigurationProperties(prefix = DB_PREFIX)
    public DataSource mysqlDataSource() {
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
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources( MysqlDatasourceConfig.MAPPER_LOCATION));
        bean.setPlugins(new Interceptor[]{new MapF2FInterceptor()});
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
