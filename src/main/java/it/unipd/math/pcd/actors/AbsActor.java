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
 * Defines common properties of all actors.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

public abstract class AbsActor<T extends Message> implements Actor<T> {

    /**
     * Mailbox of the Actor
     */

    protected final Mailbox<T> mb;

    /**
     * True if the actor is stopped, otherwise false
     */
    private volatile boolean stopped;

    /**
     * Self-reference of the actor
     */
    protected ActorRef<T> self;

    /**
     * Sender of the current message
     */
    protected ActorRef<T> sender;


    public AbsActor(){
        mb = new MailBoxImpl<>();
        stopped = false;
        startRManager();
    }

    /**
     * Manages all the messages in the mailbox
     * */
    public synchronized void clearMailbox(){
     while(!mb.isEmpty()){
         synchronized (AbsActor.this) {
         MailBoxElement<T> msg = mb.extract();
         sender = msg.getSender();
         receive(msg.getMessage());
         }
     }
 }

 /**
  * Get the mailbox of an actor
  * */

 public Mailbox<T> getMb(){
     return  mb;
 }

 /**
  * Manager for the incoming Messages
  *
  * */
    private void startRManager() {
        Thread rM = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isStopped()) {
                            while (mb.isEmpty()) {
                                synchronized (mb){
                                    mb.wait();
                            }
                        }
                        if (!mb.isEmpty()){
                            synchronized (mb){
                                MailBoxElement<T> MBhead = mb.extract();
                                sender = MBhead.getSender();
                                receive(MBhead.getMessage());
                            }
                        }
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    //setStopped();
                }
                finally {
                    clearMailbox();
                    while (!mb.isEmpty() ) {
                        synchronized (AbsActor.this) {
                            try {
                                AbsActor.this.setStopped();
                                AbsActor.this.notifyAll();
                            } catch (NoSuchActorException nsae) {
                                nsae.printStackTrace();
                            }

                        }
                    }
                }
            }
        });
        rM.start();
    }

    /**
     * Gets the stopped value.
     */

    public boolean isStopped(){
        return stopped;
    }

   /**
   * Sets the stopped value for an actor.
   */

   public synchronized void setStopped() throws NoSuchActorException{
       if(!stopped){
           stopped = true;
           synchronized (mb) {
               mb.notifyAll();
           }
       }

       else throw new NoSuchActorException("Actor already stopped!");
   }

   /**
   * Add a Message to the Mailbox
   *
   */

    public void addMessage(final T msg, final ActorRef<T> snd) {
        synchronized (mb){
		if(!stopped) {
                mb.add(msg,snd);
                mb.notifyAll();
            }
        }
    }

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
