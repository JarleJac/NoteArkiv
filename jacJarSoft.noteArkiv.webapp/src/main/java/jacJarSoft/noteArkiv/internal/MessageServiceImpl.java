package jacJarSoft.noteArkiv.internal;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import jacJarSoft.noteArkiv.dao.MessageDao;
import jacJarSoft.noteArkiv.model.Message;
import jacJarSoft.util.StringUtils;

public class MessageServiceImpl extends BaseService implements jacJarSoft.noteArkiv.service.MessageService {
	@Autowired
	MessageDao messageDao;
	
	@Override
	public Response addMessage(Message message) {
		validateMessage(message);

		return runWithTransaction((ec, p)-> {
			return Response.ok(messageDao.addMessage(message)).build();
		}, null);
	}

	private void validateMessage(Message message) {
		if (StringUtils.isEmpty(message.getHeading()))
			throw new ValidationErrorException("Meldingen må ha en overskrift!");
		if (StringUtils.isEmpty(message.getMessage()))
			throw new ValidationErrorException("Du må angi en melding!");
	}

	@Override
	public Response updateMessage(Message message) {
		validateMessage(message);
		return runWithTransaction((ec, p)-> {
			Message readMessage = getVerifiedMessage(message.getId());
			readMessage.setAutoExpireDate(message.getAutoExpireDate());
			readMessage.setExpired(message.isExpired());
			readMessage.setHeading(message.getHeading());
			readMessage.setMessage(message.getMessage());
			readMessage.setMessageType(message.getMessageType());
			return Response.ok(messageDao.updateMessage(readMessage)).build();
		}, null);
	}

	@Override
	public Response getMessage(long messageId) {
		return Response.ok(getVerifiedMessage(messageId)).build();
	}

	@Override
	public Response deleteMessage(long messageId) {
		return runWithTransaction((ec, p)-> {
			Message readMessage = getVerifiedMessage(messageId);
			messageDao.deleteMessage(readMessage);
			return Response.ok().build();
		}, null);
	}

	private Message getVerifiedMessage(long messageId) {
		Message readMessage = messageDao.getMessage(messageId);
		if (readMessage == null)
			throw new ValidationErrorException("Kan ikke finne bruker med id " + messageId);
		return readMessage;
	}

	@Override
	public Response getMessages() {
		return Response.ok(messageDao.getMessages()).build();
	}

}
