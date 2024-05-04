Setup logging.

1. No additional dependencies are required as the log library is included in the `spring-boot-starter` dependency.

1. Add the path for the log configuration file in the `application.properties`.

    Ensure the log configuration file is located inside the resources folder. To enhance organization, we have included a log folder.

    ```bash
    logging.config=classpath:log/logback-spring.xml
    ```

    The main sections of the configuration file include:

    2.1. Setting the maximum file size, the maximum size of all files together, and the number of days to retain the files.

    ```xml
    <maxFileSize>100MB</maxFileSize>
    <totalSizeCap>1GB</totalSizeCap>
    <maxHistory>1</maxHistory>
    ```

    2.2. Configuring the log level based on the profile. In this example, both the `default` and `development` profiles will have the log level set to `INFO`. They were set together, but multiple `springProfile` tags can be added.

    2.3. Specifying the appenders to use. In this example, logs will be sent to both the `Console` and `File` appenders. The `File` appender will write on `asynchronous` mode.

    ```xml
    <springProfile name="default | development">
    <root level="INFO">
    <appender-ref ref="Console" />
    <appender-ref ref="File" />
    </root>
    </springProfile>
    ```

1. Add the Java code.

    3.1. Import the necessary packages from `org.slf4j`.

    ```java
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    ```

    3.2. Create the logger instance.

    ```java
    Logger logger = LoggerFactory.getLogger(<Name of the Class>.class);
    ```

    3.3. Utilize the following log methods.

    ```java
    logger.trace("A TRACE Message");
    logger.debug("A DEBUG Message");
    logger.info("An INFO Message");
    logger.warn("A WARN Message");
    logger.error("An ERROR Message");
    ```

#
### Created by:

1. Luciano Sampaio.
