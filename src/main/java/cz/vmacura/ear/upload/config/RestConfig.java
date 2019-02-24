package cz.vmacura.ear.upload.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"cz.vmacura.ear.upload.rest"})
public class RestConfig {

	@Bean
	public ObjectMapper objectMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		// Support for Java 8 Date/Time API
		objectMapper.registerModule(new JavaTimeModule());
		// Only non-null properties are serialized
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// This is necessary for our way of working with Java 8 Date/Time API. If it were not configured, the
		// datetime object in JSON would consist of several attributes, each for the respective date/time part, i.e. year, day etc.
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// Ignore unknown properties in JSON
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}
}
