class OverlapTester
{
	
  static final int X=0;
  static final int Y=1;
  static final int Z=2;
  
  private double[] crossProduct(double[] v1, double[] v2){
    double[] dest = new double[3];
    dest[0] = v1[1]*v2[2] - v1[2]*v2[1]; 
    dest[1] = v1[2]*v2[0] - v1[0]*v2[2]; 
    dest[2] = v1[0]*v2[1] - v1[1]*v2[0];
    /*double magnitude = dest[0]*dest[0]+dest[1]*dest[1]+dest[2]*dest[2];
    for(int i=0; i<3; i++) dest[i] = dest[i]/magnitude;*/
    return dest;
  }

  private double dotProduct(double []v1,double []v2) {
    return ( v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2] );
  }

  private double[] sub(double[] v1, double[] v2) {
    double[] dest = new double[3];
    dest[0] = v1[0]-v2[0];
    dest[1] = v1[1]-v2[1];
    dest[2] = v1[2]-v2[2]; 
    return dest;     
  }

  private double[] minMax(double x0, double x1, double x2) {
    double min=x0;
    double max=x0;   
    if(x1<min) min=x1;
    if(x1>max) max=x1;
    if(x2<min) min=x2;
    if(x2>max) max=x2;
    return new double[] {min, max};   
  }

  private boolean planeBoxOverlap(double[] normal, double[] vert, double[] maxbox) {	
    int q;
    double[] vmin = new double[3];
    double[] vmax = new double[3];
    double v;
    for(q=X;q<=Z;q++)
    {
      v=vert[q];					
      if(normal[q]>0.0f) {
        vmin[q]=-maxbox[q] - v;	
        vmax[q]= maxbox[q] - v;	
      }
      else {
        vmin[q]= maxbox[q] - v;	
        vmax[q]=-maxbox[q] - v;	
      }
    }
    if(dotProduct(normal,vmin)>0.0f) return false;	
    if(dotProduct(normal,vmax)>=0.0f) return true;	
    return false;
  }

/*======================== X-tests ========================*/

  private boolean axisTestX01(double a,double b,double[] v0,double[] v2, double[] boxhalfsize) {
    double p0 = a*v0[Y] - b*v0[Z];
    double p2 = a*v2[Y] - b*v2[Z];
    double min,max;
    if(p0<p2) {
      min=p0;
      max=p2;
    } 
    else {
      min=p2;
      max=p0;
    } 
  	double rad = Math.abs(a) * boxhalfsize[Y] + Math.abs(b) * boxhalfsize[Z];   
  	if(min>rad || max< -rad) return false;
    return true;
  }

  private boolean axisTestX2(double a,double  b,double[] v0,double[] v1, double[] boxhalfsize)	{		   
  	double p0 = a*v0[Y] - b*v0[Z];			           
  	double p1 = a*v1[Y] - b*v1[Z];
  	double min,max;
    if(p0<p1) {
      min=p0;
      max=p1;
    }
    else {
      min=p1;
      max=p0;
    } 
  	double rad = Math.abs(a) * boxhalfsize[Y] + Math.abs(b) * boxhalfsize[Z];   
  	if(min>rad || max< -rad) return false;
    return true;
  }

/*======================== Y-tests ========================*/

  private boolean axisTestY02(double a,double  b, double[] v0,double[] v2, double[] boxhalfsize) {		   
  	double p0 = -a*v0[X] + b*v0[Z];		      	   
  	double p2 = -a*v2[X] + b*v2[Z];
  	double min,max;
    if(p0<p2) {
      min=p0;
      max=p2;
    }
    else {
      min=p2;
      max=p0;
    } 
  	double rad = Math.abs(a) * boxhalfsize[X] + Math.abs(b) * boxhalfsize[Z];   
  	if(min>rad || max< -rad) return false;
    return true;   
  }

  private boolean axisTestY1(double a,double  b, double[] v0,double[] v1, double[] boxhalfsize)	{		   
  	double p0 = -a*v0[X] + b*v0[Z];		      	   
  	double p1 = -a*v1[X] + b*v1[Z];
  	double min,max;
    if(p0<p1) {
      min=p0;
      max=p1;
    }
    else {
      min=p1;
      max=p0;
    } 
  	double rad = Math.abs(a) * boxhalfsize[X] + Math.abs(b) * boxhalfsize[Z];   
  	if(min>rad || max<-rad) return false;
    return true;
  }

