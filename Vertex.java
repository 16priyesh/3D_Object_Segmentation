// represents a vertex of a triangle mesh in 3d space

class Vertex{
	
	private double x; // x coordinate of the vertex
	private double y; // y coordinate of the vertex
	private double z; // z coordinate of the vertex

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + (int) x*10000;
		hash = hash * 31 + (int) y*10000;
		hash = hash * 31 + (int) z*10000;
		return hash;
	}

	// Overriding equals method to compare two Vertices
	@Override
    public boolean equals(Object o) {
 
        // If the object is compared with itself then return true  
        if (o == this) {
            return true;
        }
 
        /* Check if o is an instance of Vertex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Vertex)) {
            return false;
        }
         
        // typecast o to Complex so that we can compare data members 
        Vertex c = (Vertex) o;
         
        // Compare the data members and return accordingly 
        return Double.compare(x, c.x) == 0
                && Double.compare(y, c.y) == 0
                && Double.compare(z, c.z) == 0;
    }

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