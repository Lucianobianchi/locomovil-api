import csv
import numpy as np
from sklearn.preprocessing import normalize
class Data_reader(object):
	"""self class for research purposes for ITBA"""
	def __init__(self, path):
		super(Data_reader, self).__init__()
		#sets the path for the specified file
		self.path = path
		self.file = open(self.path,'r+')


	def get_data(self, separator = ',', quotechar = '|',normalize = False , 
				num_output = 1):
		#based on the first row size, it will get the data
		file = open(self.path, 'r+')
		reader = csv.reader(file , delimiter = separator, quotechar = quotechar)
		first_row = (next(reader))
		size = len(first_row)
		#this will get all values up until a certain index
		input_matrix = np.array(first_row[:-num_output])
		#the the output
		input_size = size-num_output
		output_matrix = np.array(first_row[input_size:])
		input_matrix = np.reshape(input_matrix, (1,input_size))
		output_matrix = np.reshape(output_matrix,(1,num_output))
		
		for row in reader:
			inp = np.array(row[:-num_output])
			out = np.array(row[input_size:])
			input_matrix = np.append(input_matrix, np.reshape(inp, (1,input_size)) , axis= 0)
			output_matrix = np.append(output_matrix, np.reshape(out,(1,num_output)), axis= 0)

		if normalize:
			return self.normalize(input_matrix), self.normalize(output_matrix)
		else:
			return input_matrix, output_matrix


	def normalize(self,array):
		return normalize(array, axis = 0, norm= 'max')

	def get_special_data(self, separator = ',', quotechar = '|',normalize = False , 
				num_output = 1, *indexs):

		pass
		



if __name__ == '__main__':
	test = Data_reader("test_data.data")
	print(test.get_data(num_output = 2))
	print(test.get_data(num_output = 2, normalize =True))
