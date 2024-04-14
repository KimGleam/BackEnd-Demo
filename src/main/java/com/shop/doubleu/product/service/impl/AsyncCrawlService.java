package com.shop.doubleu.product.service.impl;

import com.shop.doubleu.product.common.DataSourceProperties;
import com.shop.doubleu.product.dto.ProductDTO;
import com.shop.doubleu.product.repository.ProductDetailRepository;
import com.shop.doubleu.product.repository.ProductRepository;
import com.shop.doubleu.product.service.CrawlService;
import com.shop.doubleu.product.service.impl.async.CrawlScanner;
import com.shop.doubleu.product.service.impl.async.CrawlWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service("asyncCrawlService")
@RequiredArgsConstructor
@Slf4j
public class AsyncCrawlService implements CrawlService {

    private final DataSourceProperties dataSourceProperties;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public void execute() {
        BlockingQueue<List<ProductDTO>> queue = new LinkedBlockingQueue<>(); // BlockingQueue 초기화
        ExecutorService executorService = Executors.newFixedThreadPool(20 ); // 고정된 작업자 스레드 수
        List<CompletableFuture<Void>> crawlingFutures = new ArrayList<>(); // 크롤링 작업 실행 객체
        AtomicBoolean scannersCompleted = new AtomicBoolean(false);

        // 크롤링 작업
        for (int i = 1; i < 47; i++) {
            int TASK = i; // 카테고리 고유 번호
            CompletableFuture<Void> crawlingFuture = CompletableFuture.runAsync(() -> {
                try {
                    new CrawlScanner(queue, TASK).crawlCategoryAsync(); // 크롤링 작업 시작
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 크롤링 작업 완료 플래그
                    if (TASK == 46) {
                        scannersCompleted.set(true);
                    }
                }
            }, executorService);
            crawlingFutures.add(crawlingFuture); // 크롤링 작업 목록에 추가
        }

        // 데이터 삽입 작업
        CompletableFuture<Void> insertionFuture = CompletableFuture.runAsync(() -> {
            int TASK = 1;
            try {
                while (!Thread.currentThread().isInterrupted() && (!scannersCompleted.get() || !queue.isEmpty())) {
                    TASK++;
                    List<ProductDTO> productList = queue.poll(1, TimeUnit.SECONDS); // 1초 대기
                    if (productList != null && !productList.isEmpty()) {
                        new CrawlWorker(queue, TASK, productRepository, productDetailRepository).insertData(productList); // 데이터 삽입 작업 시작
                    } else {
                        if (scannersCompleted.get()) {
                            log.warn("모든 크롤링 작업이 완료되었으며 대기열이 비어 있습니다.");
                            return;
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, executorService);

        // 모든 크롤링 작업 및 데이터 삽입 작업이 완료될 때까지 대기
        CompletableFuture<Void> allCrawlingFutures = CompletableFuture.allOf(crawlingFutures.toArray(new CompletableFuture[0]));
        allCrawlingFutures.join();
        insertionFuture.join();

        // ExecutorService 종료
        executorService.shutdownNow();
        log.info("### All crawling tasks are complete! Bye.");
    }

}
