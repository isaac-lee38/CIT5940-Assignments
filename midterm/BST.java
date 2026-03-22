/*
package midterm;

class TreeNode {
    int val;
    TreeNode left, right;

    TreeNode(int val) {
        this.val = val;
        this.left = null;
        this.right = null;
    }
}

*/


package midterm;

public class BST {

    // --- FIND ---
    public TreeNode find(TreeNode root, int key) {
        // key is the value
        if (root==null|| key==root.val){
            return root;
        }else if (key > root.val){
            return find (root.right,key);
        }
        return find (root.left,key);
    }

    // --- INSERT ---
    public TreeNode insert(TreeNode root, int key) {
        if (root==null){ return new TreeNode(key);}
        else if (key > root.val){
            root.right = insert (root.right,key);
        }
        else if (key < root.val){
            root.left = insert (root.left,key);
        }
        return root;
    }

    // --- DELETE ---
    public TreeNode delete(TreeNode root, int key) {
        if (root == null) return null;

        if (key<root.val){
            root.left=delete(root.left,key);
        }else if (key>root.val){
            root.right=delete(root.right,key);
        } else{
            // one child or no child
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            //two children
            //take the min vlaue of the smallest right tree
            root.val = getMinValue(root.right);
            // remove the value from the subtree
            root.right =delete(root.right,root.val);

        }
        return root;
    }

    private int getMinValue(TreeNode root) {
        if (root.left==null){return root.val;}
        return getMinValue(root.left);
    }
}