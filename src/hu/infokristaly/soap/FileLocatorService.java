package hu.infokristaly.soap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class FileLocatorService {
	
	private DataSource dataSource;
	
	public String addFileLocation(String fileName, Integer path, Integer location) {
		String result = "ok";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			InitialContext ic = new InitialContext();
			dataSource = (DataSource)ic.lookup("java:jboss/datasources/FileMapDS");
			conn = dataSource.getConnection();
			String sql = "insert into fileDesc (\"fileName\",pathid,locationid) values (?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, fileName);
			stmt.setInt(2, path);
			stmt.setInt(3, location);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			result = e.getLocalizedMessage();
		} catch (NamingException e) {
			e.printStackTrace();
			result = e.getLocalizedMessage();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public Integer addLocation(String name) {
		Integer result = -1;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			InitialContext ic = new InitialContext();
			dataSource = (DataSource)ic.lookup("java:jboss/datasources/FileMapDS");
			conn = dataSource.getConnection();
			String sql = "insert into location (locationName) values (?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.execute();
			rs = stmt.getGeneratedKeys();
			rs.next();
			result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public Integer addPath(String path) {
		Integer result = -1;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			InitialContext ic = new InitialContext();
			dataSource = (DataSource)ic.lookup("java:jboss/datasources/FileMapDS");
			conn = dataSource.getConnection();
			String sql = "insert into \"filePath\" (\"filePath\") values (?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, path);
			stmt.execute();
			rs = stmt.getGeneratedKeys();
			rs.next();
			result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public String find(String namePattern) {
		String sql = "select \"fileName\", \"filePath\", locationname from \"filedesc\" f"
				+" join \"filePath\" p on f.pathid = p.id"
				+" join \"location\" l on f.locationid = l.id"
				+" where \"fileName\" ilike ? and \"filePath\" ilike ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder responseBuilder = new StringBuilder();
		try {
			InitialContext ic = new InitialContext();
			dataSource = (DataSource)ic.lookup("java:jboss/datasources/FileMapDS");
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, namePattern);
			stmt.setString(2, namePattern);
			rs = stmt.executeQuery();
			responseBuilder.append("<resultSet>\n");
			while(rs.next()) {
				responseBuilder.append("  <fileDesc>\n");
				responseBuilder.append("    <fileName>\n");
				responseBuilder.append("      "+rs.getString(1)+"\n");
				responseBuilder.append("    </fileName>\n");
				responseBuilder.append("    <filePath>\n");
				responseBuilder.append("      "+rs.getString(2)+"\n");
				responseBuilder.append("    </filePath>\n");
				responseBuilder.append("    <locationName>\n");
				responseBuilder.append("      "+rs.getString(3)+"\n");
				responseBuilder.append("    </locationName>\n");
				responseBuilder.append("  </fileDesc>\n");
			}
			responseBuilder.append("</resultSet>");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return responseBuilder.toString();
	}
}
