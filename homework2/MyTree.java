
public class MyTree<T extends Comparable<T>> {
    private MyNode<T> root;
    private int size;

    public MyTree() {
        this.root = null;
        this.size=0;
    }

    public MyNode<T> getRoot(){
        return root;
    }

    public MyNode<T> insert(T item){
        if (item == null) {throw new IllegalArgumentException ("Cannot insert null item");}
        
        //empty tree
        if (root==null){
            root = new MyNode<>(item);
            size++;
            return root;
        }
        
        //Transverse the tree 
        MyNode<T> cur=root;
        while (true){
            int cmp = item.compareTo(cur.getItem());
            //If target is less than current node, go for the left tree
            if (cmp<0) {
                //empty space on the left - plug it in, else transverse to the left
                if(cur.getLeft() == null){
                    MyNode<T> newNode = new MyNode<>(item);
                    newNode.setParent(cur);
                    cur.setLeft(newNode);
                    size++;
                    return newNode;
                }
                cur=cur.getLeft();
            } 
            //If target is greater than current node, go for the right tree 
            else if (cmp >0){
                if (cur.getRight() == null){
                    MyNode<T> newNode = new MyNode<>(item);
                    newNode.setParent(cur);
                    cur.setRight(newNode);
                    size++;
                    return newNode;
                }
                cur=cur.getRight();
            }
            // equals
            else{
                return cur;
            }
        }

    }

    public MyNode<T> contains(T item){
        if (item == null) {throw new IllegalArgumentException ("Cannot insert null item");}
        MyNode<T> cur=root;
        while(cur!=null){
            int cmp=item.compareTo(cur.getItem());
            //target < cur, cur move to left tree
            if (cmp<0){
                cur=cur.getLeft();
            //target > cur, cur move to right tree
            }else if (cmp >0){
                cur=cur.getRight();
            //target == cur, return cur node
            }else{
                return cur;
            }
        }
        return null;
    }
    // remove node from tree
    public boolean remove (T item){
        if (item == null) {throw new IllegalArgumentException ("Cannot insert null item");}

        MyNode<T> cur=root;
        while(cur!=null){
            int cmp=item.compareTo(cur.getItem());
            //target < cur, cur move to left tree
            if (cmp<0){
                cur=cur.getLeft();
            //target > cur, cur move to right tree
            }else if (cmp >0){
                cur=cur.getRight();
            //target == cur, return cur node
            }else{
                removeNode(cur);
                size--;
                return true;
            }
        }
        return false;
    }
    // helper method to remove Node
    private void removeNode(MyNode<T> node) {
        if (node.getLeft() == null) {
            replaceNodeInParent(node, node.getRight());
        } else if (node.getRight() == null) {
            replaceNodeInParent(node, node.getLeft());
        } else {
            MyNode<T> successor = findMin(node.getRight());
            if (successor.getParent() != node) {
                replaceNodeInParent(successor, successor.getRight());
                successor.setRight(node.getRight());
                if (successor.getRight() != null) successor.getRight().setParent(successor);
            }
            successor.setLeft(node.getLeft());
            if (successor.getLeft() != null) successor.getLeft().setParent(successor);
            replaceNodeInParent(node, successor);
        }
    }

    private void replaceNodeInParent(MyNode<T> node, MyNode<T> replacement) {
        if (node.getParent() == null) this.root = replacement;
        else {
            if (node == node.getParent().getLeft()) node.getParent().setLeft(replacement);
            else node.getParent().setRight(replacement);
        }
        if (replacement != null) replacement.setParent(node.getParent());
    }
    // Find min in tree
    private MyNode<T> findMin(MyNode<T> node) {
        while (node.getLeft() != null) node = node.getLeft();
        return node;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        inOrderTraversal(root, sb);
        return sb.toString();
    }
    // In Order Traversal
    private void inOrderTraversal(MyNode<T> node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.getLeft(), sb);
        if (sb.length() > 0) sb.append(", ");
        sb.append(node.getItem());
        inOrderTraversal(node.getRight(), sb);
    }

    public int getSize(){
        return size;
    }

}
