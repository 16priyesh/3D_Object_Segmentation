import java.util.ArrayList;

class Node{
	//each node represents an element of the quotient space

	ArrayList<UgcIndex> S; //partitions(sets) of basic elements of topological space X/W
	ArrayList<NodeIndex> nextNeighbours; //neighbours in next level
	ArrayList<NodeIndex> previousNeighbours; //neighbours in previous level
	int compId;

	Node(){
		S = new ArrayList<UgcIndex>();
		nextNeighbours = new ArrayList<NodeIndex>();
		previousNeighbours = new ArrayList<NodeIndex>();
		compId = 0;
	}
}