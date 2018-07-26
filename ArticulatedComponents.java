import java.util.ArrayList;

class ArticulatedComponents{
	ArrayList<ArrayList<UgcIndex>> comp;

	ArticulatedComponents(Segment3D segments){
		comp = new ArrayList<ArrayList<UgcIndex>>();
		finalComponents(segments);
	}

	private void finalComponents(Segment3D segments){
		for(ArrayList<UgcIndex> xComp : segments.mX){
			for(ArrayList<UgcIndex> yComp : segments.mY){
				for(ArrayList<UgcIndex> zComp : segments.mZ){
					ArrayList<UgcIndex> temp = new ArrayList<UgcIndex>();
					for(UgcIndex ux : xComp){
						for(UgcIndex uy : yComp){
							for(UgcIndex uz : zComp){
								if(ux.x==uy.x&&uy.x==uz.x&&ux.y==uy.y&&uy.y==uz.y&&ux.z==uy.z&&uy.z==uz.z)
									temp.add(ux);
							}
						}
					}
					if(temp.size()!=0) comp.add(temp);
				}
			} 
		}
	}
}