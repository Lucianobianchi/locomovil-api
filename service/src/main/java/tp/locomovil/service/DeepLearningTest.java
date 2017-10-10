package tp.locomovil.service;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;

public class DeepLearningTest {

	public static void main (String[] args) {
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.iterations(1)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
				.updater(Updater.ADAM)
				.learningRate(0.001)
				.list()
				.layer(0, (new DenseLayer.Builder()).nIn(3).nOut(64).activation(Activation.TANH).build())
				.layer(1, (new DenseLayer.Builder().nOut(40).activation(Activation.RELU).build()))
				.layer(2, (new DenseLayer.Builder().nOut(60).activation(Activation.RELU).build()))
				.layer(3, (new DenseLayer.Builder().nOut(30).activation(Activation.TANH).build()))
				.layer(4, (new DenseLayer.Builder().nOut(2).activation(Activation.TANH).build()))
				.backprop(true)
				.build();
		MultiLayerNetwork net = new MultiLayerNetwork(conf);

		net.init();
		net.fit();
	}
}

/**
 model = Sequential()
	optimizer = Adam(lr=0.001, beta_1=0.9, beta_2=0.999, epsilon=1e-08, decay=0.01)
	model.add(Dense(units=64, input_dim=3))
	model.add(Activation('tanh'))
	model.add(Dense(units=40))
	model.add(Activation('relu'))
	model.add(Dense(units=60))
	model.add(Activation('relu'))
	model.add(Dense(units=30))
	model.add(Activation('tanh'))
	model.add(Dense(units=2))
	model.add(Activation('tanh'))
	model.compile(loss='mean_squared_error',optimizer= optimizer,
	metrics=['accuracy'])
 **/
