package it.polimi.se2018.network.messages.responses;

import static org.junit.Assert.fail;


public class TestModelViewResponse {
    /*ModelView modelView;
    ModelViewResponse modelViewResponse;

    @Before
    public void init() {
        Player player = new Player("test",3, new Window("test",new Random().nextInt(),null, new Database().getMatrix()),null);
        List<Player> list = new ArrayList<Player>();
        list.add(player);
        modelView = new ModelView(new Board(new Random().nextInt(),list,"test",null,null));
        modelViewResponse = new ModelViewResponse(modelView);
    }

    @Test
    public void testGetModelView() {
        Assert.assertEquals(modelView,modelViewResponse.getModelView());
    }

    @Test
    public void testHandle() {
        modelViewResponse.handle(new ResponseHandler() {
            @Override
            public void handleResponse(ModelViewResponse modelViewResponse) {
            }

            @Override
            public void handleResponse(TextResponse textResponse) {
                fail();
            }

            @Override
            public void handleResponse(TurnStartResponse turnStartResponse) {
                fail();
            }

            @Override
            public void handleResponse(ToolCardResponse toolCardResponse) {
                fail();

            }
        });
    }*/
}
