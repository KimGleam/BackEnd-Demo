package com.shop.doubleu.product.common;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.util.ObjectUtils;

public class WebDriverUtil {
    private static final String WEB_DRIVER_PATH = getProjectRootPath()+"/src/main/resources/driver/chromedriver.exe"; // WebDriver 경로

    public static WebDriver getChromeDriver() {
        if (ObjectUtils.isEmpty(System.getProperty("webdriver.chrome.driver"))) {
            System.setProperty("webdriver.chrome.driver", WEB_DRIVER_PATH);
        }
        /// Chrome 옵션 세팅
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 브라우저 숨김
        options.addArguments("--remote-allow-origins=*");
        // Driver 생성
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }

    public static void quit(WebDriver driver) {
        if (!ObjectUtils.isEmpty(driver)) {
            driver.quit();
        }
    }

    public static void close(WebDriver driver) {
        if (!ObjectUtils.isEmpty(driver)) {
            driver.close();
        }
    }

    public static void moveToUrl (WebDriver driver, String path) {
        driver.get(path);
    }

    public static void sleep () throws InterruptedException {
        Thread.sleep(1000);
    }

    public static void pageLoad() {  // 페이지 로딩
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getProjectRootPath() {
        Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().toString();
    }
}
