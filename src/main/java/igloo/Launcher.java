package igloo;

import igloo.database.CustomService;
import org.apache.catalina.filters.SetCharacterEncodingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @author Yikai Gong
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Launcher extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Launcher.class);
    }

    // seems to be useless
//    @Bean
//    public SetCharacterEncodingFilter characterEncodingFilter(){
//        SetCharacterEncodingFilter encodingFilter = new SetCharacterEncodingFilter();
//        encodingFilter.setEncoding("UTF-8");
//        return encodingFilter;
//    }

    // force character encoding in page returned by controller.
    @Bean
    public CharacterEncodingFilter setCharacterEncodingFilter(){
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    @Bean
    public CustomService getCustomService(){
        CustomService customService = new CustomService();
        return customService;
    }
}
