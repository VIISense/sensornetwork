import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import javax.swing.Timer;

public class Animation implements ActionListener{
	//time in msec
	private Timer time = new Timer(10, (ActionListener) this);
	private Action a;
	private double t;
	private double step;
	private int n;
	private int[][] deadlines;
	private int[][] xia;
	private double m;
	private ArrayList<Double> xvalue;
	private ArrayList<Double> yvalue;
	public Animation(Action action){
		a=action;
		t=0;
		n=0;
		m=Double.parseDouble(a.getPP().getMultiplier().getText());
		xia = a.getilp();
		deadlines = new int[a.getPP().getArcList().size()][a.getPP().getMessageList().size()];
		xvalue=new ArrayList<Double>();
		yvalue=new ArrayList<Double>();
		step=(a.getPP().getTimeLine().getX2()-a.getPP().getTimeLine().getX1())/
				(Integer.parseInt(a.getPP().getEndTime().getText())*100);
		for(int i=0; i<a.getPP().getArcList().size(); i++){
			if(a.getPP().getArcList().get(i)[1]!=a.getPP().getSink()){
				break; //because sorted
			}
			for(int j=0; j<a.getPP().getMessageList().size(); j++){
				if(xia[i][j]==1)
					deadlines[i][j]=
					Integer.parseInt(a.getPP().getMessageList().get(j).getDeadline().getText());
			}
	
		}
		ArrayList<Node> queue = new ArrayList<Node>();
		queue.add(a.getPP().getSink());
		boolean [] visited = new boolean[a.getPP().getPointList().size()];
		visited [a.getPP().getPointList().indexOf(a.getPP().getSink())] = true;
		while(!queue.isEmpty()){
			Node cur = queue.remove(0);
			for(int i=0; i<a.getPP().getArcList().size(); i++){
				Node[] arc = a.getPP().getArcList().get(i);
				if(arc[1]==cur&&!visited[a.getPP().getPointList().indexOf(arc[0])]){
					queue.add(arc[0]);
					visited[a.getPP().getPointList().indexOf(arc[0])]=true;
					for(int j=0; j<a.getPP().getMessageList().size(); j++){
						if(xia[i][j]==1){
							double x =  arc[0].getN().getX()+arc[0].getN().getHeight()*0.5;
							double y =  arc[0].getN().getY()+arc[0].getN().getWidth()*0.5;
							Path2D.Double tri = new Path2D.Double();
							tri.moveTo(x-8, y+5);
							tri.lineTo(x, y-8);
							tri.lineTo(x+8, y+5);
							tri.closePath();
							SentPackage sm = new SentPackage(tri, 
									deadlines[i][j]-Integer.parseInt(arc[0].getTime().getText()),
									deadlines[i][j], arc);
							a.getPP().addTri(sm);
							for(int k=0; k<a.getPP().getArcList().size(); k++){
								if(a.getPP().getArcList().get(i)[0]==a.getPP().getArcList().get(k)[1]){
									deadlines[k][j]=
											deadlines[i][j]-Integer.parseInt(arc[0].getTime().getText());
								}
							}
							xvalue.add((arc[1].getN().getX()-arc[0].getN().getX())/
									(Integer.parseInt(arc[0].getTime().getText())*100));
							yvalue.add((arc[1].getN().getY()-arc[0].getN().getY())/
									(Integer.parseInt(arc[0].getTime().getText())*100));	
						}
					}
				}
			}	
		}
		time.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		if((int)Math.ceil((n*m)/100)<=Integer.parseInt(a.getPP().getEndTime().getText())){
			ArrayList<Integer> v = new ArrayList<Integer>();
			a.getPP().getTimePasses().setText(String.valueOf((int)Math.floor((n*m)/100)));
			for(int i=0; i<a.getPP().getTri().size(); i++){
				Path2D.Double tri = a.getPP().getTri().get(i).getTriangle();
				Node [] ca = a.getPP().getTri().get(i).getArc();
				if(a.getPP().getTri().get(i).getRtime()>((n*m)/100)){
					continue;
				}
				if(a.getPP().getTri().get(i).getDtime()<((n*m)/100)){
					if(ca[1]==a.getPP().getSink()){
						v.add(i);
					}
					continue;
				}
				v.add(i);
				double x = xvalue.get(i)*m;
				double y = yvalue.get(i)*m;
				AffineTransform at = new AffineTransform();
				at.translate(x, y);
				tri.transform(at);
			}
			a.getPP().getV().clear();
			for(int j=0; j<v.size(); j++){
				a.getPP().getV().add(v.get(j));
			}
			//prevent redline to run out of endpoint
			double diff = a.getPP().getTimeLine().getX2()-a.getPP().getredLine().getX2();
			if(step*m>diff){
				t+=(diff);
			}
			else{
				t+=(step*m);
			}
			a.getPP().paintTime(t);
			a.getPP().repaint();
			n++;
		}
		else{
			time.stop();
			a.getPP().getPauseButton().setEnabled(false);
			a.getPP().getNodeButton().setEnabled(true);
			a.getPP().getArcButton().setEnabled(true);
			a.getPP().getMessageButton().setEnabled(true);
			a.getPP().getSinkButton().setEnabled(true);
			a.getPP().getDeleteButton().setEnabled(true);
			a.getPP().getRandom().setEnabled(true);
		}
	}
	public Timer getTimer(){
		return time;
	}
}
