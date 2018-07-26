
class Triangle{

	//index of the three vertices in the vertex list

	private int a; 
	private int b;
	private int c;

	Triangle(int[] points){
		a = points[0];
		b = points[1];
		c = points[2];
	}

	int[] getVertices(){

		//returns an array of integers containing indices of vertices

		int[] points = new int[3];
		points[0] = a;
		points[1] = b;
		points[2] = c;
		return points;
	}
}