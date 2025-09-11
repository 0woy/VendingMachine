# 자판기 프로그램 (Vending Machine)
---

## 👀 프로젝트 개요

* **프로젝트명**: VendingMachine
* **목표**: Java 콘솔 기반 자판기 프로그램 구현 (투입 금액과 상품 재고를 관리하며 구매/거스름돈 반환/관리자 기능 제공)
* **핵심 기능 요약**

  * 동전/지폐 투입 및 잔액 표시
  * 상품 목록, 재고 및 가격 관리
  * 상품 구매 및 거스름돈 반환
  * 관리자 모드(상품 보충, 가격 변경, 매출 조회)
  * 예외 처리 (잔액 부족, 재고 없음, 반환 불가 등)

---

## 🧰 기술 스택

* **Language**: Java 11+
* **Build**: Gradle (기본 제공 없음, IDE 실행 가능)
* **IDE**: IntelliJ 

---

## 📦 주요 클래스 구조

실제 코드(`src` 디렉토리) 기준:

* **Main.java**: 프로그램 실행 진입점
* **Machine.java**: 자판기의 핵심 로직 (금액, 상품 관리)
* **Product.java**: 상품 정보 클래스 (이름, 가격, 수량)
* **Menu.java**: 사용자 입력 메뉴 처리
* **Manager.java**: 관리자 기능 처리
* **Input.java**: 사용자 입력 유틸리티

---

## 🧠 핵심 로직

### 1) 거스름돈 반환 알고리즘

* 큰 단위의 화폐부터 차례대로 반환
* 보유 수량보다 많으면 작은 단위로 보충
* 반환 불가 시 에러 메시지 출력

### 2) 상품 구매 로직

* 상품 선택 시 **재고 확인 → 잔액 확인 → 결제 처리 → 상품 배출 → 잔액 차감 및 거스름돈 계산**

### 3) 관리자 모드

* 비밀번호 인증 후 실행 가능
* 기능: 상품 재고 보충, 상품 가격 수정, 매출 조회

---

## 📚 유스케이스

| ID    | 이름        | 액터  | 기본 시나리오                    |
| ----- | --------- | --- | -------------------------- |
| UC-01 | 동전/지폐 투입  | 사용자 | 금액 투입 → 잔액 표시              |
| UC-02 | 상품 구매     | 사용자 | 상품 선택 → 잔액/재고 확인 → 결제 및 배출 |
| UC-03 | 거스름돈 반환   | 사용자 | 반환 요청 → 알고리즘 수행 → 반환       |
| UC-04 | 관리자 모드 실행 | 관리자 | 비밀번호 입력 → 관리자 메뉴 표시        |
| UC-05 | 재고 보충     | 관리자 | 상품 ID 입력 → 수량 증가           |
| UC-06 | 가격 변경     | 관리자 | 상품 ID 및 가격 입력 → 수정         |

---

## 🗂️ 실행 방법

### IDE 실행

1. 이 저장소를 클론합니다.

   ```bash
   git clone https://github.com/0woy/VendingMachine.git
   ```
2. `src/Main.java` 실행

### 콘솔 실행 (직접 컴파일)

```bash
cd src
javac Main.java
java Main
```

---

## 🧪 테스트 시나리오

* **정상 구매**: 잔액 ≥ 가격, 재고 > 0 → 구매 성공
* **잔액 부족**: 구매 실패 및 안내 메시지
* **재고 부족**: 구매 실패 및 안내 메시지
* **거스름돈 반환**: 입력 잔액에서 올바른 동전 조합 반환
* **관리자 모드**: 올바른 비밀번호 입력 시 실행, 잘못 입력 시 실패

---

## 🔒 예외 및 오류 처리

* **잔액 부족**: "잔액이 부족합니다. 돈을 더 넣어주세요." 메시지 출력
* **재고 없음**: "해당 상품은 품절입니다." 출력
* **거스름돈 부족**: "거스름돈을 반환할 수 없습니다." 안내
* **잘못된 입력**: 숫자/메뉴 이외 입력 시 재입력 요청

