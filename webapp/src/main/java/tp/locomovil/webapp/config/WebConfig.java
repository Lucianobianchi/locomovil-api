package tp.locomovil.webapp.config;

import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan({"tp.locomovil.webapp.controller", "tp.locomovil.persistence", "tp.locomovil.service"})
@Configuration
public class WebConfig {
	
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		
		return resolver;
	}

	@Profile("dev")
	@Bean
	public DataSource dataSource() {
		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(org.postgresql.Driver.class);
		ds.setUrl("jdbc:postgresql://localhost/locomovil");
		ds.setUsername("root");
		ds.setPassword("root");
		
		return ds;
	}

	@Profile("live")
	public DataSource liveDataSource() {
		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(org.postgresql.Driver.class);
		ds.setUrl("jdbc:postgres://euszfboadbbtlw:afbadf5a0b55a7a7533ef81ee4fb3b0cdf2a11f58ebd3222f1ace5f2e8b3cc6f@ec2-23-23-234-118.compute-1.amazonaws.com:5432/d7hj75fa5krdvs");
		ds.setUsername("euszfboadbbtlw");
		ds.setPassword("afbadf5a0b55a7a7533ef81ee4fb3b0cdf2a11f58ebd3222f1ace5f2e8b3cc6f");
		return ds;
	}
}