package tp.locomovil.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tp.locomovil.inter.ProjectDAO;
import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProjectJDBCDAO implements ProjectDAO {
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;

	private final static RowMapper<Project> ROW_MAPPER = new RowMapper<Project>() {
		public Project mapRow (ResultSet rs, int rowNum) throws SQLException {
			return new Project(rs.getString("name"), rs.getLong("project_id"));
		}
	};

	@Autowired
	public ProjectJDBCDAO (DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("projects")
			.usingGeneratedKeyColumns("project_id");
	}

	public Project createProject (String name) {
		if (name == null)
			throw new IllegalArgumentException("Null project name");

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("name", name);

		final Number projectId = jdbcInsert.executeAndReturnKey(args);

		return new Project(name, projectId.longValue());
	}

	public Project getProjectByName (String name) {
		if (name == null)
			return null;
		final List<Project> result = jdbcTemplate.query("SELECT * FROM projects WHERE name = ?", ROW_MAPPER, name);
		return result.isEmpty() ? null : result.get(0);
	}

	public Project getProjectById (long id) {
		final List<Project>
			result = jdbcTemplate.query("SELECT * FROM projects WHERE project_id = ?", ROW_MAPPER, id);
		return result.isEmpty() ? null : result.get(0);
	}
}
