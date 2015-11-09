package jacJarSoft.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;;

public class DbUtil {
	private DbUtil() {/*only static methods*/}
	
	/**
	 * Execute DML or Insert, Update, Delete
	 * @param connection
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static void execUpdateSql(Connection connection, String sql, Object... params) throws SQLException {
		try (PreparedStatement statement = createStatement(connection, sql, params))
		{
			statement.executeUpdate();
		}
	}

	/**
	 * Creates a PreparedStatement on the connection fro the provided SQL and params.
	 * @param connection
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static PreparedStatement createStatement(Connection connection, String sql, Object... params) throws SQLException 
	{
		PreparedStatement statement = connection.prepareStatement(sql);
		for(int i=1; i<=params.length;i++) {
			statement.setObject(i,  params[i-1]);
		}
		return statement;
	}
	public interface SqlQueryFunction<R> {
		R apply(ResultSet rs) throws SQLException;
	}
	/**
	 * Executes a SQL query statements and closes it after calling the callback
	 * @param statement
	 * @param callback
	 * @return R the result of the callback
	 * @throws SQLException
	 */
	public static <R> R execQuery(PreparedStatement statement, SqlQueryFunction<R> callback) throws SQLException {
		R result = null;
		try {
			try (ResultSet resultSet = statement.executeQuery()) {
				result = callback.apply(resultSet);
			}
		}
		finally {
			statement.close();
		}
		return result;
	}
	public static <R> R runWithConnection(EntityManager em, Function<Connection,R> function) {
		R ret = null;
		EntityTransaction transaction = em.getTransaction();
		if (transaction.isActive())
		{
			ret = unwrapConnectionAndRun(em, function);
		}
		else
		{
			try
			{
				transaction.begin();
				ret = unwrapConnectionAndRun(em, function);
				transaction.commit();
			}
			finally {
				if (transaction.isActive())
					transaction.rollback();
			}
		}
		return ret;
	}

	private static <R> R unwrapConnectionAndRun(EntityManager em, Function<Connection, R> function) {
		R ret;
		Connection connection = em.unwrap(Connection.class);
		ret = function.apply(connection);
		return ret;
	}
}
