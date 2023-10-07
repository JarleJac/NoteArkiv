package jacJarSoft.noteArkiv.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.model.Message;

@Component
public class MessageDao extends AbstractDao {

	public Message addMessage(Message message) {
		getEntityManager().persist(message);
		return message;
	}

	public List<Message> getMessages() {
		List<Message> messages = getEntityManager().createQuery("select m from Message m", Message.class).getResultList();
		return messages;
	}

	public Message getMessage(long messageId) {
		Message message = getEntityManager().find(Message.class, messageId);
		return message;
	}

	public void deleteMessage(Message message) {
		getEntityManager().remove(message);
	}

	public Message updateMessage(Message message) {
		Message mergeMessage = getEntityManager().merge(message);
		return mergeMessage;
	}

}
