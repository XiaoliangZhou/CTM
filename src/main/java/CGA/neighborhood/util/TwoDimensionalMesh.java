package CGA.neighborhood.util;

import CGA.Model.Chrome;
import CGA.neighborhood.Neighborhood;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

public class TwoDimensionalMesh implements Neighborhood {
    private int rows;
    private int columns;
    private int heighs;
    private int[][] neighborhood;
    private int[][] mesh;
    private Chrome[][] meshs;
    private Chrome[][][] meshds;

    public TwoDimensionalMesh(int rows, int columns, int[][] neighborhood) {
        this.rows = rows;
        this.columns = columns;
        this.neighborhood = neighborhood;
        //this.createMesh();
    }

    public TwoDimensionalMesh(int rows, int columns, int heighs, int[][] neighborhood) {
        this.rows = rows;
        this.columns = columns;
        this.heighs = heighs;
        this.neighborhood = neighborhood;
    }

    private void createMesh() {
        this.mesh = new int[this.rows][this.columns];
        int solution = 0;

        for(int row = 0; row < this.rows; ++row) {
            for(int column = 0; column < this.columns; ++column) {
                this.mesh[row][column] = solution++;
            }
        }

    }

    private int getRow(int solution) {
        return solution % this.columns;
    }

    private int getColumn(int solution) {
        return solution % this.columns;
    }

    private Chrome getNeighbor(int axis,int yaxis, int[] neighbor) {
        int row = this.getRow(axis);
        int col = this.getColumn(yaxis);
        int r = (row + neighbor[0]) % this.rows;
        if (r < 0) {
            r = this.rows - 1;
        }

        int c = (col + neighbor[1]) % this.columns;
        if (c < 0) {
            c = this.columns - 1;
        }

        return this.meshs[r][c];
    }
    private Chrome getD3Neighbor(int axis,int yaxis,int haxis, int[] neighbor) {
        int row = this.getRow(axis);
        int col = this.getColumn(yaxis);
        int hol = this.getColumn(haxis);
        int r = (row + neighbor[0]) % this.rows;
        if (r < 0) {
            r = this.rows - 1;
        }

        int c = (col + neighbor[1]) % this.columns;
        if (c < 0) {
            c = this.columns - 1;
        }

        int h = (hol + neighbor[2]) % this.heighs;
        if (h < 0) {
            h = this.heighs - 1;
        }

        return this.meshds[r][c][h];
    }

    private List<Chrome> findNeighbors(Chrome[][] solutionSet, int axis,int yaxis, int[][] neighborhood) {
        this.meshs = solutionSet;
        List<Chrome> neighbors = new ArrayList(neighborhood.length + 1);
        int[][] var5 = neighborhood;
        int var6 = neighborhood.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            int[] neighbor = var5[var7];
            Chrome  neighb = this.getNeighbor(axis, yaxis,neighbor);
            neighbors.add(neighb);
        }

        return neighbors;
    }

    private List<Chrome> findD3Neighbors(Chrome[][][] solutionSet, int axis,int yaxis,int haxis, int[][] neighborhood) {
        this.meshds = solutionSet;
        List<Chrome> neighbors = new ArrayList(neighborhood.length + 1);
        int[][] var5 = neighborhood;
        int var6 = neighborhood.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            int[] neighbor = var5[var7];
            Chrome  neighb = this.getD3Neighbor(axis, yaxis,haxis,neighbor);
            neighbors.add(neighb);
        }

        return neighbors;
    }

    public List<Chrome> getNeighbors(Chrome[][] solutionList,int axis, int yaxis) {
        if (solutionList == null) {
            throw new JMetalException("The solution list is null");
        } else {
            return this.findNeighbors(solutionList, axis, yaxis,this.neighborhood);
        }
    }

    @Override
    public List<Chrome> getNeighbors(Chrome[][][] solutionList, int axis, int yaxis, int haxis) {
        if (solutionList == null) {
            throw new JMetalException("The solution list is null");
        } else {
            return this.findD3Neighbors(solutionList, axis, yaxis,haxis,this.neighborhood);
        }
    }
}
