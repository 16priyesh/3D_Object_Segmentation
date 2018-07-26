//parameters which can be used as index to get a node from 2d arraylist used in reeb graph

class NodeIndex{
	int level; //location or index along the axis of reeb graph
	int index; //index of node at its level

	NodeIndex(int level, int index){
		this.level=level;
		this.index=index;
	}
}