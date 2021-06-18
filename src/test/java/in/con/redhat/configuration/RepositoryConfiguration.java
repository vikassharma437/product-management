package in.con.redhat.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"in.con.redhat.domain"})
@EnableJpaRepositories(basePackages = {"in.con.redhat.repositories"})
@EnableTransactionManagement
public class RepositoryConfiguration {
}
