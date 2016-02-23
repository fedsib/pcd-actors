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
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */

/**
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.NoSuchElementException;

/**
 * Defines common properties of all actors.
 *
 * @author Riccardo Cardin
 * @version 1.5
 * @since 1.0
 * @author Federico Silvio Busetto
 * @since 1.5
 */
public abstract class AbsActor<T extends Message> implements Actor<T> {

    /**
     * Self-reference of the actor
     */
    protected ActorRef<T> self;

    /**
     * Sender of the current message
     */
    protected ActorRef<T> sender;

    /**
     * Define Actor's Mailbox as an inner class
     */

    private final class MailboxImpl<T extends Message> implements Mailbox<T> {

        private final ConcurrentLinkedQueue<MailBoxElement<T>> q;

        MailboxImpl(){
            q = new ConcurrentLinkedQueue<>();
        }

        /**
         * Inserts the specified message at the tail of an actor's mailbox.
         * As the mailbox is unbounded, this method will never throw
         * IllegalStateException or return false.
         * @param msg The message to add in the actor's mailbox
         * @return true if the message was added in the actor's mailbox
         */
        @Override
        public boolean add(MailBoxElement<T> msg){
            try {
               return q.add(msg);
            } catch (NullPointerException |IllegalStateException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Returns true if the mailbox of the actor contains no elements.
         * @return true if the mailbox of the actor is empty.
         */
        @Override
        public boolean isEmpty(){
            return q.isEmpty();
        }

        /**
         *Returns the number of elements in the actor's mailbox.
         * @return the number of elements in the actor's mailbox
         */
        @Override
        public int size() {
            return q.size();
        }

        @Override
        public MailBoxElement<T> getElement(){
            try{
                return q.remove();
            }
            catch (NoSuchElementException e){
                e.printStackTrace();
            }
            return null;
        }

    }

    /**
     * Mailbox of the Actor
     */

    private final Mailbox mb = new MailboxImpl<>();

    /**
     * Sets the self-referece.
     *
     * @param self The reference to itself
     * @return The actor.
     */
    protected final Actor<T> setSelf(ActorRef<T> self) {
        this.self = self;
        return this;
    }
}
