package jacJarSoft.util;

import static org.junit.Assert.*;

import org.junit.Test;

import jacJarSoft.util.Auth.AuthTokenInfo;
import jacJarSoft.util.Auth.AuthTokenUtil;

public class TestAuthTokenUtil {

	@Test
	public void testVersion() {
		String user = "User";
		String part = "TEST2Test";
		String token = AuthTokenUtil.createToken(user, part);
		AuthTokenInfo tokenInfo = AuthTokenUtil.getTokenInfo(token);
		assertEquals(user, tokenInfo.getUser());
		assertEquals(part, tokenInfo.getTokenPart());
		assertNotEquals(token, tokenInfo.toString());
	}
}
