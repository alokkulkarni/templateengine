package com.alok.engine.templateengine;

import com.alok.engine.templateengine.entities.TemplateMaster;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class TemplateController {

    private final TemplateMasterRepository templateMasterRepository;
    private final SpringTemplateEngine templateEngine;

    public TemplateController(TemplateMasterRepository templateMasterRepository, SpringTemplateEngine templateEngine) {
        this.templateMasterRepository = templateMasterRepository;
        this.templateEngine = templateEngine;
    }

    private Data exampleDataForJohnDoe() {
       Data data = new Data();
        data.setFirstname("John");
        data.setLastname("Doe");
        data.setStreet("Example Street 1");
        data.setZipCode("12345");
        data.setCity("Example City");
        data.setCovered(false);
        data.setIsHTML(true);
        return data;
    }

    @GetMapping("template")
    public String index(Model model, @RequestParam("templateId") String templateId) {
        model.addAttribute("data", exampleDataForJohnDoe());
        return getTemplateDetials(templateId);
    }

    @GetMapping("getPDF")
    public ResponseEntity<byte[]> indexlink(Model model, @RequestParam("templateId") String templateId) throws Exception {
        model.addAttribute("data", exampleDataForJohnDoe());
        if (getTemplateDetials(templateId) != null) {
            return new ResponseEntity<>(PDFGenerationUtility.getInstance().createPdf(templateEngine, getTemplateDetials(templateId)), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    private String getTemplateDetials(String templateId) {
        Optional<TemplateMaster> templateMasterRepositoryById = templateMasterRepository.findById(Long.parseLong(templateId));
        if (templateMasterRepositoryById.isPresent()) {
            String templateName = templateMasterRepositoryById.get().getTemplateName();
            String templateFolder = templateMasterRepositoryById.get().getTemplateFolder();
            if (templateFolder.isBlank() || templateFolder.isEmpty()) {
                return templateName;
            } else {
                return templateFolder + "/" + templateName;
            }
        }
        return null;
    }
}
