import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

public class PointLocater extends MouseAdapter{
    private PointPanel pointPanel;
    private boolean pselected;
    private Node selected;
    private Point2D.Double offSet;
    public PointLocater(PointPanel pp)
    {
        pointPanel = pp;
    }
    public void mousePressed(MouseEvent e)
    {
    	Point p = e.getPoint();
    	
    	if(pointPanel.getEnd().contains(p)){
			pselected = true;
			offSet = new Point2D.Double(p.x - pointPanel.getEnd().getX(), p.y - pointPanel.getEnd().getY());
			return;
		}
    	if(pointPanel.getNodeButton().isSelected()){
    		if(p.x>=pointPanel.getNodeButton().getX()&&
    				p.x<=pointPanel.getClear().getX()+90&&
    				p.y<=10){
    			return;
    		}
    		boolean haveSelection = false;
    		List<Node> list = pointPanel.getPointList();
    		Ellipse2D ellipse;
    		for(int j = 0; j < list.size(); j++)
    		{
    			ellipse = list.get(j).getN();
    			if(ellipse.contains(p))
    			{
    				offSet = new Point2D.Double(p.x - ellipse.getX(), p.y - ellipse.getY());
    				selected = list.get(j);
    				haveSelection = true;
    				break;
    			}
    		}
    		if(!haveSelection){
    			selected = null;
    			pointPanel.addPoint(p);
    		}
    	}
    	if(pointPanel.getArcButton().isSelected()){
    		if(pointPanel.getPointList().size()<=1){
    			return;
    		}
        	List<Node> list = pointPanel.getPointList();
        	Node node;
        	for(int j = 0; j < list.size(); j++){
        		node = list.get(j);
        		if(node.getN().contains(p)){
        			if(pointPanel.getSelectedNode()!=null){
        				pointPanel.addArc(pointPanel.getSelectedNode(), node);
        				break;
        			}
        			if(pointPanel.getSelectedNode()==null){
            			pointPanel.setSelectedPoint(node);
            			break;
        			}
        		}
        	}
    	}
    	if(pointPanel.getDeleteButton().isSelected()){
    		ArrayList<Node> listp = pointPanel.getPointList();
    		ArrayList<Node[]> lista = pointPanel.getArcList();
    		ArrayList<Message> listm = pointPanel.getMessageList();
    		Node n;
    		Line2D line;
    		//message
    		for(int j=0; j<listm.size(); j++){
    			if(pointPanel.getMessageList().get(j).getName().getBackground()==Color.gray){
    				pointPanel.getMessageList().remove(j);
    				pointPanel.repaint();
    				return;
    			}
    		}
    		//points
    		for(int j = 0; j < listp.size(); j++){
    			n = listp.get(j);
    			if(n.getN().contains(p)){
    				if(n==pointPanel.getSink()){
    					pointPanel.setSink(null);
    				}
    				for(int k=0; k<lista.size(); k++){
    					Node [] arc = lista.get(k);
    					if(arc[0]==n||arc[1]==n){
    						pointPanel.getArcList().remove(arc);
    						k--;
    					}
    				}
    				for(int i=0; i<pointPanel.getMessageList().size(); i++){
    					if(pointPanel.getMessageList().get(i).getReleaseNode()==listp.get(j)){
        					pointPanel.getMessageList().remove(i);
        					i--;
    					}
    				}
    				pointPanel.getPointList().remove(j);
    				pointPanel.repaint();
    				return;
    			}
    		}
    		//arch
    		for(Node[] arc : lista){
    			line = new Line2D.Double(
    					arc[0].getN().getX()+arc[0].getN().getHeight()*0.5,
    					arc[0].getN().getY()+arc[0].getN().getWidth()*0.5,
    					arc[1].getN().getX()+arc[1].getN().getHeight()*0.5,
    					arc[1].getN().getY()+arc[1].getN().getWidth()*0.5);
    			if (line.intersects(p.getX()-10, p.getY()-10, 20, 20)) {
    				pointPanel.getArcList().remove(arc);
    				pointPanel.repaint();
    				return;
    		    }   
    		}
    	}
    	if(pointPanel.getSinkButton().isSelected()){
    		List<Node> list = pointPanel.getPointList();
    		Ellipse2D ellipse;
    		for(int j = 0; j < list.size(); j++)
    		{
    			ellipse = list.get(j).getN();
    			if(ellipse.contains(p))
    			{
    				pointPanel.setSink(list.get(j));
    				break;
    			}
    		}
    	}
    	
    	if(pointPanel.getMessageButton().isSelected()){
    		List<Node> list = pointPanel.getPointList();
    		Ellipse2D ellipse;
    		for(int j = 0; j < list.size(); j++)
    		{
    			ellipse = list.get(j).getN();
    			if(ellipse.contains(p))
    			{
    				JTextField r = new JTextField();
    				JTextField d = new JTextField();
    				Message m = new Message(list.get(j),r, d);
    				pointPanel.addMessageNode(m);
    				break;
    			}
    		}
    	}
    }
    public void mouseDragged(MouseEvent e){
    	if(selected!=null){
    		 double x = e.getX() - offSet.x;
             double y = e.getY() - offSet.y;
    		 Rectangle2D bounds = selected.getN().getBounds2D();
             selected.getN().setFrame(x, y, bounds.getWidth(), bounds.getHeight());
             pointPanel.repaint();
    	}
    	
    	if(pselected){
    		double x = e.getX() - offSet.x;
            Rectangle2D bounds = pointPanel.getEnd().getBounds2D();
            bounds.setFrame(new Rectangle2D.Double(x, bounds.getY(), bounds.getWidth(), bounds.getHeight()));
            pointPanel.getEnd().setFrame(bounds);
            pointPanel.repaint();
    	}
    	
    }
    public void mouseReleased(MouseEvent e) {
    	selected = null;
    	pselected = false;
    }
    public void mouseEntered(MouseEvent e){
    	for(int i=0; i<pointPanel.getMessageList().size(); i++){
    		if(e.getSource()==pointPanel.getMessageList().get(i).getName()){
    			pointPanel.getMessageList().get(i).getName().setBackground(Color.gray);
    		}
    	}
    }
    public void mouseExited(MouseEvent e){
    	for(int i=0; i<pointPanel.getMessageList().size(); i++){
    		if(e.getSource()==pointPanel.getMessageList().get(i).getName()){
    			pointPanel.getMessageList().get(i).getName().setBackground(Color.white);
    		}
    	}
    }
}
