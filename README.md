### 24/02/25 from Architect

- package name
  > 
- 자동 단위테스트 기반 구성
  > 가이드 추후 작성

### 기본 환경

- JDK 11
- Spring Boot
- Mybatis

### 프로젝트 실행

- 로컬 실행 (IntelliJ Community)
  > ### <Java version 변경>
  >
  > 1.프로젝트 우클릭 > 모듈 설정 > 프로젝트
  >
  > - SDK : corretto-11 Amazon Corretto version 11.0 (없을 경우 인텔리제이에서 설치 가능)
  >
  > - 11 - 람다 매개변수의 지역 변수 구문
  >
  > ### <인텔리제이 빌드 및 컴파일 설정>
  >
  > 1.인텔리제이 설정 > 빌드,실행,배포 > 빌드 도구 > Gradle 프로젝트
  >
  > - IntelliJ IDEA
  >
  > 2.빌드,실행,배포 > Java 컴파일러
  > - 11
  >
  > ### <Gradle 로드>
  >
  > 1.Gradle 로드로 라이브러리 설치
  >
  > ### <어플리케이션 설정>
  >
  > 1.어플리케이션 생성
  >
  > 2.빌드 및 실행 : java 11
  >
  > 3.VM 옵션 : -Dspring.profiles.active=local

- 로컬 실행 (IntelliJ Ulimated)
  > ### <Java version 변경>
  >
  > 1.프로젝트 우클릭 > 모듈 설정 > 프로젝트
  >
  > - SDK : corretto-11 Amazon Corretto version 11.0 (없을 경우 인텔리제이에서 설치 가능)
  >
  > - 11 - 람다 매개변수의 지역 변수 구문
  >
  > ### <인텔리제이 빌드 및 컴파일 설정>
  >
  > 1.인텔리제이 설정 > 빌드,실행,배포 > 빌드 도구 > Gradle 프로젝트
  >
  > - IntelliJ IDEA
  >
  > 2.빌드,실행,배포 > Java 컴파일러
  > - 11
  >
  > ### <Gradle 로드>
  >
  > 1.Gradle 로드로 라이브러리 설치
  >
  > ### <어플리케이션 설정>
  >
  > 1.Spring Boot 어플리케이션 생성
  >
  > 2.빌드 및 실행 : java 11
  >
  > 3.VM 옵션 : -Dspring.profiles.active=local

### Swagger Document

- Base Url : http://localhost:8080/airport-dutyfree/swagger-ui/index.html

### Git Commit 메시지 컨벤션

> - Feat : 새로운 기능을 추가하는 경우
> - Fix : 버그를 고친경우
> - Docs : 문서를 수정한 경우
> - Style : 코드 포맷 변경, 세미콜론 누락, 코드 수정이 없는경우
> - Refactor : 코드 리펙토링
> - Test : 테스트 코드. 리펙토링 테스트 코드를 추가했을 때
> - Chore : 빌드 업무 수정, 패키지 매니저 수정
> - Design : CSS 등 사용자가 UI 디자인을 변경했을 때
> - Rename : 파일명(or 폴더명) 을 수정한 경우
> - Remove : 코드(파일) 의 삭제가 있을 때. "Clean", "Eliminate" 를 사용하기도 함
> -  #### ex) Feat : 로그인 기능 추가

### Coding Check Style 설정 (Naver)

- 사전 준비 설정 파일 (배포)

> naver-checkstyle-rules.xml
>
> naver-intellij-formatter.xml
>
> naver-checkstyle-suppressions.xml

- 설정 방법

> 1.Check Style 플러그인 설치
>
> 2.인텔리제이 설정 > 도구(Tool) > Check style
>
> - Scan Scope : All sources (including tests)
> - Treat Checkstyle errors as warnings 체크
> - Configuration File > `+` 버튼 클릭
> - Description : Naver Checkstyle Rules
> - Use a local Checkstyle file : naver-checkstyle-rules.xml
> - suppressionFile > naver-checkstyle-suppressions.xml 입력
>
> 3.에디터(Editor) > 코드스타일(Code Style)
> - 구성표 > 구성표 가져오기 > Checkstyle configuration > naver-intellij-formatter.xml 파일 추가 > 구성표 변경
>
> 4.단축키로 Formatter 적용
>
> - 코드 서식 적용 : Opt + Command + L
> - import문 최적화 : Ctrl+ Opt + O

