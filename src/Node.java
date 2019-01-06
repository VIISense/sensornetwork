import java.awt.geom.Ellipse2D;
import javax.swing.JLabel;
import javax.swing.JTextField;
public class Node {
	private Ellipse2D n;
	private JTextField cost;
	private JTextField time;
	private JLabel c;
	private JLabel t;
	private JLabel rd;
	private JLabel dl;
	public Node (Ellipse2D e, JTextField cost, JTextField time, JLabel c, JLabel t){
		setN(e);
		setCost(cost);
		setTime(time);
		setC(c);
		setT(t);
		rd=null;
		dl=null;
	}
	public Ellipse2D getN() {
		return n;
	}
	public void setN(Ellipse2D n) {
		this.n = n;
	}
	public JTextField getCost() {
		return cost;
	}
	public void setCost(JTextField cost) {
		this.cost = cost;
	}
	public JTextField getTime() {
		return time;
	}
	public void setTime(JTextField time) {
		this.time = time;
	}
	public JLabel getC() {
		return c;
	}
	public void setC(JLabel c) {
		this.c = c;
	}
	public JLabel getT() {
		return t;
	}
	public void setT(JLabel t) {
		this.t = t;
	}
	public JLabel getRd() {
		return rd;
	}
	public void setRd(JLabel rd) {
		this.rd = rd;
	}
	public JLabel getDl() {
		return dl;
	}
	public void setDl(JLabel dl) {
		this.dl = dl;
	}
}
