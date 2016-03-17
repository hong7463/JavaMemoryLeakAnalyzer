import java.util.HashMap;

public class TimeNode {
	long TotalMemUsed;
	HashMap<String, Node> map;
	TimeNode(){
		this.map = new HashMap<String, Node>();
	}
}
