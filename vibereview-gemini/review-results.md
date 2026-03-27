# 📝 블랙잭 프로젝트 코드 품질 리뷰 결과

본 리뷰는 `vibereview-gemini/code-quality-standards.md`에 정의된 가이드라인을 바탕으로 `src` 디렉토리 내의 코드를 분석한 결과입니다.

---

## 1. 객체 지향 및 설계 (Object-Oriented Design)

### 1.1 중복된 코드와 상속/합성 활용 미흡
*   **문제되는 코드:** `Dealer.java`와 `UserPlayer.java`
    ```java
    // Dealer.java
    public class Dealer implements Participant {
        private final Name name;
        private final Hand hand = new Hand();
        // ... 중복된 필드 및 메서드
    }

    // UserPlayer.java
    public class UserPlayer implements Participant {
        private final Name name;
        private final Hand hand = new Hand();
        // ... 중복된 필드 및 메서드
    }
    ```
*   **문제점:** `Dealer`와 `UserPlayer`가 공통적으로 `Name`과 `Hand`를 가지고 있으며, `receiveCard`, `getCards` 등의 메서드 구현이 거의 동일합니다.
*   **판단 근거:** (가이드라인 3.1) 상속보다는 합성을 우선하되, 공통된 행동과 상태를 가진 객체들 간의 중복은 제거해야 합니다. 현재는 코드 중복으로 인해 유지보수성이 떨어집니다.
*   **개선된 코드:** 추상 클래스 `AbstractParticipant`를 도입하거나, `Participant` 인터페이스를 강화하고 공통 로직을 위임받는 구조로 변경합니다.
    ```java
    public abstract class AbstractParticipant implements Participant {
        protected final Name name;
        protected final Hand hand = new Hand();

        protected AbstractParticipant(Name name) {
            this.name = name;
        }

        @Override
        public void receiveCard(Card card) {
            hand.add(card);
        }
        // ... 공통 메서드 이동
    }
    ```

### 1.2 캡슐화 위반 (내부 상태 노출)
*   **문제되는 코드:** `Dealer.java`, `UserPlayer.java`, `StandardDeck.java`
    ```java
    // Dealer.java / UserPlayer.java
    public Hand getHand() {
        return hand;
    }

    // StandardDeck.java
    public List<Card> getCards() {
        return cards;
    }
    ```
*   **문제점:** 내부에서 사용하는 가변 객체(`Hand`, `List<Card>`)를 그대로 반환하고 있습니다. 외부에서 이 객체들을 조작할 위험이 있습니다.
*   **판단 근거:** (가이드라인 1.2, 3.2) 내부 구현의 변경이 외부에 파급되지 않도록 격리해야 하며, 가변 객체 반환 시 방어적 복사를 수행하거나 불변 뷰를 반환해야 합니다.
*   **개선된 코드:**
    ```java
    // Hand 내부의 카드를 반환할 때는 불변 리스트로 반환
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
    // Hand 자체를 반환하기보다 필요한 정보(점수 등)를 메서드로 제공
    ```

---

## 2. Java 언어 특성 및 안정성 (Effective Java)

### 2.1 매직 넘버 사용
*   **문제되는 코드:** `Hand.java`, `BlackjackGame.java`, `Participants.java`
    ```java
    // Hand.java
    while (sum > 21 && count > 0) {
        sum -= 10;
        count--;
    }

    // BlackjackGame.java
    participants.giveCardToDealer(deck.draw());
    participants.giveCardToDealer(deck.draw()); // 2번 반복
    ```
*   **문제점:** 21(블랙잭 점수), 10(Ace 보정값), 2(초기 배분 카드 수) 등이 숫자로 직접 사용되고 있습니다.
*   **판단 근거:** (체크리스트) 매직 넘버를 상수로 관리하거나 별도 객체로 추상화해야 합니다.
*   **개선된 코드:**
    ```java
    private static final int BLACKJACK_SCORE = 21;
    private static final int ACE_SCORE_OFFSET = 10;
    private static final int INITIAL_CARD_COUNT = 2;
    ```

### 2.2 생성자 매개변수 유효성 검사 누락
*   **문제되는 코드:** `Card.java`
    ```java
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }
    ```
*   **문제점:** `rank`나 `suit`가 `null`인 경우에 대한 방어 로직이 없습니다.
*   **판단 근거:** (가이드라인 3.2) 메서드 몸체가 실행되기 전에 인수를 검사해야 합니다.
*   **개선된 코드:**
    ```java
    public Card(Rank rank, Suit suit) {
        this.rank = Objects.requireNonNull(rank, "Rank는 null일 수 없습니다.");
        this.suit = Objects.requireNonNull(suit, "Suit는 null일 수 없습니다.");
    }
    ```

---

## 3. 가독성 및 의사소통 (Readability & Communication)

### 3.1 죽은 코드 (Dead Code) 존재
*   **문제되는 코드:** `ParticipantFactory.java`
    ```java
    private static String trim(String userInputNames) {
        return userInputNames.trim();
    }

    private static List<Name> getNames(String userInputNames) {
        // ... 사용되지 않음
    }
    ```
*   **문제점:** `create` 메서드에서 스트림을 사용하여 로직을 구현하면서, 기존에 작성된 private 메서드들이 사용되지 않은 채 남아있습니다.
*   **판단 근거:** (Clean Code) 사용하지 않는 코드는 삭제하여 가독성을 높여야 합니다.
*   **개선된 코드:** 사용하지 않는 메서드 제거.

---

## 4. 책임 중심 설계 (Responsibility-Driven Design)

### 4.1 정보 전문가 패턴 활용 미흡
*   **문제되는 코드:** `WinningResult.java` 및 `GameResult.java`
    ```java
    // GameResult.java 내의 reverse 로직
    private WinningResult reverse(WinningResult playerResult) {
        if (playerResult == WinningResult.WIN) return WinningResult.LOSE;
        // ...
    }
    ```
*   **문제점:** 승패를 뒤집는 로직은 `WinningResult` 상태와 밀접한 관련이 있음에도 `GameResult`에서 처리하고 있습니다.
*   **판단 근거:** (가이드라인 1.1) 책임을 수행하는 데 필요한 정보를 가장 많이 알고 있는 객체에게 책임을 할당해야 합니다.
*   **개선된 코드:**
    ```java
    public enum WinningResult {
        WIN, DRAW, LOSE;

        public WinningResult reverse() {
            if (this == WIN) return LOSE;
            if (this == LOSE) return WIN;
            return DRAW;
        }
    }
    ```

---

## 5. 테스트 코드 품질 (Unit Testing)

### 5.1 셔플 로직으로 인한 테스트 어려움
*   **문제되는 코드:** `StandardDeck.java`
    ```java
    public StandardDeck() {
        initializeCards();
        Collections.shuffle(cards); // 생성 시 강제 셔플
    }
    ```
*   **문제점:** 덱 생성 시 항상 랜덤하게 섞이므로, 특정 카드가 나오는 상황을 테스트하기 어렵습니다.
*   **판단 근거:** (가이드라인 5, 체크리스트) 테스트 가능하도록 별도의 전략(Shuffle Strategy)을 주입받는 구조가 권장됩니다.
*   **개선된 코드:**
    ```java
    public StandardDeck(ShuffleStrategy shuffleStrategy) {
        initializeCards();
        shuffleStrategy.shuffle(cards);
    }
    ```
