# 🤖 Gemini 1.5 Pro - Blackjack Domain Code Review (Detailed)

본 리뷰는 `vibereview-gemini/code-quality-standards.md` 기준에 따라 수행되었으며, 이해를 돕기 위해 **문제 코드**와 **개선 방향**을 구체적으로 제시합니다.

---

## 📂 1. `blackjack/domain/card`

### 🚀 개선 제안 1: 캡슐화 위반 해결
*   **현상:** `StandardDeck`이 내부 리스트를 그대로 노출하여 외부에서 카드 구성을 수정할 수 있는 위험이 있습니다.
*   **문제 코드 (`StandardDeck.java`):**
    ```java
    public List<Card> getCards() {
        return cards; // 가변 리스트를 직접 반환
    }
    ```
*   **개선안:** 방어적 복사본이나 읽기 전용 뷰를 반환합니다.
    ```java
    public List<Card> getCards() {
        return Collections.unmodifiableList(new ArrayList<>(cards));
    }
    ```

### 🚀 개선 제안 2: 테스트 가능한 구조로 변경
*   **현상:** 생성자에서 `Collections.shuffle()`을 직접 호출하여 테스트 시 카드가 나오는 순서를 제어할 수 없습니다.
*   **문제 코드 (`StandardDeck.java`):**
    ```java
    public StandardDeck() {
        initializeCards();
        Collections.shuffle(cards); // 무작위성이 내부에 고정됨
    }
    ```
*   **개선안:** 셔플 전략을 주입받거나, 팩토리 메서드에서 셔플된 리스트를 넘겨받도록 변경합니다.
    ```java
    public StandardDeck(ShuffleStrategy strategy) {
        initializeCards();
        strategy.shuffle(this.cards);
    }
    ```

### 🚀 개선 제안 3: 정적 팩토리 메서드 및 캐싱 적용
*   **현상:** `NumberRank`는 2~10 사이의 고정된 값만 가지지만 매번 새로운 객체를 생성합니다.
*   **개선안 (`NumberRank.java`):**
    ```java
    private static final Map<Integer, NumberRank> CACHE = IntStream.rangeClosed(2, 10)
            .boxed()
            .collect(Collectors.toMap(i -> i, NumberRank::new));

    public static NumberRank from(int number) {
        validateNumberRange(number);
        return CACHE.get(number);
    }
    ```

---

## 📂 2. `blackjack/domain/player`

### 🚀 개선 제안 1: 중복 코드 제거 (상속/합성 활용)
*   **현상:** `Dealer`와 `UserPlayer`가 동일한 `Hand` 관리 및 `receiveCard` 로직을 각각 구현하고 있습니다.
*   **문제 코드:** 두 클래스 모두 아래 코드를 중복 소유하고 있음.
    ```java
    private final Hand hand = new Hand();
    public void receiveCard(Card card) { hand.add(card); }
    ```
*   **개선안:** 공통 속성과 행위를 추상 클래스로 추출합니다.
    ```java
    public abstract class AbstractParticipant implements Participant {
        protected final Name name;
        protected final Hand hand = new Hand();
        // 공통 로직 구현...
    }
    ```

### 🚀 개선 제안 2: "Tell, Don't Ask" 강화
*   **현상:** `BlackjackGame`이 `Participants`로부터 리스트를 꺼내와 직접 루프를 돌며 카드를 나눠주고 있습니다.
*   **문제 코드 (`BlackjackGame.java`):**
    ```java
    public void distributeTwoCards() {
        participants.giveCardToDealer(deck.draw());
        for (UserPlayer player : participants.getPlayers()) { // 내부를 꺼내서 직접 조작
            player.receiveCard(deck.draw());
        }
    }
    ```
*   **개선안:** `Participants`에게 분배 책임을 위임합니다.
    ```java
    // BlackjackGame.java
    public void distributeTwoCards() {
        participants.initInitialCards(deck);
    }

    // Participants.java
    public void initInitialCards(Deck deck) {
        dealer.receiveCard(deck.draw());
        players.forEach(player -> player.receiveCard(deck.draw()));
    }
    ```

---

## 📂 3. `blackjack/domain` (Top-level)

### 🚀 개선 제안 1: 매직 넘버 상수화
*   **현상:** 초기 분배 카드 수(2)가 하드코딩되어 있습니다.
*   **문제 코드 (`BlackjackGame.java`):**
    ```java
    participants.giveCardToDealer(deck.draw()); // 1번
    participants.giveCardToDealer(deck.draw()); // 2번 반복
    ```
*   **개선안:** 상수를 정의하고 반복문을 사용합니다.
    ```java
    private static final int INITIAL_CARD_COUNT = 2;

    for (int i = 0; i < INITIAL_CARD_COUNT; i++) {
        participants.giveCardToDealer(deck.draw());
    }
    ```

### 🚀 개선 제안 2: 비즈니스 의미를 담은 메서드 네이밍
*   **현상:** `WinningResult`의 점수 비교 로직이 단순 산술 연산으로 보입니다.
*   **문제 코드 (`WinningResult.java`):**
    ```java
    if (dealerScore > playerScore) {
        return LOSE;
    }
    ```
*   **개선안:** `Hand` 객체에 도메인 언어를 반영한 비교 메서드를 추가합니다.
    ```java
    if (playerHand.isWeakerThan(dealerHand)) {
        return LOSE;
    }
    ```

---

## 🏆 종합 평가
수정된 리뷰는 단순한 이론적 지적을 넘어, **실제 코드 상의 문제점(Before)**과 **구체적인 리팩토링 방향(After)**을 매칭하여 제시합니다. 이를 통해 개발자가 변경의 필요성을 즉각적으로 이해하고 코드에 반영할 수 있도록 돕습니다.
