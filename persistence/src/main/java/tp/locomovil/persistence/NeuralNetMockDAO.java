package tp.locomovil.persistence;

import tp.locomovil.inter.dao.NeuralNetDAO;
import tp.locomovil.model.WifiData;
import tp.locomovil.model.WifiNeuralNet;

import java.util.List;

public class NeuralNetMockDAO implements NeuralNetDAO {


	@Override
	public WifiNeuralNet getNetworkForAPs (List<WifiData> APs) {
		return null;
	}

	@Override
	public WifiNeuralNet createNetworkForAPs (int projectId, int mapId, List<WifiData> APs) {
		return null;
	}
}
