package me.eccentric_nz.plugins.TARDIS;
/*
 * The SCHEMATIC class. By Nathanael (thespuff.com/nathanaelphillipsmith@gmail.com)
 *
 * Bits of this are from all over the place, I wish I'd kept a list of everybody who unwittingly helped out here.
 * I suppose I need to at least say 'thanks' to:
 * sk89q, who introduced me to the format.
 * bukkit.
 * Mojang.
 *
 * Code snippets from Chris Smith,
 *
 * I pulled this in from a PHP deconstructor, so you'll see some fragments of that.
 * If you clean this up or improve it, good for you. Pass it back to me, I'd love to learn from your work.
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public final class TARDISReadSchematic {

    private int width;
    private int height;
    private int length;
    public int offsetX;
    public int offsetY;
    public int offsetZ;
    private List<Integer> bArray = new ArrayList<Integer>();
    // Is it possible to make a List<byte> or something like that?
    private List<Integer> dArray = new ArrayList<Integer>();

    public TARDISReadSchematic(String fileName) throws IOException {
        byte[] uGD;
        try {
            uGD = file_get_contents(fileName);
        } catch (IOException e) {
            throw e;
        }

        kludgy(uGD);
    }

    public TARDISReadSchematic(byte[] uGD) {
        kludgy(uGD);
    }

    private void kludgy(byte[] uGD) {
        //I know that this is kludgy. Let's fix it!
        //Aren't schematics kept in NBTtag format?
        //We should use the minecraft server functions.

        this.width = uGD[indexOf(uGD, "Width") + 6];
        this.height = uGD[indexOf(uGD, "Height") + 7];
        this.length = uGD[indexOf(uGD, "Length") + 7];

        this.offsetX = uGD[indexOf(uGD, "WEOffsetX") + 10];
        this.offsetY = uGD[indexOf(uGD, "WEOffsetY") + 10];
        this.offsetZ = uGD[indexOf(uGD, "WEOffsetZ") + 10];

        int bStart = indexOf(uGD, "Blocks") + 10;
        int dStart = indexOf(uGD, "Data") + 8;

        int count = this.countBlocks();

        for (int i = 0; i < count; i++) {
            this.bArray.add((int) uGD[bStart + i]);
            this.dArray.add((int) uGD[dStart + i]);
        }
    }

    private int indexOf(byte[] data, String pattern) {
        return indexOf(data, pattern.getBytes());
    }

    public int indexOf(byte[] data, byte[] pattern) {
        // * Knuth-Morris-Pratt Algorithm for Pattern Matching
        // I got it from Chris Smith at velocityreviews.com
        // based on code from fmi.uni-sofia.bg/fmi/logic/vboutchkova/sources/KMPMatch_java.html
        int[] failure = computeFailure(pattern);

        int j = 0;
        if (data.length == 0) {
            return -1;
        }

        for (int i = 0; i < data.length; i++) {
            while (j > 0 && pattern[j] != data[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == data[i]) {
                j++;
            }
            if (j == pattern.length) {
                return i - pattern.length + 1;
            }
        }
        return -1;
    }

    private int[] computeFailure(byte[] pattern) {
        int[] failure = new int[pattern.length];

        int j = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (j > 0 && pattern[j] != pattern[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == pattern[i]) {
                j++;
            }
            failure[i] = j;
        }

        return failure;
    }

    public void paste(Location location) {
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                for (int z = 0; z < this.getLength(); z++) {
                    final Material mBlock = this.getBlock(x, y, z);
                    final Block tBlock = location.getBlock().getRelative(x + this.offsetX, y + this.offsetY, z + this.offsetZ);
                    final byte dBlock = this.getData(x, y, z);
                    if ((tBlock.getType() != mBlock)) {
                        tBlock.setTypeIdAndData(mBlock.getId(), dBlock, false);
                    }
                }
            }
        }
    }

    public byte[] file_get_contents(String message) throws IOException {
        return file_get_contents(message, 4096);
    }

    public byte[] file_get_contents(String fileName, int maxSize) throws IOException {
        try {
            File schematicFile = new File(fileName);

            //I seem to remember that WE gzips schematic files to save them... Hm. Not my test files.
            //GZIPInputStream instream =new GZIPInputStream(new FileInputStream(schematicFile));
            FileInputStream instream = new FileInputStream(schematicFile);

            byte[] buf = new byte[maxSize];
            int len = instream.read(buf);
            instream.close();
            if (len < 0) {
                return null;
            }

            byte[] fileData = new byte[len];
            for (int i = 0; i < len; i++) {
                fileData[i] = buf[i];
            }

            return fileData;

        } catch (IOException e) {
            throw e;
        }
    }

    public int countBlocks() {
        //If we've already built the block array, use the size of that.
        if (this.bArray.size() > 0) {
            return this.bArray.size();
        }

        //Otherwise, work it out from the width x height x length
        if (this.width * this.height * this.length > 0) {
            return (this.width * this.height * this.length);
        }

        //Well, crap. Nothing else is working. Maybe the data array?
        if (this.dArray.size() > 0) {
            return this.dArray.size();
        }

        //You, my friend, are out of luck.
        return -1;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getLength() {
        return this.length;
    }

    public Material getBlock(int x, int y, int z) {
        return Material.getMaterial(this.bArray.get(xyzToLoc(x, y, z)));
    }

    public int getBlockId(int x, int y, int z) {
        return this.bArray.get(xyzToLoc(x, y, z));
    }

    public byte getData(int x, int y, int z) {
        return this.dArray.get(xyzToLoc(x, y, z)).byteValue();
    }

    private int xyzToLoc(int x, int y, int z) {
        return (((y * this.length) + z) * this.width) + x;
    }
}