---

## 📈 로깅/통계

* 관리자 모드에서 누적 매출 확인 가능
* 상품별 판매 현황 출력 가능

---

## 📄 라이선스

* 개인 학습/실습용 (라이선스 지정 없음)

---

## 📎 스크린샷 (실행 예시)

<table>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/af359e35-f900-401f-9273-d9af8922d5d4" width="320" /><br/>
  <em>1) 자판기 화면</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/d88da849-437e-4a4a-beb2-9f7cf42233ff" width="320" /><br/>
  <em>2) 금액 투입 및 구매 이미지 활성화</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/9c0c9b31-6990-4073-adf8-667a26b80e89" width="320" /><br/>
  <em>3) 음료 투출 화면</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/92fd9535-8c45-4c47-ae65-c822d7740647" width="320" /><br/>
  <em>4) 잔돈 반환</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/d115c72c-ac91-458f-b944-8cb261810b81" width="320" /><br/>
  <em>5) 관리자 화면</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/9d877118-b494-4f67-ad79-cd59a5d4da8c" width="320" /><br/>
  <em>5-1) 매출 조회</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/ecdb054a-f97f-4260-81f5-981e2208e45d" width="320" /><br/>
  <em>5-2) 재고 관련 (1)</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/fc0dd7b9-8675-42b8-85bb-409502272a37" width="320" /><br/>
  <em>5-2) 재고 관련 (2)</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/19d33c16-43b1-46f3-a3fd-387ef4178baa" width="320" /><br/>
  <em>5-2) 재고 관련 (3)</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/5564a337-3c38-4c68-b0ae-c632f7d4a9f2" width="320" /><br/>
  <em>5-3) 잔돈 관련 (1)</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/5f9c14ee-d583-44aa-bb36-0aef970d5b8c" width="320" /><br/>
  <em>5-3) 잔돈 관련 (2)</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/4b44ca9f-6c79-4c2f-bb5b-c05626122574" width="320" /><br/>
  <em>5-3) 잔돈 관련 (3)</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/44554d71-3900-4122-aa08-116f73d1f9ea" width="320" /><br/>
  <em>5-4) 관리자 계정 관련 (1)</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/120f4979-049b-4079-b1a9-a779e7a16925" width="320" /><br/>
  <em>5-4) 관리자 계정 관련 (2)</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/930f2c9c-bda0-4f95-b6ec-5c7726dd7992" width="320" /><br/>
  <em>6) 예외 처리 (1)</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/357dc40f-4b4e-4234-b1a8-b9e65fba450f" width="320" /><br/>
  <em>6) 예외 처리 (2)</em>
</td></tr>
<tr><td align="center">
  <img src="https://github.com/user-attachments/assets/8bac9f6f-c419-477e-8784-09a962308aa4" width="320" /><br/>
  <em>6) 예외 처리 (3)</em>
</td></tr>
</table>

### 비밀번호 관련 팝업 (소형)

<table>
<tr>
<td align="center"><img src="https://github.com/user-attachments/assets/511d57bf-7a3d-45c2-af89-ebb895e22f39" width="240" /><br/><em>현재 비밀번호 오류</em></td>
<td align="center"><img src="https://github.com/user-attachments/assets/56802767-d85a-4746-b90d-92bf9705e473" width="240" /><br/><em>조건에 맞지 않은 비밀번호</em></td>
</tr>
<tr>
<td align="center"><img src="https://github.com/user-attachments/assets/6cb3810a-0a96-4de3-8786-fb17acdfd456" width="240" /><br/><em>비밀번호 재입력 오류</em></td>
<td align="center"><img src="https://github.com/user-attachments/assets/b6fd82a8-0833-471b-b3ff-5fdd6fcb3709" width="240" /><br/><em>비밀번호 재설정 성공</em></td>
</tr>
</table>
