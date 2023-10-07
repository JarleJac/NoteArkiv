package jacJarSoft.noteArkiv.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.model.Voice;

@Component
public class VoiceDao extends AbstractDao {

	@SuppressWarnings("unchecked")
	public List<Voice> getVoices() {
		return (List<Voice>) getEntityManager().createNativeQuery("select * from voices", Voice.class).getResultList();
	}
}
