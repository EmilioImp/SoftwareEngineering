package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.network.messages.Coordinate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestPlaceMessage {
    private PlaceMessage placeMessage;
    private Coordinate coordinate;
    private Random random;

    @Before
    public void init() {
        random = new Random();
        coordinate = new Coordinate(random.nextInt(),random.nextInt());
        placeMessage = new PlaceMessage(0,0, coordinate);
    }

    @Test
    public void testGetFinalPosition() {
        Assert.assertEquals(coordinate,placeMessage.getFinalPosition());
        Assert.assertNotEquals(new Coordinate(coordinate.getRow()+1,coordinate.getCol()-1),placeMessage.getFinalPosition());
    }

    @Test
    public void testSetFinalPosition() {
        Coordinate coordinate = new Coordinate(random.nextInt(),random.nextInt());
        placeMessage.setFinalPosition(coordinate);
        Assert.assertEquals(coordinate,placeMessage.getFinalPosition());
    }

    @Test
    public void testHandle() {
        placeMessage.handle(new MessageHandler() {
            @Override
            public void handleMove(ToolCardMessage toolCardMessage) {
                fail();
            }

            @Override
            public void handleMove(PassMessage passMessage) {
                fail();
            }

            @Override
            public void handleMove(PlaceMessage placeMessage) {
            }

            @Override
            public void handleMove(DraftMessage draftMessage) {
                fail();
            }

            @Override
            public void handleMove(ToolCardRequestMessage toolCardRequestMessage) {
                fail();
            }

            @Override
            public void handleMove(SetupMessage setupMessage) {fail();}

            @Override
            public void handleMove(InputMessage inputMessage) {fail();}
        });
    }
    
}
