public class Node
{
	public String value;
	public Node down;
	public Node right;
	public boolean terminal;
	public boolean initial;

	public Node()
	{
		value = null;
		down = null;
		right = null;
		terminal = true;
		initial = false;
	}

	public Node(String value)
	{
		this.value = value;
		down = null;
		right = null;
		terminal = true;
		initial = false;
	}
}