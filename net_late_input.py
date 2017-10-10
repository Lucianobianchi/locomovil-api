from keras.models import Sequential, Model
import numpy as np
from keras.layers import Input, Dense, Activation, concatenate
from keras.optimizers import Adam
import math
import csv

main_input = Input(shape=(2,), dtype="float32", name='main_input')

x = Dense(64, activation='tanh')(main_input)
x =	Dense(64, activation='relu')(x)
aux_output = Dense(2, activation='tanh',name='aux_output')(x)

auxiliary_input = Input(shape=(1,), name='aux_input')
y = concatenate([aux_output, auxiliary_input])

h = Dense(40, activation = 'relu' )(y)
h = Dense(20, activation = 'tanh')(h)
h = Dense(30, activation = 'relu')(h)
main_output = Dense(2, activation = 'tanh', name = 'real_loss')(h)

model = Model([main_input, auxiliary_input],[main_output, aux_output])
optimizer = Adam(lr=0.001, beta_1=0.9, beta_2=0.999, epsilon=1e-08, decay=0.01)
model.compile(optimizer=optimizer, loss= 'mse', loss_weights = [1., 0.2])


output_index = 3
input_len = 3

def load_data(file_name):
	#it will return an x y numpy array
	x = np.zeros((1,input_len-1))
	y = np.zeros((1,2))
	aux_in = np.zeros((1,1))
	with open(file_name,'r+') as csvfile:

		reader = csv.reader(csvfile, delimiter= ',', quotechar = '"')
		for row in reader:
			index = 0
			# just to get the variables
			x_aux = []
			y_aux = []
			aux = []
			for elem in row:
				if(index == 0 ):
					x_aux.append(float(elem)/50)
				elif index == 1 :
					#x_aux.append(math.sin(float(elem)))
					aux.append(math.sin(float(elem)))
				elif index == 2:
					x_aux.append(float(elem))
				if index >= output_index:
					y_aux.append(elem)
				index = index + 1

			x = np.append(x,np.array(x_aux).reshape((1,input_len-1)), axis = 0)
			y =np.append(y,np.array(y_aux).reshape((1,2)), axis = 0 )
			aux_in = np.append(aux_in, np.array(aux).reshape((1,1)), axis = 0)
			if index > 3000:
				csvfile.close()
				return x,y

		csvfile.close()

	return x, y, aux_in

def load_train(file_name):
	x = np.zeros((1,input_len))

	with open(file_name,'r+') as csvfile:
		reader = csv.reader(csvfile, delimiter= ',', quotechar = '"')
		for row in reader:

			index = 0
			# just to get the variables
			x_aux = []
			for elem in row:
				if(index == 0 ):
					x_aux.append(float(elem)/50)
				elif index == 1 :
					x_aux.append(math.sin(float(elem)))
				elif index == 2:
					x_aux.append(float(elem))
				index = index + 1

			x = np.append(x,np.array(x_aux).reshape((1,input_len)), axis = 0)


		csvfile.close()

	return x

x,y,aux =load_data('test_data.data')

model.fit([x,aux],[y,y],epochs=5, batch_size=32)

guess, non, aux = load_data('test_data.data')
print(model.predict([guess,aux]))


print(" expected")
print(y)
i = 0
j = 0
# for x in model.predict([guess,aux]):
# 	print(x - y[i])
# 	i = i + 1
# 	j = j + 1
