import java.awt.geom.Path2D;
public class SentPackage{ 
	private Path2D.Double triangle;
	private int rtime;
	private int dtime;
	private Node[] arc;
	public SentPackage(Path2D.Double tri, int r, int d, Node[] a){
		triangle = tri;
		rtime = r;
		dtime=d;
		arc=a;
	}
	public Path2D.Double getTriangle() {
		return triangle;
	}
	public void setTriangle(Path2D.Double tri) {
		triangle = tri;
	}
	public int getRtime() {
		return rtime;
	}
	public void setRtime(int rtime) {
		this.rtime = rtime;
	}
	public int getDtime() {
		return dtime;
	}
	public void setDtime(int dtime) {
		this.dtime = dtime;
	}
	public Node[] getArc() {
		return arc;
	}
	public void setArc(Node[] a) {
		arc = a;
	}
}
