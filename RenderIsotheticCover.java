import java.io.File;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;

class RenderIsotheticCover{

    private static void renderVertex(BufferedWriter buffer, double[] d) throws Exception {
        buffer.write("#vertices\n");
        for(int zc=0; zc<2; zc++) for(int yc=0; yc<2; yc++) for(int xc=0; xc<2; xc++){
            buffer.write("v "+(d[0]+xc)+"    "+(d[1]+yc)+"    "+(d[2]+zc)+"\n");
        }
        buffer.newLine();
    }

    private static void renderFace(BufferedWriter buffer, int l) throws Exception {
        buffer.write("#faces\n");
        buffer.write("f "+(l+5)+" "+(l+6)+" "+(l+2)+" "+(l+1)+"\n");
        buffer.write("f "+(l+6)+" "+(l+8)+" "+(l+4)+" "+(l+2)+"\n");
        buffer.write("f "+(l+4)+" "+(l+8)+" "+(l+7)+" "+(l+3)+"\n");
        buffer.write("f "+(l+3)+" "+(l+7)+" "+(l+5)+" "+(l+1)+"\n");
        buffer.write("f "+(l+7)+" "+(l+8)+" "+(l+6)+" "+(l+5)+"\n");
        buffer.write("f "+(l+1)+" "+(l+2)+" "+(l+4)+" "+(l+3)+"\n\n");
    }

    public static void main(String[] args) throws Exception {
        File file = new File("canstick.obj");
        IsotheticCover ic = new IsotheticCover(0.001,file);
        FileWriter writer = new FileWriter("C:\\Users\\Priyesh Kumar\\Desktop\\IsotheticCoverOf" + file.getName() + ".obj");
        BufferedWriter buffer = new BufferedWriter(writer);
        int l=0;
        for(int k=0; k<ic.lz; k++){
            for(int j=0; j<ic.ly; j++){
                for(int i=0; i<ic.lx; i++){
                    if(ic.grid[i][j][k].T.size()!=0){ //checking for object occupied ugc
                        renderVertex(buffer, new double[]{i, j, k});
                        renderFace(buffer, l);
                        l+=8;
                    }
                }
            }
        }
        buffer.close();
    }
}
