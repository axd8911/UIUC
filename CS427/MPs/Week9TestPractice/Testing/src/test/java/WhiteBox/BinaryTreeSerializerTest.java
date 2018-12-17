package WhiteBox;

import BlackBox.CycleSort;
import Utils.TreeNode;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BinaryTreeSerializerTest {

    @Test
    public void testSerlializeNull(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        TreeNode root = null;
        String ret = bs.serialize(root);
        assertEquals("[null]",ret);
    }

    @Test
    public void testSerlializeDepth0(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        TreeNode root = new TreeNode(5);
        String ret = bs.serialize(root);
        assertEquals("[5,null,null]",ret);
    }

    @Test
    public void testSerlializeDepth1(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        TreeNode root = new TreeNode(5);
        TreeNode left = new TreeNode(1);
        TreeNode rght = new TreeNode(8);
        root.left = left;
        root.right = rght;

        String ret = bs.serialize(root);
        assertEquals("[5,1,8,null,null,null,null]",ret);
    }

    @Test
    public void testSerlializeDepth2(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        TreeNode root = new TreeNode(5);

        TreeNode left = new TreeNode(1);
        TreeNode rght = new TreeNode(8);

        TreeNode left1 = new TreeNode(2);
        TreeNode rght1 = new TreeNode(6);

        TreeNode left2 = new TreeNode(3);
        TreeNode rght2 = new TreeNode(4);

        root.left = left;
        root.right = rght;

        left.left = left1;
        left.right = rght1;

        rght.left = left2;
        rght.right = rght2;

        String ret = bs.serialize(root);
        assertEquals("[5,1,8,2,6,3,4,null,null,null,null,null,null,null,null]",ret);
    }

    @Test
    public void testDeserializeNull(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        String s = "[null]";
        TreeNode root = null;
        TreeNode rootret = bs.deserialize(s);
        assertEquals(root,rootret);
    }

    @Test
    public void testDeserializeDepth0(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        String s = "[5,null,null]";
        TreeNode root = new TreeNode(5);

        TreeNode rootret = bs.deserialize(s);
        assertEquals(root,rootret);
    }

    @Test
    public void testDeserializeDepth2MidLevelOfNulls(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        String s = "[5,null,null,1,2,3,4]";
        TreeNode root = new TreeNode(5);

        TreeNode rootret = bs.deserialize(s);
        assertEquals(root, rootret);
    }

    @Test
    public void testDeserializeDepth2MidLevelOfNull(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        String s = "[5,6,null,1,2,3,4,null,null,null,null,null,null,null,null]";
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(6);

        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(2);

        TreeNode rootret = bs.deserialize(s);
        System.out.println(bs.serialize(root));
        System.out.println(bs.serialize(rootret));
        assertEquals(bs.serialize(root), "[5,6,null,1,2,null,null,null,null]");
    }

    @Test
    public void testDeserializeDepth2MidLevelOfNull2(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        String s = "[5,null,7,1,2,3,4,null,null,null,null,null,null,null,null]";
        TreeNode root = new TreeNode(5);
        root.right = new TreeNode(7);

        root.right.left = new TreeNode(3);
        root.right.right = new TreeNode(4);

        TreeNode rootret = bs.deserialize(s);
        System.out.println(bs.serialize(root));
        System.out.println(bs.serialize(rootret));
        assertEquals(bs.serialize(root), "[5,null,7,3,4,null,null,null,null]");
    }

    @Test
    public void testDeserializeDepth1(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        String s = "[5,1,8,null,null,null,null]";
        TreeNode root = new TreeNode(5);
        TreeNode left = new TreeNode(1);
        TreeNode rght = new TreeNode(8);
        root.left = left;
        root.right = rght;
        TreeNode rootret = bs.deserialize(s);
        assertEquals(root,rootret);
    }

    @Test
    public void testDeserializeDepth2(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        String s = "[5,1,8,2,6,3,4,null,null,null,null,null,null,null,null]";
        TreeNode root = new TreeNode(5);

        TreeNode left = new TreeNode(1);
        TreeNode rght = new TreeNode(8);

        TreeNode left1 = new TreeNode(2);
        TreeNode rght1 = new TreeNode(6);

        TreeNode left2 = new TreeNode(3);
        TreeNode rght2 = new TreeNode(4);

        root.left = left;
        root.right = rght;

        left.left = left1;
        left.right = rght1;

        rght.left = left2;
        rght.right = rght2;
        TreeNode rootret = bs.deserialize(s);
        assertEquals(root,rootret);
    }

    @Test
    public void testDeserializeInvalidInput(){
        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
        String s = "[]";
        TreeNode rootret = bs.deserialize(s);
        assertEquals(null,rootret);
    }


//    @Test
//    public void testDeserializeRecEmptyLevel() throws NoSuchMethodException{
//
//        BinaryTreeSerializer bs = new BinaryTreeSerializerImpl();
//        Class[] cArg = new Class[3];
//        cArg[0] = String[].class;
//        cArg[1] = int.class;
//        cArg[2] = List.class;
//
//        Method method = bs.getClass().getDeclaredMethod("deserializeRec",cArg);
//        method.setAccessible(true);
//
//        String[] parts = new String[] {"one"};
//        int i = 2;
//        List<TreeNode> level = new ArrayList<>();
//
//        Object[] objects = new Object[] { parts, i, level };
//
//        try {
//            method.invoke(bs, objects);
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//        String s = "[]";
//        TreeNode rootret = bs.deserialize(s);
//        assertEquals(null,rootret);
//    }



    //@TODO: Create more tests
}
