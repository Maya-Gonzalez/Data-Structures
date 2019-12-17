public class AVLTreeMap implements Map {
	class Node {
		String key;
		String value;
		Node left;
		Node right;
		int height;

		public Node(String key, String value) {
			this.key = key;
			this.value = value;
			this.left = null;
			this.right = null;
		}
	}

	private int size = 0;
	private Node root;

	public AVLTreeMap() {
		root = null;
	}

	public int size() {
		return size;
	}

	public int height() {
		return height(root);
	}

	private int height(Node n) {
		if (n == null) {
			return -1; // default value
		}
		return n.height;
	}

	public boolean remove(String key, String value) {
		boolean ifExists;
		// if in the tree
		if (root == null) {
			ifExists = false;
		}
		if (get(key) != null) {
			root = remove(root, key, value);
			ifExists = true;
		} else {
			ifExists = false;
		}

		return ifExists;
	}

	private Node remove(Node node, String key, String value) {
		if (node == null) {
			return node;
		} else if (key.compareTo(node.key) > 0) { // if pos(new key is larger than node-> go right)
			node.right = remove(node.right, key, value);
		} else if (key.compareTo(node.key) < 0) { // if pos(new key is smaller than node-> go left)
			node.left = remove(node.left, key, value);
		} else {// node found
			Node temp;
			if (node.right == null) {// case: one child: only has left
				temp = node.left;
				return temp;
			} else if (node.left == null) { // case: one child: only has right
				temp = node.right;
				return temp;
			} else if (node.left == null && node.right == null) { // case: leaf node
				node = null;
				return node;

			}

			else {// two children, copy over largest value/ key of node that will replace the
					// removed node
				temp = node.left;
				while (temp.right != null) {
					temp = temp.right;

				}
				node.key = temp.key;
				node.value = temp.value;

				node.left = remove(node.left, temp.key, temp.value);
				temp = null;
				return node;
			}

		}
		// update height
		node.height = Math.max(height(node.left), height(node.right)) + 1;

		int balance = balanceFactor(node);
		if (balance > 1) {
			// LEFT Case
			node = rotateRight(node);
		}
		if (balance > 1 && key.compareTo(node.left.key) < 0) {
			// LEFT RIGHT Case
			node.left = rotateLeft(node.left);
			node = rotateRight(node);
		}
		if (balance < -1 && key.compareTo(node.left.key) <= 0) {
			// RIGHT CASE
			node = rotateLeft(node);
		}
		if (balance < -1 && key.compareTo(node.right.key) > 0) {
			// RIGHT LEFT Case
			node.right = rotateRight(node.right);
			node = rotateLeft(node);
		}

		return node;
	}

	public boolean put(String key, String value) {
		boolean ifExists;
		if (get(key) != null) {// key already in map -> update value

			ifExists = true;
		} else {
			ifExists = false;
		}
		root = put(root, key, value);
		return ifExists;
	}

	private Node put(Node node, String key, String value) {
		if (node == null) {
			size++;
			return new Node(key, value);
		}

		if (key.compareTo(node.key) < 0) {

			node.left = put(node.left, key, value);
		}
		if (key.compareTo(node.key) > 0) {

			node.right = put(node.right, key, value);
		} else if (key.compareTo(node.key) == 0) {
			node.value = value;

		}
		// update height
		node.height = Math.max(height(node.left), height(node.right)) + 1; // update height

		if (balanceFactor(node) > 1) {
			// LEFT Case
			node = rotateRight(node);
		}
		if (balanceFactor(node) < -1 && key.compareTo(node.right.key) < 0) {
			// RIGHT LEFT Case
			node.right = rotateRight(node.right);
			node = rotateLeft(node);
		}
		if (balanceFactor(node) < -1) {
			// RIGHT CASE
			node = rotateLeft(node);
		}
		if (balanceFactor(node) > 1 && key.compareTo(node.left.key) > 0) {
			// LEFT RIGHT Case
			node.left = rotateLeft(node.left);
			node = rotateRight(node);
		}

		return node;

	}

	public Node rotateLeft(Node node) {
		Node n = node.right;
		Node n2 = n.left;
		n.left = node;
		node.right = n2;

		// update heights
		node.height = Math.max(height(node.left), height(node.right)) + 1;
		n.height = Math.max(height(n.left), height(n.right)) + 1;
		return n;
	}

	public Node rotateRight(Node node) {
		Node n = node.left;
		Node n2 = n.right;
		n.right = node;
		node.left = n2;

		// update heights
		node.height = Math.max(height(node.left), height(node.right)) + 1;
		n.height = Math.max(height(n.left), height(n.right)) + 1;
		return n;
	}

	public int balanceFactor(Node n) {
		if (n == null) {
			return 0;
		}
		return height(n.left) - height(n.right);
	}

	public Node get(Node node, String key) {

		if (node != null) {

			int compare = key.compareTo(node.key);

			if (compare < 0) {
				return get(node.left, key);
			} else if (compare > 0) {
				return get(node.right, key);
			} else {
				return node;
			}
		}
		return null;
	}

	public String get(String key) {
		Node n = get(root, key);
		if (n == null) {
			return null;
		}
		return n.value;
	}

	public void clear() {
		root = null;
		size = 0;
	}

	public void print() {
		this.print(this.root, "", 0);
	}

	private void print(Node node, String prefix, int depth) {
		if (node == null) {
			return;
		}
		for (int i = 0; i < depth; i++) {
			System.out.print("  ");
		}
		if (!prefix.equals("")) {
			System.out.print(prefix);
			System.out.print(":");
		}
		System.out.print(node.key);
		System.out.print(" (");
		System.out.print("H:");
		System.out.print(node.height);
		System.out.println(")");
		this.print(node.left, "L", depth + 1);
		this.print(node.right, "R", depth + 1);
	}

	public String preorderString() {
		return this.preorderString(this.root);
	}

	private String preorderString(Node node) {
		if (node == null) {
			return "()";
		}
		return "(" + node.key + " " + this.preorderString(node.left) + " " + this.preorderString(node.right) + ")";
	}

	public static void main(String[] args) {
		AVLTreeMap map = new AVLTreeMap();
		String[] keys = { "7", "9", "6", "0", "4", "2", "1" };
		String[] values = { "seven", "nine", "six", "zero", "four", "two", "one" };

		// insert all keys
		for (int i = 0; i < keys.length; i++) {
			boolean exists = map.put(keys[i], values[i]);
			if (exists) {
				System.out.println("Failed to insert key " + keys[i] + " and value " + values[i]);
				return;
			}
		}

		// check size
		if (map.size() != keys.length) {
			System.out.println("Map should have size() = " + Integer.toString(keys.length)
					+ " after insertion of numbers " + "but had size " + Integer.toString(map.size()) + " instead");
			return;
		}

		// retrieve all keys and check their values
		for (int i = 0; i < keys.length; i++) {
			String value = map.get(keys[i]);
			if (!value.equals(values[i])) {
				System.out.println(
						"Expected " + values[i] + " from retrieve key " + keys[i] + " " + "got " + value + " instead");
			}
		}
		map.print();
		map.remove("7", "seven");
		map.remove("6", "six");
		System.out.println("Deleting 9");
		map.remove("9", "nine");
		map.print();
		map.clear();

		// check size
		if (map.size() != 0) {
			System.out.println("Map should have size() = 0 after clear() " + "but had size "
					+ Integer.toString(map.size()) + " instead");
			return;

		} else {
			System.out.println("Map had size zero");
		}

		map.put("doe", "A deer, a female deer.");
		map.put("ray", "A drop of golden sun.");
		map.put("me", "A name I call myself.");
		map.put("far", "A long long way to run.");

		// check size
		if (map.size() != 4) {
			System.out.println("Map should have size() = 4 after insertion of musical keys " + "but had size "
					+ Integer.toString(map.size()) + " instead");
			return;
		}

		if (!map.get("ray").equals("A drop of golden sun.")) {
			System.out.println("Expected \"A drop of golden sun.\" from retrieve key \"ray\" " + "got \""
					+ map.get("ray") + "\" instead");
			return;
		}

		return;
	}

}
