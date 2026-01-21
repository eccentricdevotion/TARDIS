package me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.Locale;

public class Letters {

    public static ItemStack A() {
        ItemStack a = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) a.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        a.setItemMeta(bm);
        return a;
    }

    public static ItemStack B() {
        ItemStack b = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) b.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.CURLY_BORDER));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        b.setItemMeta(bm);
        return b;
    }

    public static ItemStack C() {
        ItemStack c = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) c.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_MIDDLE));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        c.setItemMeta(bm);
        return c;
    }

    public static ItemStack D() {
        ItemStack d = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) d.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.CURLY_BORDER));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        d.setItemMeta(bm);
        return d;
    }

    public static ItemStack E() {
        ItemStack e = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) e.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        e.setItemMeta(bm);
        return e;
    }

    public static ItemStack F() {
        ItemStack f = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) f.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        f.setItemMeta(bm);
        return f;
    }

    public static ItemStack G() {
        ItemStack g = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) g.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.HALF_HORIZONTAL));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        g.setItemMeta(bm);
        return g;
    }

    public static ItemStack H() {
        ItemStack h = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) h.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        h.setItemMeta(bm);
        return h;
    }

    public static ItemStack I() {
        ItemStack i = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) i.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        i.setItemMeta(bm);
        return i;
    }

    public static ItemStack J() {
        ItemStack j = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) j.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.HALF_HORIZONTAL));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        j.setItemMeta(bm);
        return j;
    }

    public static ItemStack K() {
        ItemStack k = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) k.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.HALF_HORIZONTAL));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        k.setItemMeta(bm);
        return k;
    }

    public static ItemStack L() {
        ItemStack l = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) l.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        l.setItemMeta(bm);
        return l;
    }

    public static ItemStack M() {
        ItemStack m = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) m.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_TOP));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.TRIANGLES_TOP));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        m.setItemMeta(bm);
        return m;
    }

    public static ItemStack N() {
        ItemStack n = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) n.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.TRIANGLE_TOP));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        n.setItemMeta(bm);
        return n;
    }

    public static ItemStack O() {
        ItemStack o = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) o.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        o.setItemMeta(bm);
        return o;
    }

    public static ItemStack P() {
        ItemStack p = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) p.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.HALF_HORIZONTAL_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        p.setItemMeta(bm);
        return p;
    }

    public static ItemStack Q() {
        ItemStack q = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) q.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.RHOMBUS));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        q.setItemMeta(bm);
        return q;
    }

    public static ItemStack R() {
        ItemStack r = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) r.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_CENTER));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        r.setItemMeta(bm);
        return r;
    }

    public static ItemStack S() {
        ItemStack s = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) s.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.RHOMBUS));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_MIDDLE));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        s.setItemMeta(bm);
        return s;
    }

    public static ItemStack T() {
        ItemStack t = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) t.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        t.setItemMeta(bm);
        return t;
    }

    public static ItemStack U() {
        ItemStack u = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) u.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        u.setItemMeta(bm);
        return u;
    }

    public static ItemStack V() {
        ItemStack v = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) v.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.TRIANGLE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        v.setItemMeta(bm);
        return v;
    }

    public static ItemStack W() {
        ItemStack w = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) w.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.TRIANGLES_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        w.setItemMeta(bm);
        return w;
    }

    public static ItemStack X() {
        ItemStack x = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) x.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.CROSS));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        x.setItemMeta(bm);
        return x;
    }

    public static ItemStack Y() {
        ItemStack y = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) y.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.HALF_HORIZONTAL_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        y.setItemMeta(bm);
        return y;
    }

    public static ItemStack Z() {
        ItemStack z = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) z.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        z.setItemMeta(bm);
        return z;
    }

    public static ItemStack exclamation() {
        ItemStack ex = ItemStack.of(Material.CYAN_BANNER);
        BannerMeta bm = (BannerMeta) ex.getItemMeta();
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_LEFT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_RIGHT));
        bm.addPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER));
        ex.setItemMeta(bm);
        return ex;
    }

    public static void giveAll(Player player) {
        player.performCommand("/give @p minecraft:cyan_shulker_box[container=[{slot:0,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_right,color:white},{pattern:stripe_left,color:white},{pattern:stripe_middle,color:white},{pattern:stripe_top,color:white},{pattern:border,color:cyan}],custom_name:\"Letter A\"},count:16}},{slot:1,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_right,color:white},{pattern:stripe_bottom,color:white},{pattern:stripe_top,color:white},{pattern:curly_border,color:cyan},{pattern:stripe_left,color:white},{pattern:stripe_middle,color:white},{pattern:border,color:cyan}],custom_name:\"Letter B\"},count:16}},{slot:2,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_top,color:white},{pattern:stripe_bottom,color:white},{pattern:stripe_right,color:white},{pattern:stripe_middle,color:cyan},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter C\"},count:16}},{slot:3,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_right,color:white},{pattern:stripe_bottom,color:white},{pattern:stripe_top,color:white},{pattern:curly_border,color:cyan},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter D\"},count:16}},{slot:4,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_left,color:white},{pattern:stripe_top,color:white},{pattern:stripe_middle,color:white},{pattern:stripe_bottom,color:white},{pattern:border,color:cyan}],custom_name:\"Letter E\"},count:16}},{slot:5,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_middle,color:white},{pattern:stripe_right,color:cyan},{pattern:stripe_top,color:white},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter F\"},count:16}},{slot:6,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_right,color:white},{pattern:half_horizontal,color:cyan},{pattern:stripe_bottom,color:white},{pattern:stripe_left,color:white},{pattern:stripe_top,color:white},{pattern:border,color:cyan}],custom_name:\"Letter G\"},count:16}},{slot:7,item:{id:white_banner,components:{banner_patterns:[{pattern:stripe_top,color:cyan},{pattern:stripe_bottom,color:cyan},{pattern:stripe_left,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter H\"},count:16}},{slot:8,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_center,color:white},{pattern:stripe_top,color:white},{pattern:stripe_bottom,color:white},{pattern:border,color:cyan}],custom_name:\"Letter I\"},count:16}},{slot:9,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_left,color:white},{pattern:half_horizontal,color:cyan},{pattern:stripe_bottom,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter J\"},count:16}},{slot:10,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_downright,color:white},{pattern:half_horizontal,color:cyan},{pattern:stripe_downleft,color:white},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter K\"},count:16}},{slot:11,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_bottom,color:white},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter L\"},count:16}},{slot:12,item:{id:cyan_banner,components:{banner_patterns:[{pattern:triangle_top,color:white},{pattern:triangles_top,color:cyan},{pattern:stripe_left,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter M\"},count:16}},{slot:13,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_left,color:white},{pattern:triangle_top,color:cyan},{pattern:stripe_downright,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter N\"},count:16}},{slot:14,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_left,color:white},{pattern:stripe_right,color:white},{pattern:stripe_bottom,color:white},{pattern:stripe_top,color:white},{pattern:border,color:cyan}],custom_name:\"Letter O\"},count:16}},{slot:15,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_right,color:white},{pattern:half_horizontal_bottom,color:cyan},{pattern:stripe_middle,color:white},{pattern:stripe_top,color:white},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter P\"},count:16}},{slot:16,item:{id:white_banner,components:{banner_patterns:[{pattern:rhombus,color:cyan},{pattern:stripe_right,color:white},{pattern:stripe_left,color:white},{pattern:square_bottom_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter Q\"},count:16}},{slot:17,item:{id:cyan_banner,components:{banner_patterns:[{pattern:half_horizontal,color:white},{pattern:stripe_center,color:cyan},{pattern:stripe_top,color:white},{pattern:stripe_left,color:white},{pattern:stripe_downright,color:white},{pattern:border,color:cyan}],custom_name:\"Letter R\"},count:16}},{slot:18,item:{id:white_banner,components:{banner_patterns:[{pattern:rhombus,color:cyan},{pattern:stripe_middle,color:cyan},{pattern:stripe_downright,color:white},{pattern:border,color:cyan}],custom_name:\"Letter S\"},count:16}},{slot:19,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_top,color:white},{pattern:stripe_center,color:white},{pattern:border,color:cyan}],custom_name:\"Letter T\"},count:16}},{slot:20,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_bottom,color:white},{pattern:stripe_left,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter U\"},count:16}},{slot:21,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_downleft,color:white},{pattern:stripe_left,color:white},{pattern:triangle_bottom,color:cyan},{pattern:stripe_downleft,color:white},{pattern:border,color:cyan}],custom_name:\"Letter V\"},count:16}},{slot:22,item:{id:cyan_banner,components:{banner_patterns:[{pattern:triangle_bottom,color:white},{pattern:triangles_bottom,color:cyan},{pattern:stripe_left,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter W\"},count:16}},{slot:23,item:{id:cyan_banner,components:{banner_patterns:[{pattern:cross,color:white},{pattern:border,color:cyan}],custom_name:\"Letter X\"},count:16}},{slot:24,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_downright,color:white},{pattern:half_horizontal_bottom,color:cyan},{pattern:stripe_downleft,color:white},{pattern:border,color:cyan}],custom_name:\"Letter Y\"},count:16}},{slot:25,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_top,color:white},{pattern:stripe_downleft,color:white},{pattern:stripe_bottom,color:white},{pattern:border,color:cyan}],custom_name:\"Letter Z\"},count:16}}]]");
    }

    public static void makeCode(Player player) {
        char[] upper = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        char[] lower = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        Registry<PatternType> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN);
        // get shulker box player is targeting
        Block b = player.getTargetBlock(null, 4);
        if (b.getType() == Material.CYAN_SHULKER_BOX) {
            ShulkerBox box = (ShulkerBox) b.getState();
            Inventory inventory = box.getInventory();
            int i = 0;
            for (ItemStack is : inventory.getContents()) {
                if (is != null && is.getType() == Material.CYAN_BANNER) {
                    TARDIS.plugin.debug("public static ItemStack " + upper[i] + "() {");
                    TARDIS.plugin.debug("ItemStack " + lower[i] + " = ItemStack.of(Material.CYAN_BANNER);");
                    TARDIS.plugin.debug("BannerMeta bm = (BannerMeta) " + lower[i] + ".getItemMeta();");
                    BannerMeta bm = (BannerMeta) is.getItemMeta();
                    for (Pattern p : bm.getPatterns()) {
                        String type = registry.getKey(p.getPattern()).getKey().toUpperCase(Locale.ROOT);
                        TARDIS.plugin.debug("bm.addPattern(new Pattern(DyeColor." + p.getColor() + ", PatternType." + type + "));");
                    }
                    TARDIS.plugin.debug(lower[i] + ".setItemMeta(bm);");
                    TARDIS.plugin.debug("return ex;");
                    TARDIS.plugin.debug("}");
                    TARDIS.plugin.debug(" ");
                    i++;
                }
            }
        }
    }
}
