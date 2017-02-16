package com.anthem.oss.nimbus.core.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import com.anthem.oss.nimbus.core.domain.command.execution.EmailTaskExecutor;
import com.anthem.oss.nimbus.core.domain.model.state.builder.TemplateDefinition;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by AF13233 on 9/8/16.
 */

@ConfigurationProperties(prefix = "template")
@Configuration
public class EmailTaskExecutorBeanRegistrar implements ApplicationContextAware {
    @Setter
    @Getter
    List<TemplateDefinition> definitions = new ArrayList<>();
    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
        generateVelocityTemplateBeans(genericApplicationContext);

    }


    private void generateVelocityTemplateBeans(GenericApplicationContext genericApplicationContext) throws BeansException {
        definitions.forEach(templateDefinition -> {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(EmailTaskExecutor.class).setLazyInit(true);
            builder.addPropertyValue("templateDefinition", templateDefinition);
            genericApplicationContext.registerBeanDefinition(templateDefinition.getId(), builder.getBeanDefinition());
        });
    }

}