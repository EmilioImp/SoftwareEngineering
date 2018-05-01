package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Color;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ColorVarietyObjective extends PublicObjective {
    private static ColorVarietyObjective instance = null;

    private ColorVarietyObjective(String imagePath, String title) {
        super(imagePath, title);
    }

    private static synchronized ColorVarietyObjective createInstance(String imagePath, String title) {
        if (instance == null) instance = new ColorVarietyObjective(imagePath, title);
        return instance;
    }

    public static ColorVarietyObjective instance(String imagePath, String title) {
        if (instance == null) createInstance(imagePath, title);
        return instance;
    }


    private Predicate<Square> checkIfContainsColor(final Color color) {
        return (square -> square.getDie() != null && square.getDie().getColor() == color);
    }


    @Override
    public int evalPoints(Player player) {
        Optional<Integer> min = (Stream.of(Color.values())
                .map(color -> ( (int)StreamSupport.stream(player.getMap().spliterator(), false)
                        .filter(checkIfContainsColor(color))
                        .count()))
                .reduce(Integer::min));
        return (min.map(integer -> integer * 4).orElse(0));

    }

}
