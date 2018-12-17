package WhiteBox;

import Utils.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class BinaryTreeSerializerImpl implements BinaryTreeSerializer {
    public static HashSet<String> hash_set = new HashSet<String>();



    public String serialize(TreeNode root) {
        if (root == null){
            hash_set.add("A");
            //System.out.println(hash_set);
            return "[null]";
        }
        hash_set.add("B");
        System.out.println(hash_set);
        List<TreeNode> level = new ArrayList<TreeNode>();
        level.add(root);
        String s = serializeLevel(level);
        //cut last ','
        s = s.substring(0, s.length() - 1);
        return "[" + s + "]";
    }

    private String serializeLevel(List<TreeNode> level) {
        //to check if the next level exists
        boolean anyNode = false;
        StringBuilder sb = new StringBuilder();
        for (TreeNode node : level) {
            hash_set.add("C");
            System.out.println(hash_set);
            if (node != null) {
                hash_set.add("D");
                System.out.println(hash_set);
                anyNode = true;
                sb.append("" + node.val + ",");
            } else{
                hash_set.add("E");
                System.out.println(hash_set);
                sb.append("null,");
            }
            hash_set.add("F");
            System.out.println(hash_set);
        }
        if (!anyNode){
            hash_set.add("G");
            System.out.println(hash_set);
            return sb.toString();
        }
        hash_set.add("H");
        System.out.println(hash_set);
        //if next level exists -> process
        List<TreeNode> levelNext = new ArrayList<TreeNode>();
        for (TreeNode node : level) {
            if (node != null) {
                hash_set.add("I");
                System.out.println(hash_set);
                levelNext.add(node.left);
                levelNext.add(node.right);
            }
            hash_set.add("J");
            System.out.println(hash_set);
        }
        hash_set.add("K");
        System.out.println(hash_set);
        sb.append(serializeLevel(levelNext));
        return sb.toString();
    }


    public TreeNode deserialize(String data) {
        //check if data is valid
        if (data.length() < 3){
            hash_set.add("L");
            System.out.println(hash_set);
            return null;
        }
        hash_set.add("M");
        System.out.println(hash_set);
        //remove "[" and "]"
        data = data.substring(1, data.length() - 1);
        //separate to parts
        String[] parts = data.split(",");
        List<TreeNode> level = new ArrayList<TreeNode>();
        TreeNode node = getNode(parts[0]);
        if (node != null) {
            hash_set.add("N");
            System.out.println(hash_set);
            level.add(node);
            deserializeRec(parts, 1, level);
        }
        hash_set.add("O");
        System.out.println(hash_set);
        return node;
    }

    private void deserializeRec(String[] parts, int i, List<TreeNode> level) {
        if (i >= parts.length){
            hash_set.add("P");
            System.out.println(hash_set);

            return;
        }

        if (level.size() == 0) {
            hash_set.add("Q");
            System.out.println(hash_set);

            return;
        }

        List<TreeNode> nextLevel = new ArrayList<TreeNode>();
        for (TreeNode n : level) {
            n.left = getNode(parts[i++]);
            n.right = getNode(parts[i++]);

            hash_set.add("R");
            System.out.println(hash_set);
            if (n.left != null){
                hash_set.add("S");
                System.out.println(hash_set);
                nextLevel.add(n.left);
            }
            if (n.right != null) {
                hash_set.add("T");
                System.out.println(hash_set);
                nextLevel.add(n.right);
            }
        }
        hash_set.add("U");
        System.out.println(hash_set);
        deserializeRec(parts, i, nextLevel);
    }

    private TreeNode getNode(String s) {
        if (s.charAt(0) == 'n'){
            hash_set.add("V");
            System.out.println(hash_set);
            return null;
        }
        hash_set.add("W");
        System.out.println(hash_set);
        return new TreeNode(Integer.parseInt(s));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

