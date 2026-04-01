package blackjack.ui.dto;

import blackjack.domain.player.Participants;
import java.util.List;

public record InitialDistributionDto(ParticipantDto dealer, List<ParticipantDto> userPlayers) {

    public static InitialDistributionDto of(Participants participants) {
        ParticipantDto dealerDto = ParticipantDto.fromDealerInitialDraw(participants.getDealer());

        List<ParticipantDto> playerDtos = participants.getPlayers().stream()
                .map(ParticipantDto::fromPlayer)
                .toList();

        return new InitialDistributionDto(dealerDto, playerDtos);
    }
}
