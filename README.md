# java-blackjack

블랙잭 미션 저장소

## 우아한테크코스 코드리뷰

- [온라인 코드 리뷰 과정](https://github.com/woowacourse/woowacourse-docs/blob/master/maincourse/README.md)

## 그래프
```mermaid
graph TD
    A[controller] -.-> G[BlackjackGame]
    A -.-> CD[CardDeck] --> C[Card]
    A -.-> WS
    G --> PS[Players] --> P[Player] --> H[HandCard]
    G --> D[Dealer] --> H
    P -.-> CS[CardScore] 

    CS--> |create|WS
    H--> |create|CS
    WS[WinningfResult]

```

## 입력

- [x] 참여자의 이름을 입력받는다
    - [x] 쉼표(,)를 기준으로 분리한다
- [x] 참여자가 카드를 더 받을지 입력받는다
    - [x] "y" 나 "n"이 아니면 예외처리한다
- [x] 플레이어들의 배팅 금액을 입력받는다.

## 출력

- [x] 카드를 처음 나눈 후, 보유한 카드를 출력한다
    - [x] 딜러는 한장의 카드만 출력한다
- [x] 참여자가 카드를 더 받은 후, 보유한 카드를 출력한다
- [x] 딜러가 카드를 더 받았는지 여부를 출력한다
- [x] 딜러와 플레이어들의 카드 점수 합산결과를 출력한다
- [x] 게임을 완료한 후 각 플레이어별로 수익을 출력한다.

## Name

- [x] 이름이 빈 문자열이면 예외처리한다

## Player

- [x] 게임을 시작하면 플레이어는 두 장씩의 카드를 지급받는다.
- [x] 카드를 받아서 가지고 있는다
- [x] 카드 점수를 계산한다
- [x] 버스트 여부를 판단한다
- [x] 블랙잭 여부를 판단한다
- [x] 합계가 21을 넘지 않을 경우 원한다면 얼마든지 카드를 계속 뽑을 수 있다
  - [x] 플레이어가 n를 입력하면 카드를 더 뽑지 않는다.
  
## Dealer

- [x] 게임을 시작하면 딜러는 두 장의 카드를 받아 1장만 공개한다.
- [x] 카드를 받아서 가지고 있는다.
- [x] 카드 점수를 계산한다.
- [x] 버스트 여부를 판단한다.
- [x] 블랙잭 여부를 판단한다.
- [x] 카드 점수의 합계가 16 이하이면 한장의 카드를 추가로 받아야 하고, 17점 이상이면 추가로 받을 수 없다

## 블랙잭 게임

- [x] 딜러와 플레이어 중 카드 합 21또는 21에 가장 가까운 사람이 이긴다.
- [x] 카드 점수 합계가 21을 초과하면 버스트이며, 게임에서 패배한다.
- [x] 카드 점수 합계가 21이면 블랙잭이며, 게임에서 승리한다.

### 카드

- [x] 카드 숫자는 2~10,A,K,Q,J 로 13종류이다.
- [x] 카드 문양은 다이아몬드, 하트, 스페이드, 클로버로 4종류이다.
- [x] 카드를 랜덤하게 뽑는다.
    - [x] 같은 카드는 한판에 또 나올 수 없다.

#### 카드 숫자 계산

- [x] 카드의 숫자 계산은 카드 숫자를 기본으로 한다.
    - [x] Ace는 1또는 11로 계산한다.
    - [x] King, Queen, Jack은 각각 10으로 계산한다.

### 배팅
- [x] 카드를 추가로 뽑아 21을 초과할 경우 배팅 금액을 모두 잃게 된다.
  - [x] 딜러가 21을 초과하면 그 시점까지 남아 있던 플레이어들은 가지고 있는 패에 상관 없이 승리해 베팅 금액을 받는다.
- [x] 처음 두 장의 카드 합이 21일 경우 블랙잭이 되면 베팅 금액의 1.5 배를 딜러에게 받는다.
- [x] 딜러와 플레이어가 모두 동시에 블랙잭인 경우 플레이어는 베팅한 금액을 돌려받는다.
