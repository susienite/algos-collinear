/* *****************************************************************************
 *  Name: Susan Tan
 *  Date: 09/05/2021
 *  Description:
 * Write a program BruteCollinearPoints.java that examines 4 points at a time
 *  and checks whether they all lie on the same line segment, returning all such
 * line segments. To check whether the 4 points p, q, r, and s are collinear,
 * check whether the three slopes between p and q, between p and r,
 * and between p and s are all equal.
 **************************************************************************** */


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;


public class BruteCollinearPoints {


    private int numLS = 0;
    private LineSegment[] arrayLS;
    private int capacity;

    private Point maxP(Point[] ps) {
        Point max = ps[0];
        for (int i = 1; i < ps.length; i++) {
            if (max.compareTo(ps[i]) < 0) {
                max = ps[i];
            }
        }
        return max;
    }

    private Point minP(Point[] ps) {
        Point min = ps[0];
        for (int i = 1; i < ps.length; i++) {
            if (min.compareTo(ps[i]) > 0) {
                min = ps[i];
            }
        }
        return min;
    }

    private void resize(int newCapacity) {
        capacity = newCapacity;
        LineSegment[] copy = new LineSegment[newCapacity];
        for (int i = 0; i < numLS; i++) {
            copy[i] = arrayLS[i];
        }
        arrayLS = copy;
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        // Checking for errors in point array
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        Point[] repoints = points.clone();
        Arrays.sort(repoints);
        capacity = repoints.length;
        arrayLS = new LineSegment[capacity];

        for (int i = 0; i < repoints.length - 3; i++) {
            Point pt1 = repoints[i];

            for (int j = i + 1; j < repoints.length - 2; j++) {
                Point pt2 = repoints[j];
                double slope12 = pt1.slopeTo(pt2);

                for (int k = j + 1; k < repoints.length - 1; k++) {
                    Point pt3 = repoints[k];
                    double slope13 = pt1.slopeTo(pt3);

                    if (slope12 == slope13) {
                        for (int m = k + 1; m < repoints.length; m++) {
                            Point pt4 = repoints[m];
                            double slope14 = pt1.slopeTo(pt4);

                            if (slope12 == slope14) {
                                // compare the points to see which one is the biggest and make a line segment
                                if (numLS == capacity) resize(2 * capacity);
                                LineSegment ls = new LineSegment(pt1, pt4);
                                arrayLS[numLS] = ls;
                                numLS++;
                            }

                        }
                    }

                }
            }
        }

        if (capacity != numLS) {
            resize(numLS);
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return numLS;
    }

    // the line segments
    public LineSegment[] segments() {
        return arrayLS;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

