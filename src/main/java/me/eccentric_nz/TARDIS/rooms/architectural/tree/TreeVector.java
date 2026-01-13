package me.eccentric_nz.TARDIS.rooms.architectural.tree;

public record TreeVector(double x, double y, double z) {

    public static TreeVector add(TreeVector vec1, TreeVector vec2) {
        return new TreeVector(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
    }

    public static TreeVector sub(TreeVector vec1, TreeVector vec2) {
        return new TreeVector(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
    }

    public static TreeVector div(TreeVector vec1, int n) {
        return new TreeVector(vec1.x / n, vec1.y / n, vec1.z / n);
    }

    public static TreeVector mul(TreeVector vec1, int n) {
        return new TreeVector(vec1.x * n, vec1.y * n, vec1.z * n);
    }

    public static TreeVector normalize(TreeVector vec1) {
        double length = length(vec1);
        return new TreeVector(vec1.x / length, vec1.y / length, vec1.z / length);
    }

    public static double length(TreeVector vec1) {
        return Math.sqrt(Math.pow(vec1.x, 2.0D) + Math.pow(vec1.y, 2.0D) + Math.pow(vec1.z, 2.0D));
    }

    public static double distance(TreeVector vec1, TreeVector vec2) {
        return Math.sqrt(Math.pow(vec2.x - vec1.x, 2.0D) + Math.pow(vec2.y - vec1.y, 2.0D) + Math.pow(vec2.z - vec1.z, 2.0D));
    }
}