/*======================== Z-tests ========================*/

  private boolean axisTestZ12(double a,double  b,double[] v1, double[] v2, double[] boxhalfsize)	{
  	double p1 = a*v1[X] - b*v1[Y];			           
  	double p2 = a*v2[X] - b*v2[Y];
  	double min,max;
    if(p2<p1) {
      min=p2;
      max=p1;
    }
    else {
      min=p1;
      max=p2;
    } 
  	double rad = Math.abs(a) * boxhalfsize[X] + Math.abs(b) * boxhalfsize[Y];   
  	if(min>rad || max<-rad) return false;  
    return true;                
  }

  private boolean axisTestZ0(double a,double  b,double[] v0, double[] v1, double[] boxhalfsize)	{		   
  	double p0 = a*v0[X] - b*v0[Y];				   
    double p1 = a*v1[X] - b*v1[Y];	
    double min,max;
    if(p0<p1) {
      min=p0;
      max=p1;
    }
    else {
      min=p1;
      max=p0;
    } 
  	double rad = Math.abs(a) * boxhalfsize[X] + Math.abs(b) * boxhalfsize[Y];   
  	if(min>rad || max<-rad) return false; 
    return true;         
  }

  public boolean triBoxOverlap(double[] boxcenter,double[] boxhalfsize,double[][] triverts) {
    
    /*using separating axis theorem to test overlap between triangle and box, we
      need to test for overlap in these directions: 
      1) the {x,y,z}-directions (axes perpendicular to faces of box)
      2) normal of the triangle
      3) crossproduct(edge from tri, {x,y,z}-direction i.e. the axes perpendicular to box faces)
         this gives 3x3=9 more tests 
      so 13 axis in total*/

    // double axis[3];

    double min,max,rad;
    double[] normal,v0,v1,v2,e0,e1,e2,range;

    // move everything so that the boxcenter is in (0,0,0)

    v0 = sub(triverts[0],boxcenter);
    v1 = sub(triverts[1],boxcenter);
    v2 = sub(triverts[2],boxcenter);

    // compute triangle edges

    e0 = sub(v1,v0);      // tri edge 0
    e1 = sub(v2,v1);      // tri edge 1
    e2 = sub(v0,v2);      // tri edge 2

    // Bullet 3:
    // test the 9 tests first

    if(!axisTestX01(e0[Z], e0[Y], v0, v2, boxhalfsize)) return false;
    if(!axisTestY02(e0[Z], e0[X], v0, v2, boxhalfsize)) return false;
    if(!axisTestZ12(e0[Y], e0[X], v1, v2, boxhalfsize)) return false;

    if(!axisTestX01(e1[Z], e1[Y], v0, v2, boxhalfsize)) return false;
    if(!axisTestY02(e1[Z], e1[X], v0, v2, boxhalfsize)) return false;
    if(!axisTestZ0(e1[Y], e1[X], v0, v1, boxhalfsize)) return false;

    if(!axisTestX2(e2[Z], e2[Y], v0, v1, boxhalfsize)) return false;
    if(!axisTestY1(e2[Z], e2[X], v0, v1, boxhalfsize)) return false;
    if(!axisTestZ12(e2[Y], e2[X], v1, v2, boxhalfsize)) return false;

    /* Bullet 1:
       first test overlap in the {x,y,z}-directions
       find min, max of the triangle each direction, and test for overlap in
       that direction -- this is equivalent to testing a minimal AABB around
       the triangle against the AABB */

    // test in X-direction

    range = minMax(v0[X],v1[X],v2[X]);
    min=range[0];
    max=range[1];
    if(min>boxhalfsize[X] || max< -boxhalfsize[X]) return false;

    // test in Y-direction

    range = minMax(v0[Y],v1[Y],v2[Y]);
    min=range[0];
    max=range[1];
    if(min>boxhalfsize[Y] || max< -boxhalfsize[Y]) return false;

    // test in Z-direction

    range = minMax(v0[Z],v1[Z],v2[Z]);
    min=range[0];
    max=range[1];
    if(min>boxhalfsize[Z] || max< -boxhalfsize[Z]) return false;

    /* Bullet 2:
       test if the box intersects the plane of the triangle
       compute plane equation of triangle: normal*x+d=0 */

    normal = crossProduct(e0,e1);
    if(!planeBoxOverlap(normal,v0,boxhalfsize)) return false;

    return true;   // box and triangle overlaps

  }
	
  //driver program to test our code

	public static void main (String[] args) throws java.lang.Exception
	{
		double[] center = {0.5,0.5,0.5};
		double[] extent = {0.5,0.5,0.5};
		double[][] vertices = new double[3][3];
		vertices[0][0]=0.3;
		vertices[0][1]=0.2;
		vertices[0][2]=0.1;
		vertices[1][0]=2.5;
		vertices[1][1]=1.3;
		vertices[1][2]=3.2;
		vertices[2][0]=-2.1;
		vertices[2][1]=-1.5;
		vertices[2][2]=-2.3;
		OverlapTester obj = new OverlapTester();
		if(obj.triBoxOverlap(center,extent,vertices)) System.out.println("overlaps");
		else System.out.println("does not overlap");
	}
}