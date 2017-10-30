package tp.locomovil.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tp.locomovil.inter.dao.MapDAO;
import tp.locomovil.inter.dao.NeuralNetDAO;
import tp.locomovil.inter.dao.ProjectDAO;
import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;
import tp.locomovil.model.WifiData;
import tp.locomovil.model.WifiNeuralNet;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class LocationNetsDAO implements NeuralNetDAO {

	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate namedJdbcTemplate;
	private final SimpleJdbcInsert netBSSIDInsert;
	private final SimpleJdbcInsert netDataInsert;

	@Autowired
	private MapDAO mapDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	public LocationNetsDAO (DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);
		netDataInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("nets_data")
				.usingGeneratedKeyColumns("network_id");
		netBSSIDInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("neural_nets");
	}

	private final static RowMapper<WifiNeuralNet> ROW_MAPPER = (RowMapper<WifiNeuralNet>) (rs, rowNum) -> {
		byte[] data = rs.getBytes("network_data");
		return WifiNeuralNet.fromBytes(rs.getString("projects.name"), rs.getString("maps.name"), data);
	};

	@Override
	public WifiNeuralNet getNetworkForAPs (List<WifiData> APs) {
		List<String> apBSSID = APs.stream().map(WifiData::getBSSID).collect(Collectors.toList());

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("aps", apBSSID);
		paramMap.put("ap_count", apBSSID.size());

		final List<WifiNeuralNet> result = namedJdbcTemplate.query(
				"SELECT * FROM projects JOIN maps ON projects.project_id = maps.project_id "
				+ "NATURAL JOIN (select * from nets_data NATURAL JOIN (SELECT NETWORK_ID FROM "
				+ "neural_nets WHERE bssid IN (:aps) "
				+ "GROUP BY network_id HAVING COUNT(*) = :ap_count) net_id) nets"
				, paramMap, ROW_MAPPER);


		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public WifiNeuralNet createNetworkForAPs (final int projectId, final int mapId, List<WifiData> APs) {
		// TODO: chequear project y map existentes
		Project project = projectDAO.getProjectById(projectId);
		SMap map = mapDAO.getMapById(projectId, mapId);

		WifiNeuralNet net = WifiNeuralNet.newNet(project.getName(), map.getMapName());

		Map<String, Object> bytesArgs = new HashMap<>();
		bytesArgs.put("map_id", mapId);
		bytesArgs.put("network_data", net.getBytes());
		long id = (long) netDataInsert.executeAndReturnKey(bytesArgs);

		for (WifiData ap: APs) {
			Map<String, Object> args = new HashMap<>();
			args.put("bssid", ap.getBSSID());
			args.put("network_id", id);
			netBSSIDInsert.execute(args);
		}

		return net;
	}

	@Override
	public WifiNeuralNet updateNetworkWithId (int id, WifiNeuralNet net) {
		jdbcTemplate.update("update nets_data set network_data = ? where network_id = ?",
				net.getBytes(), id);
		return getNetworkWithId(id); // o retornar net? Debería ser lo mismo
	}

	private WifiNeuralNet getNetworkWithId (int id) {
		final List<WifiNeuralNet> result = jdbcTemplate.query("SELECT * FROM "
				+ "nets_data NATURAL JOIN maps NATURAL JOIN projects "
				+ "WHERE network_id = ?", ROW_MAPPER, id);

		return result.isEmpty() ? null : result.get(0);
	}
}
