// ECS629/759 Assignment 2 - ID3 Skeleton Code
// Author: Simon Dixon

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

class ID3 {

	/** Each node of the tree contains either the attribute number (for non-leaf
	 *  nodes) or class number (for leaf nodes) in <b>value</b>, and an array of
	 *  tree nodes in <b>children</b> containing each of the children of the
	 *  node (for non-leaf nodes).
	 *  The attribute number corresponds to the column number in the training
	 *  and test files. The children are ordered in the same order as the
	 *  Strings in strings[][]. E.g., if value == 3, then the array of
	 *  children correspond to the branches for attribute 3 (named data[0][3]):
	 *      children[0] is the branch for attribute 3 == strings[3][0]
	 *      children[1] is the branch for attribute 3 == strings[3][1]
	 *      children[2] is the branch for attribute 3 == strings[3][2]
	 *      etc.
	 *  The class number (leaf nodes) also corresponds to the order of classes
	 *  in strings[][]. For example, a leaf with value == 3 corresponds
	 *  to the class label strings[attributes-1][3].
	 **/
	class TreeNode {

		TreeNode[] children;
		int value;

		public TreeNode(TreeNode[] ch, int val) {
			value = val;
			children = ch;
		} // constructor

		public String toString() {
			return toString("");
		} // toString()
		
		String toString(String indent) {
			if (children != null) {
				String s = "";
				for (int i = 0; i < children.length; i++)
					s += indent + data[0][value] + "=" +
							strings[value][i] + "\n" +
							children[i].toString(indent + '\t');
				return s;
			} else
				return indent + "Class: " + strings[attributes-1][value] + "\n";
		} // toString(String)

	} // inner class TreeNode

	private int attributes; 	// Number of attributes (including the class)
	private int examples;		// Number of training examples
	private TreeNode decisionTree;	// Tree learnt in training, used for classifying
	private String[][] data;	// Training data indexed by example, attribute
	private String[][] strings; // Unique strings for each attribute
	private int[] stringCount;  // Number of unique strings for each attribute

	public ID3() {
		attributes = 0;
		examples = 0;
		decisionTree = null;
		data = null;
		strings = null;
		stringCount = null;
	} // constructor
	
	public void printTree() {
		if (decisionTree == null)
			error("Attempted to print null Tree");
		else
			System.out.println(decisionTree);
	} // printTree()

	/** Print error message and exit. **/
	static void error(String msg) {
		System.err.println("Error: " + msg);
		System.exit(1);
	} // error()

	static final double LOG2 = Math.log(2.0);

	static double xlogx(double x) {
		return x == 0? 0: x * Math.log(x) / LOG2;
	} // xlogx()

	/** Execute the decision tree on the given examples in testData, and print
	 *  the resulting class names, one to a line, for each example in testData.
	 **/
	public void classify(String[][] testData) {
		if (decisionTree == null)
			error("Please run training phase before classification");
		// PUT  YOUR CODE HERE FOR CLASSIFICATION

	} // classify()

	public void train(String[][] trainingData) {
		indexStrings(trainingData);
		// PUT  YOUR CODE HERE FOR TRAINING

		// init the tree
		decisionTree = new TreeNode(null, 0);

		// calculate the class Entropy first
		double class_totalEntropy = cal_Entropy_total(trainingData);
		System.out.println(class_totalEntropy);
		// reason attributes-1: already cal the last value
		double[] arr_totalEntropy = new double[attributes-1];
		for (int i = 0; i < attributes-1; i++) {
			// double[] instanceCount = new double[stringCount[i]];
			// double[] subSetEntropy = new double[stringCount[i]];
			arr_totalEntropy[i] = class_totalEntropy - cal_Entropy_arr(trainingData,i);

		}

		int select_Index = getIndexOfLargest(arr_totalEntropy);

		decisionTree.value = select_Index;
		decisionTree.children = new TreeNode[stringCount[select_Index]];
		int[] checked_rows = new int[attributes];
		for (int i = 0;i < attributes;i++){
			checked_rows[i] = 0;
		}
		checked_rows[select_Index] = 1;

		for (int i = 0; i < stringCount[select_Index]; i++) {
			decisionTree.children[i] = new TreeNode(null, 0);


			String[][] each_sub = creat_sub(trainingData,select_Index,strings[select_Index][i]);

			growTree(decisionTree.children[i], each_sub,checked_rows, class_totalEntropy);

		}

		//

	} // train()


	void growTree(TreeNode node, String[][] trainingData,int[] checked_rows,double class_totalEntropy){
		// System.out.println("2");
		double[] arr_totalEntropy = new double[attributes-1];
		for (int i = 0; i < attributes-1; i++) {
			// double[] instanceCount = new double[stringCount[i]];
			// double[] subSetEntropy = new double[stringCount[i]];
			// System.out.println(attributes-1);
			arr_totalEntropy[i] = class_totalEntropy - cal_Entropy_arr(trainingData,i);

		}

	}

	String[][] creat_sub(String[][] trainingData,int value_i,String value){
		// String value = strings[value_i][value_j];
		int counter = 0;
		for (int i = 1; i < trainingData.length; i++) {
			if (trainingData[i][value_i].equals(value)) {
				counter++;
			}
		}
		int rowCount = 1;
		String[][] subSet = new String[counter+1][trainingData[0].length-1];
		subSet[0] = trainingData[0];
		for (int i = 1; i < subSet.length; i++) {
			if (trainingData[i][value_i].equals(value)) {
				subSet[rowCount] = trainingData[i];
				rowCount++;
			}
		}
		return subSet;

	}



