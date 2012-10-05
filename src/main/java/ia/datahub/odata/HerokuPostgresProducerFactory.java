package ia.datahub.odata;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.EntityManagerProperties;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.ODataProducerFactory;
import org.odata4j.producer.jpa.JPAProducer;

public class HerokuPostgresProducerFactory implements ODataProducerFactory {

    @Override
    public ODataProducer create(Properties properties) {
        String persistenceUnitName = "persistenceUnit";
        String edmNamespace = "DataHub";
        int maxResults = 20;

        // access postgres connection info from DATABASE_URL environment variable
        String databaseUrl = System.getenv("DATABASE_URL");
        Matcher m = Pattern.compile("postgres://(.+):(.+)@(.+)/(.+)").matcher(databaseUrl);
        if (m.matches()) {
            String postgresUser = m.group(1);
            String postgresPassword = m.group(2);
            String postgresHost = m.group(3);
            String postgresName = m.group(4);
            String jdbcUrl = String.format("jdbc:postgresql://%s/%s", postgresHost, postgresName);

            properties.setProperty(EntityManagerProperties.JDBC_USER, postgresUser);
            properties.setProperty(EntityManagerProperties.JDBC_PASSWORD, postgresPassword);
            properties.setProperty(EntityManagerProperties.JDBC_URL, jdbcUrl);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
            return new JPAProducer(emf, edmNamespace, maxResults);
        } else {
            throw new IllegalStateException("Unable to parse DATABASE_URL: " + databaseUrl);
        }
    }

}
