package blackjack.ui.dto;

import blackjack.domain.player.Participants;
import java.util.List;

public record FinalScoreDto(ParticipantScoreDto dealer, List<ParticipantScoreDto> userPlayers) {

    public static FinalScoreDto of(Participants participants) {
        ParticipantScoreDto dealerDto = ParticipantScoreDto.fromDealer(participants.getDealer());
        List<ParticipantScoreDto> playerDtos = participants.getPlayers().stream()
                .map(ParticipantScoreDto::fromPlayer)
                .toList();

        return new FinalScoreDto(dealerDto, playerDtos);
    }
}
