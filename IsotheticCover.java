
//here we will make an approximate outer 3d Isothetic Cover of a given 3d object

import java.util.ArrayList;
import java.io.File;
import java.lang.Math;

class IsotheticCover{

	ArrayList<Vertex> vertices; //arraylist of vertices
	ArrayList<Triangle> faces; //arraylist of faces
	Ugc[][][] grid; //continuous grid of ugcs' containing the object
	int lx, ly, lz; //size of 3d grid array
	int [] base; //starting coordinates for ugc grid
	private double g; //scale of grid

	IsotheticCover(double length, File f) throws Exception{
		g = length;
		ObjectReader or = new ObjectReader();
		vertices = or.readVertexData(f);
		faces = or.readFaceData(f);
		initializeGrid();
		make3dIsotheticCover();
	}

	void initializeGrid(){

		//here we initialize the grid parameters

		double[] range = new double[6]; //array to store all minimum and maximum coordinates
		double[] first = vertices.get(1).getCoordinates();
		for(int i=0; i<3; i++){ //initializing the min and max values to coordinates of first vertex
			range[2*i] = first[i];
			range[2*i+1] = first[i];
		}
		int l = vertices.size();
		for(int i=2; i<l; i++){ //calculating the min and max coordinates
			double[] coordinates = vertices.get(i).getCoordinates();
			int axis = 0;
			for(double d : coordinates){
				range[axis]=(d<range[axis])?d:range[axis];
				axis++;
				range[axis]=(d>range[axis])?d:range[axis];
				axis++;
			}
		}

		//setting the size of the array to cover all the coordinates from min to max
		lx = (int) Math.floor((range[1]-range[0])/g)+3;
		ly = (int) Math.floor((range[3]-range[2])/g)+3;
		lz = (int) Math.floor((range[5]-range[4])/g)+3;
		grid = new Ugc[lx][ly][lz];
		for(int i=0; i<lx; i++){
			for(int j=0; j<ly; j++){
				for(int k=0; k<lz; k++) grid[i][j][k] = new Ugc();
			}
		}

		//setting the base coordinates as the minimum coordinates so as to use it for indexing
		base = new int[3];
		for(int i=0; i<3; i++) {
			double checkInteger = range[2*i]/g;
			if(checkInteger%1==0) checkInteger--;
			base[i] = (int) Math.floor(checkInteger);
		}
	}

	private void make3dIsotheticCover(){

		//now making the outer 3d isothetic cover
		/*
			for each triangular face, we will look at all the ugcs' intersected by the triangle and 
			store the index of those ugcs' in a arraylist temp while adding the index of this triangle
			to set T of all these ugcs'. Now all the ugcs' in the temp are added to each other's list U.
			This completes all the enteries due to a particular face. While adding these information 
			we ensure that no duplicate enteries are made in any set. 
			Ti - list of triangles(ti) intesecting the ith ugc. (Topological space X)
			Ui - list of ugcs'(ui) intersected by triangles in Ti. (Topological space W)
		*/

		int size = faces.size();
		for(int t=1; t<size; t++){
			ArrayList<UgcIndex> temp = new ArrayList<UgcIndex>();
			addTraingle(t, temp); //adds all triagles to corresponding Ti and stores ui in temp
			for(UgcIndex u : temp) { //now adding all the ugcs' in temp to each others set U
				for(UgcIndex v : temp){
					boolean flag = true;
					for(UgcIndex c : grid[u.x][u.y][u.z].U) //check for duplicacy
						if(v.x==c.x&&v.y==c.y&&v.z==c.z) flag = false;
					if(flag) grid[u.x][u.y][u.z].U.add(v);
				}
			}
		}
	}

	private void swap(double[][] v, int j, int k, int i){

		//swaps ith element of two arrays a and b

		double temp = v[j][i];
		v[j][i]=v[k][i];
		v[k][i]=temp;
	}

	private UgcIndex getUgc(double[][] coordinates, int v, boolean flag){

		/*checks which ugc does the coordinates belongs to and returns a bounding ugc's index*/
		int p = (int) Math.floor((coordinates[v][0]/g-base[0]));
		int q = (int) Math.floor((coordinates[v][1]/g-base[1]));
		int r = (int) Math.floor((coordinates[v][2]/g-base[2]));
		if(flag){
			if(coordinates[v][0]%1==0) p--;
			if(coordinates[v][1]%1==0) q--;
			if(coordinates[v][2]%1==0) r--;
		}
		else{
			p++; q++; r++;
		}
		return new UgcIndex(p,q,r);
	}

