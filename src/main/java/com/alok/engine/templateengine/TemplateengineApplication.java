package com.alok.engine.templateengine;

import com.alok.engine.templateengine.entities.TemplateMaster;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TemplateengineApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemplateengineApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(TemplateMasterRepository templateMasterRepository) {
		return args -> {

			templateMasterRepository.deleteAll();

			TemplateMaster template = TemplateMaster.builder()
					.templateName("template")
					.templateFolder("")
					.isHTML(true)
					.isPDF(true)
					.build();

			TemplateMaster template2 = TemplateMaster.builder()
					.templateName("templatelink")
					.templateFolder("")
					.isHTML(true)
					.isPDF(true)
					.build();

			templateMasterRepository.save(template);
			templateMasterRepository.save(template2);
		};
	}
}
