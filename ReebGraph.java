import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.io.File;

class ReebGraph{
	ArrayList<ArrayList<Node>> nodes; //2d list of nodes contained in each level
	int levels; //total number of levels
	int axis; //axis along which reeb graph is made
	int numberOfNodes; //total number of nodes

	ReebGraph(IsotheticCover ic, int axis){
		this.axis = axis;
		switch(axis){
			case 0 : levels = ic.lx; break;
			case 1 : levels = ic.ly; break;
			case 2 : levels = ic.lz; break;
		}
		nodes = new ArrayList<ArrayList<Node>>();
		for(int i=0; i<levels; i++){
			nodes.add(new ArrayList<Node>());
		}
		numberOfNodes = 0;
		switch(axis){
			case 0 : alongX(ic); break;
			case 1 : alongY(ic); break;
			case 2 : alongZ(ic); break;
		}
	}

	private void alongZ(IsotheticCover ic){

		//creating reeb graph along z axis

		/*first we make the partitions of basic elements of topological set X/W
		 parallel to xy plane at each level. Each of the components represents a node of
		 the reeb graph. So we iterate through all the levels to partition them. We use breadth first
		 traversal to make partitions at a particular level.
		 We iterate through all the ugcs' at a level marking them visited as encountered.
		 Each time we encounter a unvisited object occupied ugc, we make a new partition/node and 
		 use breadth first traversal to find other neighbouring object occupied ugc
		 which are going to be a part of this node and mark them all as visited. Like this we 
		 identify all the components or say the nodes of the reeb graph.*/

		Queue<UgcIndex> queue = new LinkedList<UgcIndex>(); //queue for breadth first traversal
		for(int k=0; k<levels; k++){//iterating through each level
			boolean[][] isVisited = new boolean[ic.lx][ic.ly]; //keep track of visited ugcs'
			for(int j=0; j<ic.ly; j++){
				for(int i=0; i<ic.lx; i++){
					isVisited[i][j] = false;
				}
			}
			for(int j=0; j<ic.ly; j++){
				for(int i=0; i<ic.lx; i++){
					if(!isVisited[i][j]) {
						isVisited[i][j] = true;
						if(ic.grid[i][j][k].T.size()!=0){//for each unvisited object occupied ugc
							Node n = new Node();
							numberOfNodes++;
							queue.add(new UgcIndex(i,j,k));
							while(queue.size()!=0){ //breadth first traversal for object occupied neighbours
								UgcIndex itr = queue.poll();
								n.S.add(itr);
								int f0=itr.y;
								if(f0-1>=0) f0--;
								int g0 = itr.x;
								if(g0-1>=0) g0--;
								for(int f=f0; f<ic.ly&&f<=itr.y+1; f++){
									for(int g=g0; g<ic.lx&&g<=itr.x+1; g++) {if((!isVisited[g][f])&&ic.grid[g][f][itr.z].T.size()!=0) {
										//for each unvisited object occupied neighbour
										queue.add(new UgcIndex(g,f,itr.z));
										isVisited[g][f]=true;
									}}
								}
							}
						nodes.get(k).add(n); //adding the node to the 2d arraylist
						}
					}
				}
			}
		}

		//now connecting all the nodes at different level which are having some ugc touching each other
		/*here we simply check all the ugc's in a node to check if it touches any other ugc of other
		level and if does we add the two nodes at different level as neighbours of each other*/

		for(int k=0; k<levels-1; k++){ 
			for(Node node : nodes.get(k)){//for each node at a level
				for(UgcIndex u : node.S){ 
					if(ic.grid[u.x][u.y][u.z+1].T.size()!=0){//we check each of its object occupied ugcs'
						for(Node upperNode : nodes.get(k+1)){//for each node of next level
							for(UgcIndex v : upperNode.S) if(v.x==u.x&&v.y==u.y&&v.z==u.z+1){
								//we find the node to which the touching object occupied ugc belongs
								//and add them as each others neighbour if they don't already contain 
								//each other.
								boolean flag = true;
								for(NodeIndex n: node.nextNeighbours)
									if((n.level==k+1&&n.index==nodes.get(k+1).indexOf(upperNode)))
									flag = false;
								if(flag) node.nextNeighbours.add(new NodeIndex(k+1,nodes.get(k+1).indexOf(upperNode)));
								flag = true;
								for(NodeIndex n: upperNode.previousNeighbours)
									if((n.level==k&&n.index==nodes.get(k).indexOf(node)))
										flag = false;
								if(flag) upperNode.previousNeighbours.add(new NodeIndex(k,nodes.get(k).indexOf(node)));
							}
						}
					}
				}
			}
		}
	}

