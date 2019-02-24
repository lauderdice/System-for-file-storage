package cz.vmacura.ear.upload.config;

import org.springframework.context.annotation.*;

@Configuration
@PropertySource("classpath:app.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({PersistenceConfig.class, ServiceConfig.class, WebAppConfig.class})
public class AppConfig
{
}