### 주석 템플릿 적용

#### Class 주석

> 1.인텔리제이 설정 > 에디터 > 파일 및 코드 템플릿
> - "포함" 탭 > File Header > 아래 설정값 입력

```java
/**
 * ${PACKAGE_NAME}.${NAME}
 * <p>
 * ${NAME}
 *
 * @author 김태욱
 * @version 1.0
 * @since ${YEAR}/${MONTH}/${DAY}
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *     수정일        수정자           수정내용
 *  ----------    --------        ---------------------------
 *  ${YEAR}/${MONTH}/${DAY}    김태욱            최초 생성
 * </pre>
 */
```

#### Method 주석

> 1.인텔리제이 설정 > 에디터 > 파일 및 코드 템플릿
>
> - "포함" 탭 > `+` 클릭 > Method Header 생성 (아무것도 입력할 필요 없음)
> - "코드" 탭 > JavaDoc Method > 아래 설정값 입력

```
#parse("Method Header.java")
#foreach($param in $PARAMS)
 *@param $param
#end
#if($RETURN_TYPE !="void")
 *@return
#end
```

> 2.사용법
> - 메소드 생성 > 메소드 상단에 /** 입력 후 엔터

# ======================== 개발 가이드 ========================

## 1. API Request Parameter Annotation 정리

- @Parameters, @parameter

```java

@GetMapping("/info/all")
@Parameters({
	@Parameter(name = "pageNumber", description = "페이지 번호", schema = @Schema(type = "int", defaultValue = "1"), in = ParameterIn.QUERY),
	@Parameter(name = "recordSize", description = "페이지당 행 개수", schema = @Schema(type = "int", defaultValue = "10"), in = ParameterIn.QUERY),
	@Parameter(name = "memberName", description = "검색조건: 최신순, 이순신", schema = @Schema(allowableValues = {"홍길동",
		"이순신"}, type = "string", nullable = false, defaultValue = "홍길동"), in = ParameterIn.QUERY)
})
public PagingResultVO getMemberAll(@Parameter(hidden = true) @Valid ReqAllMember memberInfo) {
}
```

> - `@Parameters`, `@Parameter`는 전달 받는 객체를 Swagger 상에서 JSON 형태가 아닌 각 필드 별로 보거나 입력할 수 있는 Annotation. API 호출 자체에 영향 없음.
>
> - `Query String`으로 전송 : ?pageNumber=1&recordSize=1&offset=0&memberName=HongGilDong
>
> - `@Parameters` 내 `@Parameter` : 전달 받는 객체 내 존재하는 필드의 이름과 동일하게 설정, 동일하지 않을 경우 별도의 파라미터로 인식
>
> - 메소드 매개변수 설정하는 `@Parameter` : hidden = true 옵션으로 Swagger 상에서는 숨김
>
> - `@Parameter` 사용 시 `@RequestParam` 사용 X

- @RequestParam

```java

@GetMapping("/info")
public ResMemberInfo getMember(@RequestParam @Valid ReqMemberInfo memberInfo) {
}

@GetMapping("/info")
public ResMemberInfo getMember(@RequestParam @Valid String memberName) {
}
```

> 첫 번째의 경우 객체 안에 존재하는 필드들이 `Query String`으로 전송. Nested 객체는 해당 어노테이션으로 받을 수 없음.
>
> - ?pageNumber=1&recordSize=1&offset=0&memberName=HongGilDong
>
> 두 번째의 경우 하나의 필드가 `Query String`으로 전송
>
> - ?memberName=HongGilDong

- @RequestBody

```java

@PostMapping("/add")
public void updateMember(@RequestBody @Valid ReqAddMember memberInfo) {
}
```

> - `@PostMapping` 사용 시 데이터 수신은 `@ReqeustBody`로 통일
>
> - `@ReqeustBody` 사용 시 `@Parameters` `@Parameter` 사용 X
>
> - `Nested` 객체에 적합
>
> - 객체가 `Body`에 담겨 JSON 형태로 전송
    > {"pageNumber": 1, "recordSize": 1, "offset": 0, "memberName": "string" }

- @PathVariable

```java

@GetMapping("/info/{memberName}")
public ResMemberInfo getMember(@PathVariable String memberName) {
}
```

> URI의 일부를 템플릿 변수로 매핑하여 컨트롤러 메서드에서 사용

## 2. Validate

- 사용자의 입력값을 Controller 에서 유효성 검사를 진행 (`@Validated`, `@Valid`)

```java

@Tag(name = "Member", description = "회원 관리")
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
@RestController
public class MemberController {

	@GetMapping("/info")
	@Operation(summary = "member", description = "회원 정보 조회")
	public ResMemberInfo getMember(@Parameter(hidden = false) @Valid ReqMemberInfo memberInfo) {
		ResMemberInfo member = memberService.getMemberInfo(memberInfo);

		return member;
	}

}
```

- Nested Valid 정의

> `Nested`로 Dto 가 구성되어 있는 경우 @Valid 속성추가

```java
public class ReqNotice {
	@Valid
	@Schema(description = "다국어공지", type = "ReqNoticeLanguage", nullable = true)
	private List<ReqNoticeLanguage> reqNoticeLanguageList;
}
```

- 선택적인 값만 입력받아야 하는 경우

```java

@Pattern(regexp = "^$|Y|N", message = "{display.yn}")
@Schema(description = "전시여부: ['']전체, [Y], [N]", allowableValues = {"Y",
	"N"}, type = "String", maxLength = 1, nullable = true)
private String expsrYn;
```

- 지정된 패턴만 입력받아야 하는 경우

```java

@Pattern(regexp = "^[[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+]{1,40}$", message = "{valid.email}")
@Schema(description = "이메일주소", type = "String", example = "ulsc-mz@gmail.com")
private String emlAddr;
```

- @Valid 에 지정되지 않은 사용자오류 처리

> ServiceImpl 에서 처리함.

```java
if(reqNoticeSearchFilter.getSearchCd() ==null){
String validMessage = CommonUtils.getValidMessage("sample.id.required");
    throw new

InputCheckException(validMessage);
}
```

- 주요 Validator

```java
// null 만 혀용.
@Null
// null 을 허용안함. "", " "는 허용.
@NotNull
// null, "" 을 허용안함. " "는 허용.
@NotEmpty
// null, "", " " 모두 허용안함.
@NotBlank

// 이메일 형식을 검사. ""의 경우는 통과.
@Email
// 정규식을 검사.
@Pattern(regexp =)
// 길이를 제한할 때 사용.
@Size(min =, max =)
// value 이하의 값을 받을 때 사용.
@Max(value =)
// value 이상의 값을 받을 때 사용.
@Min(value =)

// 값을 양수로 제한.
@Positive
// 값을 양수와 0만 가능하도록 제한.
@PositiveOrZero
// 값을 음수로 제한.
@Negative
// 값을 음수와 0만 가능하도록 제한.
@NegativeOrZero
// 현재보다 미래
@Future
// 현재보다 과거
@Past
// false 여부, null은 체크안함.
@AssertFalse
// true 여부, null은 체크안함.
@AssertTrue  
```

## 3. Exception 처리

- Exception 클래스 : kr/airport/dutyfree/exception
- 에러 코드 : kr/airport/dutyfree/exception/code

```java
if(name.equals("")){
	throw new

InvalidInputException(InputCheckErrorCode.INVALID_INPUT_NAME);
}
```

- Exception 클래스 생성 시 GlobalExceptionHandler 추가
- kr/airport/dutyfree/exception/GlobalExceptionHandler > 해당 Exception 핸들러 추가

```java
/*
 * ================================================================= 커스텀 Exception =================================================================
 */
