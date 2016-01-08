package jacJarSoft.util;

import static org.junit.Assert.*;

import org.junit.Test;

import jacJarSoft.util.Auth.AuthTokenInfo;
import jacJarSoft.util.Auth.AuthTokenUtil;

public class TestAuthTokenUtil {

	@Test
	public void testcreateToken() {
		String user = "User";
		String part = "TEST2Test";
		String token = AuthTokenUtil.createToken(user, part);
		AuthTokenInfo tokenInfo = AuthTokenUtil.getTokenInfo(token);
		assertEquals(user, tokenInfo.getUser());
		assertEquals(part, tokenInfo.getTokenPart());
		assertFalse(token.contains(user));
		assertFalse(token.contains(part));
		assertTrue(token.indexOf(AuthTokenInfo.AuthSchema) == 0);
	}
}
