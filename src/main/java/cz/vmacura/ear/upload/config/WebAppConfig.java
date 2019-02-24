package cz.vmacura.ear.upload.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vmacura.ear.upload.rest.HomeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.nio.charset.Charset;
import java.util.List;


@Configuration
@EnableWebMvc
@Import({RestConfig.class, SecurityConfig.class})
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebAppConfig implements WebMvcConfigurer {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Note that the resource handlers work only for the DispatcherServlet-processed URLs, so the URL
		// has to be .../(static|rest)/resources/** (according to DispatcherServletInitializer mapping) to be processed by the handlers.
		// On the other hand, the resources are also accessible directly in ../resources/**, where tomcat will serve them.
		// Also note that during development it is advisable to disable the cache (like we have it here) to prevent the
		// browser from using cached versions of the static resource files (e.g. bundle.js).
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		commonsMultipartResolver.setMaxUploadSize(20000000);
		commonsMultipartResolver.setResolveLazily(false);
		return commonsMultipartResolver;
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// Here we register Jackson as converter of HTTP messages (in our case, JSON)
		final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper);
		converters.add(converter);
		// String converter makes sure that e.g. Czech characters are correctly interpreted
		final StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("utf-8"));
		converters.add(stringConverter);
	}
}
