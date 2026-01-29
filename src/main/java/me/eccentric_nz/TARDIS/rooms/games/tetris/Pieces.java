package me.eccentric_nz.TARDIS.rooms.games.tetris;

public enum Pieces {
    I(
            new byte[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}},
            new byte[][]{{0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}}
    ),
	J(
			new byte[][]{{0, 0, 0}, {3, 3, 3}, {0, 0, 3}},
			new byte[][]{{0, 3, 0}, {0, 3, 0}, {3, 3, 0}},
			new byte[][]{{3, 0, 0}, {3, 3, 3}, {0, 0, 0}},
			new byte[][]{{0, 3, 3}, {0, 3, 0}, {0, 3, 0}}
	),
	L(
			new byte[][]{{0, 0, 0}, {2, 2, 2}, {2, 0, 0}},
			new byte[][]{{2, 2, 0}, {0, 2, 0}, {0, 2, 0}},
			new byte[][]{{0, 0, 2}, {2, 2, 2}, {0, 0, 0}},
			new byte[][]{{0, 2, 0}, {0, 2, 0}, {0, 2, 2}}
	),
	O(
			new byte[][]{{1, 1}, {1, 1}}
	),
	S(
			new byte[][]{{0, 0, 0}, {0, 3, 3}, {3, 3, 0}},
			new byte[][]{{0, 3, 0}, {0, 3, 3}, {0, 0, 3}}
	),
	T(
			new byte[][]{{0, 0, 0}, {1, 1, 1}, {0, 1, 0}},
			new byte[][]{{0, 1, 0}, {1, 1, 0}, {0, 1, 0}},
			new byte[][]{{0, 1, 0}, {1, 1, 1}, {0, 0, 0}},
			new byte[][]{{0, 1, 0}, {0, 1, 1}, {0, 1, 0}}
	),
	Z(
			new byte[][]{{0, 0, 0}, {2, 2, 0}, {0, 2, 2}},
			new byte[][]{{0, 0, 2}, {0, 2, 2}, {0, 2, 0}}
	);

	private final byte[][][] shapes;
	private final int startX, startY;
	private int x, y;
	private byte rotation;

	Pieces(byte[][]... shapes) {
		this.shapes = shapes;
		startX = -shapes[0][0].length / 2;
		for (int i = 0; i < shapes[0].length; i++) {
            for (byte b : shapes[0][i]) {
                if (b != 0) {
                    startY = -i;
                    x = startX;
                    y = startY;
                    return;
                }
            }
        }
		startY = 0;
	}

	public byte[][] getShape() {
		return shapes[rotation];
	}

	public int getXOffset() {
		return x;
	}

	public int getYOffset() {
		return y;
	}

	public void rotateClockwise() {
		rotation = (byte) ((rotation + 1) % shapes.length);
	}

	public void rotateCounterClockwise() {
		rotation = (byte) ((rotation + 3) % shapes.length);
	}

	void move(int dx, int dy) {
		x += dx;
		y += dy;
	}

	public void reset() {
		x = startX;
		y = startY;
		rotation = 0;
	}
}
