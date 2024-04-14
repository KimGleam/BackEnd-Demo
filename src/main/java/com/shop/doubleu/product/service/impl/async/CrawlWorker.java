package com.shop.doubleu.product.service.impl.async;

import com.shop.doubleu.product.dto.ProductDTO;
import com.shop.doubleu.product.entity.Product;
import com.shop.doubleu.product.entity.ProductDetail;
import com.shop.doubleu.product.repository.ProductDetailRepository;
import com.shop.doubleu.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CrawlWorker implements Runnable {
    private final BlockingQueue<List<ProductDTO>> queue;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    int TASK;
    private final int successCnt = 0;
    private final int failCnt = 0;

    public CrawlWorker(BlockingQueue<List<ProductDTO>> queue, int TASK, ProductRepository productRepository, ProductDetailRepository productDetailRepository) {
        this.queue = queue;
        this.TASK = TASK;
        this.productRepository = productRepository;
        this.productDetailRepository = productDetailRepository;
    }
    @Override
    public void run() {
        log.info("### CrawlWorker TASK[{}] START", TASK);
        try {
            while (!Thread.currentThread().isInterrupted()) {
                List<ProductDTO> productList = queue.take();
                if (productList.isEmpty()) {
                    log.info("### CrawlWorker TASK[{}] Queue 대기열이 비어 있어 Worker 가 중단됩니다.", TASK);
                    break;
                }
                insertData(productList);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("### CrawlWorker TASK[{}] interrupted: {}", TASK, e.getMessage());
        }
        log.info("### CrawlWorker TASK[{}] Complete!!", TASK);
    }

    public void insertData(List<ProductDTO> productList) {
        try {
            log.info("### CrawlWorker TASK[{}] START", TASK);
            for (ProductDTO detailVo : productList) {
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
                        .detailImage(detailVo.getDetailImage())
                        .seller(detailVo.getSeller())
                        .packageType(detailVo.getPackageType())
                        .weight(detailVo.getWeight())
                        .salesUnit(detailVo.getSalesUnit())
                        .allergyInfo(detailVo.getAllergyInfo())
                        .deliveryInfo(detailVo.getDeliveryInfo())
                        .livestockHistoryInfo(detailVo.getLivestockHistoryInfo())
                        .stockingInfo(detailVo.getStockingInfo())
                        .afterServiceInfo(detailVo.getAfterServiceInfo())
                        .sugarContent(detailVo.getSugarContent())
                        .notification(detailVo.getNotification())
                        .carefulInfo(detailVo.getCarefulInfo())
                        .expirationDate(detailVo.getExpirationDate())
                        .build();
                productDetailRepository.save(productDetail);
            }
            log.info("### CrawlWorker TASK[{}] Complete!!", TASK);
        } catch (Exception e) {
            log.error("### CrawlWorker TASK[{}] ERROR:: ", TASK + e.getMessage());
            throw new RuntimeException("Failed to insert data.", e);
        }
    }

}