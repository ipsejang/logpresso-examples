package com.logpresso.example.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Requires;
import org.araqne.msgbus.Request;
import org.araqne.msgbus.Response;
import org.araqne.msgbus.handler.MsgbusMethod;
import org.araqne.msgbus.handler.MsgbusPlugin;
import org.logpresso.jdbc.JdbcProfile;
import org.logpresso.jdbc.JdbcProfileRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage MariaDB service code table.
 * 
 * @author xeraph
 * 
 */
@MsgbusPlugin
@Component(name = "demo-app-plugin")
public class DemoAppPlugin {
	private final Logger slog = LoggerFactory.getLogger(DemoAppPlugin.class);

	@Requires
	private JdbcProfileRegistry jdbcProfileRegistry;

	@MsgbusMethod
	public void getServices(Request req, Response resp) {
		List<Map<String, Object>> services = new ArrayList<Map<String, Object>>();

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = newConnection();
			stmt = conn.prepareStatement("SELECT id, code, name, description FROM service_codes");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Map<String, Object> m = new HashMap<String, Object>();

				m.put("id", rs.getInt(1));
				m.put("code", rs.getString(2));
				m.put("name", rs.getString(3));
				m.put("description", rs.getString(4));

				services.add(m);
			}

		} catch (SQLException e) {
			throw new IllegalStateException("demo app: cannot list service", e);
		} finally {
			ensureClose(stmt);
			ensureClose(conn);
		}

		resp.put("services", services);
	}

	@MsgbusMethod
	public void insertService(Request req, Response resp) {
		String code = req.getString("code", true); // required
		String name = req.getString("name", true); // required
		String description = req.getString("description"); // optional

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = newConnection();
			stmt = conn.prepareStatement("INSERT INTO service_codes (code, name, description) VALUES (?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, code);
			stmt.setString(2, name);
			stmt.setString(3, description);

			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (!rs.next())
				throw new IllegalStateException("cannot fetch auto-generated id");

			int id = rs.getInt(1);

			// set response object
			resp.put("id", id);

			slog.debug("demo app: created id [{}] code [{}] name [{}] description [{}]", new Object[] { id, code, name,
					description });
		} catch (SQLException e) {
			throw new IllegalStateException("cannot insert service", e);

		} finally {
			ensureClose(stmt);
			ensureClose(conn);
		}
	}

	@MsgbusMethod
	public void updateService(Request req, Response resp) {
		int id = req.getInteger("id", true); // required
		String code = req.getString("code", true); // required
		String name = req.getString("name", true); // required
		String description = req.getString("description"); // optional

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = newConnection();
			stmt = conn.prepareStatement("UPDATE service_codes SET code = ?, name = ?, description = ? WHERE id = ?");
			stmt.setString(1, code);
			stmt.setString(2, name);
			stmt.setString(3, description);
			stmt.setInt(4, id);

			stmt.execute();

			slog.debug("demo app: updated id [{}] code [{}] name [{}] description [{}]", new Object[] { id, code, name,
					description });

		} catch (SQLException e) {
			throw new IllegalStateException("demo app: cannot insert service", e);

		} finally {
			ensureClose(stmt);
			ensureClose(conn);
		}
	}

	@MsgbusMethod
	public void deleteServices(Request req, Response resp) {
		@SuppressWarnings("unchecked")
		List<Integer> services = (List<Integer>) req.get("services", true);

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = newConnection();

			for (Integer id : services) {
				stmt = conn.prepareStatement("DELETE FROM service_codes WHERE id = ?");
				stmt.setInt(1, id);
				stmt.executeUpdate();

				slog.debug("demo app: deleted id [{}]", id);
			}
		} catch (SQLException e) {
			throw new IllegalStateException("demo app: cannot insert service", e);

		} finally {
			ensureClose(stmt);
			ensureClose(conn);
		}
	}

	private Connection newConnection() throws SQLException {
		JdbcProfile demoProfile = jdbcProfileRegistry.getProfile("demo");
		return demoProfile.connect();
	}

	private void ensureClose(Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (Throwable t) {
		}
	}

	private void ensureClose(Connection c) {
		try {
			if (c != null)
				c.close();
		} catch (Throwable t) {
		}
	}

}
