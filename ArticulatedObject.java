import java.io.File;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;

class ArticulatedObject{
	public static void main(String[] args) throws Exception{
		File file = new File("elephant.obj");
		IsotheticCover ic = new IsotheticCover(40,file);
		for(int fileNumber = 1; fileNumber<10; fileNumber++){
			double row = (double) fileNumber/10;
			Segment3D segments = new Segment3D(ic,row);
			ArticulatedComponents components = new ArticulatedComponents(segments);
			String directory = file.getName();
			directory = directory.substring(0,directory.length()-4);
			directory = "C:\\Users\\Priyesh Kumar\\Desktop\\articulated_"+directory;
			new File(directory).mkdir();
			FileWriter writer = new FileWriter(directory+"\\"+fileNumber+".obj");
			BufferedWriter buffer = new BufferedWriter(writer);
			buffer.write("mtllib ./manycolor.obj.mtl");
			buffer.newLine();
			for(Vertex v : ic.vertices){
				if(v!=null){
					double[] d = v.getCoordinates();
					buffer.write("v "+d[0]+"    "+d[1]+"    "+d[2]);
					buffer.newLine();
				}
			}
			int count = 1;
			boolean[] triangleWritten = new boolean[ic.faces.size()];
			for(int i=0; i<triangleWritten.length; i++) triangleWritten[i]=false;
			for(ArrayList<UgcIndex> list : components.comp){
				buffer.write("usemtl " + "c"+count);
				buffer.newLine();
				for(UgcIndex u : list){
					Ugc ugc = ic.grid[u.x][u.y][u.z];
					for(int t : ugc.T){
						if(!triangleWritten[t]){
							int[] points = ic.faces.get(t).getVertices();
							buffer.write("f "+points[0]+" "+points[1]+" "+points[2]);
							buffer.newLine();
							triangleWritten[t]=true;
						}
					}
				}
				count++;
			}	
			buffer.close();
		}
	}
}