package tp.locomovil.model;


import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;

public class WifiNeuralNet {
	private String projectName;
	private String mapName;
	private MultiLayerNetwork network;

	public WifiNeuralNet (String projectName, String mapName, MultiLayerNetwork network) {
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

	public MultiLayerNetwork getNetwork () {
		return network;
	}

	public Location getLocationForWifis(List<WifiData> wifis) {
		INDArray out = network.output(createNetInputs(wifis));
		double x = out.getDouble(0), y = out.getDouble(1); // TODO: poner en "tamaño real"
		// O se puede cambiar el contrato y devolver valores de 0 a 1, que despues se encargue otro de
		// traducirlo a valor real
		return new Location(projectName, mapName, "TODO - PLACEHOLDER", x, y, 10.50);
	}

	private static INDArray createNetInputs (List<WifiData> APs) {
		double features[] = new double[APs.size()];
		for (int i = 0; i < APs.size(); i++)
			features[i] = APs.get(0).getLevel(); // TODO: normalizar y eso
		return Nd4j.create(features);
	}
}
