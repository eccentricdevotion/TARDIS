package me.eccentric_nz.TARDIS.rooms.architectural.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree {

    public final List<Branch> branches = new ArrayList<>();
    public final List<Leaf> leaves = new ArrayList<>();
    public final List<Branch> stems = new ArrayList<>();
    private final int leafWidth;
    private final int leafHeight;
    private final int leafDensity;
    private final int branchMinLength;
    private final int branchMaxLength;
    private final int stemLength;
    private final int branchThickness;

    public Tree(int leafWidth, int leafHeight, int leafDensity, int branchMinLength, int branchMaxLength, int stemLength, int branchThickness) {
        this.leafWidth = leafWidth;
        this.leafHeight = leafHeight;
        this.leafDensity = leafDensity;
        this.branchMinLength = branchMinLength;
        this.branchMaxLength = branchMaxLength;
        this.stemLength = stemLength;
        this.branchThickness = branchThickness;
    }

    public void createTree() {
        fractalTree();
    }

    private void fractalTree() {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int i;
        for (i = 0; i < leafDensity; i++) {
            leaves.add(new Leaf(new TreeVector((rand.nextInt(leafWidth) - Math.round(((float) leafWidth / 2))), (rand.nextInt(leafHeight - stemLength) + stemLength), (rand.nextInt(leafWidth) - Math.round(((float) leafWidth / 2))))));
        }
        for (i = 0; i < branchThickness; i++) {
            for (int j = 0; j < branchThickness; j++) {
                Branch root = new Branch(null, new TreeVector(0.0D, 1.0D, 0.0D), new TreeVector(i - Math.round((float) branchThickness / 2), -2.0D, (j - Math.round((float) branchThickness / 2))));
                branches.add(root);
                Branch current = new Branch(root);

                while (!closeEnough(current)) {
                    Branch trunk = new Branch(current);
                    branches.add(trunk);
                    current = trunk;
                }
                stems.add(current);
            }
        }
    }

    private boolean closeEnough(Branch branch) {
        for (Leaf leaf : leaves) {
            double d = TreeVector.distance(branch.getPosition(), leaf.getPosition());
            if (d < branchMaxLength) {
                return true;
            }
        }
        return false;
    }

    public void grow() {
        int i;
        for (i = 0; i < leaves.size(); i++) {
            Leaf l = leaves.get(i);
            Branch closestBranch = null;
            TreeVector closestDir = null;
            double record = 100000.0D;
            for (Branch b : branches) {
                TreeVector dir = TreeVector.sub(l.getPosition(), b.getPosition());
                double d = TreeVector.length(dir);
                if (d < branchMinLength) {
                    l.reached = true;
                    closestBranch = null;
                    break;
                }
                if (d <= branchMaxLength) {
                    if (closestBranch == null || d < record) {
                        closestBranch = b;
                        closestDir = dir;
                        record = d;
                    }
                }
            }
            if (closestBranch != null) {
                for (Branch stem : stems) {
                    TreeVector stemDir = TreeVector.sub(l.getPosition(), stem.getPosition());
                    stemDir = TreeVector.normalize(stemDir);
                    TreeVector ve = TreeVector.add(stem.getDirection(), stemDir);
                    stem = new Branch(stem.getParent(), ve, stem.getPosition());
                    stem.setDirection(TreeVector.add(stemDir, stem.getDirection()));
                    stem.count++;
                    branches.add(stem);
                }
                stems.clear();
                closestDir = TreeVector.normalize(closestDir);
                TreeVector v = TreeVector.add(closestBranch.getDirection(), closestDir);
                closestBranch = new Branch(closestBranch.getParent(), v, closestBranch.getPosition());
                closestBranch.setDirection(TreeVector.add(closestDir, closestBranch.getDirection()));
                closestBranch.count++;
                branches.add(closestBranch);
            }
        }
        for (i = leaves.size() - 1; i >= 0; i--) {
            if (leaves.get(i).reached) {
                leaves.remove(i);
            }
        }
        for (i = branches.size() - 1; i >= 0; i--) {
            Branch b = branches.get(i);
            if (b.count > 0) {
                b.setDirection(TreeVector.div(b.getDirection(), b.count));
                b.setDirection(TreeVector.normalize(b.getDirection()));
                Branch newB = new Branch(b);
                branches.add(newB);
                b.reset();
            }
        }
    }
}
