package tp.locomovil.persistence;

import org.deeplearning4j.nn.api.NeuralNetwork;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tp.locomovil.inter.dao.NeuralNetDAO;
import tp.locomovil.model.WifiData;
import tp.locomovil.model.WifiNeuralNet;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class LocationNetsDAO implements NeuralNetDAO {

	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate namedJdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;

	@Autowired
	public LocationNetsDAO (DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
	}

	private final static RowMapper<WifiNeuralNet> ROW_MAPPER = (RowMapper<WifiNeuralNet>) (rs, rowNum) -> {
		try {
			MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(rs.getString("neural_nets.network_file"));
			return new WifiNeuralNet(rs.getString("projects.name"), rs.getString("maps.name"), net);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	};

	@Override
	public WifiNeuralNet getNetworkForAPs (List<WifiData> APs) {
		List<String> apBSSID = APs.stream().map(WifiData::getBSSID).collect(Collectors.toList());

		Map<String, List> paramMap = new HashMap<>();
		paramMap.put("aps", apBSSID);

		final List<WifiNeuralNet> result = namedJdbcTemplate.query("SELECT * FROM neural_nets_bssids  "
				+ "NATURAL JOIN neural_nets NATURAL JOIN maps NATURAL JOIN projects "
				+ "GROUP BY NETWORK_ID HAVING BSSID IN (:aps)", paramMap, ROW_MAPPER);

		return result.isEmpty() ? null : result.get(0);
	}
}
