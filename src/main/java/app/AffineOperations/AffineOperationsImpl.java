package app.AffineOperations;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class AffineOperationsImpl implements AffineOperations {

    public Array2DRowRealMatrix perspectiveProjection(Array2DRowRealMatrix matrix) {
        final var perspective = new Array2DRowRealMatrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, -1d/300d },
                { 0, 0, 0, 1 }
        });

        matrix = matrix.multiply(perspective);
        correctH(matrix);
        return matrix;
    }

    public Array2DRowRealMatrix move(double dx, double dy, Array2DRowRealMatrix matrix) {
        final var offset = new Array2DRowRealMatrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 },
                { dx, dy, 0, 1 }
        });

        return matrix.multiply(offset);
    }

    public Array2DRowRealMatrix scale(double scaleFactor, Array2DRowRealMatrix matrix) {
        final var scale = new Array2DRowRealMatrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 / scaleFactor }
        });

        matrix = matrix.multiply(scale);
        correctH(matrix);
        return matrix;
    }

    public Array2DRowRealMatrix oXRotation(double angle, Array2DRowRealMatrix matrix) {
        final var rotation = new Array2DRowRealMatrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, Math.cos(angle), Math.sin(angle), 0 },
                { 0, -Math.sin(angle), Math.cos(angle), 0 },
                { 0, 0, 0, 1 }
        });

       return matrix.multiply(rotation);
    }

    public Array2DRowRealMatrix oYRotation(double angle, Array2DRowRealMatrix matrix) {
        final var rotation = new Array2DRowRealMatrix(new double[][] {
                { Math.cos(angle), 0, -Math.sin(angle), 0 },
                { 0, 1, 0, 0 },
                { Math.sin(angle), 0, Math.cos(angle), 0 },
                { 0, 0, 0, 1 }
        });

       return matrix.multiply(rotation);
    }

    public Array2DRowRealMatrix oZRotation(double angle, Array2DRowRealMatrix matrix) {
        final var rotation = new Array2DRowRealMatrix(new double[][] {
                { Math.cos(angle), Math.sin(angle), 0, 0 },
                { -Math.sin(angle), Math.cos(angle), 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        });

        return matrix.multiply(rotation);
    }

    public Array2DRowRealMatrix mirrorOX(Array2DRowRealMatrix matrix) {
        final var mirror = new Array2DRowRealMatrix(new double[][] {
                { -1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        });

        return matrix.multiply(mirror);
    }

    public Array2DRowRealMatrix mirrorOY(Array2DRowRealMatrix matrix) {
        final var mirror = new Array2DRowRealMatrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, -1, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        });

        return matrix.multiply(mirror);
    }

    public Array2DRowRealMatrix mirrorOZ(Array2DRowRealMatrix matrix) {
        final var mirror = new Array2DRowRealMatrix(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, -1, 0 },
                { 0, 0, 0, 1 }
        });

        return matrix.multiply(mirror);
    }

    private void correctH(Array2DRowRealMatrix matrix) {
        for(int i = 0; i < matrix.getRowDimension(); i++) {
            matrix.setEntry(i, 0, matrix.getEntry(i, 0) / matrix.getEntry(i, 3));
            matrix.setEntry(i, 1, matrix.getEntry(i, 1) / matrix.getEntry(i, 3));
            matrix.setEntry(i, 2, matrix.getEntry(i, 2) / matrix.getEntry(i, 3));
            matrix.setEntry(i, 3, 1);
        }
    }

}
