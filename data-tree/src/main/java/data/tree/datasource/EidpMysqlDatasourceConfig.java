package data.tree.datasource;

import com.alibaba.druid.pool.DruidDataSource;

import data.tree.intercepts.MapF2FInterceptor;
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
@MapperScan(basePackages = EidpMysqlDatasourceConfig.PACKAGE, sqlSessionFactoryRef = "eidpMysqlSqlSessionFactory")
public class EidpMysqlDatasourceConfig {

    static final String PACKAGE = "com.hyit.bigdata.odo.dao.mysql.eidp";
    static final String MAPPER_LOCATION = "classpath:mapper/mysql/eidp/*.xml";
    private static final String DB_PREFIX = "spring.datasource.eidp";
    @Value("${spring.datasource.eidp.url}")
    private String url;
    @Value("${spring.datasource.eidp.username}")
    private String user;
    @Value("${spring.datasource.eidp.password}")
    private String password;
    @Value("${spring.datasource.eidp.driver-class-name}")
    private String driverClass;
    @Value("${spring.datasource.eidp.maxActive}")
    private Integer maxActive;
    @Value("${spring.datasource.eidp.minIdle}")
    private Integer minIdle;
    @Value("${spring.datasource.eidp.initialSize}")
    private Integer initialSize;
    @Value("${spring.datasource.eidp.maxWait}")
    private Long maxWait;
    @Value("${spring.datasource.eidp.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.eidp.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${spring.datasource.eidp.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${spring.datasource.eidp.validationQuery}")
    private String validationQuery;
    @Value("${spring.datasource.eidp.testWhileIdle}")
    private Boolean testOnBorrow;
    @Value("${spring.datasource.eidp.testOnBorrow}")
    private Boolean testOnReturn;
    @Value("${spring.datasource.eidp.poolPreparedStatements}")
    private Boolean poolPreparedStatements;
    @Value("${spring.datasource.eidp.maxPoolPreparedStatementPerConnectionSize}")
    private Integer maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.eidp.filters}")
    private String filters;

    @Bean
    @ConfigurationProperties(prefix = DB_PREFIX)
    public DataSource eidpMysqlDataSource() {
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
    public SqlSessionFactory eidpMysqlSqlSessionFactory(@Qualifier("eidpMysqlDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources( EidpMysqlDatasourceConfig.MAPPER_LOCATION));
        bean.setPlugins(new Interceptor[]{new MapF2FInterceptor()});
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager eidpMysqlTransactionManager(@Qualifier("eidpMysqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate eidpMysqlSqlSessionTemplate(@Qualifier("eidpMysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
