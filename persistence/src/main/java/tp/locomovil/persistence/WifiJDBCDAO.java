package tp.locomovil.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tp.locomovil.inter.dao.WifiDAO;
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
	private final JdbcTemplate jdbcTemplate;

	private final SimpleJdbcInsert jdbcInsert;

	private final static RowMapper<WifiData> ROW_MAPPER = (rs, rowNum) -> {
		WifiData.WifiDataBuilder builder = new WifiData.WifiDataBuilder();
		builder.bssid(rs.getString("bssid"));
		builder.level(rs.getInt("level"));
		builder.frequency(rs.getInt("frequency"));

		return builder.build();
	};

	@Autowired
	public WifiJDBCDAO (DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("wifi_scans");
	}

	public void saveWifiData(int wifiScanId, WifiData wifi) {
		final Map<String, Object> args = new HashMap<>();
		args.put("wifi_scan_id", wifiScanId);
		args.put("bssid", wifi.getBSSID());
		args.put("level", wifi.getLevel());
		args.put("frequency", wifi.getFrequency());

		jdbcInsert.execute(args);
	}

	public List<WifiData> getWifiScanById(long wifiId) {
		return jdbcTemplate.query("SELECT wifi_scan_id, bssid, level, frequency FROM "
						+ "wifi_scans WHERE wifi_scan_id = ?;", ROW_MAPPER, wifiId);
	}

	public List<WifiData> getWifiScanInCoordinates(Location location, double precision) {
		return jdbcTemplate.query("SELECT wifi_scan_id, bssid, level, frequency "
				+ "FROM wifi_scans WHERE abs(coord_x - ?) < ? AND abs(coord_y - ?) < ?;",
			ROW_MAPPER, location.XCoordinate(), precision, location.YCoordinate(), precision);
	}
}
