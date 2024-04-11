package com.shop.doubleu.product.service;

import com.shop.doubleu.product.common.DataSourceProperties;
import com.shop.doubleu.product.common.ProductDTO;
import com.shop.doubleu.product.common.WebDriverUtil;
import com.shop.doubleu.product.entity.Product;
import com.shop.doubleu.product.entity.ProductDetail;
import com.shop.doubleu.product.repository.ProductDetailRepository;
import com.shop.doubleu.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class CrawlService {
    private final DataSourceProperties dataSourceProperties;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    private WebDriver driver;
    private final String baseUrl = "https://www.kurly.com/categories/";
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

    /**
     * Crawling 실행
     */
    public void start() {
        driver = WebDriverUtil.getChromeDriver();
        log.info("Crawling Execution START:: {}", new Date());

        for (int i = 1; i < 47; i++) {
            try {
                crawlCategory(i);
            } catch (Exception e) {
                failCnt++;
                log.error("Error while crawling category {}: {}", i, e.getMessage());
            }
        }
        WebDriverUtil.quit(driver);
        log.info("Crawling Execution END:: SuccessCnt: {}, FailedCnt: {}", successCnt, failCnt);
    }

    /**
     * 카테고리 별 상품 크롤링 후 데이터 저장 (MySQL)
     * @param categoryNum: 카테고리 고유 번호 (CATEGORY_CODE)
     */
    private void crawlCategory(int categoryNum) {
        List<String> result = getCategory(baseUrl, categoryNum);
        String categoryCd = result.get(0);
        String targetUrl = result.get(1);

        if (targetUrl.isEmpty()) {
            log.warn("Skip targetUrl for category {}: {}", categoryNum, targetUrl);
            return;
        }

        log.info("Crawling category {}: {}", categoryCd, targetUrl);
        driver.get(targetUrl);
        WebDriverUtil.pageLoad();

        List<WebElement> childElements = driver.findElements(By.cssSelector("#container > div > div.css-1d3w5wq.ef36txc6 > div.css-11kh0cw.ef36txc5 > a"));

        for (int j = 1; j <= childElements.size(); j++) {
            WebElement element = driver.findElement(By.cssSelector("#container > div > div.css-1d3w5wq.ef36txc6 > div.css-11kh0cw.ef36txc5 > a:nth-child(" + j + ")"));
            element.click();
            WebDriverUtil.pageLoad();

            // 상품 정보 추출
            ProductDTO detailVo = getDetailInfo(driver, categoryCd);
            // 상품 정보 DB 저장
            insertData(detailVo);
            successCnt++;

            // 페이지 뒤로 가기
            driver.navigate().back();
        }

        log.info("Category Execution END:: Category: {}, SuccessCnt: {}, FailedCnt: {}", categoryCd, successCnt, failCnt);
    }

    // =================================================================================================================
    // 상품 정보 추출 메서드
    /**
     * 상품 정보 추출
     * @param driver: 크롬 드라이버
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
                    log.debug("배송 info: {}", info);
                    detailVo.setProductDeliveryInfo(info.toString());
                    break;
                case "판매자":
                    log.debug("판매자 info: {}", info);
                    detailVo.setProductSeller(info.toString());
                    break;
                case "포장타입":
                    log.debug("포장타입 info: {}", info);
                    detailVo.setProductPackageType(info.toString());
                    break;
                case "판매단위":
                    detailVo.setSalesUnit(info.toString());
                    log.debug("판매단위 info: {}", info);
                    break;
                case "중량":
                    detailVo.setProductWeight(info.toString());
                    log.debug("중량 info: {}", info);
                    break;
                case "소비기한":
                    detailVo.setProductExpirationDate(info.toString());
                    log.debug("소비기한 info: {}", info);
                    break;
                case "안내사항":
                    detailVo.setProductNotification(info.toString());
                    log.debug("안내사항 info: {}", info);
                    break;
                case "알레르기":
                    detailVo.setProductAllergyInfo(info.toString());
                    log.debug("알레르기 info: {}", info);
                    break;
                default:    // 새로운 필드 추가
                    log.warn("getDetailInfo WARN:: Undefined field: {}, element:{}", text, dtElement);
                    break;
            }
        }
    }

    private static void getImageInfo(WebDriver driver, String category, ProductDTO detailVo) {
        WebElement mainImgElement = driver.findElement(By.cssSelector("#product-atf > div > div > div > div > div > span > img"));
        WebElement detailElement = driver.findElement(By.cssSelector("#detail > div.css-kqvkc7.es6jciw1 > img"));
        detailVo.setProductImage(mainImgElement.getAttribute("src"));
        detailVo.setProductDetail(detailElement.getAttribute("src"));
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
    // =================================================================================================================

    /**
     * 상품등록 JPA 버전
     * : product 테이블 insert 후 product_id를 product_detail 테이블에서 foreign key 로 등록
     * @param detailVo: Product Info 객체
     */
    public void insertData(ProductDTO detailVo) {
        try {
            // Save Product entity
            Product product = Product.builder()
                    .productName(detailVo.getProductName())
                    .productSubName(detailVo.getProductSubName())
                    .categoryCode(detailVo.getCategoryCode())
                    .productImage(detailVo.getProductImage())
                    .registrationDate(LocalDateTime.now())
                    .regularPrice(detailVo.getRegularPrice())
                    .discountPrice(detailVo.getDiscountPrice())
                    .discountRate(detailVo.getDiscountRate())
                    .build();
            productRepository.save(product);

            ProductDetail productDetail = ProductDetail.builder()
                    .productId(product.getId()) // Foreign Key
                    .productSeller(detailVo.getProductSeller())
                    .productPackageType(detailVo.getProductPackageType())
                    .productWeight(detailVo.getProductWeight())
                    .salesUnit(detailVo.getSalesUnit())
                    .productAllergyInfo(detailVo.getProductAllergyInfo())
                    .productNotification(detailVo.getProductNotification())
                    .productExpirationDate(detailVo.getProductExpirationDate())
                    .productDetail(detailVo.getProductDetail())
                    .productDeliveryInfo(detailVo.getProductDeliveryInfo())
                    .build();
            productDetailRepository.save(productDetail);

            log.info("insertData INFO:: Data inserted successfully into both Product and ProductDetail entities.");
        } catch (Exception e) {
            log.error("insertData ERROR:: " + e.getMessage());
            throw new RuntimeException("Failed to insert data.", e);
        }
    }

    /**
     * 상품 카테고리 URL 생성
     * @param baseUrl: 컬리 기본 카테고리 URL
     * @param num: 카테고리 번호
     * @return 카테고리 번호 및 URL
     */
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
