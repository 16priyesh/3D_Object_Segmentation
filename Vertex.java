// represents a vertex of a triangle mesh in 3d space

class Vertex{

	private double x; // x coordinate of the vertex
	private double y; // y coordinate of the vertex
	private double z; // z coordinate of the vertex

	Vertex(double[] coordinate){
		// getting input data
		x=coordinate[0];
		y=coordinate[1];
		z=coordinate[2];
	}

	double[] getCoordinates(){
		// returning data
		double[] coordinate=new double[3];
		coordinate[0]=x;
		coordinate[1]=y;
		coordinate[2]=z;
		return coordinate;
	}
}
