package com.alok.engine.templateengine;

import org.apache.commons.codec.binary.Base64;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.IDENTITY_H;

public class PDFGenerationUtility {

    private static PDFGenerationUtility INSTANCE;
    private static final String OUTPUT_FILE = "test.pdf";
    private static final String UTF_8 = "UTF-8";
    private static final String INPUT_PATH = "file:///" + System.getProperty("user.home")+ File.separator + "/Documents/Development/Templates/";
    private static final String OUTPUT_PATH= System.getProperty("user.home")+ File.separator;
    private static final String FONT_PATH= "file:///" + System.getProperty("user.home")+ File.separator + "/Documents/Development/Templates/Code39.ttf";

    private PDFGenerationUtility() {
    }

    public static PDFGenerationUtility getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PDFGenerationUtility();
        }
        return INSTANCE;
    }

    public byte[] createPdf(SpringTemplateEngine springTemplateEngine, String templateName) throws Exception {

        Data data = exampleDataForJohnDoe();

        Context context = new Context();
        context.setVariable("data", data);

        String renderedHtmlContent = springTemplateEngine.process(templateName, context);
        String xHtml = convertToXhtml(renderedHtmlContent);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont(FONT_PATH, IDENTITY_H, EMBEDDED);
        renderer.setDocumentFromString(xHtml, INPUT_PATH);
        renderer.layout();

        // And finally, we create the PDF:
        String outputFolder = OUTPUT_PATH + OUTPUT_FILE;
        OutputStream outputStream = new FileOutputStream(outputFolder);
        renderer.createPDF(outputStream);
        outputStream.close();
        System.out.println("file:///" + outputFolder);
        byte[] inFileBytes = Files.readAllBytes(Paths.get(outputFolder));
        return Base64.encodeBase64(inFileBytes);
    }

    private Data exampleDataForJohnDoe() {
        Data data = new Data();
        data.setFirstname("John");
        data.setLastname("Doe");
        data.setStreet("Example Street 1");
        data.setZipCode("12345");
        data.setCity("Example City");
        data.setCovered(false);
        data.setIsHTML(false);
        return data;
    }

    private String convertToXhtml(String html) {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString(StandardCharsets.UTF_8);
    }
}
