import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JOptionPane;

//Algorithm
public class Action implements ActionListener{
	private PointPanel pp;
	private double[][] pairLP; //arc;message
	private int[][] pairILP; //arc;message
	private int n; //#message
	private int a; //#Arc
	Animation animate;
	public Action(PointPanel p){
		pp = p;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == pp.getClear()){
			if(animate!=null){
				animate.getTimer().stop();
				animate=null;
			}
			pp.removeAll();
			pp.getGroup().clearSelection();
			pp.clearPointList();
			pp.clearMessageList();
			pp.clearArcList();
			pp.getTri().clear();
			pp.getV().clear();
			Line2D l = pp.getredLine();
			pp.getredLine().setLine(l.getX1(), l.getY1(), 10, l.getY2());
			pp.getMaxC().setText("Max. Cost: ");
			pp.getEndTime().setText("");
			pp.getTimePasses().setText("0");
			pp.repaint();
			pp.getPauseButton().setEnabled(false);
			pp.getNodeButton().setEnabled(true);
			pp.getArcButton().setEnabled(true);
			pp.getMessageButton().setEnabled(true);
			pp.getSinkButton().setEnabled(true);
			pp.getDeleteButton().setEnabled(true);
			pp.getRandom().setEnabled(true);

		}
		if(e.getSource()==pp.getRandom()){
    		pp.repaint();
    		for(int i=0; i<pp.getMessageList().size(); i++){
    			int r = (int)(Math.random()*51);
    			pp.getMessageList().get(i).getReleaseDate().setText(String.valueOf(r));
    			int d = (int)(Math.random()*(100-r))+r;
    			pp.getMessageList().get(i).getDeadline().setText(String.valueOf(d));
    		}
    		for(int i=0; i<pp.getPointList().size(); i++){
    			int c = (int)(Math.random()*41+1);
    			pp.getPointList().get(i).getCost().setText(String.valueOf(c));
    			int t = (int)(Math.random()*41+1);
    			pp.getPointList().get(i).getTime().setText(String.valueOf(t));
    		}
    	}
		if(pp.getPauseButton().isSelected()){
			if(animate!=null){
				animate.getTimer().stop();
			}
		}
		if(!pp.getPauseButton().isSelected()){
			if(animate!=null){
				animate.getTimer().start();
			}
		}
		if(e.getSource() == pp.getStart()){
			if(animate!=null){
				animate.getTimer().stop();
				Line2D l = pp.getredLine();
				pp.getredLine().setLine(l.getX1(), l.getY1(), 10, l.getY2());
				pp.getTri().clear();
				pp.getV().clear();
				animate=null;
			}
			if(pp.getPauseButton().isSelected()){
				pp.getPauseButton().doClick();
			}
			pp.getGroup().clearSelection();
			pp.repaint();
			if(pp.getSink()==null){
				JOptionPane.showMessageDialog(pp, "No Sink avaiable");
				return;
			}
			if(pp.getPointList().isEmpty()){
				JOptionPane.showMessageDialog(pp, "No Points avaiable");
				return;
			}
			if(pp.getPointList().size()>pp.getArcList().size()+1){
				JOptionPane.showMessageDialog(pp, "Not connected");
				return;
			}
			boolean somethingwrong = false;
			for(int i=0; i<pp.getPointList().size(); i++){
				if(pp.getPointList().get(i)==pp.getSink()){
					continue;
				}
				try{
					Integer.parseInt(pp.getPointList().get(i).getCost().getText());
				}catch(NumberFormatException ex){
					somethingwrong = true;
				}
				try{
					Integer.parseInt(pp.getPointList().get(i).getTime().getText());
				}catch(NumberFormatException ex){
					somethingwrong = true;
				}
			}
			for(int i=0; i<pp.getMessageList().size(); i++){
				try{
					Integer.parseInt(pp.getMessageList().get(i).getDeadline().getText());
				}catch(NumberFormatException ex){
					somethingwrong = true;
				}
				try{
					Integer.parseInt(pp.getMessageList().get(i).getReleaseDate().getText());
				}catch(NumberFormatException ex){
					somethingwrong = true;
				}
			}
			if(somethingwrong){
				JOptionPane.showMessageDialog(pp, "Please insert Integer\n Speed can be in decimal");
				return;
			}
			try{
				Double.parseDouble(pp.getMultiplier().getText());
			}catch(NumberFormatException ex){
				JOptionPane.showMessageDialog(pp, "Please insert decimal number as multiplier");
				return;
			}
			// directed graph plus circle detect
			//sort by distance
			//BFS
			//tail->head
			//[0]->[1]
			ArrayList<Node[]> newList = new ArrayList<Node[]>();
			ArrayList<Node[]> copy = new ArrayList<Node[]>();
			copy.addAll(pp.getArcList());
			ArrayList<Node> queue = new ArrayList<Node>();
			queue.add(pp.getSink());
			boolean[] visited = new boolean[pp.getPointList().size()];
			visited[pp.getPointList().indexOf(pp.getSink())]=true;
			while(!queue.isEmpty()){
				Node level = queue.remove(0);
				for(int i=0; i<copy.size(); i++){
					Node[] current = copy.get(i);
					if(current[0]==level){
						if(visited[pp.getPointList().indexOf(current[1])]){
							JOptionPane.showMessageDialog(pp, "Circle detected");
							return;
						}
						newList.add(new Node[]{current[1],current[0]});
						queue.add(current[1]);
						visited[pp.getPointList().indexOf(current[1])]=true;
						copy.remove(current);
						i--;
					}
					if(current[1]==level){
						if(visited[pp.getPointList().indexOf(current[0])]){
							JOptionPane.showMessageDialog(pp, "Circle detected");
							return;
						}
						newList.add(current);
						queue.add(current[0]);
						visited[pp.getPointList().indexOf(current[0])]=true;
						copy.remove(current);
						i--;
					}
				}
			}
			pp.setArcList(newList);
			
			//throw message away, which can not reach the deadline in time
			int maxd = -1; //max Deadline
			ArrayList<Integer> r = new ArrayList<Integer>(); //earliest possible arrival time
			for(int i=0; i<pp.getMessageList().size(); i++){
				Node s = pp.getMessageList().get(i).getReleaseNode();
				int time = Integer.parseInt(pp.getMessageList().get(i).getReleaseDate().getText());
				for(int j=pp.getArcList().size()-1; j>=0; j--){
					if(pp.getArcList().get(j)[0]==s){
						time += Integer.parseInt(pp.getArcList().get(j)[0].getTime().getText());
						s=pp.getArcList().get(j)[1];
						if(s==pp.getSink()){
							break;
						}
					}
				}
				int currdeadline = Integer.parseInt(
						pp.getMessageList().get(i).getDeadline().getText());
				if(time>currdeadline){
					pp.getMessageList().remove(i);
					i--;
					continue;
				}
				if(maxd<currdeadline){
					maxd=currdeadline;
				}
				r.add(time);
			}
			//If entry in endtime is invalid set it as maxd
			try{
				Integer.parseInt(pp.getEndTime().getText());
			}catch(NumberFormatException ex){
				if(maxd!=-1)
				pp.getEndTime().setText(String.valueOf(maxd));
			}
			pp.repaint();
			if(pp.getMessageList().isEmpty()){
				JOptionPane.showMessageDialog(pp, "No Message will arrive at the Sink");
				return;
			}
			
			n = pp.getMessageList().size();
			a = pp.getArcList().size();
			pairLP = new double[a][n];
			pairILP = new int [a][n];
			
			//sort message by deadline
			ArrayList<Integer> rsorted = new ArrayList<Integer>();
			ArrayList<Message> sortedList = new ArrayList<Message>();
			sortedList.add(pp.getMessageList().get(0));
			rsorted.add(r.get(0));
			for(int i=1; i<n; i++){
				for(int k=0; k<sortedList.size(); k++){
					if(Integer.parseInt(sortedList.get(k).getDeadline().getText())
							>Integer.parseInt(pp.getMessageList().get(i).getDeadline().getText())){
						sortedList.add(k, pp.getMessageList().get(i));
						rsorted.add(k, r.get(i));
						break;
					}
					if(k+1==sortedList.size()){
						sortedList.add(pp.getMessageList().get(i));
						rsorted.add(r.get(i));
						break;
					}
				}
			}
			pp.setMessageList(sortedList);
			
			//headtail for 3.NB
			ArrayList<Integer[]> headtail = new ArrayList<Integer[]>();
			//[0]->[1]
			//tail->head
			for(int i=0; i<pp.getPointList().size(); i++){
				Node c = pp.getPointList().get(i);
				if(c==pp.getSink()){
					continue;
				}
				int head = -1;
				ArrayList<Integer> tail = new ArrayList<Integer>();
				for(int j=0; j<a; j++){
					Node[] current = pp.getArcList().get(j);
					// [0]->c --> c->[1]
					if(current[1]==c){
						tail.add(j);
					}
					if(current[0]==c){
						head=j;
					}
				}
				if(head==-1||tail.isEmpty()){
					continue;
				}
				for(int x=0; x<tail.size(); x++){
					headtail.add(new Integer[]{tail.get(x), head});
				}
			}
			
			//LP
			long[][] m = new long[a+n+headtail.size()*n][n*a+1];
			long [] objective = new long[n*a+1];
			objective [0] = -1;
			long [] b = new long [a+n+headtail.size()*n];
			for(int i=0; i<n; i++){
				b[a+i]=-1;
			}
			
			//1. nb
			for(int i=0; i<a; i++){
				m[i][0]=-1;
				int c = Integer.parseInt(pp.getArcList().get(i)[0].getCost().getText());
				for(int j=0; j<n; j++){
					m[i][1+i*n+j]=c;
				}
			}
			//2.nb
			for(int i=0; i<n; i++){
				Node release = pp.getMessageList().get(i).getReleaseNode();
				int rj = rsorted.get(i);
				int jmin=0;
				int ap=0;
				for(int j=0; j<a; j++){
					if(pp.getArcList().get(j)[0]==release){
						ap = j;
					}
				}
				for(int j=0; j<=i; j++){
					if(Integer.parseInt(pp.getMessageList().get(j).getDeadline().getText())>=rj){
						jmin=j;
						break;
					}
				}
				for(int k=jmin; k<=i; k++){
					m[a+i][1+n*ap+k]=-1;
				}
			}
			//3.nb
			int z = 0;
			for(int i=0; i<headtail.size(); i++){
				int t = headtail.get(i)[0];
				int h = headtail.get(i)[1];
				for(int j=0; j<n; j++){
					m[a+n+z][1+t*n+j]=1;
					m[a+n+z][1+h*n+j]=-1;
					z++;
				}
			}
			for(int i=0; i<m.length; i++){
				for(int j=0; j<m[i].length; j++){
					if(j+1==m[i].length){
						System.out.println(m[i][j]);
					}
					else{
						System.out.print(m[i][j]+ "|");
					}
				}
			}
			Rational[] res = new Rational[a*n+1];
			Rational maxz = 
					Simplex.simplex(Simplex.cnv(m), Simplex.cnv(b), Simplex.cnv(objective), res);
			maxz = maxz.mul(new Rational(-1));
			pp.getMaxC().setText("Max. Cost: " + maxz.longValue());
			
			pp.repaint();
			for(int i = 0; i<res.length; i++){
				if(i+1==res.length)System.out.println(res[i]);
				else System.out.print(res[i]+"|");
			}
			int x = 0;
			for(int i=0; i<a; i++){
				for(int j=0; j<n; j++){
					pairLP[i][j] = res[1+x].doubleValue();
					x++;
					if(j+1==n)System.out.println(pairLP[i][j]);
					else System.out.print(pairLP[i][j]+"|");
				}
			}
			//start calculate
			//all 1
			ArrayList<Integer> modified = new ArrayList<Integer>();
	    	for(Node[] arc : pp.getArcList()){
	    		if(arc[1] == pp.getSink()){
	    			int pos = pp.getArcList().indexOf(arc);
	    			modified.add(pos);
	    			for(int k=0; k<pp.getMessageList().size(); k++){
	    				pairILP[pos][k]=1;
	    			}
	    		}
	    	}
	    
	    	for(int i=0; i<modified.size(); i++){
	    		pairILP[modified.get(i)]=
	    				rounding(pairLP[modified.get(i)], pairILP[modified.get(i)]);
	    	}
	    	
	    	//spreading
	    	while(!modified.isEmpty()){
	    		ArrayList<Integer> nm = new ArrayList<Integer>();
	    		for(Integer i : modified){
	    			Node current = pp.getArcList().get(i)[0];
	    			for(Node[] node : pp.getArcList()){
	    				if(node[1]==current){
	    					int pos = pp.getArcList().indexOf(node);
	    	    			nm.add(pos);
	    	    			//copy
	    	    			for(int k=0; k<pp.getMessageList().size(); k++){
	    	    				pairILP[pos][k]=pairILP[i][k];
	    	    			}
	    				}
	    			}
	    		}
	    		modified.clear();
	    		modified.addAll(nm);
	    		for(int i=0; i<modified.size(); i++){
	        		pairILP[modified.get(i)]=
	        				rounding(pairLP[modified.get(i)], pairILP[modified.get(i)]);
	        	}
	    	}
	    	for(int i=0; i<a; i++){
				for(int j=0; j<n; j++){
					if(j+1==n){
						System.out.println(pairILP[i][j]);
					}
					else{
						System.out.print(pairILP[i][j]+ "|");
					}
				}
			}
	    	pp.getPauseButton().setEnabled(true);
			pp.getNodeButton().setEnabled(false);
			pp.getArcButton().setEnabled(false);
			pp.getMessageButton().setEnabled(false);
			pp.getSinkButton().setEnabled(false);
			pp.getDeleteButton().setEnabled(false);
			pp.getRandom().setEnabled(false);
	    	animate=new Animation(this);	    	
		}		
	}
    public int[] rounding(double[] lp, int[] ilp ){
		for(int i=0; i<n;i++){
			if(ilp[i]==0){
				continue;
			}
			double asum = 0;
			double bsum = 0;
			outloop:
			for(int j=i; j<n; j++){
				asum+=lp[j];
				bsum+=ilp[j];
				if(asum>=1&&bsum==1){
					break;
				}
				if(i!=0){
					double asum2=asum;
					double bsum2=bsum;
					for(int k=i-1; k>=0; k--){
						asum2+=lp[k];
						bsum2+=ilp[k];
						if(asum2>=1&&bsum2==1){
							break outloop;
						}
					}
				}
				if(j+1==n){
					ilp[i]=0;
				}
			}
		}
		return ilp;
	}
	public PointPanel getPP(){
		return pp;
	}
	public int[][] getilp(){
		return pairILP;
	}
}