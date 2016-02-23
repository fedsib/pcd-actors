package it.unipd.math.pcd.actors;
    /**
    * @author Federico Silvio Busetto
     * @version 1.0
     * @since 1.0
     */
public class MailBoxElement<T extends Message> {

        private final T message;
        private final ActorRef<T> sender;

        public MailBoxElement(final T message, final ActorRef<T> sender) {
            this.message = message;
            this.sender = sender;
        }

        public T getMessage(){
            return message;
        }

        public ActorRef<T> getSender(){
            return sender;
        }
}
