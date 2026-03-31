# Blackjack 도메인 코드 리뷰

> 리뷰 기준: Clean Code (Robert C. Martin), Effective Java (Joshua Bloch), 오브젝트 (조영호)
> 리뷰 대상: `src/main/java/blackjack/domain/` 전체 소스 코드

---

## 목차

1. [R-01: UserPlayer와 Dealer의 중복 코드](#r-01-userplayer와-dealer의-중복-코드)
2. [R-02: Hand 객체를 외부에 노출하는 getter](#r-02-hand-객체를-외부에-노출하는-getter)
3. [R-03: GameResult가 Hand를 꺼내서 직접 비교하는 구조](#r-03-gameresult가-hand를-꺼내서-직접-비교하는-구조)
4. [R-04: BlackjackGame.drawDealerCard()의 중복 검증](#r-04-blackjackgamedrawdealercard의-중복-검증)
5. [R-05: Participants가 Dealer의 행동을 대행하는 구조](#r-05-participants가-dealer의-행동을-대행하는-구조)
6. [R-06: WinningResult.reverse()가 GameResult에 고립](#r-06-winningresultreverse가-gameresult에-고립)
7. [R-07: StandardDeck.getCards()가 내부 가변 리스트를 그대로 노출](#r-07-standarddeckgetcards가-내부-가변-리스트를-그대로-노출)
8. [R-08: ParticipantFactory의 미사용 메서드](#r-08-participantfactory의-미사용-메서드)
9. [R-09: 매직 넘버 사용](#r-09-매직-넘버-사용)
10. [R-10: Dealer와 UserPlayer의 getName() 반환 타입 불일치](#r-10-dealer와-userplayer의-getname-반환-타입-불일치)
11. [R-11: distributeTwoCards()에서 Participants의 캡슐화 위반](#r-11-distributetwocards에서-participants의-캡슐화-위반)
12. [R-12: Participants의 validateNoDuplicateNames()에서 디미터 법칙 위반](#r-12-participants의-validatenoduplicatenames에서-디미터-법칙-위반)
13. [R-13: Card 클래스에 정적 팩토리 메서드 부재](#r-13-card-클래스에-정적-팩토리-메서드-부재)
14. [R-14: NumberRank의 동일 값 객체 반복 생성](#r-14-numberrank의-동일-값-객체-반복-생성)

---

## R-01: UserPlayer와 Dealer의 중복 코드

### 문제 코드

**UserPlayer.java**

```java
public class UserPlayer implements Participant {
    private final Name name;
    private final Hand hand = new Hand();

    public int getCurrentCardSize() {
        return hand.getSize();
    }

    public List<Card> getCards() {
        return hand.getCards();
    }

    public Name getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }
}
```

**Dealer.java**

```java
public class Dealer implements Participant {
    private final Name name;
    private final Hand hand = new Hand();

    public int getCurrentCardSize() {
        return hand.getSize();
    }

    public List<Card> getCards() {
        return hand.getCards();
    }

    public String getName() {
        return name.name();
    }

    public Hand getHand() {
        return hand;
    }
}
```

### 문제점

`UserPlayer`와 `Dealer`는 `name`, `hand` 필드와 `getCurrentCardSize()`, `getCards()`, `getHand()` 메서드가 완전히 동일합니다.
`receiveCard()`와 `isDrawable()`만 다릅니다.

### 근거

- **오브젝트 10장 (상속과 코드 재사용)**: "두 메서드가 유사하게 보인다면 차이점을 메서드로 추출하고, 공통 부분을 부모 클래스로 올린다."
- **Clean Code 10장 (창발성)**: "단 몇 줄이라도 중복을 제거한다. 소규모 재사용은 시스템 복잡도를 극적으로 줄여준다."

### 개선안

공통 로직을 추상 클래스로 추출합니다. `receiveCard()`와 `isDrawable()`만 각 구현체에서 정의합니다.

```java
public abstract class AbstractParticipant implements Participant {
    private final Name name;
    private final Hand hand = new Hand();

    protected AbstractParticipant(Name name) {
        this.name = name;
    }

    protected Hand hand() {
        return hand;
    }

    public int getCurrentCardSize() {
        return hand.getSize();
    }

    public List<Card> getCards() {
        return hand.getCards();
    }

    public Name getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }
}

public class UserPlayer extends AbstractParticipant {
    public UserPlayer(Name name) {
        super(name);
        validateNotReservedWord(name);
    }

    @Override
    public void receiveCard(Card card) {
        if (hand().isBusted()) {
            throw new IllegalStateException("Bust 상태에서는 카드를 더 받을 수 없습니다");
        }
        hand().add(card);
    }

    @Override
    public boolean isDrawable() {
        return hand().calculateScore() < 21;
    }
}

public class Dealer extends AbstractParticipant {
    public Dealer(Name name) {
        super(name);
    }

    @Override
    public void receiveCard(Card card) {
        hand().add(card);
    }

    @Override
    public boolean isDrawable() {
        return hand().calculateScore() < 17;
    }
}
```

---

## R-02: Hand 객체를 외부에 노출하는 getter

### 문제 코드

**UserPlayer.java:49, Dealer.java:40**

```java
public Hand getHand() {
    return hand;
}
```

**GameResult.java:22-27** (사용하는 곳)

```java
Dealer dealer = participants.getDealer();
Hand dealerHand = dealer.getHand();

for (UserPlayer player : participants.getPlayers()) {
    Hand playerHand = player.getHand();
    WinningResult winningResult = WinningResult.of(dealerHand, playerHand);
    results.put(player, winningResult);
}
```

### 문제점

`getHand()`를 통해 내부 가변 객체인 `Hand`를 그대로 반환합니다. `Hand`는 `add()` 메서드를 가진 가변 객체이므로, 외부에서 `player.getHand().add(card)`를 호출하면
Player의 캡슐화가 깨집니다.

### 근거

- **Effective Java 아이템 50**: "가변 내부 객체를 반환할 때도 방어적 복사본을 반환한다."
- **오브젝트 4장**: "getter/setter를 통해 내부 상태를 그대로 노출하면 캡슐화가 아니다."
- **오브젝트 6장 (묻지 말고 시켜라)**: "객체의 상태를 묻는 오퍼레이션을 호출한 후 반환된 상태를 기반으로 결정을 내리지 않는다."

### 개선안

`Hand`를 외부에 노출하지 않고, 참가자에게 직접 질문합니다.

```java
// Participant 인터페이스에 메서드 추가
public interface Participant {
    void receiveCard(Card card);
    boolean isDrawable();
    int calculateScore();
    boolean isBusted();
}

// GameResult에서 Hand 대신 Participant에게 직접 질문
WinningResult winningResult = WinningResult.of(dealer, player);

// WinningResult도 Hand가 아닌 Participant를 받도록 변경
public static WinningResult of(Participant dealer, Participant player) {
    if (player.isBusted()) {
        return LOSE;
    }
    if (dealer.isBusted()) {
        return WIN;
    }
    if (dealer.calculateScore() > player.calculateScore()) {
        return LOSE;
    }
    if (dealer.calculateScore() == player.calculateScore()) {
        return DRAW;
    }
    return WIN;
}
```

이렇게 하면 `getHand()` 메서드를 완전히 제거할 수 있습니다.

---

## R-03: GameResult가 Hand를 꺼내서 직접 비교하는 구조

### 문제 코드

**GameResult.java:19-33**

```java
private Map<UserPlayer, WinningResult> calculateResults(Participants participants) {
    Map<UserPlayer, WinningResult> results = new HashMap<>();

    Dealer dealer = participants.getDealer();
    Hand dealerHand = dealer.getHand();

    for (UserPlayer player : participants.getPlayers()) {
        Hand playerHand = player.getHand();
        WinningResult winningResult = WinningResult.of(dealerHand, playerHand);
        results.put(player, winningResult);
    }
    return results;
}
```

### 문제점

`GameResult`가 `Participants` → `Dealer` → `Hand` 순서로 내부 구조를 탐색합니다. 이는 전형적인 **기차 충돌(Train Wreck)** 패턴이며, `GameResult`가
`Participants`의 내부 구조에 깊이 의존하게 됩니다.

### 근거

- **Clean Code 6장 (디미터 법칙)**: "모듈은 자신이 조작하는 객체의 내부를 몰라야 한다."
- **오브젝트 6장 (디미터 법칙)**: "객체의 내부 구조에 강하게 결합되지 않도록 협력 경로를 제한한다."
- **오브젝트 5장 (정보 전문가 패턴)**: "책임을 수행할 정보를 알고 있는 객체에게 책임을 할당한다."

### 개선안

게임 결과 계산 책임을 `Participants`에게 위임합니다. `Participants`가 Dealer와 Player를 모두 알고 있으므로 정보 전문가입니다.

```java
// Participants에 결과 계산 책임 위임
public class Participants {
    public Map<UserPlayer, WinningResult> calculateResults() {
        Map<UserPlayer, WinningResult> results = new HashMap<>();
        for (UserPlayer player : players) {
            results.put(player, WinningResult.of(dealer, player));
        }
        return results;
    }
}

// GameResult는 이미 계산된 결과만 받음
public class GameResult {
    private final Map<UserPlayer, WinningResult> playerResults;

    public GameResult(Map<UserPlayer, WinningResult> playerResults) {
        this.playerResults = playerResults;
    }
}

// BlackjackGame
public GameResult calculateGameResult() {
    return new GameResult(participants.calculateResults());
}
```

---

## R-04: BlackjackGame.drawDealerCard()의 중복 검증

### 문제 코드

**BlackjackGame.java:31-36**

```java
public void drawDealerCard() {
    if (!participants.isDealerDrawable()) {  // ← 1차 검증
        throw new IllegalStateException("딜러는 17점 이상이므로 카드를 더 이상 받을 수 없습니다");
    }
    participants.drawDealerCard(deck.draw()); // ← 내부에서 2차 검증
}
```

**Participants.java:68-73**

```java
public void drawDealerCard(Card card) {
    if (!dealer.isDrawable()) {              // ← 동일한 검증 반복
        throw new IllegalStateException("딜러는 17점 이상이므로 카드를 더 이상 받을 수 없습니다");
    }
    dealer.receiveCard(card);
}
```

### 문제점

동일한 검증 로직(`isDealerDrawable`)이 `BlackjackGame`과 `Participants`에서 중복됩니다. 동일한 예외 메시지도 두 곳에서 하드코딩되어 있습니다.

### 근거

- **Clean Code 10장 (창발성 - DRY)**: "중복은 소프트웨어에서 모든 악의 근원이다."
- **Clean Code 2장 (함수)**: "함수는 한 가지만 해라."
- **오브젝트 6장 (묻지 말고 시켜라)**: "상태를 묻고 판단하는 대신, 객체에게 행동을 시켜라."

### 개선안

검증 책임을 하나의 계층에만 둡니다. `BlackjackGame`은 `Participants`에게 시키고, 검증은 `Participants` (또는 `Dealer`) 내부에서만 수행합니다.

```java
// BlackjackGame — 검증을 제거하고 위임만
public void drawDealerCard() {
    participants.drawDealerCard(deck.draw());
}

// Participants — 검증은 여기서만
public void drawDealerCard(Card card) {
    if (!dealer.isDrawable()) {
        throw new IllegalStateException("딜러는 17점 이상이므로 카드를 더 이상 받을 수 없습니다");
    }
    dealer.receiveCard(card);
}
```

동일하게 `drawUserPlayerCard()`도 `UserPlayer.receiveCard()` 내부에 이미 버스트 검증이 있으므로, `BlackjackGame`에서의 검증은 제거합니다.

```java
// BlackjackGame — 검증 제거
public void drawUserPlayerCard(UserPlayer userPlayer) {
    userPlayer.receiveCard(deck.draw());
}
```

---

## R-05: Participants가 Dealer의 행동을 대행하는 구조

### 문제 코드

**Participants.java:64-85**

```java
public boolean isDealerDrawable() {
    return dealer.isDrawable();
}

public void drawDealerCard(Card card) {
    if (!dealer.isDrawable()) {
        throw new IllegalStateException("딜러는 17점 이상이므로 카드를 더 이상 받을 수 없습니다");
    }
    dealer.receiveCard(card);
}

public void giveCardToDealer(Card drawCard) {
    dealer.receiveCard(drawCard);
}
```

### 문제점

`Participants`는 Dealer와 Player를 **묶는 역할**이지만, Dealer에 대한 위임 메서드가 3개(`isDealerDrawable`, `drawDealerCard`,
`giveCardToDealer`)나 있습니다. 이는 `Participants`가 Dealer의 프록시처럼 동작하는 것으로, `Participants`의 응집도를 낮춥니다.

또한 `giveCardToDealer()`와 `drawDealerCard()`는 둘 다 Dealer에게 카드를 주지만 검증 여부만 다릅니다. 이름만으로는 차이가 명확하지 않습니다.

### 근거

- **Clean Code 9장 (클래스 - SRP)**: "클래스나 모듈을 변경할 이유가 하나뿐이어야 한다."
- **Clean Code 2장 (의미 있는 이름)**: "한 개념에 한 단어를 사용하라."
- **오브젝트 4장**: "응집도가 낮으면 변경의 이유가 여러 가지가 된다."

### 개선안

`Participants`의 역할을 명확히 합니다. 초기 배분(`distributeTwoCards`)은 `Participants`가 담당하되, 이후 턴별 카드 드로우는 `BlackjackGame`이 `Dealer`
에게 직접 위임합니다.

```java
public class Participants {
    private final Dealer dealer;
    private final List<UserPlayer> players;

    // 초기 카드 배분 — Participants가 모든 참가자에게 배분하는 것은 자연스럽다
    public void distributeInitialCards(Deck deck) {
        dealer.receiveCard(deck.draw());
        dealer.receiveCard(deck.draw());
        for (UserPlayer player : players) {
            player.receiveCard(deck.draw());
            player.receiveCard(deck.draw());
        }
    }

    public Dealer getDealer() { return dealer; }
    public List<UserPlayer> getPlayers() { return Collections.unmodifiableList(players); }
}

// BlackjackGame에서 Dealer에게 직접 위임
public void drawDealerCard() {
    Dealer dealer = participants.getDealer();
    dealer.receiveCard(deck.draw());
}
```

---

## R-06: WinningResult.reverse()가 GameResult에 고립

### 문제 코드

**GameResult.java:53-61**

```java
private WinningResult reverse(WinningResult playerResult) {
    if (playerResult == WinningResult.WIN) {
        return WinningResult.LOSE;
    }
    if (playerResult == WinningResult.LOSE) {
        return WinningResult.WIN;
    }
    return WinningResult.DRAW;
}
```

### 문제점

`reverse()`는 `WinningResult`의 상태를 기반으로 `WinningResult`의 반대값을 결정합니다. 이 로직은 `WinningResult` 자신이 가장 잘 아는 정보입니다.
`GameResult`에 두면 `WinningResult`에 새로운 상수가 추가될 때 `GameResult`도 함께 수정해야 합니다.

### 근거

- **오브젝트 5장 (정보 전문가 패턴)**: "책임을 수행하는 데 필요한 정보를 가장 많이 알고 있는 객체에게 책임을 할당한다."
- **Effective Java 아이템 34**: "열거 타입에는 임의의 메서드나 필드를 추가할 수 있다."
- **Clean Code 6장 (객체와 자료 구조)**: "객체는 자료를 숨기고 함수를 공개한다."

### 개선안

`reverse()`를 `WinningResult` enum 안으로 이동합니다.

```java
public enum WinningResult {
    WIN {
        @Override
        public WinningResult reverse() { return LOSE; }
    },
    DRAW {
        @Override
        public WinningResult reverse() { return DRAW; }
    },
    LOSE {
        @Override
        public WinningResult reverse() { return WIN; }
    };

    public abstract WinningResult reverse();

    public static WinningResult of(Hand dealerHand, Hand playerHand) {
        // ... 기존 로직 유지
    }
}
```

```java
// GameResult에서 간결하게 사용
public Map<WinningResult, Integer> getDealerResults() {
    Map<WinningResult, Integer> dealerResults = new EnumMap<>(WinningResult.class);
    for (WinningResult result : WinningResult.values()) {
        dealerResults.put(result, 0);
    }

    for (WinningResult playerResult : playerResults.values()) {
        WinningResult dealerResult = playerResult.reverse();
        dealerResults.merge(dealerResult, 1, Integer::sum);
    }
    return dealerResults;
}
```

---

## R-07: StandardDeck.getCards()가 내부 가변 리스트를 그대로 노출

### 문제 코드

**StandardDeck.java:45-47**

```java
public List<Card> getCards() {
    return cards;
}
```

### 문제점

`cards`는 `draw()` 시 `removeFirst()`로 원소가 삭제되는 내부 가변 리스트입니다. 이를 외부에 그대로 반환하면 외부에서 `deck.getCards().clear()` 같은 호출로 덱을 파괴할
수 있습니다.

### 근거

- **Effective Java 아이템 50**: "적시에 방어적 복사본을 만들라. 가변 내부 객체를 반환할 때도 방어적 복사본을 반환한다."
- **Effective Java 아이템 17**: "자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다."
- **오브젝트 4장**: "내부 구현의 변경이 외부로 퍼져나가는 파급 효과를 최소화한다."

### 개선안

이 메서드가 테스트 용도라면, 방어적 복사를 적용하거나 테스트 전용 접근 방식을 고려합니다.

```java
// 방어적 복사
public List<Card> getCards() {
    return Collections.unmodifiableList(cards);
}
```

또는 이 메서드가 테스트에서만 사용된다면, package-private으로 접근 범위를 제한합니다.

```java
// package-private (이미 테스트 용 생성자도 package-private이므로 일관성)
List<Card> getCards() {
    return Collections.unmodifiableList(cards);
}
```

---

## R-08: ParticipantFactory의 미사용 메서드

### 문제 코드

**ParticipantFactory.java:30-42**

```java
private static String trim(String userInputNames) {
    return userInputNames.trim();
}

private static List<Name> getNames(String userInputNames) {
    String[] split = userInputNames.split(",");
    List<Name> nameList = new ArrayList<>();
    for (String eachName : split) {
        nameList.add(new Name(trim(eachName)));
    }
    return nameList;
}
```

### 문제점

`trim()`과 `getNames()` 메서드는 `create()` 메서드에서 사용되지 않습니다. 리팩토링 과정에서 스트림 방식으로 전환하면서 남은 잔재로 보입니다.

### 근거

- **Clean Code 3장 (주석 - 주석으로 처리한 코드)**: "소스 코드 관리 시스템이 기억한다. 그냥 삭제하라."
- **Clean Code 10장 (YAGNI 원칙과 클래스/메서드 수 최소화)**: "사용하지 않는 코드는 혼란을 야기한다."

### 개선안

미사용 메서드를 삭제합니다.

```java
public class ParticipantFactory {

    public static Participants create(String userInputNames) {
        validateUserInputNotNull(userInputNames);

        List<UserPlayer> userPlayers = Arrays.stream(userInputNames.split(","))
                .map(String::trim)
                .map(Name::new)
                .map(UserPlayer::new)
                .collect(Collectors.toList());

        Dealer dealer = new Dealer(new Name("딜러"));
        return new Participants(dealer, userPlayers);
    }

    private static void validateUserInputNotNull(String userInputNames) {
        if (userInputNames == null || userInputNames.isBlank()) {
            throw new IllegalArgumentException("참여자 이름들이 null 이거나 비어 있을 수 없습니다");
        }
    }
}
```

---

## R-09: 매직 넘버 사용

### 문제 코드

**UserPlayer.java:46**

```java
public boolean isDrawable() {
    return hand.calculateScore() < 21;
}
```

**Dealer.java:32**

```java
public boolean isDrawable() {
    return hand.calculateScore() < 17;
}
```

**Hand.java:32**

```java
while (sum > 21 && count > 0) {
    sum -= 10;
    count--;
}
```

**Hand.java:40-42**

```java
public boolean isBusted() {
    return calculateScore() > 21;
}
```

### 문제점

`21`, `17`, `10` 등 블랙잭 규칙에서 중요한 숫자들이 코드 곳곳에 리터럴로 흩어져 있습니다. 같은 `21`이라는 숫자가 `UserPlayer`, `Hand`, `WinningResult`에서 각각
사용되고 있어 의미 파악이 어렵고, 변경 시 누락 위험이 있습니다.

### 근거

- **Clean Code 1장 (의미 있는 이름)**: "검색하기 쉬운 이름을 사용하라."
- **Effective Java 아이템 34**: "int 상수 대신 열거 타입을 사용하라. 열거 타입에는 메서드와 필드를 추가할 수 있다."
- **오브젝트 9장 (변경 보호)**: "변화가 예상되는 불안정한 지점들을 식별하고 안정된 인터페이스를 형성한다."

### 개선안

블랙잭 규칙 관련 상수를 한 곳에서 관리합니다.

```java
// 블랙잭 규칙 상수
public class BlackjackRule {
    public static final int BLACKJACK_SCORE = 21;
    public static final int DEALER_STAY_THRESHOLD = 17;
    public static final int ACE_BONUS = 10;

    private BlackjackRule() {}
}

// 사용
public boolean isDrawable() {
    return hand.calculateScore() < BlackjackRule.BLACKJACK_SCORE;
}

public boolean isBusted() {
    return calculateScore() > BlackjackRule.BLACKJACK_SCORE;
}
```

---

## R-10: Dealer와 UserPlayer의 getName() 반환 타입 불일치

### 문제 코드

**UserPlayer.java:40-42**

```java
public Name getName() {
    return name;  // Name 객체 반환
}
```

**Dealer.java:27-29**

```java
public String getName() {
    return name.name();  // String 반환
}
```

### 문제점

같은 개념(`name`)에 대해 `UserPlayer`는 `Name` 객체를, `Dealer`는 `String`을 반환합니다. 동일한 `Participant` 구현체인데 반환 타입이 다르면 다형적으로 사용하기
어렵고, 호출하는 쪽에서 타입에 따라 다르게 처리해야 합니다.

### 근거

- **Clean Code 1장 (의미 있는 이름)**: "한 개념에 한 단어를 사용하라."
- **Effective Java 아이템 51**: "메서드 시그니처를 신중히 설계하라."
- **오브젝트 13장 (리스코프 치환 원칙)**: "서브타입은 기반 타입에 대해 대체 가능해야 한다."

### 개선안

반환 타입을 통일합니다.

```java
// Participant 인터페이스에 getName() 추가
public interface Participant {
    void receiveCard(Card card);
    boolean isDrawable();
    Name getName();
}

// Dealer도 Name 반환
public class Dealer implements Participant {
    @Override
    public Name getName() {
        return name;
    }
}
```

---

## R-11: distributeTwoCards()에서 Participants의 캡슐화 위반

### 문제 코드

**BlackjackGame.java:17-25**

```java
public void distributeTwoCards() {
    participants.giveCardToDealer(deck.draw());
    participants.giveCardToDealer(deck.draw());

    for (UserPlayer player : participants.getPlayers()) {
        player.receiveCard(deck.draw());
        player.receiveCard(deck.draw());
    }
}
```

### 문제점

`BlackjackGame`이 `Participants`의 내부 구조(딜러 + 플레이어 목록)를 알고 있고, 직접 순회하면서 카드를 배분합니다. 이는 `Participants`의 캡슐화를 깨뜨립니다. 만약 참가자
구조가 변경되면 `BlackjackGame`도 함께 수정해야 합니다.

### 근거

- **오브젝트 6장 (묻지 말고 시켜라)**: "객체에게 시키고, 객체 스스로 판단하게 한다."
- **오브젝트 4장 (캡슐화)**: "내부 구현의 변경이 외부로 퍼져나가는 파급 효과를 최소화한다."
- **Clean Code 6장 (디미터 법칙)**: "모듈은 자신이 조작하는 객체의 내부를 몰라야 한다."

### 개선안

카드 배분 로직을 `Participants`에 위임합니다.

```java
// Participants
public void distributeInitialCards(Deck deck) {
    for (int i = 0; i < INITIAL_DISTRIBUTION_COUNT; i++) {
        dealer.receiveCard(deck.draw());
    }
    for (UserPlayer player : players) {
        for (int i = 0; i < INITIAL_DISTRIBUTION_COUNT; i++) {
            player.receiveCard(deck.draw());
        }
    }
}

// BlackjackGame
public void distributeTwoCards() {
    participants.distributeInitialCards(deck);
}
```

---

## R-12: Participants의 validateNoDuplicateNames()에서 디미터 법칙 위반

### 문제 코드

**Participants.java:44-51**

```java
private void validateNoDuplicateNames(List<UserPlayer> players) {
    final Set<String> uniqueNames = players.stream()
            .map(player -> player.getName().name())  // ← 2단계 탐색
            .collect(Collectors.toSet());

    if (uniqueNames.size() != players.size()) {
        throw new IllegalArgumentException("참여자들의 이름은 중복될 수 없습니다");
    }
}
```

### 문제점

`player.getName().name()`으로 2단계에 걸쳐 내부 값을 꺼냅니다. `Participants`가 `UserPlayer` → `Name` → `String` 구조를 알아야 합니다.

### 근거

- **오브젝트 6장 (디미터 법칙)**: "오직 인접한 이웃하고만 말하라."
- **Clean Code 6장**: "기차 충돌을 피하라."

### 개선안

`UserPlayer`에 이름 문자열을 반환하는 메서드를 추가하거나, `Name`이 `equals/hashCode`를 지원하므로 `Name` 레벨에서 비교합니다. (`Name`은 record이므로
`equals/hashCode`가 자동 생성됩니다.)

```java
private void validateNoDuplicateNames(List<UserPlayer> players) {
    final Set<Name> uniqueNames = players.stream()
            .map(UserPlayer::getName)
            .collect(Collectors.toSet());

    if (uniqueNames.size() != players.size()) {
        throw new IllegalArgumentException("참여자들의 이름은 중복될 수 없습니다");
    }
}
```

---

## R-13: Card 클래스에 정적 팩토리 메서드 부재

### 문제 코드

**StandardDeck.java:22-28** (Card 생성 부분)

```java
for (int i = 2; i <= 10; i++) {
    cards.add(new Card(new NumberRank(i), suit));
}

for (FaceRank face : FaceRank.values()) {
    cards.add(new Card(face, suit));
}
```

### 문제점

`Card`는 `Rank`와 `Suit`의 조합으로 만들어지는 값 객체이지만, `new Card(new NumberRank(i), suit)`처럼 클라이언트가 내부 구조(`NumberRank`라는 구체 클래스)를
알아야 합니다. 카드는 52장으로 고정되어 있으므로 캐싱도 가능합니다.

### 근거

- **Effective Java 아이템 1**: "생성자 대신 정적 팩토리 메서드를 고려하라. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다."
- **Effective Java 아이템 6**: "불필요한 객체 생성을 피하라. 불변 객체는 언제든 재사용할 수 있다."
- **오브젝트 8장**: "new는 해롭다. 구체 클래스에 대한 결합도가 높아진다."

### 개선안

`Card`에 정적 팩토리 메서드를 추가하고, 52장을 캐싱합니다.

```java
public class Card {
    private static final Map<String, Card> CACHE = new HashMap<>();

    static {
        for (Suit suit : Suit.values()) {
            for (int i = 2; i <= 10; i++) {
                Rank rank = new NumberRank(i);
                CACHE.put(toKey(rank, suit), new Card(rank, suit));
            }
            for (FaceRank face : FaceRank.values()) {
                CACHE.put(toKey(face, suit), new Card(face, suit));
            }
        }
    }

    private final Rank rank;
    private final Suit suit;

    private Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public static Card of(Rank rank, Suit suit) {
        return CACHE.get(toKey(rank, suit));
    }

    private static String toKey(Rank rank, Suit suit) {
        return rank.getDisplayName() + "_" + suit.name();
    }
}
```

---

## R-14: NumberRank의 동일 값 객체 반복 생성

### 문제 코드

**StandardDeck.java:22-24**

```java
for (int i = 2; i <= 10; i++) {
    cards.add(new Card(new NumberRank(i), suit));
}
```

### 문제점

`new NumberRank(5)`는 매번 새 객체를 생성하지만 논리적으로 동일한 값입니다. `NumberRank`는 `equals`와 `hashCode`를 올바르게 구현한 값 객체이므로, 동일한 값에 대해 같은
인스턴스를 재사용할 수 있습니다.

### 근거

- **Effective Java 아이템 6**: "똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다."
- **Effective Java 아이템 1**: "정적 팩토리 메서드는 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다."

### 개선안

`NumberRank`에 캐싱을 적용합니다.

```java
public class NumberRank implements Rank {
    private static final Map<Integer, NumberRank> CACHE = new HashMap<>();

    static {
        for (int i = 2; i <= 10; i++) {
            CACHE.put(i, new NumberRank(i));
        }
    }

    private final int number;

    private NumberRank(int number) {
        validateNumberRange(number);
        this.number = number;
    }

    public static NumberRank of(int number) {
        NumberRank rank = CACHE.get(number);
        if (rank == null) {
            throw new IllegalArgumentException("숫자 카드는 2부터 10 사이여야 합니다.");
        }
        return rank;
    }

    // ... 나머지 동일
}
```

---

## 리뷰 요약

### 심각도 분류

| 심각도    | 항목         | 핵심 문제                                  |
|--------|------------|----------------------------------------|
| **높음** | R-02, R-03 | `getHand()`로 내부 가변 객체 노출, 묻지 말고 시켜라 위반 |
| **높음** | R-01       | UserPlayer/Dealer 중복 코드                |
| **높음** | R-07       | 가변 리스트 그대로 외부 노출                       |
| **중간** | R-04       | 중복 검증 로직                               |
| **중간** | R-05       | Participants의 응집도 저하                   |
| **중간** | R-06       | reverse() 책임 위치 부적절                    |
| **중간** | R-09       | 매직 넘버 산재                               |
| **중간** | R-10       | getName() 반환 타입 불일치                    |
| **중간** | R-11       | 카드 배분 로직의 캡슐화 위반                       |
| **중간** | R-12       | 디미터 법칙 위반                              |
| **낮음** | R-08       | 미사용 메서드 잔재                             |
| **낮음** | R-13       | 정적 팩토리 메서드 부재                          |
| **낮음** | R-14       | 값 객체 반복 생성                             |

### 잘 된 점

| 항목                  | 설명                                                  | 근거                                 |
|---------------------|-----------------------------------------------------|------------------------------------|
| **Rank 인터페이스 분리**   | `NumberRank`와 `FaceRank`를 인터페이스로 추상화한 설계            | 오브젝트 9장 OCP, Effective Java 아이템 20 |
| **Deck 인터페이스**      | 테스트 시 고정 덱 주입이 가능한 구조                               | Effective Java 아이템 5 의존 객체 주입      |
| **Name record**     | 불변 값 객체로 `record` 활용                                | Effective Java 아이템 17 불변 클래스       |
| **Hand.getCards()** | `Collections.unmodifiableList`로 방어적 복사              | Effective Java 아이템 50              |
| **Ace 점수 계산**       | `Hand.calculateScore()`의 에이스 처리 로직이 도메인 규칙을 정확히 반영  | 오브젝트 5장 정보 전문가 패턴                  |
| **생성자 유효성 검증**      | `Name`, `NumberRank`, `Participants` 등에서 적극적인 입력 검증 | Effective Java 아이템 49              |
| **의존 객체 주입**        | `BlackjackGame`이 `Deck`과 `Participants`를 생성자로 주입받음  | Effective Java 아이템 5, 오브젝트 9장      |