	public double cal_Entropy_total(String[][] trainingData){
		double rows = trainingData.length-1;
		double[] count = new double[stringCount[attributes-1]];
		for (int i = 0; i < stringCount[attributes-1]; i++) {
			count[i] = 0;

			for (int j = 1; j < trainingData.length; j++) {
				if(trainingData[j][attributes-1].equals(strings[attributes-1][i])){
					count[i]++;
				}
			}
		}
		double entropy = 0;
		for (int i = 0; i < count.length; i++) {
			entropy -= xlogx(count[i]/rows);
			//System.out.println(count[i]);
		}
		// double a = 10/14;
		// System.out.println(xlogx(a) + xlogx(4/14) );

		return Math.abs(entropy);
	}



	double cal_Entropy_arr(String[][] trainingData, int index){
		double rows = trainingData.length-1;
		int target_index = index;
		double arr_entropy_IG = 0;
		for (int i = 0; i < stringCount[target_index]; i++) {
			// sub arr is

			double[] arr_class_count = new double[stringCount[trainingData[0].length-1]];
			double total_arr_count = 0;
			String value = strings[target_index][i];
			for (int j = 1; j < trainingData.length; j++) {
				for (int k = 0;k< stringCount[trainingData[0].length-1];k++){
					int the_last_training_col = trainingData[0].length-1;
					System.out.println("the_last_training_col     :   "+the_last_training_col);
					System.out.println("j     :   "+j);
					System.out.println("k     :   "+k);
					System.out.println("target_index     :   "+target_index);

					if (trainingData[j][the_last_training_col].equals(strings[the_last_training_col][k])&& trainingData[j][target_index].equals(value)) {
						arr_class_count[k]++;
						total_arr_count++;
					}
				}

			}

			double arr_entropy = 0;
			for (int j = 0; j < arr_class_count.length; j++) {
				arr_entropy -= (xlogx(arr_class_count[j]/total_arr_count));
			}
			arr_entropy = Math.abs(arr_entropy);

			arr_entropy_IG += total_arr_count/rows * arr_entropy;


		}
		// System.out.println(arr_entropy_IG);
		return arr_entropy_IG;
	}



	int getIndexOfLargest(double[] array)
	{
		if ( array == null || array.length == 0 ) return -1; // null or empty

		int largest = 0;
		for ( int i = 1; i < array.length; i++ )
		{
			if ( array[i] > array[largest] ) largest = i;
		}
		return largest; // position of the first largest found
	}

	/** Given a 2-dimensional array containing the training data, numbers each
	 *  unique value that each attribute has, and stores these Strings in
	 *  instance variables; for example, for attribute 2, its first value
	 *  would be stored in strings[2][0], its second value in strings[2][1],
	 *  and so on; and the number of different values in stringCount[2].
	 **/
	void indexStrings(String[][] inputData) {
		data = inputData;
		examples = data.length;
		attributes = data[0].length;
		stringCount = new int[attributes];
		strings = new String[attributes][examples];// might not need all columns
		int index = 0;
		for (int attr = 0; attr < attributes; attr++) {
			stringCount[attr] = 0;
			for (int ex = 1; ex < examples; ex++) {
				for (index = 0; index < stringCount[attr]; index++)
					if (data[ex][attr].equals(strings[attr][index]))
						break;	// we've seen this String before
				if (index == stringCount[attr])		// if new String found
					strings[attr][stringCount[attr]++] = data[ex][attr];
			} // for each example
		} // for each attribute
		// System.out.print(stringCount);
	} // indexStrings()

	/** For debugging: prints the list of attribute values for each attribute
	 *  and their index values.
	 **/
	void printStrings() {
		for (int attr = 0; attr < attributes; attr++)
			for (int index = 0; index < stringCount[attr]; index++)
				System.out.println(data[0][attr] + " value " + index +
									" = " + strings[attr][index]);
	} // printStrings()
		
	/** Reads a text file containing a fixed number of comma-separated values
	 *  on each line, and returns a two dimensional array of these values,
	 *  indexed by line number and position in line.
	 **/
	static String[][] parseCSV(String fileName)
								throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String s = br.readLine();
		int fields = 1;
		int index = 0;
		while ((index = s.indexOf(',', index) + 1) > 0)
			fields++;
		int lines = 1;
		while (br.readLine() != null)
			lines++;
		br.close();
		String[][] data = new String[lines][fields];
		Scanner sc = new Scanner(new File(fileName));
		sc.useDelimiter("[,\n]");
		for (int l = 0; l < lines; l++)
			for (int f = 0; f < fields; f++)
				if (sc.hasNext())
					data[l][f] = sc.next();
				else
					error("Scan error in " + fileName + " at " + l + ":" + f);
		sc.close();
		return data;
	} // parseCSV()

	public static void main(String[] args) throws FileNotFoundException,
												  IOException {
		if (args.length != 2)
			error("Expected 2 arguments: file names of training and test data");
		String[][] trainingData = parseCSV(args[0]);
		String[][] testData = parseCSV(args[1]);

		ID3 classifier = new ID3();
		classifier.train(trainingData);

		classifier.printTree();
		classifier.classify(testData);
	} // main()

} // class ID3
