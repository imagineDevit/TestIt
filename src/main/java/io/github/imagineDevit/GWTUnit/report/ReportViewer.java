package io.github.imagineDevit.GWTUnit.report;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ReportViewer {

    private final Template template;

    public ReportViewer() throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        File tempDirectory = FileUtils.getTempDirectory();
        File file = new File(tempDirectory.getPath() + "/report.ftl");
        FileUtils.copyInputStreamToFile(Objects.requireNonNull(ReportViewer.class.getClassLoader().getResourceAsStream("report.ftl")), file);

        configuration.setDirectoryForTemplateLoading(tempDirectory);

        configuration.setDefaultEncoding("UTF-8");

        this.template = configuration.getTemplate("report.ftl");
    }


    public void view(TestCaseReport testCaseReport) throws Exception {

        var dataModel = Map.of("report", testCaseReport.toMap());

        File file = new File("target/report.html");
        template.process(dataModel, new FileWriter(file));

        System.out.println("------------------------------------------------------------------");
        System.out.println("Report generated: " + file.getAbsolutePath());
        System.out.println("------------------------------------------------------------------");

    }
}
