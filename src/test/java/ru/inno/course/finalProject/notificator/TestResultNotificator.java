package ru.inno.course.finalProject.notificator;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.inno.course.finalProject.helpers.PropsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class TestResultNotificator implements TestWatcher, AfterAllCallback {
    private static final Logger LOG = LoggerFactory.getLogger(TestResultNotificator.class);

    private List<TestResultStatus> testResultsStatus = new ArrayList<>();

    private enum TestResultStatus {
        SUCCESSFUL, ABORTED, FAILED, DISABLED;
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        Map<TestResultStatus, Long> summary = testResultsStatus.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        LOG.info("Test results for {} {}", context.getDisplayName(), summary.toString());
        String text = String.format("Test results for %s %s", context.getDisplayName(), summary.toString()
                        .replaceAll("\\{", ""))
                .replaceAll("}", "");
        String uri = String.format(PropsHelper.getTelegramUrl(), text);
        given().log().all()
                .contentType(ContentType.JSON)
                .when().get(uri);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        LOG.info("Test Disabled for test {}: with reason :- {}",
                context.getDisplayName(),
                reason.orElse("No reason"));
        testResultsStatus.add(TestResultStatus.DISABLED);

    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        LOG.info("Test Successful for test {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.SUCCESSFUL);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        LOG.info("Test Aborted for test {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.ABORTED);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        LOG.info("Test Failed for test {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.FAILED);
    }
}
