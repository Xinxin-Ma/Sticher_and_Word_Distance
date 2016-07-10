
/**
 * PathNode object: node in a linked list of minimum-cost alignment points. Do
 * not modify this file
 * 
 */

public class PathNode {
	public int row;
	public int col;
	public int cost;
	public PathNode next;

	public PathNode(int row, int col, int cost, PathNode next) {
		this.row = row;
		this.col = col;
		this.cost = cost;
		this.next = next;
	}
}