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
 * A concrete implementation of ActorRef
 *
 * @author Federico Silvio Busetto
 * @version 1.0
 * @since 1.0
 */

package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

public class ConcreteActorRef<T extends Message> implements ActorRef<T> {

    protected final AbsActorSystem actorSys;

    public ConcreteActorRef( AbsActorSystem aS ) { 
		actorSys = aS;
	}

    /**
	* Send a message to the actor a
	* @param msg, the message to be sent
	* @param a, the ActorRef of the Actor who will receive the message
	*/
    @Override
    public void send(T msg, ActorRef a) throws NoSuchActorException {
        AbsActor tmpAct = (AbsActor) actorSys.getUnderlyingActor(a);
        if (!tmpAct.isStopped())
            tmpAct.addMessage(msg, this);
    }

	/**
	* Compare two ActoRef
	* @param ar, an ActorRef that will be compared to this one 
	*/
    @Override
    public int compareTo(ActorRef ar) {
        return (this == ar) ? 0 : -1;
    }
}
