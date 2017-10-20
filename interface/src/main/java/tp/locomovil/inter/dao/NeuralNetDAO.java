package tp.locomovil.inter.dao;

import tp.locomovil.model.WifiData;
import tp.locomovil.model.WifiNeuralNet;

import java.util.List;

public interface NeuralNetDAO {

	WifiNeuralNet getNetworkForAPs(List<WifiData> APs);
}
