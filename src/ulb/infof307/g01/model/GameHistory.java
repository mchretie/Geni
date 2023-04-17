package ulb.infof307.g01.model;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GameHistory implements Iterable<Game> {
    private final List<Game> games;

    public GameHistory(List<Game> games) {
        this.games = games;
        games.sort((a, b) -> b.timestamp().compareTo(a.timestamp()));
    }

    @Override
    public Iterator<Game> iterator() {
        return games.iterator();
    }

    @Override
    public void forEach(Consumer<? super Game> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Game> spliterator() {
        return Iterable.super.spliterator();
    }
}
