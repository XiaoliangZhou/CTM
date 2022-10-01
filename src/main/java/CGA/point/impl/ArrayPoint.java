package CGA.point.impl;

import CGA.point.Point;
import org.uma.jmetal.util.JMetalException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ArrayPoint implements Point {
    protected double[] point;

    public ArrayPoint() {
        this.point = null;
    }

    public ArrayPoint(int dimension) {
        this.point = new double[dimension];

        for(int i = 0; i < dimension; ++i) {
            this.point[i] = 0.0D;
        }

    }

    public ArrayPoint(Point point) {
        if (point == null) {
            throw new JMetalException("The point is null");
        } else {
            this.point = new double[point.getDimension()];

            for(int i = 0; i < point.getDimension(); ++i) {
                this.point[i] = point.getValue(i);
            }

        }
    }

    public ArrayPoint(double[] point) {
        if (point == null) {
            throw new JMetalException("The array of values is null");
        } else {
            this.point = new double[point.length];
            System.arraycopy(point, 0, this.point, 0, point.length);
        }
    }

    public ArrayPoint(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        Throwable var5 = null;

        try {
            List<Double> auxiliarPoint = new ArrayList();

            for(String aux = br.readLine(); aux != null; aux = br.readLine()) {
                StringTokenizer st = new StringTokenizer(aux);

                while(st.hasMoreTokens()) {
                    Double value = new Double(st.nextToken());
                    auxiliarPoint.add(value);
                }
            }

            this.point = new double[auxiliarPoint.size()];

            for(int i = 0; i < auxiliarPoint.size(); ++i) {
                this.point[i] = (Double)auxiliarPoint.get(i);
            }
        } catch (Throwable var17) {
            var5 = var17;
            throw var17;
        } finally {
            if (br != null) {
                if (var5 != null) {
                    try {
                        br.close();
                    } catch (Throwable var16) {
                        var5.addSuppressed(var16);
                    }
                } else {
                    br.close();
                }
            }

        }

    }

    public int getDimension() {
        return this.point.length;
    }

    public double[] getValues() {
        return this.point;
    }

    public double getValue(int index) {
        if (index >= 0 && index < this.point.length) {
            return this.point[index];
        } else {
            throw new JMetalException("Index value invalid: " + index + ". The point length is: " + this.point.length);
        }
    }

    public void setValue(int index, double value) {
        if (index >= 0 && index < this.point.length) {
            this.point[index] = value;
        } else {
            throw new JMetalException("Index value invalid: " + index + ". The point length is: " + this.point.length);
        }
    }

    public void update(double[] point) {
        if (point.length != this.point.length) {
            throw new JMetalException("The point to be update have a dimension of " + point.length + " while the parameter point has a dimension of " + point.length);
        } else {
            for(int i = 0; i < point.length; ++i) {
                this.point[i] = point[i];
            }

        }
    }

    public String toString() {
        String result = "";
        double[] var2 = this.point;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            double anObjectives_ = var2[var4];
            result = result + anObjectives_ + " ";
        }

        return result;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ArrayPoint that = (ArrayPoint)o;
            return Arrays.equals(this.point, that.point);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Arrays.hashCode(this.point);
    }
}
