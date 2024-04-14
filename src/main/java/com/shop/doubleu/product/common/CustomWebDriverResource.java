package com.shop.doubleu.product.common;

import org.openqa.selenium.WebDriver;

public class CustomWebDriverResource implements AutoCloseable {
    private final WebDriver driver;

    public CustomWebDriverResource() {
        this.driver = WebDriverUtil.getChromeDriver();
    }

    public WebDriver getWebDriver() {
        return this.driver;
    }

    @Override
    public void close() {
        if (driver != null) {
            WebDriverUtil.quit(driver);
        }
    }
}