package cz.vmacura.ear.upload.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySources({
	@PropertySource("classpath:jpa.properties"),
	@PropertySource("classpath:jdbc.properties")
})
@ComponentScan(basePackages = {"cz.vmacura.ear.upload.repositories"})
public class PersistenceConfig
{
	private final Environment environment;

	@Autowired
	public PersistenceConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean(name = "ds")
	public DataSource dataSource() {
		final BoneCPDataSource ds = new BoneCPDataSource();
		ds.setDriverClass(environment.getRequiredProperty("jdbc.driverClassName"));
		ds.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
		ds.setUsername(environment.getRequiredProperty("jdbc.username"));
		ds.setPassword(environment.getRequiredProperty("jdbc.password"));
		return ds;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
		final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(ds);
		emf.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
		emf.setPackagesToScan("cz.vmacura.ear.upload.entities");

		final Properties props = new Properties();
		props.setProperty("databasePlatform", environment.getRequiredProperty("jpa.platform"));
		props.setProperty("generateDdl", "true");
		props.setProperty("showSql", "true");
		props.setProperty("eclipselink.weaving", "static");
		props.setProperty("eclipselink.ddl-generation", environment.getRequiredProperty("eclipselink.ddl-generation"));
		emf.setJpaProperties(props);
		return emf;
	}

	@Bean(name = "txManager")
	JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
}
