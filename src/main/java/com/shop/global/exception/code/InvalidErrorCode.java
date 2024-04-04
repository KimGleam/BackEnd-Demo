package com.shop.global.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 class 한글 설명(요약)
 <PRE>
 한글 설명(상세)
 </PRE>
 @author   : 김재두(메가존)(megazone.jdkim@seegene.com)
 @History
 <PRE>
  * No  Date           Author            Desc
  *---- ------------ ---------------- ------------------------------------
  *   1 2023/11/08   김재두(메가존)   최초작성
 </PRE>
 */
@Getter
@RequiredArgsConstructor
public enum InvalidErrorCode implements CustomErrorCode{
	/**
	 * 에러코드 규칙 (임시)
	 * 400 클라이언트 에러 + 000 3자리 구분 코드 + 000 순서 구분 코드
	 * 500 서버 에러 + 000 3자리 구분 코드 + 000 순서 구분 코드
	 *
	 * 확인요청 작성은 Front 개발시 Exception 발생 대처를 위해 임시 작성 내용 임.
	 * 개발 종료시 삭제 예정
	 */

	// 500400000 ~
	// 새로운 필수 값 체크 에러 코드 추가
	USERNAME_REQUIRED(500400000,"사용자 이름은 필수 값입니다."),
	EMAIL_REQUIRED(500400001, "이메일은 필수 값입니다"),
	ANLNM_REQUIRED(500400002, "Analyte Name은 필수 입력 항목입니다."),
	ANLABB_REQUIRED(500400003, "Analyte Abbreviation은 필수 입력 항목입니다."),
	HOST_REQUIRED(500400004, "Host Setting은 필수 입력 항목입니다."),
	ICTPCD_REQUIRED(500400005, "IC TYPE은 필수 입력 항목입니다."),
	ANALYTE_GENERATE_REQUIRED(500400006, "Analyte (Mutation)을 최소 1개 이상 생성해야 합니다."),
	TAXON_INCLUSIVITY_REQUIRED(500400007, "Taxon Inclusivity은 필수 입력 항목입니다."),
	REQ_DEL_ID_REQUIRED(500400008, "Analyte ID, Cluster ID, Multiplex ID 중 하나는 필수 입력입니다."),

	IDENTITY_PERCENT_REQUIRED(500400009, "[In Identity(%)]은 필수 입력 항목입니다."),
	SEQ_COVERAGE_PERCENT_REQUIRED(500400010, "[In Seq Cov(%)]은 필수 입력 항목입니다."),
	EX_CHECK_PERCENT_REQUIRED(500400011, "[Ex Check(%)]은 필수 입력 항목입니다."),
	METHOD_REQUIRED(500400012, "[In list collection method]은 필수 입력 항목입니다."),
	DBTYPE_REQUIRED(500400013, "[DB Type]은 필수 입력 항목입니다."),
	SEQLVL_REQUIRED(500400014, "[Sequence Level]은 필수 입력 항목입니다."),
	GENENM_REQUIRED(500400015, "[Gene Name]은 필수 입력 항목입니다."),
	GENEABB_REQUIRED(500400016, "[Gene Abb]은 필수 입력 항목입니다."),

	MONO_APCR_PERCENT_REQUIRED(500400017, "[APCR(%)]은 필수 입력 항목입니다. Range:1~100"),
	MONO_ECOV_PERCENT_REQUIRED(500400018, "[E.Cov(%)]은 필수 입력 항목입니다. Range:30~100"),
	MONO_PATIAL_MATCH_PERCENT_REQUIRED(500400019, "[Partial Match(%)]은 필수 입력 항목입니다.Range: 30~100"),
	MONO_PROB5ENDGCNUM_REQUIRED(500400020, "[Probe 5’end GC#]은 필수 입력 항목입니다. Range:0~2"),
	MONO_DEG_REQUIRED(500400021, "[DEG#]은 필수 입력 항목입니다. Range:0~2"),
	MONO_AOCO_OLIGO_ADDVAL_PERCENT_REQUIRED(500400022, "[AOCO oligo Addval(%)]은 필수 입력 항목입니다. Range:0.1~10"),
	MONO_PROBTPCD_REQUIRED(500400023, "[Probe Type]은 필수 입력 항목입니다."),
	MONO_PRMRTPCD_REQUIRED(500400024, "[Primer Type]은 필수 입력 항목입니다."),
	MONO_COMPLEXOLIGO_REQUIRED(500400025, "[Complex Oligo]은 필수 입력 항목입니다."),

	ID_CANNOT_BE_GENERATED(500400026, "더 이상 새로운 ID를 생성할 수 없습니다."),
	INVALID_ANLABB(500400027, "Abb. can be entered in 4 characters or less."),
	INVALID_ICTPCD(500400028, "잘못된 IC TYPE 입니다."),
	INVALID_ID_PREFIX(500400029, "잘못된 ID PREFIX 입니다."),

	INVALID_STATUSCD(500400030, "최초 생성시에만 [statusCd='C'] 가능합니다. [statusCd='C'] 를 변경해주세요."),
	INVALID_CLSTID(500400031, "존재하지 않는 Cluster ID 입니다. Cluster 생성 및 확인 후 진행해주세요."),
	INVALID_DESIGNTP(500400032, "잘못된 디자인타입 입니다."),
	INVALID_CLSTABB(500400033, "Abb. can be entered in 4 characters or less."),

	// 500400100 ~
	// PMOD - Cluster 필수 입력
	CLSTNM_REQUIRED(500400100, "Cluster Gene Name 은 필수 입력 항목입니다."),
	CLSTABB_REQUIRED(500400101, "Cluster Gene Abbreviation은 필수 입력 항목입니다."),
	CLUST_ACCID_REQUIRED(500400102, "Cluster AccID 필수 입력 항목입니다."),
	CLUST_TPLASEQ_REQUIRED(500400103, "Cluster Template Sequence 필수 입력 항목입니다."),
	CLUST_SEQCLLTPCD_REQUIRED(500400104, "Cluster Seq Collection Type 필수 입력 항목입니다."),
	CLUST_INCLUSIVITY_REQUIRED(500400105, "Cluster Taxon Inclusivity 필수 입력 항목입니다."),

	CLUST_PRMR_SETTING_REQUIRED(500400106, "[Primer Setting] 필수 입력 항목입니다."),
	CLUST_PRMRTP_REQUIRED(500400107, "[Primer Type] 필수 입력 항목입니다."),
	CLUST_DPO_REQUIRED(500400108, "[DPO Structure] 필수 입력 항목입니다."),
	PRMR_APCR_PERCENT_REQUIRED(500400109, "Primer Parameter [APCR(%)]은 필수 입력 항목입니다."),
	PRMR_ECOV_PERCENT_REQUIRED(500400110, "Primer Parameter [E.Cov(%)]은 필수 입력 항목입니다."),
	PRMR_PATIAL_MATCH_PERCENT_REQUIRED(500400111, "Primer Parameter [Partial Match(%)]은 필수 입력 항목입니다."),
	PRMR_DEG_REQUIRED(500400112, "Primer Parameter [DEG#]은 필수 입력 항목입니다."),
	PRMR_AOCO_OLIGO_ADDVAL_PERCENT_REQUIRED(500400113, "Primer Parameter [AOCO oligo Addval(%)]은 필수 입력 항목입니다."),

	PKG_EXE_RUN_ERROR(500400999, "모듈 구동 이력이 없습니다. 'TOPK_PKG_RUN' 테이블 'OUTP_DT' 값이 비어 있습니다.")
	;

	private final int code;
	private final String message;
}
