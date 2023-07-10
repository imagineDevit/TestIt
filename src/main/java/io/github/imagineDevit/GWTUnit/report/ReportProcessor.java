package io.github.imagineDevit.GWTUnit.report;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * This class is responsible for processing the report template and generating the report.
 */
public class ReportProcessor {

    public static final String TARGET_GWTUNIT = "target/gwtunit";
    private final Template template;

    public ReportProcessor() throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        File tempDirectory = FileUtils.getTempDirectory();
        File file = new File(tempDirectory.getPath() + "/report.ftl");
        FileUtils.copyInputStreamToFile(Objects.requireNonNull(ReportProcessor.class.getClassLoader().getResourceAsStream("report.ftl")), file);

        configuration.setDirectoryForTemplateLoading(tempDirectory);

        configuration.setDefaultEncoding("UTF-8");

        this.template = configuration.getTemplate("report.ftl");
    }


    public void process(TestCaseReport testCaseReport) throws Exception {

        var dataModel = Map.of("report", testCaseReport.toMap());

        if (!Files.exists(new File(TARGET_GWTUNIT).toPath())) {
            Files.createDirectory(new File(TARGET_GWTUNIT).toPath());
        }

        File file = new File("%s/report.html".formatted(TARGET_GWTUNIT));
        template.process(dataModel, new FileWriter(file));

        System.out.println("------------------------------------------------------------------");
        System.out.println("Report generated: " + file.getAbsolutePath());
        System.out.println("------------------------------------------------------------------");

    }
}
