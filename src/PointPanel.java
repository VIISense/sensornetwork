import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class PointPanel extends JPanel{
	private ArrayList<Node> pointList;
	private ArrayList<Node[]> arcList;
	private ArrayList <Message> message;
	private Node selectedPoint;
	private Node sink;
	private ButtonGroup group = new ButtonGroup();
	private PointLocater pl = new PointLocater(this);
	private Action a = new Action(this);
	private ArrayList<SentPackage> triangleList;
	private boolean firstpaint = true;
	private ArrayList<Integer> v;
	private Line2D timeline1;
	private Line2D timeline2;
	private Ellipse2D p;
	private JTextField timepassed;
	private JTextField endtime;
	private JLabel tp;
	private JLabel t;
	private JLabel maxCost;
	private JLabel m;
	private JTextField multiplier;
	private JToggleButton addNode;
	private JToggleButton addArc;
	private JToggleButton delete;
	private JToggleButton addSink;
	private JToggleButton addMessage;
	private JButton start;
	private JButton clear;
	private JButton random;
	private JToggleButton pause;
    public PointPanel(){
    	pointList = new ArrayList<Node>();
    	arcList = new ArrayList<Node[]>();
    	message = new ArrayList<Message>();
    	v=new ArrayList<Integer>();
    	triangleList = new ArrayList<SentPackage>();
    	addNode = new JToggleButton("Node");
    	addArc = new JToggleButton("Arc");
    	delete = new JToggleButton("Delete");
    	addSink = new JToggleButton("Sink");
    	addMessage = new JToggleButton("Message");
    	start = new JButton("Start");
    	clear = new JButton("Clear");
    	random = new JButton("Random");
    	pause = new JToggleButton("Pause");
        setBackground(Color.white);
        group.add(addNode);
        group.add(addArc);
        group.add(delete);  
        group.add(addSink);
        group.add(addMessage);
        random.addActionListener(a);
        pause.addActionListener(a);
        start.addActionListener(a);
        clear.addActionListener(a);
        addMouseListener(pl);
        addMouseMotionListener(pl);
        pause.setEnabled(false);
        endtime = new JTextField();
        timepassed=new JTextField("0");
        tp = new JLabel("Time passed");
        t = new JLabel("Endtime");
        m = new JLabel("Speed");
        maxCost = new JLabel("Max. Cost: ");
        multiplier = new JTextField("1");
    }
    
    protected void paintComponent(Graphics g){
    	this.removeAll();

    	addNode.setBounds((int) (this.getWidth()*0.5)-45-360, 10, 90, 25);
    	addArc.setBounds((int) (this.getWidth()*0.5)-45-270, 10, 90, 25);
    	addMessage.setBounds((int) (this.getWidth()*0.5)-45-180, 10, 90, 25);
    	addSink.setBounds((int) (this.getWidth()*0.5)-45-90, 10, 90, 25);
    	delete.setBounds((int) (this.getWidth()*0.5)-45, 10, 90, 25);
        random.setBounds((int) (this.getWidth()*0.5)+45, 10, 90, 25);
    	start.setBounds((int) (this.getWidth()*0.5)+45+90, 10, 90, 25);
        pause.setBounds((int) (this.getWidth()*0.5)+45+180, 10, 90, 25);
    	clear.setBounds((int) (this.getWidth()*0.5)+45+270, 10, 90, 25);
    	this.add(addNode);
        this.add(addArc);
        this.add(delete);
        this.add(addSink);
        this.add(addMessage);
        this.add(start);
        this.add(clear);
        this.add(pause);
        this.add(random);
        multiplier.setBounds(this.getWidth()-70, this.getHeight()-55, 25, 15);
        m.setBounds(this.getWidth()-100-20, this.getHeight()-55, 75, 15);
        this.add(multiplier);
        this.add(m);
    	if(firstpaint) {
    		p = new Ellipse2D.Double(600-7,this.getHeight()-50-7,15,15);
    		timeline1=new Line2D.Double(10, p.getY()+7, p.getX()+7, p.getY()+7);
    		timeline2=new Line2D.Double(10,  p.getY()+7, 10,  p.getY()+7);
    		firstpaint=false;
    	}
    	else{
    		p.setFrame(p.getX(),this.getHeight()-50-7,15,15);
    		timeline1.setLine(10, p.getY()+7, p.getX()+7, p.getY()+7);
    		timeline2.setLine(10, p.getY()+7, timeline2.getX2(), p.getY()+7);
    	}
    	endtime.setBounds((int)p.getX()-8, (int)p.getY()+15, 33, 15);
    	timepassed.setBounds((int)p.getX()-8, (int)p.getY()+30, 33, 15);
    	t.setBounds((int)p.getX()-70, (int)p.getY()+15, 100, 15);
    	tp.setBounds((int) p.getX()-95, (int)p.getY()+30, 100, 15);
    	this.add(t);
    	this.add(tp);
    	this.add(endtime);
    	this.add(timepassed);
    	maxCost.setBounds(20, this.getHeight()-75, 175,15);
    	this.add(maxCost);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));   
        g.setColor(Color.black);
        for(Node[] arc :arcList){
			Line2D line = new Line2D.Double(
					arc[0].getN().getX()+arc[0].getN().getHeight()*0.5, 
					arc[0].getN().getY()+arc[0].getN().getWidth()*0.5, 
					arc[1].getN().getX()+arc[1].getN().getHeight()*0.5, 
					arc[1].getN().getY()+arc[1].getN().getWidth()*0.5);
            g2.draw(line);
        }
        Color color;
        for(int j = 0; j < pointList.size(); j++){
            boolean b = false;
            Node cur = pointList.get(j);
            if(cur == selectedPoint){
            	color = Color.red;
            }
            else if(cur==sink)
            	color = Color.green;
            else {
            	for(Message m : message){
            		if(m.getReleaseNode()==cur){
                			b=true;	
                			break;
                		}
            		}
            	if(b)color=Color.orange;
            	else color=Color.black;
            }
            g2.setPaint(color);
            g2.fill(pointList.get(j).getN());
            if(pointList.get(j)!=sink){
            	pointList.get(j).getC().setBounds(
            			(int)cur.getN().getX()-23,(int)cur.getN().getY()+30, 30, 15);
        		pointList.get(j).getT().setBounds(
        				(int)cur.getN().getX()-23,(int)cur.getN().getY()+45, 30, 15);
        		pointList.get(j).getCost().setBounds(
        				(int)cur.getN().getX()+7,(int)cur.getN().getY()+30, 40, 15);
        		pointList.get(j).getTime().setBounds(
        				(int)cur.getN().getX()+7,(int)cur.getN().getY()+45, 40, 15);
        		this.add(cur.getCost());
        		this.add(cur.getTime());
        		this.add(cur.getC());
        		this.add(cur.getT());
        	}
        }
    	int [] numofMes = new int [pointList.size()];
        for(int j=0; j<message.size(); j++){
        	Node r = message.get(j).getReleaseNode();
        	int p = pointList.indexOf(r);
        	if(numofMes[p]==0){
        		JLabel rd = new JLabel("Realese at");
        		JLabel dl = new JLabel("Deadline");
        		rd.setBounds((int)r.getN().getX()+25, 
        			(int)r.getN().getY()-8, 65, 15);
        		dl.setBounds((int)r.getN().getX()+25, 
        			(int)r.getN().getY()+7, 65, 15);
        		r.setRd(rd);
        		r.setDl(dl);
        		this.add(rd);
        		this.add(dl);	
        	}
        	message.get(j).getReleaseDate().setBounds((int)r.getN().getX()+25+numofMes[p]*40+65, 
        			(int)r.getN().getY()-8, 40, 15);
        	message.get(j).getDeadline().setBounds((int)r.getN().getX()+25+numofMes[p]*40+65, 
        			(int)r.getN().getY()+7, 40, 15);

        	JLabel n = new JLabel("Mes " + (j+1));
        	n.setBounds((int)r.getN().getX()+25+numofMes[p]*40+65, 
        			(int)r.getN().getY()-23, 40, 15);
        	n.addMouseListener(pl);
        	n.addMouseMotionListener(pl);
        	n.setOpaque(true);
        	message.get(j).setName(n);
        	this.add(message.get(j).getDeadline());
        	this.add(message.get(j).getReleaseDate());
        	this.add(message.get(j).getName());
        	numofMes[p]++;
        }
        
 
    	g2.setColor(Color.red);
        for(int i=0; i<v.size(); i++){
        	g2.draw(triangleList.get(v.get(i)).getTriangle());
        	g2.fill(triangleList.get(v.get(i)).getTriangle());	
        }
        g2.setPaint(Color.black);
        g2.draw(timeline1);
        g2.setPaint(Color.red);
        g2.draw(timeline2);
        g2.setPaint(Color.black);
        g2.fill(p);
    }
	public JLabel getMaxC(){
    	return maxCost;
    }
    public JToggleButton getNodeButton(){
    	return addNode;
    }
    public JToggleButton getArcButton(){
    	return addArc;
    }
    public JToggleButton getDeleteButton(){
    	return delete;
    }
    public JToggleButton getSinkButton(){
    	return addSink;
    }
    public JToggleButton getMessageButton(){
    	return addMessage;
    }
    public JButton getRandom(){
    	return random;
    }
    public JButton getStart(){
    	return start;
    }
    public JToggleButton getPauseButton(){
    	return pause;
    }
    public JButton getClear(){
    	return clear;
    }
    public ButtonGroup getGroup(){
    	return group;
    }
    public Node getSink(){
    	return sink;
    }
    public Node getSelectedNode(){
    	return selectedPoint;
    }
    public ArrayList<Node> getPointList(){
        return pointList;
    }
    public ArrayList<Node[]> getArcList(){
    	return arcList;
    }
    public ArrayList<Message> getMessageList(){
    	return message;
    }
    public ArrayList<Integer> getV(){
    	return v;
    }
    public JLabel getT(){
    	return t;
    }
    public JLabel getTP(){
    	return tp;
    }
    public void clearPointList(){
    	pointList.clear();
    }
    public void clearArcList(){
    	arcList.clear();
    }
    public void clearMessageList(){
    	message.clear();
    }
    public void setSelectedPoint(Node e){
        selectedPoint = e;
        repaint();
    }
    public void setSink(Node n){
        sink = n;
        for(int i=0; i<message.size(); i++){
        	if(message.get(i).getReleaseNode()==sink){
        		message.remove(i);
        		i--;
        	}
        }
        selectedPoint = null;
        repaint();
    }
    public void addPoint(Point p){
        Ellipse2D e = new Ellipse2D.Double(p.x - 12, p.y - 12, 25, 25);
        JTextField 	cost = new JTextField();
        JTextField 	time = new JTextField();
        JLabel c = new JLabel("Cost");
        JLabel t = new JLabel("Time");
        pointList.add(new Node(e,cost, time,c,t));
        selectedPoint = null;
        repaint();
    }
    public void addArc(Node n1, Node n2){
    	selectedPoint=null;
    	if(n1==n2){
			repaint();
			return;
    	}
    	for(Node[] arc : arcList){
    		if((arc[0]==n1&&arc[1]==n2) || (arc[0]==n2&&arc[1]==n1)){
    			repaint();
    			return;
    		}
    	}
    	arcList.add(new Node []{n1,n2});
    	repaint();
    }
    public void addMessageNode(Message m){
    	message.add(m);
    	selectedPoint = null;
    	repaint();
    }
    public void addTri(SentPackage p){
    	triangleList.add(p);
    }
    public ArrayList<SentPackage> getTri(){
    	return triangleList;
    }
    public void clearTri(){
    	triangleList.clear();
    }
    public void paintTime(double time){
    	timeline2.setLine(10, this.getHeight()-50, 10+time, p.getY()+7);
    }
    public Ellipse2D getEnd(){
    	return p;
    }
    public JTextField getEndTime(){
    	return endtime;
    }
    public Line2D getTimeLine(){
    	return timeline1;
    }
    public Line2D getredLine(){
    	return timeline2;
    }
    public JTextField getTimePasses(){
    	return timepassed;
    }
    public JTextField getMultiplier(){
    	return multiplier;
    }
    public void setMessageList(ArrayList<Message> l){
    	message=l;
    }
    public void setArcList(ArrayList<Node[]> l){
    	arcList = l;
    }
}

