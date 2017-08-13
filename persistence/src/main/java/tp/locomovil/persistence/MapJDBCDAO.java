package tp.locomovil.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tp.locomovil.inter.MapDAO;
import tp.locomovil.model.SMap;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MapJDBCDAO implements MapDAO {
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;

	@Autowired
	public MapJDBCDAO (DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("maps")
			.usingGeneratedKeyColumns("map_id");
	}

	public SMap createMap (String name) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("name", name);

		final Number mapId = jdbcInsert.executeAndReturnKey(args);

		return new SMap(name, mapId.longValue());
	}
}