	private void alongY(IsotheticCover ic){

		//creating reeb graph along y axis just like along z

		Queue<UgcIndex> queue = new LinkedList<UgcIndex>(); //queue for breadth first traversal
		for(int j=0; j<levels; j++){//iterating through each level
			boolean[][] isVisited = new boolean[ic.lx][ic.lz]; //keep track of visited ugcs'
			for(int k=0; k<ic.lz; k++){
				for(int i=0; i<ic.lx; i++){
					isVisited[i][k] = false;
				}
			}
			for(int k=0; k<ic.lz; k++){
				for(int i=0; i<ic.lx; i++){
					if(!isVisited[i][k]) {
						isVisited[i][k] = true;
						if(ic.grid[i][j][k].T.size()!=0){//for each unvisited object occupied ugc
							Node n = new Node();
							numberOfNodes++;
							queue.add(new UgcIndex(i,j,k));
							while(queue.size()!=0){ //breadth first traversal for object occupied neighbours
								UgcIndex itr = queue.poll();
								n.S.add(itr);
								int f0=itr.z;
								if(f0-1>=0) f0--;
								int g0 = itr.x;
								if(g0-1>=0) g0--;
								for(int f=f0; f<ic.lz&&f<=itr.z+1; f++){
									for(int g=g0; g<ic.lx&&g<=itr.x+1; g++) {if((!isVisited[g][f])&&ic.grid[g][itr.y][f].T.size()!=0) {
										//for each unvisited object occupied neighbour
										queue.add(new UgcIndex(g,itr.y,f));
										isVisited[g][f]=true;
									}}
								}
							}
						nodes.get(j).add(n); //adding the node to the 2d arraylist
						}
					}
				}
			}
		}

		//now connecting all the nodes similar to that along z

		for(int k=0; k<levels-1; k++){ 
			for(Node node : nodes.get(k)){//for each node at a level
				for(UgcIndex u : node.S){ 
					if(ic.grid[u.x][u.y+1][u.z].T.size()!=0){//we check each of its object occupied ugcs'
						for(Node upperNode : nodes.get(k+1)){//for each node of next level
							for(UgcIndex v : upperNode.S) if(v.x==u.x&&v.y==u.y+1&&v.z==u.z){
								//we find the node to which the touching object occupied ugc belongs
								//and add them as each others neighbour if they don't already contain
								boolean flag = true;
								 for(NodeIndex n: node.nextNeighbours)
								 	if((n.level==k+1&&n.index==nodes.get(k+1).indexOf(upperNode)))
										flag = false;
								 if(flag) node.nextNeighbours.add(new NodeIndex(k+1,nodes.get(k+1).indexOf(upperNode)));
								 flag = true;
								 for(NodeIndex n: upperNode.previousNeighbours)
								 	if((n.level==k&&n.index==nodes.get(k).indexOf(node)))
								 		flag = false;
								 if(flag) upperNode.previousNeighbours.add(new NodeIndex(k,nodes.get(k).indexOf(node)));
							}
						}
					}
				}
			}
		}
	}

	private void alongX(IsotheticCover ic){

		//creating reeb graph along x axis just like along z

		Queue<UgcIndex> queue = new LinkedList<UgcIndex>(); //queue for breadth first traversal
		for(int i=0; i<levels; i++){//iterating through each level
			boolean[][] isVisited = new boolean[ic.ly][ic.lz]; //keep track of visited ugcs'
			for(int k=0; k<ic.lz; k++){
				for(int j=0; j<ic.ly; j++){
					isVisited[j][k] = false;
				}
			}
			for(int k=0; k<ic.lz; k++){
				for(int j=0; j<ic.ly; j++){
					if(!isVisited[j][k]) {
						isVisited[j][k] = true;
						if(ic.grid[i][j][k].T.size()!=0){//for each unvisited object occupied ugc
							Node n = new Node();
							numberOfNodes++;
							queue.add(new UgcIndex(i,j,k));
							while(queue.size()!=0){ //breadth first traversal for object occupied neighbours
								UgcIndex itr = queue.poll();
								n.S.add(itr);
								int f0=itr.z;
								if(f0-1>=0) f0--;
								int g0 = itr.y;
								if(g0-1>=0) g0--;
								for(int f=f0; f<ic.lz&&f<=itr.z+1; f++){
									for(int g=g0; g<ic.ly&&g<=itr.y+1; g++) {if((!isVisited[g][f])&&ic.grid[itr.x][g][f].T.size()!=0) {
										//for each unvisited object occupied neighbour
										queue.add(new UgcIndex(itr.x,g,f));
										isVisited[g][f]=true;
									}}
								}
							}
						nodes.get(i).add(n); //adding the node to the 2d arraylist
						}
					}
				}
			}
		}

		//now connecting all the nodes similar to that along z

		for(int k=0; k<levels-1; k++){ 
			for(Node node : nodes.get(k)){//for each node at a level
				for(UgcIndex u : node.S){ 
					if(ic.grid[u.x+1][u.y][u.z].T.size()!=0){//we check each of its object occupied ugcs'
						for(Node upperNode : nodes.get(k+1)){//for each node of next level
							for(UgcIndex v : upperNode.S) if(v.x==u.x+1&&v.y==u.y&&v.z==u.z){
								//we find the node to which the touching object occupied ugc belongs
								//and add them as each others neighbour if they don't already contain
								boolean flag = true;
								 for(NodeIndex n: node.nextNeighbours)
								 	if((n.level==k+1&&n.index==nodes.get(k+1).indexOf(upperNode)))
										flag = false;
								 if(flag) node.nextNeighbours.add(new NodeIndex(k+1,nodes.get(k+1).indexOf(upperNode)));
								 flag = true;
								 for(NodeIndex n: upperNode.previousNeighbours)
								 	if((n.level==k&&n.index==nodes.get(k).indexOf(node)))
								 		flag = false;
								 if(flag) upperNode.previousNeighbours.add(new NodeIndex(k,nodes.get(k).indexOf(node)));
							}
						}
					}
				}
			}
		}
	}

	//driver program for testing

	/*public static void main(String[] args) throws Exception {
		ReebGraph rg = new ReebGraph(new IsotheticCover(1,new File("example2.obj")), 2);
		int count = 1;
		for(int i=0; i<rg.levels; i++){
			for(Node n : rg.nodes.get(i)){
				System.out.println(i+" "+count+" UGCs'");
				for(UgcIndex u : n.S) System.out.println(u.x+" "+u.y+" "+u.z);
				System.out.println(count+" next neighbours");
				for(NodeIndex ni : n.nextNeighbours) System.out.println(ni.level+" "+ni.index);
				System.out.println(count+" previous neighbours");
				for(NodeIndex ni : n.previousNeighbours) System.out.println(ni.level+" "+ni.index);
				System.out.println();
				count++;
			}
		}
	}*/

}