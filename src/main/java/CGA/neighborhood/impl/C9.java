package CGA.neighborhood.impl;

import CGA.Model.Chrome;
import CGA.neighborhood.Neighborhood;
import CGA.neighborhood.util.TwoDimensionalMesh;

import java.util.List;

public class C9 extends TwoDimensionalMesh {
    private static final int[] north = new int[]{-1, 0};
    private static final int[] south = new int[]{1, 0};
    private static final int[] east = new int[]{0, 1};
    private static final int[] west = new int[]{0, -1};
    private static final int[] north_east = new int[]{-1, 1};
    private static final int[] north_west = new int[]{-1, -1};
    private static final int[] south_east = new int[]{1, 1};
    private static final int[] south_west = new int[]{1, -1};
    private static final int[][] neighborhood;

    public C9(int rows, int columns) {
        super(rows, columns, neighborhood);
    }

    static {
        neighborhood = new int[][]{north, south, west, east, north_east, north_west, south_east, south_west};
    }


}