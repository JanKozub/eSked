package org.jk.eSked.configuration;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Configuration
@ComponentScan("org.jk.eSked.dao")
@MapperScan("org.jk.eSked.dao")
class DatabaseConfiguation {
        @Bean
        public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, List<TypeHandler<?>> typeHandlers) {
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(dataSource);
            sessionFactory.setTypeHandlers(typeHandlers.toArray(new TypeHandler[0]));
            return sessionFactory;
        }


        @Bean
        public DataSource dataSourceLocal(@Value("${database:file:~/test}") String databasePath) throws SQLException {
            String url = "jdbc:h2:" + databasePath;

            PooledDataSource dataSource = new PooledDataSource("org.h2.Driver", url, "sa", "");
            dataSource.setPoolMaximumActiveConnections(10);

            try(Connection c = dataSource.getConnection()) {
                initializeDatabase(c);
            }


            return dataSource;
        }


    private void initializeDatabase(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            statement.executeQuery("SELECT * FROM Events");
        } catch(SQLException ex) {
            try(InputStream initScript = new ClassPathResource("db-init.sql").getInputStream();
                Statement statement = connection.createStatement()) {

                String script = new String(initScript.readAllBytes());
                statement.executeUpdate(script);
            } catch(IOException ioex) {
                throw new UncheckedIOException(ioex);
            } catch(SQLException sqlex) {
                throw new RuntimeException(sqlex);
            }
        }
    }
}
