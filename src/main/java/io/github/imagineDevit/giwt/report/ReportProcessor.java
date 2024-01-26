package io.github.imagineDevit.giwt.report;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * This class is responsible for processing the report template and generating the report.
 */
public class ReportProcessor {

    public static final String TARGET_GIWTUNIT = "target/giwtunit";
    private final Template template;

    public ReportProcessor() throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);

        File tempDirectory = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tempDirectory.getPath() + "/report.ftl");

        InputStream source = Objects.requireNonNull(ReportProcessor.class.getClassLoader().getResourceAsStream("report.ftl"));

         try (var target = new FileOutputStream(file)) {
                target.write(source.readAllBytes());
         }


        configuration.setDirectoryForTemplateLoading(tempDirectory);

        configuration.setDefaultEncoding("UTF-8");

        this.template = configuration.getTemplate("report.ftl");
    }


    public void process(TestCaseReport testCaseReport) throws Exception {

        var dataModel = Map.of("report", testCaseReport.toMap());

        if (!Files.exists(new File(TARGET_GIWTUNIT).toPath())) {
            Files.createDirectory(new File(TARGET_GIWTUNIT).toPath());
        }

        File file = new File("%s/report.html".formatted(TARGET_GIWTUNIT));
        template.process(dataModel, new FileWriter(file));

        System.out.println("------------------------------------------------------------------");
        System.out.println("Report generated: " + file.getAbsolutePath());
        System.out.println("------------------------------------------------------------------");

    }
}
