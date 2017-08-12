package tp.locomovil.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tp.locomovil.inter.WifiDAO;
import tp.locomovil.model.Location;
import tp.locomovil.model.WifiData;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WifiJDBCDAO implements WifiDAO {
	private JdbcTemplate jdbcTemplate;

	private final SimpleJdbcInsert jdbcInsert;

	private final static RowMapper<WifiData> ROW_MAPPER = new RowMapper<WifiData>() {
		public WifiData mapRow (ResultSet rs, int rowNum) throws SQLException {
			WifiData.WifiDataBuilder builder = new WifiData.WifiDataBuilder();
			builder.bssid(rs.getString("bssid"));
			builder.level(rs.getInt("level"));
			builder.frequency(rs.getInt("frequency"));

			return builder.build();
		}
	};

	@Autowired
	public WifiJDBCDAO (DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("wifi_scans");
	}

	// TODO los valores de retorno de estas cosas no estan muy buenos
	public void saveWifiData(int wifiScanId, WifiData wifi) {
		final Map<String, Object> args = new HashMap<String, Object>();
		args.put("wifi_scan_id", wifiScanId);
		args.put("bssid", wifi.getBSSID());
		args.put("level", wifi.getLevel());
		args.put("frequency", wifi.getFrequency());

		jdbcInsert.execute(args);
	}

	public List<WifiData> getWifiScanInCoordinates(Location location, double precision) {
		List<WifiData> wifis = jdbcTemplate.query("SELECT wifi_scan_id, bssid, level, frequency FROM "
			+ "scans NATURAL JOIN wifi_scans WHERE abs(coord_x - ?) < ? AND abs(coord_y - ?) < ?;",
			ROW_MAPPER, location.XCoordinate(), precision, location.YCoordinate(), precision);

		return wifis;
	}
}
