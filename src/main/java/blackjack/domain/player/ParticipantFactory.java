package blackjack.domain.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
}
