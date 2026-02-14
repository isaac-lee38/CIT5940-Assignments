public class MyNode<T> {
    private T item;
    private MyNode<T> left;
    private MyNode<T> right;
    private MyNode<T> parent;

    public MyNode(T item) {
        this.item=item;
        this.left=null;
        this.right=null;
        this.parent=null;
    }
    // return current
    public T getItem() {
        return item;
    }

    public MyNode<T> getLeft(){
        return this.left;
    }

    public void setLeft(MyNode<T> left){
        this.left=left;
    }

    public MyNode<T> getRight(){
        return this.right;
    }
    
    public void setRight(MyNode<T> right){
        this.right=right;
    }

    public MyNode<T> getParent(){
        return this.parent;
    }

    public void setParent(MyNode<T> parent){
        this.parent=parent;
    }

}
