package com.fx.fx_app.utils;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles the entire spectrum of log levels for the program.
 * Logs exceptions that may or may not be fatal.
 */
public class LogHandler {

    private static final Logger logger = LogManager.getLogger("com.fx.fx_app");
	
    public static void configFileLoadError(String exceptionMessage) {
        logger.fatal("ConfigLoader — file not found 'src/main/resources/config.properties': " + exceptionMessage);
    }

    public static void configSettingsError(String setting) {
        logger.fatal("ConfigLoader — '{}'' setting not found in 'src/main/resources/config.properties'", setting);
    }

    public static void configSettingsOK() {
        logger.info("ConfigLoader — settings OK — 'src/main/resources/config.properties'");
    }

    public static void sourceDataLoadOK(File dataFile) {
        logger.info("DataIO — data file read OK — '{}'", dataFile);
    }

    public static void dataSessionInitOK() {
        logger.info("DataSession — init OK");
    }

    public static void dataSessionInitWarn() {
        logger.warn("DataSession — cannot re-init");
    }

    public static void dataSessionInitError(String exceptionMessage) {
        logger.fatal("DataSession — " + exceptionMessage);
    }

    public static void sourceDataLoadError(String exceptionMessage) {
        logger.fatal("DataValidator — data missing — check source: " + exceptionMessage);
    }

    public static void sourceDataLoadOK() {
        logger.info("DataValidator — data file loads OK");
    }

    public static void userNamePresentInSession(String name) {
        logger.debug("DataValidator — User {} exists in session", name);
    }

    public static void userNameNotPresentInSession(String name) {
        logger.debug("DataValidator — User {} does not exist in session", name);
    }

    public static void currencyPresentInSession(String currency) {
        logger.debug("DataValidator — Currency {} exists in session", currency);
    }

    public static void currencyNotPresentInSession(String currency) {
        logger.debug("DataValidator — Currency {} does not exist in session", currency);
    }

    public static void userNotFound(String fxTrade, String exceptionMessage) {
        logger.warn("DataValidator — User not found: {} : {}", fxTrade, exceptionMessage);
    }

    public static void invalidCurrency(String fromOrTo, String fxTrade, String exceptionMessage) {
        logger.warn("DataValidator — {} currency not found: {} : {}", fromOrTo, fxTrade, exceptionMessage);
    }

    public static void currenciesMatch(String fxTrade) {
        logger.info("DataValidator — currencies match — no operation: {}", fxTrade);
    }

    public static void invalidAmount(String fxTrade) {
        logger.warn("DataValidator — invalid amount: {}", fxTrade);
    }

    public static void userFundsInsufficient(String wallet, String fxTrade, String exceptionMessage) {
        logger.error("DataValidator — {} : {} : {}", exceptionMessage, wallet, fxTrade);
    }


}
