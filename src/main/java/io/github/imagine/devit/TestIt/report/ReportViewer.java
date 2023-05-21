package io.github.imagine.devit.TestIt.report;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class ReportViewer {

    private final Template template;

    public ReportViewer() throws URISyntaxException, IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration
                .setDirectoryForTemplateLoading(
                        new File(Objects.requireNonNull(getResource()).toURI()));

        configuration.setDefaultEncoding("UTF-8");

        this.template = configuration.getTemplate("report.ftl");
    }

    @Nullable
    private static URL getResource() {
        return ReportViewer.class.getResource("/templates/");
    }

    public void view(TestCaseReport testCaseReport) throws Exception {


        var dataModel = Map.of("report", testCaseReport.toMap());

        File file = new File(Objects.requireNonNull(getResource()).toURI().getPath() + "report.html");
        template.process(dataModel, new FileWriter(file));

        System.out.println("------------------------------------------------------------------");
        System.out.println("Report generated: " + file.getAbsolutePath());
        System.out.println("------------------------------------------------------------------");

    }
}
