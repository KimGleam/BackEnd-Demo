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
        ExecutorService executorService = Executors.newFixedThreadPool(20); // 고정된 작업자 스레드 수
        List<CompletableFuture<Void>> futures = new ArrayList<>(); // 비동기 작업 실행 객체

        // 크롤링 작업과 데이터 삽입 작업을 동시에 실행
        for (int i = 1; i < 47; i++) {
            int TASK = i; // 카테고리 고유 번호

            // 크롤링 작업
            CompletableFuture<Void> crawlingFuture = CompletableFuture.runAsync(() -> {
                try {
                    new CrawlScanner(queue, TASK).crawlCategoryAsync().join(); // 크롤링 작업 시작
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, executorService);
            futures.add(crawlingFuture); // 크롤링 작업 목록에 추가

            // 데이터 삽입 작업
            CompletableFuture<Void> insertionFuture = CompletableFuture.runAsync(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        List<ProductDTO> productList = queue.take(); // 큐에 데이터가 있으면 가져옴
                        if (!productList.isEmpty()) {
                            new CrawlWorker(queue, TASK, productRepository, productDetailRepository).insertData(productList); // 데이터 삽입 작업 시작
                        } else {
                            log.warn("Queue 대기열이 비어있습니다.");
                           return;
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, executorService);
            futures.add(insertionFuture); // 데이터 삽입 작업 목록에 추가
        }

        // 모든 작업이 완료될 때까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        // ExecutorService 종료
        executorService.shutdown();
        log.info("### All crawling tasks are complete! Bye.");
    }

}
