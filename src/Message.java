import javax.swing.JLabel;
import javax.swing.JTextField;

public class Message {
	private Node releaseNode;
	private JTextField releaseDate;
	private JTextField deadline;
	private JLabel name;
	public Message (Node n, JTextField r, JTextField d){
		setReleaseNode(n);
		setReleaseDate(r);
		setDeadline(d);
	}
	public Node getReleaseNode() {
		return releaseNode;
	}
	public void setReleaseNode(Node releaseNode) {
		this.releaseNode = releaseNode;
	}
	public JTextField getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(JTextField releaseDate) {
		this.releaseDate = releaseDate;
	}
	public JTextField getDeadline() {
		return deadline;
	}
	public void setDeadline(JTextField deadline) {
		this.deadline = deadline;
	}
	public JLabel getName() {
		return name;
	}
	public void setName(JLabel name) {
		this.name = name;
	}
}
