package com.shop.doubleu.product.dummy.crawling;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class Crawling {
    final String URL = "jdbc:mysql://localhost:3306/doubleu";
    final String USERNAME = "root";
    final String PASSWORD = "root99";
    int successCnt = 0;
    int failCnt = 0;
    private final WebDriver driver;

    public Crawling() {
        this.driver = WebDriverUtil.getChromeDriver();
    }

    public void start() {
        log.info("Crawling Execution START:: {}", new Date());
        String baseUrl = "https://www.kurly.com/categories/";

        for (int i = 1; i < 47; i++) {
            try {
                List<String> result  = getCategory(baseUrl, i);
                String category = result.get(0);
                String targetUrl = result.get(1);
                if (targetUrl.isEmpty()) {
                    continue;
                }

                log.info("targetUrl: {}", targetUrl);
                driver.get(targetUrl);
                List<WebElement> childElements = driver.findElements(By.cssSelector("#container > div > div.css-1d3w5wq.ef36txc6 > div.css-11kh0cw.ef36txc5 > a"));

                for (int j = 1; j <= childElements.size(); j++) {
                    WebElement element = driver.findElement(By.cssSelector("#container > div > div.css-1d3w5wq.ef36txc6 > div.css-11kh0cw.ef36txc5 > a:nth-child(" + j + ")"));
                    element.click();
                    WebDriverUtil.pageLoad();

                    ProductVO detailVo = getDetailInfo(driver, category);
                    insertData(detailVo);

                    // 이전 페이지로 다시 이동
                    successCnt++;
                    driver.navigate().back();
                }
                WebDriverUtil.pageLoad();
            } catch (Exception e) {
                failCnt++;
                e.printStackTrace();
            }
        }
        WebDriverUtil.quit(driver);
        log.info("Crawling Execution END:: {}, SuccessCnt: {}, FailedCnt: {}", new Date(), successCnt, failCnt);
    }

    public ProductVO getDetailInfo(WebDriver driver, String category) {
        ProductVO detailVo = new ProductVO();

        // 상품명 추출
        WebElement nameElement = driver.findElement(By.cssSelector("#product-atf > section > div.css-1qy9c46.ezpe9l12 > h1"));
        WebElement subNameElement = driver.findElement(By.cssSelector("#product-atf > section > div.css-1qy9c46.ezpe9l12 > h2"));
        detailVo.setProductName(nameElement.getText());
        detailVo.setProductSubName(subNameElement.getText());

        // 가격 추출
        List<WebElement> priceElements = driver.findElements(By.cssSelector("#product-atf > section > h2"));
        for (int i = 0; i < priceElements.size(); i++) {
            WebElement priceElement = priceElements.get(i);
            String cssSelector = priceElement.findElement(By.tagName("span")).getAttribute("class");

            WebElement regularPriceElement = priceElement.findElement(By.cssSelector("span.css-9pf1ze.e1q8tigr2"));
            int regularPrice = Integer.parseInt(regularPriceElement.getText().replaceAll("[,원]", ""));
            detailVo.setRegularPrice(regularPrice);

            if (cssSelector.contains("span.css-5nirzt.e1q8tigr3")) {
                WebElement discountRateElement = priceElement.findElement(By.cssSelector("span.css-5nirzt.e1q8tigr3"));
                int discountRate = Integer.parseInt(discountRateElement.getText().replace("%", ""));
                int discountPrice = (int) (regularPrice * (1 - discountRate / 100.0)); // 할인된 가격 계산
                detailVo.setDiscountRate(discountRate);
                detailVo.setDiscountPrice(discountPrice);
            }
        }

        // 상품 사진 추출
        WebElement mainImgElement = driver.findElement(By.cssSelector("#product-atf > div > div > div > div > div > span > img"));
        WebElement detailElement = driver.findElement(By.cssSelector("#detail > div.css-kqvkc7.es6jciw1 > img"));
        detailVo.setProductImage(mainImgElement.getAttribute("src"));
        detailVo.setProductDetail(detailElement.getAttribute("src"));
        detailVo.setCategoryCode(category);

        // 상품 정보 추출
        List<WebElement> childElements = driver.findElements(By.cssSelector("#product-atf > section > ul > li"));
        for (int i = 0; i < childElements.size(); i++) {
            WebElement dtElement = childElements.get(i).findElement(By.cssSelector("li:nth-child(" + (i+1) + ") > dt"));
            WebElement ddElement = childElements.get(i).findElement(By.cssSelector("li:nth-child(" + (i+1) + ") > dd > p"));
            String text = dtElement.getText();

            if (text.contains("배송")) {
                log.info("delivery info: {}", ddElement.getText());
                detailVo.setProductDeliveryInfo(ddElement.getText());
            } else if (text.contains("판매자")) {
                log.info("seller info: {}", ddElement.getText());
                detailVo.setProductSeller(ddElement.getText());
            } else if (text.contains("포장타입")) {
                log.info("packageType info: {}", ddElement.getText());
                detailVo.setProductPackageType(ddElement.getText());
            } else if (text.contains("판매단위")) {
                detailVo.setSalesUnit(ddElement.getText());
                log.info("salesUnit info: {}", ddElement.getText());
            } else if (text.contains("중량")) {
                detailVo.setProductWeight(ddElement.getText());
                log.info("weight info: {}", ddElement.getText());
            } else if (text.contains("소비기한")) {
                detailVo.setProductExpirationDate(ddElement.getText());
                log.info("expirationDate info: {}", ddElement.getText());
            } else if (text.contains("안내사항")) {
                detailVo.setProductNotification(ddElement.getText());
                log.info("noti info: {}", ddElement.getText());
            } else if (text.contains("알레르기")) {
                detailVo.setProductAllergyInfo(ddElement.getText());
                log.info("allergy info: {}", ddElement.getText());
            } else {
                log.warn("getDetailInfo WARN:: Undefined field");
            }
        }

        return detailVo;
    }

    public void insertData(ProductVO detailVo) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            // Insert into PRODUCT table
            String productSql = "INSERT INTO PRODUCT (PRODUCT_NAME, PRODUCT_SUBNAME, CATEGORY_CODE, PRODUCT_IMAGE, REGISTRATION_DATE, PRODUCT_REGULAR_PRICE, PRODUCT_DISCOUNT_PRICE, PRODUCT_DISCOUNT_RATE) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement productStatement = connection.prepareStatement(productSql, Statement.RETURN_GENERATED_KEYS);
            productStatement.setString(1, detailVo.getProductName());
            productStatement.setString(2, detailVo.getProductSubName());
            productStatement.setString(3, detailVo.getCategoryCode());
            productStatement.setString(4, detailVo.getProductImage());
            LocalDateTime now = LocalDateTime.now();
            productStatement.setObject(5, now);
            productStatement.setInt(6, detailVo.getRegularPrice());
            productStatement.setInt(7, detailVo.getDiscountPrice());
            productStatement.setInt(8, detailVo.getDiscountRate());

            int rowsInserted = productStatement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = productStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Long productId = generatedKeys.getLong(1);

                    // Insert into PRODUCT_DETAIL table
                    String productDetailSql = "INSERT INTO PRODUCT_DETAIL (PRODUCT_ID, PRODUCT_SELLER, PRODUCT_PACKAGE_TYPE, " +
                            "PRODUCT_WEIGHT, PRODUCT_SALES_UNIT, PRODUCT_ALLERGY_INFO, PRODUCT_NOTIFICATION, " +
                            "PRODUCT_EXPIRATION_DATE, PRODUCT_DETAIL) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement detailStatement = connection.prepareStatement(productDetailSql);
                    detailStatement.setLong(1, productId);
                    detailStatement.setString(2, detailVo.getProductSeller());
                    detailStatement.setString(3, detailVo.getProductPackageType());
                    detailStatement.setString(4, detailVo.getProductWeight());
                    detailStatement.setString(5, detailVo.getSalesUnit());
                    detailStatement.setString(6, detailVo.getProductAllergyInfo());
                    detailStatement.setString(7, detailVo.getProductNotification());
                    detailStatement.setString(8, detailVo.getProductExpirationDate());
                    detailStatement.setString(9, detailVo.getProductDetail());

                    int detailRowsInserted = detailStatement.executeUpdate();
                    if (detailRowsInserted > 0) {
                        log.info("insertData INFO:: Data inserted successfully into both PRODUCT and PRODUCT_DETAIL tables.");
                    } else {
                        log.info("insertData ERROR:: Failed to insert data into PRODUCT_DETAIL table.");
                    }
                } else {
                    log.info("insertData ERROR:: Failed to retrieve generated keys for PRODUCT_ID.");
                }
            } else {
                log.info("insertData ERROR:: Failed to insert data into PRODUCT table. ");
            }
        } catch (SQLException e) {
            log.error("insertData ERROR:: " + e.getMessage());
        }
    }

    private List<String> getCategory(String baseUrl, int num) {
        List<String> result = new ArrayList<>();
        String categoryUrl = "";

        if (num >= 1 && num <= 8) {
            String categoryCode = "90700" + num;
            categoryUrl = baseUrl + categoryCode;

            result.add(categoryCode);
            result.add(categoryUrl);
        } else if (num >= 9 && num <= 16) {
            String categoryCode = "90800" + (num - 8);
            categoryUrl = baseUrl + categoryCode;

            result.add(categoryCode);
            result.add(categoryUrl);
        } else if (num >= 17 && num <= 31) {
            int categoryIndex = num - 16;
            String[] categoryCodes = {"909001", "909002", "909003", "909004", "909005", "909006", "909007", "909009", "909010", "909011",
                    "909012", "909013", "909014", "909015", "909016"};
            categoryUrl = baseUrl + categoryCodes[categoryIndex - 1];

            result.add(categoryCodes[categoryIndex - 1]);
            result.add(categoryUrl);
        } else if (num >= 32 && num <= 41) {
            int categoryIndex = num - 31;
            String[] categoryCodes = {"910001", "910002", "910003", "910004", "910005", "910007", "910009", "910010", "910011", "910012"};
            categoryUrl = baseUrl + categoryCodes[categoryIndex - 1];

            result.add(categoryCodes[categoryIndex - 1]);
            result.add(categoryUrl);
        } else if (num >= 42 && num <= 46) {
            int categoryIndex = num - 41;
            String[] categoryCodes = {"911001", "911002", "911003", "911005", "911006"};
            categoryUrl = baseUrl + categoryCodes[categoryIndex - 1];

            result.add(categoryCodes[categoryIndex - 1]);
            result.add(categoryUrl);
        }

        return result;
    }

    public static void main(String[] args) {
        Crawling crawling = new Crawling();
        crawling.start();
    }
}
