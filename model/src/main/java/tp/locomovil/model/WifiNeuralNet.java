package tp.locomovil.model;


import org.deeplearning4j.nn.api.NeuralNetwork;

public class WifiNeuralNet {
	private String projectName;
	private String mapName;
	private NeuralNetwork network;

	public WifiNeuralNet (String projectName, String mapName, NeuralNetwork network) {
		this.projectName = projectName;
		this.mapName = mapName;
		this.network = network;
	}

	public String getProjectName () {
		return projectName;
	}

	public String getMapName () {
		return mapName;
	}

	public NeuralNetwork getNetwork () {
		return network;
	}
}
