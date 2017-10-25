package tp.locomovil.model;


import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WifiNeuralNet {
	private String projectName;
	private String mapName;
	private String saveFile;
	private MultiLayerNetwork network;

	// TODO: Al archivo de configuracion
	private static final int MAX_COINCIDENCES = 5;
	private static final int STRONGEST_AP_NUMBER = 5;

	// Lower level is stronger so this sorts signals from strongest to weakest
	private static final Comparator<WifiData> LEVEL_SORT = (o1, o2) -> o1.getLevel() - o2.getLevel();

	public WifiNeuralNet (String projectName, String mapName, String saveFile) throws IOException {
		this.projectName = projectName;
		this.mapName = mapName;
		this.saveFile = saveFile;
		this.network = ModelSerializer.restoreMultiLayerNetwork(saveFile);
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

	public String getSaveFile () {
		return saveFile;
	}

	public void train(List<Scan> scans) {
		for (Scan s: scans) {
			double x = s.getUserCoordX(), y = s.getUserCoordY();
			List<WifiData> wifis = s.getWifis().stream()
					.sorted(LEVEL_SORT).limit(STRONGEST_AP_NUMBER).collect(Collectors.toList());
			DataSet ds = new DataSet();
			ds.setFeatures(createNetInputs(wifis));
			ds.setLabels(createNetExpectedOutputs(x, y));
		}

		try {
			ModelSerializer.writeModel(network, saveFile, true);
		} catch (IOException e) {
			// TODO: hacer algo si no se pudo guardar el entrenamiento
			e.printStackTrace();
		}
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

	private static INDArray createNetExpectedOutputs (double x, double y) {
		return Nd4j.create(new double[]{x, y});
	}
}
