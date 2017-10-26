package tp.locomovil.model;


import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WifiNeuralNet {
	private final String projectName;
	private final String mapName;
	private final MultiLayerNetwork network;

	// TODO: Al archivo de configuracion
	private static final int SEED = 123;
	private static final int MAX_COINCIDENCES = 5;
	private static final int STRONGEST_AP_NUMBER = 5;
	private static final int inHidden1 = 50;

	// Lower level is stronger so this sorts signals from strongest to weakest
	private static final Comparator<WifiData> LEVEL_SORT = (o1, o2) -> o1.getLevel() - o2.getLevel();

	private WifiNeuralNet (String projectName, String mapName, MultiLayerNetwork net) {
		this.projectName = projectName;
		this.mapName = mapName;
		this.network = net;
	}

	public static WifiNeuralNet fromBytes(String projectName, String mapName, byte[] data) {
		try {
			MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(new ByteArrayInputStream(data));
			return new WifiNeuralNet(projectName, mapName, net);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static WifiNeuralNet newNet(String projectName, String mapName) {
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.seed(SEED)
				.iterations(1)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
				.learningRate(0.3)
				.updater(Updater.NESTEROVS)
				.list()
				.layer(0, new DenseLayer.Builder()
						.nIn(STRONGEST_AP_NUMBER)
						.nOut(inHidden1)
						.weightInit(WeightInit.DISTRIBUTION)
						.activation(Activation.RELU)
						.build())
				.layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
						.nIn(inHidden1)
						.nOut(2)
						.weightInit(WeightInit.XAVIER)
						.activation(Activation.SOFTMAX)
						.build())
				.pretrain(false).backprop(true).build();

		System.out.println(conf);

		MultiLayerNetwork net = new MultiLayerNetwork(conf);
		return new WifiNeuralNet(projectName, mapName, net);
	}

	public String getProjectName () {
		return projectName;
	}

	public String getMapName () {
		return mapName;
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
	}

	public Location getLocationForWifis(List<WifiData> wifis) {
		INDArray out = network.output(createNetInputs(wifis));
		double x = out.getDouble(0), y = out.getDouble(1); // TODO: poner en "tamaño real"
		// O se puede cambiar el contrato y devolver valores de 0 a 1, que despues se encargue otro de
		// traducirlo a valor real
		return new Location(projectName, mapName, "TODO - PLACEHOLDER", x, y, 10.50);
	}

	public byte[] getBytes() {
		ByteArrayOutputStream os = new ByteArrayOutputStream(16000); // TODO: size conf
		try {
			ModelSerializer.writeModel(network, os, true); // TODO: que es true?
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return os.toByteArray();
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
