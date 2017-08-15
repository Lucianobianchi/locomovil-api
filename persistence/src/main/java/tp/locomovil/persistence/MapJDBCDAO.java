package tp.locomovil.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tp.locomovil.inter.MapDAO;
import tp.locomovil.model.SMap;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MapJDBCDAO implements MapDAO {
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;

	private final static RowMapper<SMap> ROW_MAPPER = new RowMapper<SMap>() {
		public SMap mapRow (ResultSet rs, int rowNum) throws SQLException {
			return new SMap(rs.getString("name"), rs.getLong("map_id"));
		}
	};

	@Autowired
	public MapJDBCDAO (DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("maps")
			.usingGeneratedKeyColumns("map_id");
	}

	public SMap createMap (String name) {
		if (name == null)
			return null;

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("name", name);

		final Number mapId = jdbcInsert.executeAndReturnKey(args);

		return new SMap(name, mapId.longValue());
	}

	public SMap getMapById (long id) {
		final List<SMap> result = jdbcTemplate.query("SELECT * FROM maps WHERE map_id = ?", ROW_MAPPER, id);
		return result.isEmpty() ? null : result.get(0);
	}

	public SMap getMapByName (String name) {
		if (name == null)
			return null;
		final List<SMap> result = jdbcTemplate.query("SELECT * FROM maps WHERE name = ?", ROW_MAPPER, name);
		return result.isEmpty() ? null : result.get(0);
	}
}