@ExceptionHandler(InvalidInputException.class)
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public ErrorMessage handleException(final InvalidInputException e, WebRequest request) {
	log.error("[handleException] Error : ", e);

	final CustomErrorCode customErrorCode = e.getCustomErrorCode();
	return handleCustomException(customErrorCode, request);
}
```

## 4. Paging

- Request DTO에 페이징 관련 Parameter 필요
- `pageNumber`, `recordSize`, `offset`

```java
public class ReqAllMember {
	@Min(value = 1, message = "페이지번호")
	@Schema(description = "페이지번호", type = "int")
	private int pageNumber;

	@Min(value = 1, message = "페이지당 데이터 수")
	@Schema(description = "페이지당 데이터 수", type = "int")
	private int recordSize;

	@Schema(description = "페이징 offSet")
	private int offset;

	@Schema(description = "이름(성)", type = "String")
	private String memberName;
}
```

- Service

```java
public PagingResultVO getAllMemberList(ReqAllMember reqAllMember) {
	// request paging info set
	reqAllMember.setOffset((CommonUtils.getOffset(reqAllMember.getPageNumber(), reqAllMember.getRecordSize())));

	// 아래는 Request 값에 의해 조회가 된 결과값이라고 가정
	List<ResAllMemberList> resAllMemberList = memberMapper.getAllMember(reqAllMember);

	// response paging info set
	PaginationInfo paging = new PaginationInfo();
	paging.setNumber(reqAllMember.getPageNumber());
	paging.setRecordSize(reqAllMember.getRecordSize());
	paging.setTotalCount(memberMapper.getAllMemberCount());    // 조회 리스트의 Count

	return new PagingResultVO(paging, resAllMemberList);
}
```

- Mapper

```java

