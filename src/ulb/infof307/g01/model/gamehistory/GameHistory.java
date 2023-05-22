package ulb.infof307.g01.model.gamehistory;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class GameHistory implements Iterable<Game> {
    private final List<Game> games;

    public GameHistory(List<Game> games) {
        this.games = games;
        games.sort(Game::compareTo);
    }

    public int getNumberOfGames() {
        return games.size();
    }

    public int totalScore() {
        return games
                .stream()
                .mapToInt(Game::getScore)
                .sum();
    }

    @Override
    public Iterator<Game> iterator() {
        return games.iterator();
    }

    @Override
    public void forEach(Consumer<? super Game> action) {
        Iterable.super.forEach(action);
    }
}
