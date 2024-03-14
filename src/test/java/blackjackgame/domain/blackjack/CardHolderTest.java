package blackjackgame.domain.blackjack;

import static blackjackgame.domain.card.CardName.ACE;
import static blackjackgame.domain.card.CardName.EIGHT;
import static blackjackgame.domain.card.CardName.JACK;
import static blackjackgame.domain.card.CardName.QUEEN;
import static blackjackgame.domain.card.CardName.SEVEN;
import static blackjackgame.domain.card.CardName.TWO;
import static blackjackgame.domain.card.CardType.HEART;
import static blackjackgame.domain.card.CardType.SPADE;

import blackjackgame.domain.card.Card;
import blackjackgame.domain.card.Deck;
import blackjackgame.domain.gamers.CardHolder;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CardHolderTest {

    public static Stream<Arguments> isDeadParameters() {
        return Stream.of(
                Arguments.of(new Card(TWO, HEART), true), Arguments.of(new Card(ACE, HEART), false)
        );
    }

    @Test
    @DisplayName("이름과 손패를 갖는 게이머 클래스의 생성자를 만들 수 있다.")
    void createGamerWithNameAndHoldingCardsConstructorTest() {
        Assertions.assertThatCode(() ->
                new CardHolder("게이머", HoldingCards.of())
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임 참가자가 카드를 뽑았을 때 점수가 올바르게 계산되는지 검증")
    void draw() {
        CardHolder CardHolderGamer = new CardHolder("robin", HoldingCards.of());
        CardHolderGamer.draw(new Deck(List.of(new Card(JACK, HEART))), new TestPlayerCardDrawStrategy(CardHolderGamer));

        SummationCardPoint actual = CardHolderGamer.getSummationCardPoint();
        SummationCardPoint expected = new SummationCardPoint(10);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("플레이어는 총합이 21이 넘으면 카드를 뽑을 수 없는지 검증")
    void validateDrawLimit() {
        CardHolder CardHolderGamer = new CardHolder("robin", HoldingCards.of(
                new Card(JACK, HEART), new Card(EIGHT, HEART), new Card(JACK, SPADE)
        ));
        Deck deck = new Deck(List.of(
                new Card(TWO, SPADE)
        ));

        Assertions.assertThatThrownBy(() -> CardHolderGamer.draw(deck, new TestPlayerCardDrawStrategy(CardHolderGamer)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("카드를 더이상 뽑을 수 없습니다.");
    }

    @Test
    @DisplayName("딜러는 총합이 16이 넘으면 카드를 뽑을 수 없는지 검증")
    void validateDealerDrawLimit() {
        CardHolder cardHolderGamer = new CardHolder("robin", HoldingCards.of(
                new Card(JACK, HEART), new Card(SEVEN, HEART)
        ));
        Deck deck = new Deck(List.of(
                new Card(TWO, SPADE)
        ));

        Assertions.assertThatThrownBy(() -> cardHolderGamer.draw(deck, new TestDealerCardDrawStrategy(cardHolderGamer)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("카드를 더이상 뽑을 수 없습니다.");
    }

    @ParameterizedTest
    @MethodSource("isDeadParameters")
    @DisplayName("게이머의 점수가 21이 넘으면 죽었다고 판단하는지 검증")
    void isDead(Card additionalCard, boolean expected) {
        CardHolder cardHolderGamer = new CardHolder("robin", HoldingCards.of(
                new Card(JACK, HEART), new Card(QUEEN, HEART), additionalCard
        ));

        Assertions.assertThat(cardHolderGamer.isDead()).isEqualTo(expected);
    }
}
