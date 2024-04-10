package com.shop.doubleu.product.crawl;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CrawlService {
    private final DataSourceProperties dataSourceProperties;

    public CrawlService(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    public void start() {
        WebDriver driver = WebDriverUtil.getChromeDriver();
        int successCnt = 0;
        int failCnt = 0;
        String baseUrl = "https://www.kurly.com/categories/";

        log.info("Crawling Execution START:: {}", new Date());

        for (int i = 1; i < 47; i++) {
            List<String> result = getCategory(baseUrl, i);
            String categoryCd = result.get(0);
            String targetUrl = result.get(1);
            if (targetUrl.isEmpty()) {
                log.warn("Skip targetUrl: {}", targetUrl);
                continue;
            }

            try {
                log.info("targetUrl: {}", targetUrl);
                driver.get(targetUrl);
                WebDriverUtil.pageLoad();

                List<WebElement> childElements = driver.findElements(By.cssSelector("#container > div > div.css-1d3w5wq.ef36txc6 > div.css-11kh0cw.ef36txc5 > a"));

                for (int j = 1; j <= childElements.size(); j++) {
                    WebElement element = driver.findElement(By.cssSelector("#container > div > div.css-1d3w5wq.ef36txc6 > div.css-11kh0cw.ef36txc5 > a:nth-child(" + j + ")"));
                    element.click();
                    WebDriverUtil.pageLoad();

                    // 상품 정보 추출
                    ProductVO detailVo = getDetailInfo(driver, categoryCd);
                    // 상품 정보 DB 저장
                    insertData(detailVo);

                    successCnt++;
                    // 이전 페이지로 다시 이동
                    driver.navigate().back();
                }
            } catch (Exception e) {
                failCnt++;
                log.error(e.getMessage());
            }
            log.info("Category Execution END:: Category: {}, SuccessCnt: {}, FailedCnt: {}", categoryCd, successCnt, failCnt);
        }
        WebDriverUtil.quit(driver);
        log.info("Crawling Execution END:: SuccessCnt: {}, FailedCnt: {}", successCnt, failCnt);
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
        for (WebElement priceElement : priceElements) {
            String cssSelector = priceElement.findElement(By.tagName("span")).getAttribute("class");

            WebElement regularPriceElement = priceElement.findElement(By.cssSelector("span.css-9pf1ze.e1q8tigr2"));
            int regularPrice = Integer.parseInt(regularPriceElement.getText().replaceAll("[,원]", ""));
            detailVo.setRegularPrice(regularPrice);
            if (cssSelector.contains("e1q8tigr3")) {
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
            WebElement dtElement = childElements.get(i).findElement(By.cssSelector("li:nth-child(" + (i + 1) + ") > dt"));
            WebElement ddElement = childElements.get(i).findElement(By.cssSelector("li:nth-child(" + (i + 1) + ") > dd"));
            String text = dtElement.getText();
            StringBuilder info = new StringBuilder();

            List<WebElement> pElements = ddElement.findElements(By.tagName("p"));
            for (int j = 0; j < pElements.size(); j++) {
                WebElement pElement = pElements.get(j);
                info.append(pElement.getText());
                if (pElements.size() > 1 && j < pElements.size() - 1) info.append("\n");
            }

            if (text.contains("배송")) {
                log.debug("delivery info: {}", info);
                detailVo.setProductDeliveryInfo(String.valueOf(info));
            } else if (text.contains("판매자")) {
                log.debug("seller info: {}", info);
                detailVo.setProductSeller(String.valueOf(info));
            } else if (text.contains("포장타입")) {
                log.debug("packageType info: {}", info);
                detailVo.setProductPackageType(String.valueOf(info));
            } else if (text.contains("판매단위")) {
                detailVo.setSalesUnit(String.valueOf(info));
                log.debug("salesUnit info: {}", info);
            } else if (text.contains("중량")) {
                detailVo.setProductWeight(String.valueOf(info));
                log.debug("weight info: {}", info);
            } else if (text.contains("소비기한")) {
                detailVo.setProductExpirationDate(String.valueOf(info));
                log.debug("expirationDate info: {}", info);
            } else if (text.contains("안내사항")) {
                detailVo.setProductNotification(String.valueOf(info));
                log.debug("noti info: {}", info);
            } else if (text.contains("알레르기")) {
                detailVo.setProductAllergyInfo(String.valueOf(info));
                log.debug("allergy info: {}", info);
            } else {
                log.warn("getDetailInfo WARN:: Undefined field: {}, element:{}", text, dtElement);
            }
        }

        return detailVo;
    }

    public void insertData(ProductVO detailVo) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
            connection.setAutoCommit(false);

            // PRODUCT 테이블 데이터 저장
            String productSql = "INSERT INTO product (PRODUCT_NAME, PRODUCT_SUBNAME, CATEGORY_CODE, PRODUCT_IMAGE, REGISTRATION_DATE, PRODUCT_REGULAR_PRICE, PRODUCT_DISCOUNT_PRICE, PRODUCT_DISCOUNT_RATE) " +
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
                    long productId = generatedKeys.getLong(1);

                    // PRODUCT_DETAIL 테이블 데이터 저장
                    String productDetailSql = "INSERT INTO product_detail (PRODUCT_ID, PRODUCT_SELLER, PRODUCT_PACKAGE_TYPE, " +
                            "PRODUCT_WEIGHT, PRODUCT_SALES_UNIT, PRODUCT_ALLERGY_INFO, PRODUCT_NOTIFICATION, " +
                            "PRODUCT_EXPIRATION_DATE, PRODUCT_DETAIL, PRODUCT_DELIVERY_INFO) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                    detailStatement.setString(10, detailVo.getProductDeliveryInfo());

                    int detailRowsInserted = detailStatement.executeUpdate();
                    if (detailRowsInserted > 0) {
                        log.info("insertData INFO:: Data inserted successfully into both product and 'product_detail' tables.");
                        connection.commit();
                    } else {
                        log.error("insertData ERROR:: Failed to insert data into 'product_detail' table.");
                        connection.rollback();  // 트랜젹션 롧백
                    }
                } else {
                    log.error("insertData ERROR:: Failed to retrieve generated keys for PRODUCT_ID.");
                    connection.rollback();
                }
            } else {
                log.error("insertData ERROR:: Failed to insert data into 'product' table. ");
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error("insertData ERROR:: " + e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                log.error("insertData ERROR:: Rollback failed: " + ex.getMessage());
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("insertData ERROR:: Failed to close connection: " + e.getMessage());
            }
        }
    }

    private List<String> getCategory(String baseUrl, int num) {
        List<String> result = new ArrayList<>();
        String categoryUrl;

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
}
