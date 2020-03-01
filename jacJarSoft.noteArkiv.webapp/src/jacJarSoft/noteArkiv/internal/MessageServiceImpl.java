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

		if (StringUtils.isEmpty(message.getHeading()))
			throw new ValidationErrorException("Meldingen må ha en overskrift!");
		if (StringUtils.isEmpty(message.getMessage()))
			throw new ValidationErrorException("Du må angi en melding!");

		return runWithTransaction((ec, p)-> {
			return Response.ok(messageDao.addMessage(message)).build();
		}, null);
	}

	@Override
	public Response updateMessage(Message message) {
		throw new ValidationErrorException("Ikke laget!");
	}

	@Override
	public Response getMessage(long messageId) {
		throw new ValidationErrorException("Ikke laget!");
	}

	@Override
	public Response getMessages() {
		return Response.ok(messageDao.getMessages()).build();
	}

}
