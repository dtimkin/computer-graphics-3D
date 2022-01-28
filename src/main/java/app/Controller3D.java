package app;

import app.AffineOperations.AffineOperations;
import app.AffineOperations.AffineOperationsImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class Controller3D {

    @FXML Canvas canvas;

    AffineOperations affineOperationsImpl = new AffineOperationsImpl();

    Figure currentFigure;
    Array2DRowRealMatrix nodes;
    ArrayList<Polygon> polygons;

    Point pointWhereDraggingStarted;
    Array2DRowRealMatrix figureMiddlePoint;

    boolean isPerspectiveProjection = false;
    boolean isRobertsAlgorithmEnabled = true;

    public void setUpScene(Scene scene) {
        final var gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.GREEN);
        onShowPyramidClick();
    }

    // Drawing Logic

    private void redrawCanvas() {
        final var gc = canvas.getGraphicsContext2D();
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        final var xOffset = canvas.getWidth() / 2;
        final var yOffset = canvas.getHeight() / 2;

        var nodes = this.nodes;
        if(isPerspectiveProjection)
            nodes = affineOperationsImpl.perspectiveProjection(nodes);

        final var shouldDrawPolygon = calculateVisiblePolygons(nodes);

        for (var i = 0; i < polygons.size(); i++) {
            if (isRobertsAlgorithmEnabled && shouldDrawPolygon.getEntry(i) < 0)
                continue;

            final var polygon = polygons.get(i);

            gc.strokeLine(nodes.getEntry(polygon.p1, 0) + xOffset, nodes.getEntry(polygon.p1, 1) + yOffset,
                    nodes.getEntry(polygon.p2, 0) + xOffset, nodes.getEntry(polygon.p2, 1) + yOffset);

            gc.strokeLine(nodes.getEntry(polygon.p2, 0) + xOffset, nodes.getEntry(polygon.p2, 1) + yOffset,
                    nodes.getEntry(polygon.p3, 0) + xOffset, nodes.getEntry(polygon.p3, 1) + yOffset);

            gc.strokeLine(nodes.getEntry(polygon.p3, 0) + xOffset, nodes.getEntry(polygon.p3, 1) + yOffset,
                    nodes.getEntry(polygon.p1, 0) + xOffset, nodes.getEntry(polygon.p1, 1) + yOffset);
        }
    }

    private RealVector calculateVisiblePolygons(Array2DRowRealMatrix nodes) {
        final var figureMatrix = new Array2DRowRealMatrix(4, polygons.size());

        for (var i = 0; i < polygons.size(); i++) {
            final var p1 = nodes.getRow(polygons.get(i).p1);
            final var p2 = nodes.getRow(polygons.get(i).p2);
            final var p3 = nodes.getRow(polygons.get(i).p3);

            final var x = (p2[1] - p1[1]) * (p3[2] - p1[2]) - (p2[2] - p1[2]) * (p3[1] - p1[1]);
            final var y = -((p2[0] - p1[0]) * (p3[2] - p1[2]) - (p2[2] - p1[2]) * (p3[0] - p1[0]));
            final var z = (p2[0] - p1[0]) * (p3[1] - p1[1]) - (p2[1] - p1[1]) * (p3[0] - p1[0]);
            final var d = -(x * p1[0] + y * p1[1] + z * p1[2]);

            figureMatrix.setEntry(0, i, x);
            figureMatrix.setEntry(1, i, y);
            figureMatrix.setEntry(2, i, z);
            figureMatrix.setEntry(3, i, d);
        }

        final var testResult = figureMatrix.preMultiply(figureMiddlePoint);
        for (var i = 0; i < testResult.getColumnDimension(); i++)
            if (testResult.getEntry(0, i) > 0)
                figureMatrix.setColumnVector(i, figureMatrix.getColumnVector(i).mapMultiply(-1));

        final var viewPoint = new ArrayRealVector(new double[] { 0, 0, 30000, 1 });
        return figureMatrix.preMultiply(viewPoint);
    }

    // Mouse Events

    @FXML
    private void onCanvasMouseClicked(MouseEvent e) {
        pointWhereDraggingStarted = null;
    }

    @FXML
    private void onCanvasMouseDrugged(MouseEvent e) {
        final var currentPoint = new Point(e.getX(), e.getY());

        if (pointWhereDraggingStarted == null) {
            pointWhereDraggingStarted = currentPoint;
            return;
        }

        final var dx = currentPoint.x - pointWhereDraggingStarted.x;
        final var dy = currentPoint.y - pointWhereDraggingStarted.y;
        final var oneRadian = Math.PI * 2 / 360;
        final var xRadian = oneRadian * -dy * 0.5;
        final var yRadian = oneRadian * dx * 0.5;

        pointWhereDraggingStarted = currentPoint;

        nodes = affineOperationsImpl.oXRotation(xRadian, nodes);
        nodes = affineOperationsImpl.oYRotation(yRadian, nodes);
        figureMiddlePoint = affineOperationsImpl.oXRotation(xRadian, figureMiddlePoint);
        figureMiddlePoint = affineOperationsImpl.oYRotation(yRadian, figureMiddlePoint);

        redrawCanvas();
    }

    @FXML
    private void onZooming(ZoomEvent z) {
        nodes = affineOperationsImpl.scale(z.getZoomFactor(), nodes);
        figureMiddlePoint = affineOperationsImpl.scale(z.getZoomFactor(), figureMiddlePoint);
        redrawCanvas();
    }

    @FXML
    private void onScrolling(ScrollEvent s) {
        nodes = affineOperationsImpl.move(s.getDeltaX(), s.getDeltaY(), nodes);
        figureMiddlePoint = affineOperationsImpl.move(s.getDeltaX(), s.getDeltaY(), figureMiddlePoint);
        redrawCanvas();
    }

    @FXML
    private void onRotating(RotateEvent z) {
        nodes = affineOperationsImpl.oZRotation(z.getAngle() / 20, nodes);
        figureMiddlePoint = affineOperationsImpl.oZRotation(z.getAngle() / 20, figureMiddlePoint);
        redrawCanvas();
    }

    // Event Handlers

    @FXML
    private void onShowPyramidClick() {
        changeFigure(Figure.PYRAMID);
    }

    @FXML
    private void onShowCubeClick() {
        changeFigure(Figure.CUBE);
    }

    @FXML
    private void onShowOctahedronClick() {
        changeFigure(Figure.OCTAHEDRON);
    }

    @FXML
    private void onShowIcosahedronClick() {
        changeFigure(Figure.ICOSAHEDRON);
    }

    @FXML
    private void onShowDodecahedronClick() {
        changeFigure(Figure.DODECAHEDRON);
    }

    @FXML
    private void onShowSphereWithPolesClick() {
        changeFigure(Figure.SPHERE_WITH_POLES);
        nodes = affineOperationsImpl.oXRotation(Math.PI / 2, nodes);
        redrawCanvas();
    }

    @FXML
    private void onShowSphereWithoutPolesClick() {
        changeFigure(Figure.SPHERE_WITHOUT_POLES);
    }

    private void changeFigure(Figure figure) {
        currentFigure = figure;
        final var pair = currentFigure.getNodesAndPolygons();
        nodes = pair.getKey();
        polygons = pair.getValue();
        figureMiddlePoint = new Array2DRowRealMatrix(new double[][] { { 0, 0, 0, 1 } });
        redrawCanvas();
    }

    @FXML
    private void onMirrorOXClick() {
        nodes = affineOperationsImpl.mirrorOX(nodes);
        figureMiddlePoint = affineOperationsImpl.mirrorOX(figureMiddlePoint);
        redrawCanvas();
    }

    @FXML
    private void onMirrorOYClick() {
        nodes = affineOperationsImpl.mirrorOY(nodes);
        figureMiddlePoint = affineOperationsImpl.mirrorOY(figureMiddlePoint);
        redrawCanvas();
    }

    @FXML
    private void onMirrorOZClick() {
        nodes = affineOperationsImpl.mirrorOZ(nodes);
        figureMiddlePoint = affineOperationsImpl.mirrorOZ(figureMiddlePoint);
        redrawCanvas();
    }

    @FXML
    private void onParallelProjectionClick() {
        isPerspectiveProjection = false;
        redrawCanvas();
    }

    @FXML
    private void onPerspectiveProjectionClick() {
        isPerspectiveProjection = true;
        redrawCanvas();
    }

    @FXML
    private void onRobertsAlgorithmEnableClick() {
        isRobertsAlgorithmEnabled = true;
        redrawCanvas();
    }

    @FXML
    private void onRobertsAlgorithmDisableClick() {
        isRobertsAlgorithmEnabled = false;
        redrawCanvas();
    }

    @FXML
    private void onSaveNodesClick() {
        try {
            PrintWriter writer = new PrintWriter("SavedNodes/" + currentFigure.toString().toLowerCase() + ".txt");
            writer.println(currentFigure);

            writer.println(nodes.getRowDimension());
            for (int i = 0; i < nodes.getRowDimension(); i++) {
                final var x = String.valueOf(nodes.getEntry(i, 0));
                final var y = String.valueOf(nodes.getEntry(i, 1));
                final var z = String.valueOf(nodes.getEntry(i, 2));
                writer.println(x + " " + y + " " + z);
            }

            writer.println(polygons.size());
            for (var polygon : polygons) {
                writer.println(polygon.p1 + " " + polygon.p2 + " " + polygon.p3);
            }

            writer.close();
        }
        catch (IOException e) {
            System.out.println("Whoops, something went wrong");
        }
    }

    @FXML
    private void onAboutAuthorClick() {
        openController("AboutAuthorController.fxml", "About", 500, 344);
    }

    private void openController(String name, String title, int width, int height) {
        try {
            final var fxmlLoader = new FXMLLoader(getClass().getResource(name));
            final var scene = new Scene(fxmlLoader.load(), width, height);
            final var stage = new Stage();

            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) {
            System.out.println("Whoops, something went wrong");
        }
    }

}
