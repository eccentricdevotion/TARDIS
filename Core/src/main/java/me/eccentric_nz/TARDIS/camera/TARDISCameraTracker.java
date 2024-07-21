package me.eccentric_nz.TARDIS.camera;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TARDISCameraTracker {

    public static final HashMap<UUID, CameraLocation> SPECTATING = new HashMap<>();
    public static final Set<Integer> CAMERA_IN_USE = new HashSet<>();
}
