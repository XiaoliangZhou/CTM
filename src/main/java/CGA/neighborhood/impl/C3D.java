package CGA.neighborhood.impl;

import CGA.neighborhood.util.TwoDimensionalMesh;

public class C3D extends TwoDimensionalMesh {
    private static final int[] north = new int[]{-1, 0, 0};
    private static final int[] south = new int[]{1, 0 , 0};
    private static final int[] east = new int[]{0, 0, 1};
    private static final int[] west = new int[]{0, 0, -1};
    private static final int[] north_east = new int[]{0, 1, 0};
    private static final int[] north_west = new int[]{0 ,-1, 0};

    private static final int[] south_east = new int[]{-1,0, 1};
    private static final int[] south_west = new int[]{-1,0, -1};

    private static final int[] south_east1 = new int[]{1,0, 1};
    private static final int[] south_west1 = new int[]{1,0, -1};

    private static final int[] south_eas2 = new int[]{1,1, 0};
    private static final int[] south_wes2 = new int[]{-1,1, 0};

    private static final int[] south_east3 = new int[]{-1,-1, 0};
    private static final int[] south_west3 = new int[]{1,-1, 0};

    private static final int[][] neighborhood;

    public C3D(int rows, int columns,int haxis) {
        super(rows, columns,haxis, neighborhood);
    }

    static {
       /* neighborhood = new int[][]{north, south, west, east, north_east, north_west};*/
        neighborhood = new int[][]{north, south, west, east, north_east, north_west,
                south_east,south_west,south_east1,south_west1,south_eas2,south_wes2,south_east3,south_west3};
       /* neighborhood = new int[][]{north, south, west, east, north_east, north_west,
                south_east,south_west};*/
    }


}
