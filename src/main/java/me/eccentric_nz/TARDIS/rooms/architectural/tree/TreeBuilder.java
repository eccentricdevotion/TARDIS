package me.eccentric_nz.TARDIS.rooms.architectural.tree;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Lantern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeBuilder {

    private final ArrayList<Block> blocks = new ArrayList<>();
    private int width;
    private int height;
    private int leafDensity;
    private int branchMinLength;
    private int branchMaxLength;
    private int stemLength;
    private int growIterations;
    private int leafPerBranch;
    private int branchThickness;
    private List<Material> branchMaterial;
    private List<Material> leafMaterial;

    public void place(Block block) {
        block.getWorld().spawnParticle(Particle.FIREWORK, block.getLocation(), 3);
        setTreeProperties();
        growFractalTree(block);
    }

    private void setTreeProperties() {
        width = 13;
        height = 11;
        leafDensity = 50;
        branchMinLength = 3;
        branchMaxLength = 5;
        stemLength = 4;
        growIterations = 25;
        branchMaterial = List.of(Material.WAXED_COPPER_GRATE, Material.WAXED_EXPOSED_COPPER_GRATE, Material.WAXED_OXIDIZED_COPPER_GRATE, Material.WAXED_WEATHERED_COPPER_GRATE);
        leafMaterial = List.of(Material.WAXED_COPPER_CHAIN, Material.WAXED_EXPOSED_COPPER_CHAIN, Material.WAXED_OXIDIZED_COPPER_CHAIN, Material.WAXED_WEATHERED_COPPER_CHAIN);
        leafPerBranch = 1;
        branchThickness = 1;
    }

    private void growFractalTree(Block block) {
        Tree tree = new Tree(width, height, leafDensity, branchMinLength, branchMaxLength, stemLength, branchThickness);
        tree.createTree();
        for (int i = 0; i < growIterations; i++) {
            tree.grow();
        }
        tree.leaves.clear();
        renderTree(tree, block);
        generateLeaves(block);
        generateRoots(block);
    }

    private void renderTree(Tree tree, Block block) {
        for (Branch branch : tree.branches) {
            if (branch.getParent() != null) {
                List<TreeVector> line = Fractal.line(branch.getPosition(), branch.getParent().getPosition());
                for (TreeVector loc : line) {
                    Block b = block.getWorld().getBlockAt((int) loc.x() + block.getX(), (int) loc.y() + block.getY() + 1, (int) loc.z() + block.getZ());
                    Random r = new Random();
                    int low = 1;
                    int high = branchMaterial.size() + 1;
                    int result = r.nextInt(high - low) + low;
                    b.setType(branchMaterial.get(result - 1));
                    blocks.add(b);
                }
            }
        }
    }

    private void generateLeaves(Block block) {
        for (Block branch : blocks) {
            if (branch.getY() > block.getY() + stemLength / 5 * 3) {
                for (int i = 0; i < leafPerBranch; i++) {
                    int x = branch.getX() - 1 + (int) (Math.random() * 3.0D);
                    int y = branch.getY() - 1 + (int) (Math.random() * 3.0D);
                    int z = branch.getZ() - 1 + (int) (Math.random() * 3.0D);
                    Block b = block.getWorld().getBlockAt(x, y, z);
                    if (!branchMaterial.contains(b.getType())) {
                        Random r = new Random();
                        int low = 1;
                        int high = leafMaterial.size() + 1;
                        int result = r.nextInt(high - low) + low;
                        Material material = leafMaterial.get(result - 1);
                        b.setType(material);
                        BlockData lantern = getLantern(material);
                        Block down = b.getRelative(BlockFace.DOWN);
                        if (down.getType().isAir()) {
                            down.setBlockData(lantern);
                        }
                    }
                }
            }
        }
    }

    private BlockData getLantern(Material material) {
        Lantern lantern;
        try {
            Material m = Material.valueOf(material.toString().replace("CHAIN", "LANTERN"));
            lantern = (Lantern) m.createBlockData();
        } catch (IllegalArgumentException e) {
            lantern = (Lantern) Material.WAXED_COPPER_LANTERN.createBlockData();
        }
        lantern.setHanging(true);
        return lantern;
    }

    private void generateRoots(Block block) {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 3; i++) {
            int x = -1 + (int) (Math.random() * 3.0D);
            int y = -rand.nextInt(3) + 1;
            int z = -1 + (int) (Math.random() * 3.0D);
            List<TreeVector> line = Fractal.line(new TreeVector(x, y, z), new TreeVector(0.0D, 0.0D, 0.0D));
            for (TreeVector loc : line) {
                Block b = block.getWorld().getBlockAt((int) loc.x() + block.getX(), (int) loc.y() + 1 + block.getY(), (int) loc.z() + block.getZ());
                Random r = new Random();
                int low = 1;
                int high = branchMaterial.size() + 1;
                int result = r.nextInt(high - low) + low;
                b.setType(branchMaterial.get(result - 1));
            }
        }
    }
}
