package blackjack.controller;

import static blackjack.controller.BlackjackCommand.INVALID_COMMAND_MESSAGE;
import static blackjack.controller.BlackjackCommand.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BlackjackCommandTest {

    @Test
    void 올바른_입력값이_아닌_경우_예외를_던진다() {
        assertThatThrownBy(() -> from("허브"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_COMMAND_MESSAGE + "허브");
    }

    @ParameterizedTest(name = "Y 또는 N을 받아서 명령을 생성한다. 입력값: {0}, 결과: {1}")
    @CsvSource({"y, HIT", "n, STAY"})
    void Y_또는_N을_받아서_명령을_생성한다(final String input, final BlackjackCommand command) {
        assertThat(from(input)).isEqualTo(command);
    }

    @ParameterizedTest(name = "커맨드가 Hit인지 확인한다 입력값: {0}, 결과: {1}")
    @CsvSource({"HIT, true", "STAY, false"})
    void 커맨드가_Hit인지_확인한다(final BlackjackCommand command, final boolean result) {
        assertThat(command.isHit()).isEqualTo(result);
    }
}
