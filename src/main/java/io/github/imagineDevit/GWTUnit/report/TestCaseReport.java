package io.github.imagineDevit.GWTUnit.report;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for generating the report.
 * It contains the data model for the report.
 * <p>
 * The data model is a tree structure of {@link TestCaseReport.ClassReport} and {@link TestCaseReport.TestReport}.
 * </p>
 */
public class TestCaseReport {

    private final Set<ClassReport> classReports = new TreeSet<>(Comparator.comparing(ClassReport::getName));

    // region public methods
    public void addClassReport(ClassReport classReport) {
        classReports.add(classReport);
    }

    public Set<ClassReport> getClassReports() {
        return classReports;
    }

    public Optional<ClassReport> getClassReport(String name){
        return classReports.stream()
                .filter(cr -> cr.name.equals(name))
                .findFirst();
    }

    public String toString() {
        return "TestCaseReport{" +
                "classReports=" + classReports +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("classReports", classReports.stream().map(ClassReport::toMap).collect(Collectors.toList()));

        map.put("totalCount", getTotalTestCount());
        map.put("failureCount", getFailureCount());
        map.put("skippedCount", getSkippedCount());
        map.put("successCount", getSuccessCount());

        map.put("successRate", (((double) getSuccessCount())/(double)getTotalTestCount())*100);

        return map;
    }

    // endregion

    // region private methods
    private int getTotalTestCount(){
        return classReports.stream()
                .mapToInt(cr -> cr.testReports.size())
                .sum();
    }

    private long getFailureCount() {
        return classReports.stream()
                .mapToLong(ClassReport::getFailureCount)
                .sum();
    }

    private long getSkippedCount() {
        return classReports.stream()
                .mapToLong(ClassReport::getSkippedCount)
                .sum();
    }

    private long getSuccessCount() {
        return classReports.stream()
                .mapToLong(ClassReport::getSuccessCount)
                .sum();
    }

    // endregion

    public static class ClassReport {
        private final String name;

        public ClassReport(String name) {
            this.name = name;
        }

        private final List<TestReport> testReports = new ArrayList<>();

        public String getName() {
            return name;
        }


        public void addTestReport(TestReport testReport) {
            testReports.add(testReport);
        }

        public double getSuccessRate() {
            var successCount = testReports.stream()
                    .filter(testReport -> testReport.getStatus() == TestReport.Status.SUCCESS)
                    .count();
            return (double) successCount / testReports.size();
        }

        public String toString() {
            return "ClassReport{" +
                    "name='" + name + '\'' +
                    ", testReports=" + testReports +
                    '}';
        }

        public Map<String,Object> toMap(){
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("testReports", testReports.stream().map(TestReport::toMap).collect(Collectors.toList()));

            map.put("totalCount", testReports.size());
            map.put("successCount", getSuccessCount());
            map.put("failureCount", getFailureCount());
            map.put("skippedCount", getSkippedCount());
            map.put("successRate", getSuccessRate() * 100);

            return map;
        }

        public long getFailureCount() {
            return testReports.stream()
                    .filter(testReport -> testReport.getStatus() == TestReport.Status.FAILURE)
                    .count();
        }

        public long getSkippedCount() {
            return  testReports.stream()
                    .filter(testReport -> testReport.getStatus() == TestReport.Status.SKIPPED)
                    .count();
        }

        public long getSuccessCount() {
            return testReports.stream()
                    .filter(testReport -> testReport.getStatus() == TestReport.Status.SUCCESS)
                    .count();
        }
    }

    public static class TestReport {

        public record DescriptionItem(String prefix, String label) {

            public static DescriptionItem given(String label) {
                return new DescriptionItem("GIVEN", label);
            }

            public static DescriptionItem when(String label) {
                return new DescriptionItem("WHEN", label);
            }

            public static DescriptionItem then(String label) {
                return new DescriptionItem("THEN", label);
            }

            public static DescriptionItem and(String label) {
                return new DescriptionItem("&#8627;  AND", label);
            }


            public Map<String, String> toMap(){
                HashMap<String, String> map = new HashMap<>();
                map.put("prefix", prefix);
                map.put("label", label);
                return map;
            }
        }

        public enum Status {
            SUCCESS("&#9989;", "passed", "text-success"),
            FAILURE("&#10060;", "failed", "text-danger"),
            SKIPPED("&#10069;", "skipped", "text-warning");

            final String symbol;
            final String label;

            final String cssClass;

            Status(String s, String l, String css){
                this.symbol = s;
                this.label = l;
                this.cssClass = css;
            }

        }

        private String name;
        private final List<DescriptionItem> descriptionItems = new ArrayList<>();
        private Status status;
        private final List<String> stacktraces = new ArrayList<>();
        private String failureReason;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Status getStatus() {
            return status;
        }

        public void addDescriptionItem(DescriptionItem description) {
            this.descriptionItems.add(description);
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public void addTrace(String stacktrace) {
            this.stacktraces.add(stacktrace);
        }

        public void setFailureReason(String failureReason) {
            this.failureReason = failureReason;
        }

        public Map<String, Object> toMap(){
            Map<String, Object> map = new HashMap<>();
            map.put("name", "%s %s".formatted(status.symbol, name));
            map.put("descriptions", descriptionItems.stream().map(DescriptionItem::toMap).collect(Collectors.toList()));
            map.put("status", status.label);
            map.put("statusColor", status.cssClass);

            if (!stacktraces.isEmpty()) map.put("stacktraces", stacktraces);

            map.put("failureReason", failureReason);
            return map;
        }
    }
}
