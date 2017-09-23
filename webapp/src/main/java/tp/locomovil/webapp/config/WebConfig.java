package tp.locomovil.webapp.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan({"tp.locomovil.webapp.controller", "tp.locomovil.persistence", "tp.locomovil.service"})
@Configuration
public class WebConfig {

	@Profile("dev")
	@Bean
	public DataSource devDataSource () {
		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(org.postgresql.Driver.class);
		ds.setUrl("jdbc:postgresql://localhost/locomovil");
		ds.setUsername("root");
		ds.setPassword("root");
		
		return ds;
	}

	@Profile("dataset")
	@Bean
	public DataSource datasetDataSource () {
		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(org.postgresql.Driver.class);
		ds.setUrl("jdbc:postgresql://localhost/dataset-locomovil");
		ds.setUsername("Bianchi");

		return ds;
	}

	@Profile("live")
	@Bean
	public DataSource liveDataSource() throws URISyntaxException {
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];

		Properties props = new Properties();
		props.setProperty("ssl", "true");
		props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
		props.setProperty("user", username);
		props.setProperty("password", password);

		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(org.postgresql.Driver.class);
		ds.setConnectionProperties(props);
		ds.setUrl("jdbc:postgresql://ec2-23-23-234-118.compute-1.amazonaws.com:5432/d7hj75fa5krdvs");
		return ds;
	}
}
