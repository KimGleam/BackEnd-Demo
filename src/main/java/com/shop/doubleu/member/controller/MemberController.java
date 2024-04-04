package com.shop.doubleu.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * com.shop.doubleu.member.controller.MemberController
 * <p>
 * MemberController
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
 *  2024/02/24    김태욱            최초 생성
 * </pre>
 */

@OpenAPIDefinition(
	info = @Info(
		title = "Member API",
		version = "1.0",
		description = "Member API"
	)
)
@Tag(name = "Member API", description = "Member API Controller")
@Slf4j
@RestController
@RequestMapping("/user")
public class MemberController {

	/**
	 *
	 * @return
	 */
	@Operation(summary = "회원 정보", description = "회원 정보")
	@GetMapping("/info")
	public String test(){
		return "Member Name";
	}

}
