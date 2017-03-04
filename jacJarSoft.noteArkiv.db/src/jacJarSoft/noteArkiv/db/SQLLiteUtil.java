package jacJarSoft.noteArkiv.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.sqlite.Function;

public class SQLLiteUtil {
	public static class Contains extends Function {

	    @Override
	    protected void xFunc() throws SQLException {
	        if (args() != 2) {
	            throw new SQLException("Contains(t1,t2): Invalid argument count. Requires 2, but found " + args());
	        }
            String testValue = value_text(0).toLowerCase();
            String isLike = value_text(1).toLowerCase();
            
            if (testValue.contains(isLike)) {
                result(1);
            } else {
                result(0);
            }
	    }
	}
	public static void registerContainsFunc(Connection con) throws SQLException {
		Function.create(con, Contains.class.getSimpleName(), new Contains());
	}
	public static void destroyContainsFunc(Connection con) throws SQLException {
		Function.destroy(con, Contains.class.getSimpleName());
	}
}
