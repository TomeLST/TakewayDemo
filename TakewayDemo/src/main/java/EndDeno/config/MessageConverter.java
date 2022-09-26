/*
package EndDeno.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
//@RequestMapping("/backend/page/login")
@Slf4j
@Configuration

public class MessageConverter implements WebMvcConfigurer {

    */
/*protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/backend/")
                .addResourceLocations("classpath:/front/");
    }
*//*

    //@GetMapping("/login")
    //    扩展mvc框架的消息转换器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
         log.info("扩展消息转换器...");
        //消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        */
/*将转换器对象追加到mvc框架的转换器集合中
         *设置索引  将自定义转换器的优先级提高
         * *//*

        converters.add(0,messageConverter);

    }
}
*/
