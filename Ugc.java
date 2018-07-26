
//represents an ugc

import java.util.ArrayList;

class Ugc{
	
	ArrayList<Integer> T; //set of indices of traingles intersecting the ugc
	ArrayList<UgcIndex> U; //set of indices of ugc's intersected by triangles cutting this ugc

	Ugc(){
		T = new ArrayList<Integer>();
		U = new ArrayList<UgcIndex>();
	}
}