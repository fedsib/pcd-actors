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
 * A concrete Actor used only for test purposes.
 *
 * @author Federico Silvio Busetto
 * @version 1.0
 * @since 1.0
 */

package it.unipd.math.pcd.actors.utils.actors;

import it.unipd.math.pcd.actors.*;
import it.unipd.math.pcd.actors.utils.ActorSystemTest;
import it.unipd.math.pcd.actors.utils.messages.TrivialMessage;


public class ActorTest extends TrivialActor {

    private AbsActorSystem refer;

    public void setRefer(AbsActorSystem absAS) {
        refer = absAS;
    }

    @Override
    public void addMessage(final TrivialMessage message, final ActorRef<TrivialMessage> send) {
		
        synchronized (mb){
            if(!isStopped()) {
                mb.add(message,send);
                mb.notifyAll();
            }
        }

         synchronized (this){ sendmex++;}
    }


    private volatile int sendmex;
    public synchronized int getSendMex() { return sendmex; }

    @Override
    public void receive(TrivialMessage message) {
        ((ActorSystemTest)refer).incrementProcessed();
    }
}
