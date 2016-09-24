package it.unipd.math.pcd.actors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import it.unipd.math.pcd.actors.utils.actors.TrivialActor;
import it.unipd.math.pcd.actors.utils.messages.TrivialMessage;
import it.unipd.math.pcd.actors.utils.ActorSystemTest;
import it.unipd.math.pcd.actors.utils.actors.ActorTest;


public class GracefullyProcessAllMexTest{
    private ActorSystem system;

    @Before
    public void init() {
        system = new ActorSystemTest();
    }

    @Test
    public void createActorTestReferenceTest() {
        ActorRef ar = system.actorOf(ActorTest.class);
        Assert.assertNotNull("Not null ActorRef created!", ar);
    }

    @Test
    public void shouldGracefullyProcessAllMexTest() {
        ActorRef ar1= system.actorOf(ActorTest.class);

        Actor act = ((ActorSystemTest)system).getUnderlyingActor(ar1);

        ((ActorTest)act).setRefer((AbsActorSystem)system);

        for (int i = 0; i < 1200; i++) {
            ar1.send(new TrivialMessage(), ar1);
        }

        system.stop(ar1);

        int sent = ((ActorSystemTest)system).getHowManySendMex();
		
        int processed = ((ActorSystemTest)system).getHowManyProcessedMex(); 

        Assert.assertEquals("Some messages aren't been processed yet!", sent, processed);

    }
}
