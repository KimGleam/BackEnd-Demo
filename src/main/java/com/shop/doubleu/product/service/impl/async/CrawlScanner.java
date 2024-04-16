package com.shop.doubleu.product.service.impl.async;

import com.shop.doubleu.product.common.CustomWebDriverResource;
import com.shop.doubleu.product.common.WebDriverUtil;
import com.shop.doubleu.product.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Async;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class CrawlScanner {
    private final BlockingQueue<List<ProductDTO>> queue;
    private final int TASK;
    private List<String> result = new ArrayList<>();
    private int successCnt = 0;
    private int failCnt = 0;
    private static final Map<Integer, String[]> CATEGORY_MAP = new HashMap<>();
    static {
        CATEGORY_MAP.put(1, new String[]{"907001", "907002", "907003", "907004", "907005", "907006", "907007", "907008"});
        CATEGORY_MAP.put(2, new String[]{"908001", "908002", "908003", "908004", "908005", "908006", "908007", "908008"});
        CATEGORY_MAP.put(3, new String[]{"909001", "909002", "909003", "909004", "909005", "909006", "909007", "909009", "909010", "909011", "909012", "909013", "909014", "909015", "909016"});
        CATEGORY_MAP.put(4, new String[]{"910001", "910002", "910003", "910004", "910005", "910007", "910009", "910010", "910011", "910012"});
        CATEGORY_MAP.put(5, new String[]{"911001", "911002", "911003", "911005", "911006"});
    }

    public CrawlScanner(BlockingQueue<List<ProductDTO>> queue, int TASK) {
        String baseUrl = "https://www.kurly.com/categories/";
        result = getCategory(baseUrl, TASK);
        this.queue = queue;
        this.TASK = TASK;
    }

    @Async
    public CompletableFuture<Void> crawlCategoryAsync() {
        log.info("### CrawlScanner TASK[{}] START:: URL: {}", TASK, result.get(1));
        CompletableFuture<Void> future = new CompletableFuture<>();

        try (CustomWebDriverResource customDriver = new CustomWebDriverResource()) {
            WebDriver driver = customDriver.getWebDriver();
            queue.put(Objects.requireNonNull(crawlCategory(driver)));
            future.complete(null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("### CrawlScanner TASK[{}] 가 중단되었습니다. Queue 대기열이 가득 찼습니다.", TASK);
            future.completeExceptionally(e);
        } catch (Exception e) {
            log.error("### CrawlScanner TASK[{}] interrupted: {}", TASK, e.getMessage());
            future.completeExceptionally(e);
        }

        log.info("### CrawlScanner TASK[{}] Complete:: SuccessCnt: {}, FailedCnt: {}", TASK, successCnt, failCnt);
        return future;
    }

    private List<ProductDTO> crawlCategory(WebDriver driver) {
        String categoryCd = result.get(0);
        String targetUrl = result.get(1);

        if (targetUrl.isEmpty()) {
            log.warn("Skip targetUrl for category {}: {}", TASK, targetUrl);
            return null;
        }
        driver.get(targetUrl);
        WebDriverUtil.pageLoad();

        List<WebElement> childElements = driver.findElements(By.cssSelector("#container > div > div.css-1d3w5wq.ef36txc6 > div.css-11kh0cw.ef36txc5 > a"));
        List<ProductDTO> productList = new ArrayList<>();
        try {
            for (int j = 1; j <= childElements.size(); j++) {
                WebElement element = driver.findElement(By.cssSelector("#container > div > div.css-1d3w5wq.ef36txc6 > div.css-11kh0cw.ef36txc5 > a:nth-child(" + j + ")"));
                element.click();
                WebDriverUtil.pageLoad();
                // 상품 정보 추출
                productList.add(getDetailInfo(driver, categoryCd)); // 상품 정보 Queue 추가
                successCnt++;
                driver.navigate().back();   // 뒤로가기
            }
        } catch (Exception e) {
            failCnt++;
        }

        return productList;
    }

    // =================================================================================================================
    // 상품 정보 추출 메서드

    /**
     * 상품 정보 추출
     *
     * @param driver:   크롬 드라이버
     * @param category: 카테고리 번호
     * @return 추출된 Product Info 객체
     */
    public ProductDTO getDetailInfo(WebDriver driver, String category) {
        ProductDTO detailVo = new ProductDTO();
        // 상품명 추출
        getProductNameInfo(driver, detailVo);
        // 상품 가격 추출
        getPriceInfo(driver, detailVo);
        // 사진 추출
        getImageInfo(driver, category, detailVo);
        // 상품 상세 정보 추출
        getProductDetailInfo(driver, detailVo);

        return detailVo;
    }

    private static void getProductDetailInfo(WebDriver driver, ProductDTO detailVo) {
        List<WebElement> childElements = driver.findElements(By.cssSelector("#product-atf > section > ul > li"));

        for (WebElement element : childElements) {
            WebElement dtElement = element.findElement(By.tagName("dt"));
            WebElement ddElement = element.findElement(By.tagName("dd"));
            String text = dtElement.getText();
            StringBuilder info = new StringBuilder();

            List<WebElement> pElements = ddElement.findElements(By.tagName("p"));
            for (int j = 0; j < pElements.size(); j++) {
                WebElement pElement = pElements.get(j);
                info.append(pElement.getText());
                if (pElements.size() > 1 && j < pElements.size() - 1) {
                    info.append("\n");
                }
            }

            switch (text) {
                case "배송":
                    log.debug("배송: {}", info);
                    detailVo.setDeliveryInfo(info.toString());
                    break;
                case "판매자":
                    log.debug("판매자: {}", info);
                    detailVo.setSeller(info.toString());
                    break;
                case "포장타입":
                    log.debug("포장타입: {}", info);
                    detailVo.setPackageType(info.toString());
                    break;
                case "판매단위":
                    detailVo.setSalesUnit(info.toString());
                    log.debug("판매단위: {}", info);
                    break;
                case "중량/용량":
                    detailVo.setWeight(info.toString());
                    log.debug("중량/용량: {}", info);
                    break;
                case "소비기한(또는 유통기한)정보":
                    detailVo.setExpirationDate(info.toString());
                    log.debug("소비기한(또는 유통기한)정보: {}", info);
                    break;
                case "유의사항":
                    detailVo.setCarefulInfo(info.toString());
                    log.debug("유의사항: {}", info);
                    break;
                case "안내사항":
                    detailVo.setNotification(info.toString());
                    log.debug("안내사항: {}", info);
                    break;
                case "알레르기정보":
                    detailVo.setAllergyInfo(info.toString());
                    log.debug("알레르기정보: {}", info);
                    break;
                case "축산물이력정보":
                case "축산물 이력정보":
                    detailVo.setLivestockHistoryInfo(info.toString());
                    log.debug("축산물 이력정보: {}", info);
                    break;
                case "무항생제 인증번호":
                    detailVo.setLivestockHistoryInfo(info.toString());
                    log.debug("축산물 이력정보: {}", info);
                    break;
                case "당도":
                    detailVo.setSugarContent(info.toString());
                    log.debug("당도: {}", info);
                    break;
                case "입고안내":
                    detailVo.setStockingInfo(info.toString());
                    log.debug("입고안내: {}", info);
                    break;
                case "A/S 안내":
                    detailVo.setAfterServiceInfo(info.toString());
                    log.debug("A/S 안내: {}", info);
                    break;
                default:    // 새로운 필드 추가
                    log.warn("getDetailInfo WARN:: Undefined field: {}, element:{}", text, dtElement);
                    break;
            }
        }
    }

    private static void getImageInfo(WebDriver driver, String category, ProductDTO detailVo) {
        WebElement mainImgElement = driver.findElement(By.cssSelector("#product-atf > div > div > div > div > div > span > img"));
        detailVo.setProductImage(mainImgElement.getAttribute("src"));
//        List<WebElement> detailElements = driver.findElements(By.cssSelector("#detail > div.css-kqvkc7.es6jciw1 > img"));
        List<WebElement> detailElements = driver.findElements(By.cssSelector("#detail > div.css-kqvkc7.es6jciw1"));
        if (!detailElements.isEmpty()) {
            WebElement detailElement = detailElements.get(0);
            detailVo.setDetailImage(detailElement.getAttribute("src"));
        } else {
            detailVo.setDetailImage(null);
        }
        detailVo.setCategoryCode(category);
    }


    private static void getProductNameInfo(WebDriver driver, ProductDTO detailVo) {
        WebElement nameElement = driver.findElement(By.cssSelector("#product-atf > section > div.css-1qy9c46.ezpe9l12 > h1"));
        WebElement subNameElement = driver.findElement(By.cssSelector("#product-atf > section > div.css-1qy9c46.ezpe9l12 > h2"));
        detailVo.setProductName(nameElement.getText());
        detailVo.setProductSubName(subNameElement.getText());
    }

    private static void getPriceInfo(WebDriver driver, ProductDTO detailVo) {
        List<WebElement> priceElements = driver.findElements(By.cssSelector("#product-atf > section > h2"));
        for (WebElement priceElement : priceElements) {
            String cssSelector = priceElement.findElement(By.tagName("span")).getAttribute("class");
            WebElement regularPriceElement = priceElement.findElement(By.cssSelector("span.css-9pf1ze.e1q8tigr2"));
            int regularPrice = Integer.parseInt(regularPriceElement.getText().replaceAll("[,원]", ""));
            detailVo.setRegularPrice(regularPrice);
            if (cssSelector.contains("e1q8tigr3")) {
                WebElement discountRateElement = priceElement.findElement(By.cssSelector("span.css-5nirzt.e1q8tigr3"));
                int discountRate = Integer.parseInt(discountRateElement.getText().replace("%", ""));
                int discountPrice = (int) (regularPrice * (1 - discountRate / 100.0));
                detailVo.setDiscountRate(discountRate);
                detailVo.setDiscountPrice(discountPrice);
            }
        }
    }

    public static List<String> getCategory(String baseUrl, int num) {
        List<String> result = new ArrayList<>();

        if (num >= 1 && num <= 8) {
            result.add("90700" + num);
            result.add(baseUrl + "90700" + num);
        } else if (num >= 9 && num <= 16) {
            result.add("90800" + (num - 8));
            result.add(baseUrl + "90800" + (num - 8));
        } else if (num >= 17 && num <= 46) {
            for (Map.Entry<Integer, String[]> entry : CATEGORY_MAP.entrySet()) {
                if (entry.getKey() * 8 + 1 <= num && num <= (entry.getKey() + 1) * 8) {
                    int categoryIndex = num - entry.getKey() * 8;
                    result.add(entry.getValue()[categoryIndex - 1]);
                    result.add(baseUrl + entry.getValue()[categoryIndex - 1]);
                    break;
                }
            }
        }
        return result;
    }

}
