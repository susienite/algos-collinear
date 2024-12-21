/* *****************************************************************************
 *  Name: Susan Tan
 *  Date: 09/25/2021
 *  Description: Given a set of n distinct points in the plane,
 * find every (maximal) line segment that connects a subset of
 * 4 or more of the points.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private LineSegment[] arrayLS;
    private int capacity;
    private int numLS;
    private Point[] dups;
    private int dupsCount;


    private void resize(int newCapacity) {
        capacity = newCapacity;
        LineSegment[] copy = new LineSegment[newCapacity];
        for (int i = 0; i < numLS; i++) {
            copy[i] = arrayLS[i];
        }
        arrayLS = copy;
    }


    // finds all line segments containing 4 or more points
    // compare the points by their slopes
    // compareTo returns the slopeTo p value
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null || points[i + 1] == null) {
                throw new java.lang.IllegalArgumentException();
            }
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        Arrays.sort(points);
        capacity = points.length;
        arrayLS = new LineSegment[capacity];
        dups = new Point[capacity];

        // set origin point p and its adjacent points q
        nest3:
        for (int p = 0; p < points.length - 3; p++) {
            Point origin = points[p];

            /*for (int i = 0; i < dupsCount; i++) {
                if (dups[i].compareTo(origin) == 0) {
                    System.out.println("there is a matching dup");
                    continue nest3;
                }
            }*/

            Comparator<Point> comp = origin.slopeOrder();
            Point[] rePoints = Arrays.copyOfRange(points, p + 1, points.length);
            MergeX.sort(rePoints, comp);

            //System.out.println("origin is " + origin);
            //System.out.println("repoints length is " + rePoints.length);


            nest2:
            for (int q = 0; q < rePoints.length - 1; q++) {
                Point q1 = rePoints[q];
                double recall = origin.slopeTo(q1);
                int counter = 1;

                nest1:
                for (int r = q + 1; r < rePoints.length; r++) {
                    Point q2 = rePoints[r];
                    double currSlope = origin.slopeTo(q2);

                    //System.out.println("point1 is " + q1 + " and point2 is " + q2);
                    //System.out.println("p: " + p + " and q: " + q);

                    // slopes are the same
                    if (recall == currSlope) {
                        counter += 1;
                    }

                    //System.out.println("counter = " + counter);

                    if (counter == rePoints.length) {
                        LineSegment ls = new LineSegment(origin, rePoints[r]);
                        arrayLS[numLS] = ls;
                        numLS++;
                        //System.out.println("counter is repoints' length: " + ls);
                        break nest3;
                    }

                    // slopes are not the same
                    if (recall != currSlope) {
                        // if there is enough to make a line segment
                        if ((r - q) >= 3) {
                            if (numLS == capacity) resize(2 * capacity);
                            LineSegment ls = new LineSegment(origin, rePoints[r - 1]);
                            arrayLS[numLS] = ls;
                            numLS++;
                            //System.out.println("slope not the same: " + ls);
                            int ctemp = counter;
                            /*for (int i = r - 1; ctemp > 1; i--) {
                                dups[dupsCount] = rePoints[i];
                                dupsCount += 1;
                                ctemp--;
                            }
                            System.out.println("dups points: ");
                            for (int i = 0; i < dupsCount; i++) {
                                System.out.print(dups[i] + ", ");
                            }
                            System.out.println();*/
                        }
                        q = q + counter - 1;
                        break;
                    }


                    // we reached the end of the search
                    if (r == (rePoints.length - 1) && ((r - q) >= 2) && recall == currSlope) {
                        if (numLS == capacity) resize(2 * capacity);
                        LineSegment ls = new LineSegment(origin, rePoints[r]);
                        arrayLS[numLS] = ls;
                        numLS++;
                        //System.out.println("end of search: " + ls);
                        int ctemp = counter;
                        /*for (int i = r - 1; ctemp > 1; i--) {
                            dups[dupsCount] = rePoints[i];
                            dupsCount += 1;
                            ctemp--;
                        } */
                        break nest2;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
