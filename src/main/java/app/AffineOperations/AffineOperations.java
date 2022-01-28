package app.AffineOperations;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public interface AffineOperations {

    Array2DRowRealMatrix perspectiveProjection(Array2DRowRealMatrix matrix);
    Array2DRowRealMatrix move(double dx, double dy, Array2DRowRealMatrix matrix);
    Array2DRowRealMatrix scale(double scaleFactor, Array2DRowRealMatrix matrix);
    Array2DRowRealMatrix oXRotation(double angle, Array2DRowRealMatrix matrix);
    Array2DRowRealMatrix oYRotation(double angle, Array2DRowRealMatrix matrix);
    Array2DRowRealMatrix oZRotation(double angle, Array2DRowRealMatrix matrix);
    Array2DRowRealMatrix mirrorOX(Array2DRowRealMatrix matrix);
    Array2DRowRealMatrix mirrorOY(Array2DRowRealMatrix matrix);
    Array2DRowRealMatrix mirrorOZ(Array2DRowRealMatrix matrix);

}