@Mapper
public interface MemberMapper {
	List<ResAllMemberList> getAllMember(ReqAllMember reqAllMember);
}
```

- Query
  > `Limit`, `offset`, `RecordSize` 추가

```xml

<select id="getAllMember" parameterType="kr.airport.dutyfree.member.model.ReqAllMember"
        resultType="kr.airport.dutyfree.member.model.response.ResAllMemberList">
    select
    m.memberName,
    m.memberAge
    from MEMBER m
    where m.MEMBER_NAME = #{memberName} limit #{recordSize}
    offset #{offset}
</select>
```

## 5. Feign Http Client (Rest Template 대체)

- kr/airport/dutyfree/common/service

```java

@FeignClient(name = "PayRequest", url = "${api.server.url}")
public interface PayRequest {
	@PostMapping(value = "${api.server.send-api}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	String post(@RequestHeader("Authorization") String apiKey, @RequestBody Map<String, Object> params);
}
```

```java
final PayRequest payRequest;

String response = payRequest.post(ApiKey, param);
```

## 6. 마스킹

모든 마스킹은 API 에서 구현하여 전달함.

```java
// 이름
String nameMasking = MaskingUtil.nameMasking(name);
// 전화번호
String phoneMasking = MaskingUtil.phoneMasking(phoneNumber);
// 이메일
String emailMasking = MaskingUtil.emailMasking(email);
// 계좌번호
String accountNoMasking = MaskingUtil.accountNoMasking(accountNo);
// 생년월일
String birthMasking = MaskingUtil.birthMasking(birthday);
// 카드번호
String cardMasking = MaskingUtil.cardMasking(cardNo);
// 주소
String addressMasking = MaskingUtil.addressMasking(address);
// 사업자등록번호
String businessNoMasking = MaskingUtil.businessNoMasking(businessNo);
```

## 7. 고유아이디 생성

보안을 위해 Sequence 로 채번하지 말아야 할 아이디 생성을 위한 function

```java
String fileId = CommonUtils.getUUID();
```

## 8. 민감정보 암복호화

- 회원의 민감 정보를 식별이 불가능하게 암호화

```java
String encryptMblTelno = CryptoUtil.encryptARIA(reqMemberJoinGeneral.getMblTelno());
```

- 회원의 민감 정보를 식별이 가능하게 Response DTO 복호화 적용

```java

@Schema(description = "휴대전화번호", type = "String")
private String mblTelno;

public void setMblTelno(String value) {
	this.mblTelno = CryptoUtil.decryptARIA(value);
}
```

- 복호화가 불가능한 암호화 (Dijest)

```java
String digestPassword = CryptoUtil.digest(reqMemberJoinGeneral.getLgnPswd());
```

## 9. DM 전송 (추가 예정)