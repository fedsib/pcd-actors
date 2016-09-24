/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Riccardo Cardin
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 /**
 * A concrete type of ActorSystem used only for test purposes.
 *
 * @author Federico Silvio Busetto
 * @version 1.0
 * @since 1.0
 */

package it.unipd.math.pcd.actors.utils;

import it.unipd.math.pcd.actors.*;
import it.unipd.math.pcd.actors.utils.actors.ActorTest;
import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

public class ActorSystemTest extends ActorSystemImpl {

    public int hmSend;
	
    private volatile int processed_count = 0;

    public void incrementProcessed() { processed_count++; }

    public int getHowManyProcessedMex() { return processed_count; }

    public int getHowManySendMex() { return hmSend; }

    /**
     * Same as stop() in the super class but this one increments 
	 * the howManySendMex counter
	 */
    @Override
    public void stop(ActorRef<?> actor) {

        if(actors.containsKey(actor)) {
            AbsActor act = (AbsActor) getUnderlyingActor(actor);

            if (!act.isStopped()) {
                act.setStopped();
                synchronized (act) {
                    while (!act.isStopped()) {
                        try {
                            act.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    act.clearMailbox();
					
                    if (act instanceof ActorTest)
                        hmSend = ((ActorTest) act).getSendMex();
                    actors.remove(actor);
                }
            }
            else {
                throw new NoSuchActorException("This actor was already stopped!");
            }

        }
        else {
            throw new NoSuchActorException("Can't find this actor!");
        }

    }
}
