package tp.locomovil.persistence;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tp.locomovil.inter.dao.MapDAO;
import tp.locomovil.inter.dao.NeuralNetDAO;
import tp.locomovil.inter.dao.ProjectDAO;
import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;
import tp.locomovil.model.WifiData;
import tp.locomovil.model.WifiNeuralNet;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class LocationNetsDAO implements NeuralNetDAO {

	private final NamedParameterJdbcTemplate namedJdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;

	@Autowired
	private MapDAO mapDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	public LocationNetsDAO (DataSource ds) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("neural_nets")
			.usingGeneratedKeyColumns("network_id");
	}

	private final static RowMapper<WifiNeuralNet> ROW_MAPPER = (RowMapper<WifiNeuralNet>) (rs, rowNum) -> {
		try {
			String fileStr = rs.getString("neural_nets.network_file");
			MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(fileStr);
			return new WifiNeuralNet(rs.getString("projects.name"), rs.getString("maps.name"), fileStr);
		} catch (IOException e) {
			e.printStackTrace(); // TODO
			return null;
		}
	};

	@Override
	public WifiNeuralNet getNetworkForAPs (List<WifiData> APs) {
		List<String> apBSSID = APs.stream().map(WifiData::getBSSID).collect(Collectors.toList());

		Map<String, List> paramMap = new HashMap<>();
		paramMap.put("aps", apBSSID);

		// FIXME: feo
		final List<WifiNeuralNet> result = namedJdbcTemplate.query("SELECT * FROM "
				+ "neural_nets NATURAL JOIN maps NATURAL JOIN projects "
				+ "GROUP BY NETWORK_ID HAVING BSSID IN (:aps)", paramMap, ROW_MAPPER);

		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public WifiNeuralNet createNetworkForAPs (final int projectId, final int mapId, List<WifiData> APs) {
		Project project = projectDAO.getProjectById(projectId);
		SMap map = mapDAO.getMapById(projectId, mapId);
		if (map == null)
			throw new IllegalArgumentException(); // TODO: Hacer bien

		try {
			String saveFile = buildSaveFile(projectId, mapId);
			WifiNeuralNet net =
					new WifiNeuralNet(project.getName(), map.getMapName(), saveFile);

			for (WifiData wifi: APs) {
				Map<String, Object> args = new HashMap<>();
				args.put("map_id", mapId);
				args.put("bssid", wifi.getBSSID());
				args.put("network_file", saveFile);
				jdbcInsert.execute(args);
			}

			return net;
		} catch (IOException e) {
			e.printStackTrace(); // TODO no se pudo crear
			return null;
		}

	}

	private String buildSaveFile(int projectId, int mapId) {
		return "/neuralNets/" + projectId + "/" + mapId;
	}

}
