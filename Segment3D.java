import java.util.ArrayList;
import java.lang.Math;
import java.io.File;
import java.util.Queue;
import java.util.LinkedList;

class Segment3D{
	ArrayList<ArrayList<UgcIndex>> mX;
	ArrayList<ArrayList<UgcIndex>> mY;
	ArrayList<ArrayList<UgcIndex>> mZ;

	Segment3D(IsotheticCover ic, double accuracyIndex){
		mX = new ArrayList<ArrayList<UgcIndex>>();
		mY = new ArrayList<ArrayList<UgcIndex>>();
		mZ = new ArrayList<ArrayList<UgcIndex>>();
		ReebGraph gX = new ReebGraph(ic,0);
		ArrayList<NodeIndex> leafX = leafNodes(gX);
		ReebGraph gY = new ReebGraph(ic,1);
		ArrayList<NodeIndex> leafY = leafNodes(gY);
		ReebGraph gZ = new ReebGraph(ic,2);
		ArrayList<NodeIndex> leafZ = leafNodes(gZ);
		Segment_Graph(gX, leafX, accuracyIndex, mX);
		Segment_Graph(gY, leafY, accuracyIndex, mY);
		Segment_Graph(gZ, leafZ, accuracyIndex, mZ);
	}

	void Segment_Graph(ReebGraph g, ArrayList<NodeIndex> leaf , double accuracyIndex, ArrayList<ArrayList<UgcIndex>> m){
		int count = 1;
		Queue<NodeIndex> q = new LinkedList<NodeIndex>();
		for(int i=0; i<leaf.size(); i++){
			Node v = g.nodes.get(leaf.get(i).level).get(leaf.get(i).index);
			boolean nodeVisited = true;
			if(v.compId==0){
				q.add(leaf.get(i));
				nodeVisited=false;
				m.add(new ArrayList<UgcIndex>());
			}
			Node v1 = v;
			double threshold = v.S.size();
			while(q.size()!=0){
				NodeIndex vi = q.poll();
				v = g.nodes.get(vi.level).get(vi.index);
				threshold = accuracyIndex*v.S.size()+(1-accuracyIndex)*threshold;
				double d = (double) Math.abs(v.S.size()-threshold)/v1.S.size();
				if(d<1){
					v.compId=count;
					for(UgcIndex ui : v.S) m.get(count-1).add(ui);
					for(NodeIndex next : v.nextNeighbours){
						if(g.nodes.get(next.level).get(next.index).compId==0) q.add(next);
					}
					for(NodeIndex prev : v.previousNeighbours){
						if(g.nodes.get(prev.level).get(prev.index).compId==0) q.add(prev);
					}
					v1 = v;
				}
				else leaf.add(vi);
			}
			if(!nodeVisited){
				count++;
			}
		}
	}

	private ArrayList<NodeIndex> leafNodes(ReebGraph g){
		ArrayList<NodeIndex> leaf = new ArrayList<NodeIndex>();
		for(ArrayList<Node> list : g.nodes){
			for(Node n : list){
				if(n.nextNeighbours.size()+n.previousNeighbours.size()==1) leaf.add(new NodeIndex(g.nodes.indexOf(list), list.indexOf(n)));
			}
		}
		return leaf;
	}

	public static void main(String[] args) throws Exception{
		Segment3D segments = new Segment3D(new IsotheticCover(0.01, new File("apple.obj")), 0.5);
		int total = 0;
		int count=1;
		System.out.println("X");
		for(ArrayList<UgcIndex> arr : segments.mX){
			System.out.println(count+" "+arr.size());
			count++;
			total+=arr.size();
		}
		System.out.println("total ugc "+total);
		count=1;
		total=0;
		System.out.println("Y");
		for(ArrayList<UgcIndex> arr : segments.mY){
			System.out.println(count+" "+arr.size());
			total+=arr.size();
			count++;
		}
		System.out.println("total ugc "+total);
		total=0;
		count=1;
		System.out.println("Z");
		for(ArrayList<UgcIndex> arr : segments.mZ){
			System.out.println(count+" "+arr.size());
			count++;
			total+=arr.size();
		}
		System.out.println("total ugc "+total);

		ArticulatedComponents components = new ArticulatedComponents(segments);
		total=0;
		count=1;
		System.out.println("articulated components");
		for(ArrayList<UgcIndex> arr : components.comp){
			System.out.println(count+" "+arr.size());
			count++;
			total+=arr.size();
		}
		System.out.println("total ugc "+total);
	}
}