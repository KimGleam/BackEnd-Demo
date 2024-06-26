package com.shop.doubleu.product.controller;

import com.shop.doubleu.product.dto.CategoryDTO;
import com.shop.doubleu.product.entity.Category;
import com.shop.doubleu.product.service.CategoryService;
import com.shop.doubleu.product.service.impl.AsyncCrawlService;
import com.shop.doubleu.product.service.CrawlService;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import com.shop.doubleu.product.service.ProductService;
import com.shop.global.support.SuccessResponse;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * com.shop.doubleu.product.controller.ProductController
 * <p>
 * ProductController
 *
 * @author 김태욱
 * @version 1.0
 * @since 2024/02/24
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *     수정일        수정자           수정내용
 *  ----------    --------        ---------------------------
 *  2024/04/06    김태욱            최초 생성
 * </pre>
 */

@OpenAPIDefinition(
	info = @Info(
		title = "Product API",
		version = "1.0",
		description = "Product API"
	)
)
@Tag(name = "Product API", description = "Product API Controller")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/goods")
public class ProductController {

	private final ProductService productService;

	@Qualifier("asyncCrawlService")
	private final CrawlService asyncCrawlService;
	private final CategoryService categoryService;
	/**
	 *
	 * @return
	 */
	@Operation(summary = "상품 목록", description = "상품 목록 조회")
	@GetMapping("/list")
	public SuccessResponse getProductList(@RequestHeader String productId) {
		return new SuccessResponse(productService.getProductList(productId));
	}
	@RequestMapping("/getAllCategoryList")
	@Description("전체 상품 카테고리 목록")
	public SuccessResponse getAllCategoryList() {
		return new SuccessResponse(categoryService.getAllCategoryList());
	}

	@RequestMapping("/getParentCategoryList")
	@Description("하위 상품 카테고리 목록")
	public SuccessResponse getParentCategoryList(@RequestParam String pCode) {
		return new SuccessResponse(categoryService.getParentCategoryList(pCode));
	}

	@Operation(summary = "상품 상세 정보", description = "상품 상세 정보 조회")
	@GetMapping("/detail")
	public SuccessResponse getProductsWithDetails(@RequestParam long productId) {
		return new SuccessResponse(productService.getProductWithDetailById(productId));
	}

	@Operation(summary = "샘플 상품", description = "상품 크롤링")
	@RequestMapping("/crawl")
	public SuccessResponse productListCrawl() {
		asyncCrawlService.execute();
		return new SuccessResponse();
	}

}