	private void addTraingle(int t, ArrayList<UgcIndex> temp){

		//this function adds the triangle t to set Ti of all Ugcs' ui which it intersects and 
		//maintains the list of those ugcs in temp
		
		int[] indexOfVertices = faces.get(t).getVertices();
		double[][] vert = new double[3][3];
		for(int i=0; i<3; i++){
			double[] coor = vertices.get(indexOfVertices[i]).getCoordinates();
			for(int j=0; j<3; j++){
				vert[i][j]=coor[j];
			}
		}

		double[][] v = new double[3][3];
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				v[i][j] = vert[i][j];
			}
		}

		for(int i=0; i<3; i++){
			if(v[0][i]>v[1][i]) swap(v,0,1,i);
			if(v[0][i]>v[2][i]) swap(v,0,2,i);
			if(v[1][i]>v[2][i]) swap(v,1,2,i);
		}

		//bounding box indices

		UgcIndex starting = getUgc(v,0,true);
		UgcIndex ending = getUgc(v,2,false);

		//checking overlappings and if it exists then adds the triangle to that ugc
		//f not added yet and then adding that ugc to temp

		/*
		for SAT voxelization based
		OverlapTester test = new OverlapTester();
		double[] halfSize = {g/2,g/2,g/2};
		for(int i=starting.x; i<=ending.x; i++){
			for(int j=starting.y; j<=ending.y; j++){
				for(int k=starting.z; k<=ending.z; k++){
					double[] center = new double[3];
					center[0] = g*(i+base[0]+0.5);
					center[1] = g*(j+base[1]+0.5);
					center[2] = g*(k+base[2]+0.5);
					boolean overlap = test.triBoxOverlap(center, halfSize, vert);
					if(overlap){
						boolean flag = false;                                                                                                                                  //checking for duplicacy
						if(grid[i][j][k].T!=null) for(int x:grid[i][j][k].T) if(x==t) flag=true;
						if(!flag) {
							grid[i][j][k].T.add(t);
							temp.add(new UgcIndex(i,j,k));
						}
					}
				}
			}
		}*/

		// for 3d isothetic cover
		OverlapTester test = new OverlapTester();
		for(int i=starting.x; i<=ending.x; i++){
			for(int j=starting.y; j<=ending.y; j++){
				for(int k=starting.z; k<=ending.z; k++){
					double[] min = new double[3];
					min[0] = g*(i+base[0]);
					min[1] = g*(j+base[1]);
					min[2] = g*(k+base[2]);
					boolean overlap = isObjectOccupiedBox(test, min, t);
					if(overlap){
						boolean flag = false;                                                                                                                                  //checking for duplicacy
						if(grid[i][j][k].T!=null) for(int x:grid[i][j][k].T) if(x==t) flag=true;
						if(!flag) {
							grid[i][j][k].T.add(t);
							temp.add(new UgcIndex(i,j,k));
						}
					}
				}
			}
		}
	}

	private boolean isObjectOccupiedBox(OverlapTester test, double[] min, int t) {
		int[] indexOfVertices = faces.get(t).getVertices();
		Vertex triA = vertices.get(indexOfVertices[0]);
		Vertex triB = vertices.get(indexOfVertices[1]);
		Vertex triC = vertices.get(indexOfVertices[2]);

    	boolean f1 = test.TrianglePlaneIntersection(triA, triB, triC, new double[] {min[0], min[1]} , g , min[2]);
    	boolean f2 = test.TrianglePlaneIntersection(triA, triB, triC, new double[] {min[0], min[1]} , g , min[2]+g);
    	boolean f3 = test.TrianglePlaneIntersection(triA, triB, triC, new double[] {min[1], min[2]} , g , min[0]);
    	boolean f4 = test.TrianglePlaneIntersection(triA, triB, triC, new double[] {min[1], min[2]} , g , min[0]+g);
    	boolean f5 = test.TrianglePlaneIntersection(triA, triB, triC, new double[] {min[0], min[2]} , g , min[1]);
  		boolean f6 = test.TrianglePlaneIntersection(triA, triB, triC, new double[] {min[0], min[2]} , g , min[1]+g);

  		
  	}

	//driver program to test Isothetic cover

	public static void main(String[] args) throws Exception{
		
		//driver program for testing

		IsotheticCover ic = new IsotheticCover(70, new File("elephant.obj"));
		int count =1;
		for(int k=0; k<ic.lz; k++){
			for(int j=0; j<ic.ly; j++){
				for(int i=0; i<ic.lx; i++){
					if(ic.grid[i][j][k].T.size()!=0){ //checking object occupied ugc
						System.out.println("T"+count); //printing set T 
						for(int x:ic.grid[i][j][k].T) {System.out.print("t"+x+" ");}
						System.out.print("\nU"+count+"\n"); //printing set U
						for(UgcIndex v:ic.grid[i][j][k].U) System.out.print("u"+v.x+v.y+v.z+" ");
						System.out.println("\n");
						count++;
					}
				}
			}
		}
	}
}