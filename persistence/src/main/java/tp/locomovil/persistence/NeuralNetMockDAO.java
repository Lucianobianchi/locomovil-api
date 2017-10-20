package tp.locomovil.persistence;

import org.deeplearning4j.nn.api.NeuralNetwork;
import tp.locomovil.inter.dao.NeuralNetDAO;
import tp.locomovil.model.WifiData;

import java.util.List;

public class NeuralNetMockDAO implements NeuralNetDAO {
	@Override
	public NeuralNetwork getNetworkForAPs (List<WifiData> APs) {
		return null;
	}
}
