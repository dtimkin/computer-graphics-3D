package app;

import javafx.util.Pair;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Figure {
    PYRAMID,
    CUBE,
    OCTAHEDRON,
    ICOSAHEDRON,
    DODECAHEDRON,
    SPHERE_WITH_POLES,
    SPHERE_WITHOUT_POLES;

    public Pair<Array2DRowRealMatrix, ArrayList<Polygon>> getNodesAndPolygons() {
        switch (this) {
            case PYRAMID:
                return getPyramid();

            case CUBE:
                return getCube();

            case OCTAHEDRON:
                return getOctahedron();

            case ICOSAHEDRON:
                return getIcosahedron();

            case DODECAHEDRON:
                return getDodecahedron();

            case SPHERE_WITH_POLES:
                return getSphereWithPoles();

            case SPHERE_WITHOUT_POLES:
                return getSphereWithoutPoles();

            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    // Figures Generators

    private Pair<Array2DRowRealMatrix, ArrayList<Polygon>> getPyramid() {
        final var nodes = new Array2DRowRealMatrix(new double[][] {
                { 0, -167, 0, 1 },
                { -150, 93, 87, 1 },
                { 150, 93, 87, 1 },
                { 0, 93, -173, 1 }
        });

        final var polygons = Arrays.asList(
                new Polygon(0, 1, 2),
                new Polygon(0, 2, 3),
                new Polygon(0, 1, 3),
                new Polygon(1, 2, 3)
        );

        return new Pair<>(nodes, new ArrayList<>(polygons));
    }

    private Pair<Array2DRowRealMatrix, ArrayList<Polygon>> getCube() {
        final var nodes = new Array2DRowRealMatrix(new double[][] {
                { -117, -117, -117,1 },
                { 117, -117, -117, 1 },
                { -117, 117, -117, 1 },
                { 117, 117, -117, 1 },
                { -117, -117, 117, 1 },
                { 117, -117, 117, 1 },
                { -117, 117, 117, 1 },
                { 117, 117, 117, 1 }
        });

        final var polygons = Arrays.asList(
                new Polygon(0, 1, 2),
                new Polygon(1, 2, 3),
                new Polygon(1, 3, 7),
                new Polygon(1, 5, 7),
                new Polygon(4, 5, 6),
                new Polygon(5, 6, 7),
                new Polygon(0, 2, 6),
                new Polygon(0, 4, 6),
                new Polygon(0, 1, 5),
                new Polygon(0, 4, 5),
                new Polygon(2, 6, 7),
                new Polygon(2, 3, 7)
        );

        return new Pair<>(nodes, new ArrayList<>(polygons));
    }

    private Pair<Array2DRowRealMatrix, ArrayList<Polygon>> getOctahedron() {
        Array2DRowRealMatrix nodes = new Array2DRowRealMatrix(new double[][] {
                {0, -150, 0, 1},
                {150, 0, 0, 1},
                {-150, 0, 0, 1},
                {0, 0, 150, 1},
                {0, 0, -150, 1},
                {0, 150, 0, 1}
        });

        List<Polygon> polygons = Arrays.asList(
                new Polygon(0, 1, 3),
                new Polygon(0, 2, 3),
                new Polygon(0, 2, 4),
                new Polygon(0, 1, 4),

                new Polygon(5, 1, 3),
                new Polygon(5, 2, 3),
                new Polygon(5, 2, 4),
                new Polygon(5, 1, 4)
        );

        return new Pair<>(nodes, new ArrayList<>(polygons));
    }

    private Pair<Array2DRowRealMatrix, ArrayList<Polygon>> getIcosahedron() {
        final var nodes = new Array2DRowRealMatrix(12, 4);
        final var polygons = new ArrayList<Polygon>();

        final var oneRadian = Math.PI * 2 / 360;
        final var radius = 150d;

        for (int i = 0; i < 10; i++) {
            final var currentRadian = (oneRadian * i + 5) * 36;
            final var x = radius * Math.cos(currentRadian);
            final var y = (i % 2 == 0) ? 75 : -75d;
            final var z = radius * Math.sin(currentRadian);

            nodes.setEntry(i, 0, x);
            nodes.setEntry(i, 1, y);
            nodes.setEntry(i, 2, z);
            nodes.setEntry(i, 3, 1);

            if (i > 1)
                polygons.add(new Polygon(i - 2, i - 1, i));

            if(i < 8) {
                final var level = (i % 2 == 0) ? 10 : 11;
                polygons.add(new Polygon(i, i + 2, level));
            }
        }

        polygons.add(new Polygon(8, 9, 0));
        polygons.add(new Polygon(9, 0, 1));
        polygons.add(new Polygon(9, 1, 11));
        polygons.add(new Polygon(8, 0, 10));

        nodes.setEntry(11, 0, 0);
        nodes.setEntry(11, 1, -165);
        nodes.setEntry(11, 2, 0);
        nodes.setEntry(11, 3, 1);

        nodes.setEntry(10, 0, 0);
        nodes.setEntry(10, 1, 165);
        nodes.setEntry(10, 2, 0);
        nodes.setEntry(10, 3, 1);

        return new Pair<>(nodes, polygons);
    }

    private Pair<Array2DRowRealMatrix, ArrayList<Polygon>> getDodecahedron() {
        final var h = 70d;
        final var w = 70d;

        Array2DRowRealMatrix nodes = new Array2DRowRealMatrix(new double[][] {
                { -117, -117, -117, 1 },
                { 117, -117, -117, 1 },
                { -117, -117, 117, 1 },
                { 117, -117, 117, 1 },
                { 0, -117 - h, -w, 1 },
                { 0, -117 - h, w, 1 },

                { -117 - h, -w, 0, 1 },
                { -117 - h, w, 0, 1 },

                { -80, 0, 117 + h, 1 },
                { 80, 0, 117 + h, 1 },

                { 117 + h, -w, 0, 1 },
                { 117 + h, w, 0, 1 },

                { -w, 0, -117 - h, 1 },
                { w, 0, -117 - h, 1 },

                { -117, 117, -117, 1 },
                { 117, 117, -117, 1 },
                { -117, 117, 117, 1 },
                { 117, 117, 117, 1 },
                { 0, 117 + h, -w, 1 },
                { 0, 117 + h, w, 1 }
        });

        List<Polygon> polygons = Arrays.asList(
                new Polygon(0, 1, 4),
                new Polygon(0, 4, 5),
                new Polygon(1, 4, 5),
                new Polygon(2, 3, 5),

                new Polygon(0, 2, 6),
                new Polygon(0, 6, 7),
                new Polygon(2, 6, 7),

                new Polygon(1, 3, 10),
                new Polygon(1, 10, 11),
                new Polygon(3, 10, 11),

                new Polygon(14, 15, 18),
                new Polygon(14, 18, 19),
                new Polygon(15, 18, 19),
                new Polygon(16, 17, 19),

                new Polygon(14, 16, 7),
                new Polygon(15, 17, 11),

                new Polygon(2, 16, 8),
                new Polygon(2, 8, 9),
                new Polygon(16, 8, 9),
                new Polygon(3, 17, 9),

                new Polygon(0, 14, 12),
                new Polygon(0, 12, 13),
                new Polygon(14, 12, 13),
                new Polygon(1, 15, 13),

                new Polygon(0, 2, 5),
                new Polygon(1, 3, 5),
                new Polygon(3, 2, 9),
                new Polygon(2, 7, 16),
                new Polygon(3, 11, 17),
                new Polygon(9, 16, 17),
                new Polygon(14, 16, 19),
                new Polygon(15, 17, 19),
                new Polygon(0, 1, 13),
                new Polygon(1, 15, 11),
                new Polygon(0, 7, 14),
                new Polygon(14, 15, 13)
        );

        return new Pair<>(nodes, new ArrayList<>(polygons));
    }

    private Pair<Array2DRowRealMatrix, ArrayList<Polygon>> getSphereWithPoles() {
        final var polygons = new ArrayList<Polygon>();
        final var nodes = new ArrayList<double[]>();

        final var RADIUS = 150d;
        final var radian = Math.PI * 2 / 360 * 20;

        for (var i = 0d; i <= Math.PI * 2; i += radian)
            for (var j = 0d; j < Math.PI; j += radian) {
                final var x1 = RADIUS * Math.sin(i) * Math.cos(j);
                final var y1 = RADIUS * Math.sin(i) * Math.sin(j);
                final var z1 = RADIUS * Math.cos(i);
                final var p1Index = addNode(new double[] { x1, y1, z1, 1 }, nodes);

                final var x2 = RADIUS * Math.sin(i + radian) * Math.cos(j);
                final var y2 = RADIUS * Math.sin(i + radian) * Math.sin(j);
                final var z2 = RADIUS * Math.cos(i + radian);
                final var p2Index = addNode(new double[] { x2, y2, z2, 1 }, nodes);

                final var x3 = RADIUS * Math.sin(i) * Math.cos(j + radian);
                final var y3 = RADIUS * Math.sin(i ) * Math.sin(j + radian);
                final var z3 = RADIUS * Math.cos(i);
                final var p3Index = addNode(new double[] { x3, y3, z3, 1 }, nodes);

                final var x4 = RADIUS * Math.sin(i + radian) * Math.cos(j + radian);
                final var y4 = RADIUS * Math.sin(i + radian) * Math.sin(j + radian);
                final var z4 = RADIUS * Math.cos(i + radian);
                final var p4Index = addNode(new double[] { x4, y4, z4, 1 }, nodes);

                polygons.add(new Polygon(p1Index, p3Index, p4Index));
                polygons.add(new Polygon(p1Index, p2Index, p4Index));
            }

        final var resultNodes = nodes.toArray(new double[nodes.size()][]);
        return new Pair<>(new Array2DRowRealMatrix(resultNodes), polygons);
    }

    private Pair<Array2DRowRealMatrix, ArrayList<Polygon>> getSphereWithoutPoles() {
        var icosahedronPair = getIcosahedron();
        var icosahedronNodes = icosahedronPair.getKey().getData();

        var polygons = icosahedronPair.getValue();
        var nodes = new ArrayList<>(Arrays.asList(icosahedronNodes));

        var RECURSION_DEPTH = 2;
        for (int i = 0; i < RECURSION_DEPTH; i++) {
            final var newPolygons = new ArrayList<Polygon>();
            final var newNodes = new ArrayList<double[]>();

            for(var polygon : polygons) {
                final var p1 = nodes.get(polygon.p1);
                final var p1Index = addNode(p1, newNodes);

                final var p2 = nodes.get(polygon.p2);
                final var p2Index = addNode(p2, newNodes);

                final var p3 = nodes.get(polygon.p3);
                final var p3Index = addNode(p3, newNodes);

                final var newP1 = calculateMiddlePoint(p1, p2);
                final var newP1Index = addNode(newP1, newNodes);

                final var newP2 = calculateMiddlePoint(p1, p3);
                final var newP2Index = addNode(newP2, newNodes);

                final var newP3 = calculateMiddlePoint(p2, p3);
                final var newP3Index = addNode(newP3, newNodes);

                newPolygons.add(new Polygon(p1Index, newP1Index, newP2Index));
                newPolygons.add(new Polygon(p2Index, newP1Index, newP3Index));
                newPolygons.add(new Polygon(p3Index, newP2Index, newP3Index));
                newPolygons.add(new Polygon(newP1Index, newP2Index, newP3Index));
            }

            nodes = newNodes;
            polygons = newPolygons;
        }

        final var resultNodes = nodes.toArray(new double[nodes.size()][]);
        return new Pair<>(new Array2DRowRealMatrix(resultNodes), polygons);
    }

    // Helpers

    private int addNode(double[] node, ArrayList<double[]> nodes) {
        for(var i = 0; i < nodes.size(); i++) {
            final var curNode = nodes.get(i);
            if(node[0] == curNode[0] && node[1] == curNode[1] && node[2] == curNode[2] && node[3] == curNode[3])
                return i;
        }

        nodes.add(node);
        return nodes.size() - 1;
    }

    private double[] calculateMiddlePoint(double[] p1, double[] p2) {
        final var x = (p1[0] + p2[0]) / 2;
        final var y = (p1[1] + p2[1]) / 2;
        final var z = (p1[2] + p2[2]) / 2;
        final var length = Math.sqrt(x * x + y * y + z * z);

        return new double[] { x / length * 1.13 * 150, y / length * 1.13 * 150 , z / length * 1.13 * 150, 1 };
    }

}
