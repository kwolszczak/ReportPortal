package pl.kwolszczak;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import com.epam.reportportal.junit5.ReportPortalExtension;
import com.epam.reportportal.logback.appender.ReportPortalAppender;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.LoggingContext;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


@ExtendWith(ReportPortalExtension.class)
class UatTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(UatTests.class);

    @Test
    void testMySimpleTest() throws IOException {
        LOGGER.info("START TEST");

        /* here we are logging some binary data as file (useful for selenium) */
        File file = File.createTempFile("rp-test", ".json");
        File file2 = File.createTempFile("rp-test", ".PNG");
        Resources.asByteSource(Resources.getResource("test.json")).copyTo(Files.asByteSink(file));
        Resources.asByteSource(Resources.getResource("screen.PNG")).copyTo(Files.asByteSink(file2));
        LOGGER.info("RP_MESSAGE#FILE#{}#{}", file.getAbsolutePath(), "I'm logging content via temp file");
        LOGGER.info("RP_MESSAGE#FILE#{}#{}", file2.getAbsolutePath(), "I'm logging content via temp file");
        LOGGER.info("FINISHED TEST");
        Assertions.assertTrue(true);
    }

    @Test
    @SuppressWarnings({"unchecked", "ReactiveStreamsUnusedPublisher"})
    public void test_logger_append() {


        Logger logger = createLoggerFor(Launch.class);
        logger.info("test message");
        LoggingContext.complete();

    }

    private static Logger createLoggerFor(Class<?> clazz) {
        LoggerContext lc = new LoggerContext();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();

        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setContext(lc);
        ple.start();
        ReportPortalAppender appender = new ReportPortalAppender();
        appender.setEncoder(ple);
        appender.setContext(lc);
        appender.start();

        ch.qos.logback.classic.Logger logger = lc.getLogger(clazz);
        logger.addAppender(appender);
        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false);

        return logger;
    }

}