//for reading the faces of a triangle mesh in 3d space

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.Math;

class ObjectReader {

	private Vertex readVertex(String coordinate){

		//returns a vertex with coordinates specified in the input line

		int l = coordinate.length();
		double[] values = new double[3]; //array of coordinates
		int axis=0; //index for each axis

		for(int i=0; i<l; i++){
			//filling the coordinates specified in the line into array 
			int temp = (int) coordinate.charAt(i);
			if(temp>47&&temp<58){
				String number = "";
				if(coordinate.charAt(i-1)=='-') number=number+'-';
				while(temp>47&&temp<58||temp==46){
					number=number+coordinate.charAt(i);
					i++;
					if(i<l) temp = (int) coordinate.charAt(i);
					else temp=0;
				}
				values[axis]=Double.parseDouble(number);
				axis++;
			}
		}
		return new Vertex(values);
	}

	private Triangle readFace(String line){

		//returns a Triangle with index of vertex specified in the input line

		int l = line.length();
		int[] values = new int[3]; //array of indices
		int x=0; //iterator for each vertex

		for(int i=0; i<l; i++){
			//filling the vertices specified in the line into array 
			int temp = (int) line.charAt(i);
			if(temp>47&&temp<58){
				String number = "";
				while(temp>47&&temp<58){
					number=number+line.charAt(i);
					i++;
					if(i<l) temp = (int) line.charAt(i);
					else temp=0;
				}
				values[x]=Integer.parseInt(number);
				x++;
				while(i<l&&line.charAt(i)!=' ') i++;
			}
		}
		return new Triangle(values);
	}

	private boolean checkObj(File file) {

		//checks for .obj file
		
	   String fileName = file.getName().toLowerCase();
	   return fileName.endsWith(".obj");
	}

	public ArrayList<Triangle> readFaceData(File f) throws Exception{

		//returns a list of triangles present in input file(expected to be .obj file)

		if(!checkObj(f)) throw new Exception("obj file expected");
		FileReader fr = null;
		BufferedReader reader = null;
		ArrayList<Triangle> faces = null;
		try{
			fr = new FileReader(f);
			reader = new BufferedReader(fr);
			faces = new ArrayList<Triangle>();
			faces.add(null);//list of faces
			int lineBreaks = 0;
			while(lineBreaks<10){
				String line = reader.readLine();
				while(line!=null&& !line.isEmpty()){
					lineBreaks=0;
					//faces are specified in .obj file with a starting character 'f'
					if(line.charAt(0)=='f'&&line.charAt(1)==' '){
						faces.add(readFace(line));
					}
					line=reader.readLine();
				}
				lineBreaks++;
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try {
				if (reader != null)
					reader.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
		return faces;
	}

	public ArrayList<Vertex> readVertexData(File f) throws Exception{

		//returns a list of vertices present in input file(expected to be .obj file)

		if(!checkObj(f)) throw new Exception("obj file expected");
		FileReader fr = null;
		BufferedReader reader = null;
		ArrayList<Vertex> vertices = null;
		try{
			fr = new FileReader(f);
			reader = new BufferedReader(fr);
			vertices = new ArrayList<Vertex>();
			vertices.add(null);//list of vertices
			int lineBreaks=0;
			while(lineBreaks<10){
				String line = reader.readLine();
				while(line!=null&& !line.isEmpty()){
					lineBreaks=0;
					//vertices are specified in .obj file with a starting character 'v'
					if(line.charAt(0)=='v'&&line.charAt(1)==' '){
						vertices.add(readVertex(line));
					}
					line=reader.readLine();
				}
				lineBreaks++;
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try {
				if (reader != null)
					reader.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
		return vertices;
	}

	/*public static void main(String[] args) throws Exception{

		//driver program

		//vertex reader testing

		ObjectReader dr = new ObjectReader();
		ArrayList<Vertex> vertexList = dr.readVertexData(new File("example.obj"));
		int count = 1;
		for(Vertex v: vertexList){
			if(v!=null){
				System.out.print(count+" ");
				double[] coordinates=v.getCoordinates();
				for(double d: coordinates){
					System.out.print(d+" ");
				}
				System.out.println("");
				count++;
			}
		}

		//face reader testing

		ArrayList<Triangle> list = dr.readFaceData(new File("example.obj"));
		count = 1;
		for(Triangle t: list){
			if(t!=null){
				System.out.print(count+" ");
				int[] vertices=t.getVertices();
				for(int i: vertices){
					System.out.print(i+" ");
				}
				System.out.println("");
				count++;
			}
		}
	}*/
}