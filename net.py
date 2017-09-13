from keras.models import Sequential
import numpy as np
from keras.layers import Dense, Activation
from keras.optimizers import Adam
import csv
import math


output_index = 3
input_len = 3

def load_data(file_name):
	#it will return an x y numpy array
	x = np.zeros((1,input_len))
	y = np.zeros((1,2))

	with open(file_name,'r+') as csvfile:

		reader = csv.reader(csvfile, delimiter= ',', quotechar = '"')
		for row in reader:
			index = 0
			# just to get the variables
			x_aux = []
			y_aux = []
			for elem in row:
				if(index == 0 ):
					x_aux.append(float(elem)/50)
				elif index == 1 :
					x_aux.append(math.sin(float(elem)))
				elif index == 2:
					x_aux.append(float(elem))
				if index >= output_index:
					y_aux.append(elem)
				index = index + 1

			x = np.append(x,np.array(x_aux).reshape((1,input_len)), axis = 0)
			y =np.append(y,np.array(y_aux).reshape((1,2)), axis = 0 )
			if index > 3000:
				csvfile.close()
				return x,y
				
		csvfile.close()

	return x, y

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


#get data for training 
#X_train, Y_train = load_data('norm_data.data')

#anet.train(X_train, Y_train, epochs = 10000, epsilon = 0.001)

x_train,y_train = load_data('norm_data.data')



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

model.fit(x_train, y_train, epochs=5, batch_size=32)


x ,y= load_data('test_data.data')

print("EXPECTED")
print(y)

print("PREDICTIONS")
print(model.predict(x))




