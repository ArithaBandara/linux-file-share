package github.com.arithabandar.waiter.application.whoHost.File;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logs {
    public Logs(Class clss,String error){
        // configure Logback programmatically
        configureLogback();

        // Get a logger instance
        Logger logger = LoggerFactory.getLogger(clss);

        // Log an error message
        logger.error(error);
    }

    private static void configureLogback() {
        ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.core.FileAppender fileAppender = new ch.qos.logback.core.FileAppender();
        fileAppender.setContext(loggerContext);
        fileAppender.setFile("error.log");

        // set append to true to append to the file instead of overwriting it
        fileAppender.setAppend(true);

        // configure encoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.setContext(loggerContext);
        encoder.start();

        fileAppender.setEncoder(encoder);
        fileAppender.start();

        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(fileAppender);

        // remove console appender
        rootLogger.detachAppender("console");
    }


}
