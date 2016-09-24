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
 *
 * A concrete implementation of ActorSystem
 *
 * @author Federico Silvio Busetto
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

public class ActorSystemImpl extends AbsActorSystem {
	
	/**
	* Creates only LOCAL actors
	* @param mode, it could be only local or remote
	*/
    @Override
    protected ActorRef createActorReference(ActorMode mode) {
        if (mode == ActorMode.LOCAL) {
            return new ConcreteActorRef(this);
        }
        else {
            throw new IllegalArgumentException("Non è possibile gestire questo tipo di Attore!");
        }

    }

	/**
	* Stop an actor
	* @param actor, the ActorRef of the actor that will be stopped
	*/
    @Override
    public void stop(ActorRef<?> actor) {

        if(actors.containsKey(actor)) {
            AbsActor act = (AbsActor) getUnderlyingActor(actor);

            if (!act.isStopped()) {
                synchronized (act) {
                    act.setStopped();
                    while (!act.isStopped()) {
                        try {
                            act.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
					act.clearMailbox();	
                    }
                    actors.remove(actor);
                }
            }
            else {
                throw new NoSuchActorException();
            }

        }
        else {
            throw new NoSuchActorException();
        }

    }

	/**
	* Stop all the actors in the ActorSystem
	*
	*/
    @Override
    public void stop() {
        for (ActorRef acR : actors.keySet() ) {
            stop(acR);
        }
    }

}
