package com.javatechie.elk;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

public class LoggingUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger auditLogger = LoggerFactory.getLogger("auditLogger");
    private static final Logger techLogger = LoggerFactory.getLogger("techLogger");

    public static void logAuditGetUserById(String action, String message, User user, UUID commonId) {
        try {
           // addCurrentLogLevel();
            MDC.put("action", action);
            MDC.put("commonIdentifier", commonId.toString());
            MDC.put("logType", LogType.audit.toString());
//            MDC.put("message", message);
            MDC.put("user", toJsonString(user));
            auditLogger.info(message);
        } finally {
            MDC.clear();  // Clear MDC values to ensure they don't bleed into other logs
        }
    }

    public static void logTechGetUserByIdInfo(String action, String message, UUID commonId) {
        try {
           // addCurrentLogLevel();
            MDC.put("action", action);
            MDC.put("commonIdentifier", commonId.toString());
            MDC.put("logType", LogType.tech.toString());
            techLogger.info(message);
        } finally {
            MDC.clear();  // Clear MDC values to ensure they don't bleed into other logs
        }
    }

    public static void logTechGetUserByIdError(String action, Exception exception, UUID commonId) {
        try {
           // addCurrentLogLevel();
            MDC.put("action", action);
            MDC.put("commonIdentifier", commonId.toString());
            MDC.put("logType", LogType.tech.toString());
            MDC.put("exception", exception.toString());
            techLogger.error(exception.getMessage());
        } finally {
            MDC.clear();  // Clear MDC values to ensure they don't bleed into other logs
        }
    }

    private static void addCurrentLogLevel() {
        Level currentLogLevel = ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).getLevel();
        if (currentLogLevel != null) {
            MDC.put("level", toJsonString(currentLogLevel));
            MDC.put("level_value", toJsonString(currentLogLevel));
        }
    }

/*    public static void addCustomFields(Map<String, String> customFields) {
        addCurrentLogLevel();
        customFields.forEach(MDC::put);
    }*/

/*    public static void addCustomField(String key, String value) {
        addCurrentLogLevel();
        MDC.put(key, value);
    }*/

/*    public static void clearCustomFields() {
        MDC.clear();
    }*/

/*
    public static void clearCustomField(String key) {
        MDC.remove(key);
    }

    public static void clearCustomField(Map<String, String> customFields) {
        customFields.forEach((s, s2) -> MDC.remove(s));
    }
*/

    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }
}
