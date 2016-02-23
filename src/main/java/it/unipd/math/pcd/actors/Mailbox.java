package it.unipd.math.pcd.actors;

/**
 * Defines common properties of an actor Mailbox, which is a Queue.
 *
 * @author Federico Silvio Busetto
 * @version 1.0
 * @since 1.0
 */
public interface Mailbox <T extends Message> {

    boolean add(MailBoxElement<T> msg);
    boolean isEmpty();
    int size();

    MailBoxElement<T> getElement();
}
