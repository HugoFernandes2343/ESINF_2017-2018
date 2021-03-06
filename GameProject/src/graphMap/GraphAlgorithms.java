/*
* A collection of graph algorithms.
 */
package graphMap;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author DEI-ESINF
 */
public class GraphAlgorithms {

    /**
     * Performs breadth-first search of a Graph starting in a Vertex
     *
     * @param g Graph instance
     * @param vInf information of the Vertex that will be the source of the
     * search
     * @return qbfs a queue with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {

        if (!g.validVertex(vert)) {
            return null;
        }

        LinkedList<V> qbfs = new LinkedList<>();
        LinkedList<V> qaux = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];  //default initializ.: false

        qbfs.add(vert);
        qaux.add(vert);
        int vKey = g.getKey(vert);
        visited[vKey] = true;

        while (!qaux.isEmpty()) {
            vert = qaux.remove();
            for (Edge<V, E> edge : g.outgoingEdges(vert)) {
                V vAdj = g.opposite(vert, edge);
                vKey = g.getKey(vAdj);
                if (!visited[vKey]) {
                    qbfs.add(vAdj);
                    qaux.add(vAdj);
                    visited[vKey] = true;
                }
            }
        }
        return qbfs;
    }

    /**
     * Performs depth-first search starting in a Vertex
     *
     * @param g Graph instance
     * @param vOrig Vertex of graph g that will be the source of the search
     * @param visited set of discovered vertices
     * @param qdfs queue with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {
        qdfs.add(vOrig);
        visited[g.getKey(vOrig)] = true;
        for (V vAdj : g.adjVertices(vOrig)) {
            if (!visited[g.getKey(vAdj)]) {
                DepthFirstSearch(g, vAdj, visited, qdfs);
            }
        }
    }

    /**
     * @param g Graph instance
     * @param vInf information of the Vertex that will be the source of the
     * search
     * @return qdfs a queue with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {
        if (!g.validVertex(vert)) {
            return null;
        }
        LinkedList<V> resultList = new LinkedList<V>();
        boolean[] visited = new boolean[g.numVertices()];
        DepthFirstSearch(g, vert, visited, resultList);
        return resultList;
    }

    /**
     * Returns all paths from vOrig to vDest
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param vDest Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path stack with vertices of the current path (the path is in
     * reverse order)
     * @param paths ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, boolean[] visited,
            LinkedList<V> path, ArrayList<LinkedList<V>> paths) {

        visited[g.getKey(vOrig)] = true;
        path.add(vOrig);
        for (V vert : g.adjVertices(vOrig)) {
            if (vert.equals(vDest)) {
                path.add(vDest);
                paths.add(revPath(path));
                path.removeLast();
            } else if (visited[g.getKey(vert)] == false) {
                allPaths(g, vert, vDest, visited, path, paths);
            }
        }
        visited[g.getKey(vOrig)] = false;
        path.removeLast();
    }

    /**
     * @param g Graph instance
     * @param voInf information of the Vertex origin
     * @param vdInf information of the Vertex destination
     * @return paths ArrayList with all paths from voInf to vdInf
     */
    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {

        if ((!g.validVertex(vOrig)) || (!g.validVertex(vDest))) {
            return null;
        }

        boolean[] visited = new boolean[g.numVertices()];
        LinkedList<V> path = new LinkedList<V>();
        ArrayList<LinkedList<V>> paths = new ArrayList<LinkedList<V>>();

        visited[g.getKey(vOrig)] = true;
        for (V vert : g.adjVertices(vOrig)) {
            if (visited[g.getKey(vert)] == false) {
                allPaths(g, vert, vDest, visited, path, paths);
            }
        }
        visited[g.getKey(vOrig)] = false;
        return paths;
    }

    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with nonnegative edge weights This implementation
     * uses Dijkstra's algorithm
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param visited set of discovered vertices
     * @param pathkeys minimum path vertices keys
     * @param dist minimum distances
     */
    private static <V, E> void shortestPathLength(Graph<V, E> g, V vOrig, V[] vertices,
            boolean[] visited, int[] pathKeys, double[] dist) {
        
        dist[g.getKey(vOrig)] = 0;
        int check = 0;
        while (check != -1) {
            visited[g.getKey(vOrig)] = true;
            for (V vertex : g.adjVertices(vOrig)) {
                Edge edgy = g.getEdge(vOrig, vertex);
                if (!visited[g.getKey(vertex)] && dist[g.getKey(vertex)] > (dist[g.getKey(vOrig)] + edgy.getWeight())) {
                    dist[g.getKey(vertex)] = dist[g.getKey(vOrig)] + edgy.getWeight();
                    pathKeys[g.getKey(vertex)] = g.getKey(vOrig);
                }

            }
            Double min = Double.MAX_VALUE;
            check = -1;
            for (int i = 0; i < visited.length; i++) {
                if (!visited[i] && dist[i] < min) {
                    min = dist[i];
                    check = i;
                    vOrig = vertices[check];
                }
            }

        }

    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf The path
     * is constructed from the end to the beginning
     *
     * @param g Graph instance
     * @param voInf information of the Vertex origin
     * @param vdInf information of the Vertex destination
     * @param pathkeys minimum path vertices keys
     * @param path stack with the minimum path (correct order)
     */
    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest, V[] verts, int[] pathKeys, LinkedList<V> path) {

        if (vOrig != vDest) {
            path.push(vDest);
            vDest = verts[pathKeys[g.getKey(vDest)]];
            getPath(g, vOrig, vDest, verts, pathKeys, path);
        } else {
            path.push(vDest);
        }
    }


    public static <V, E> double shortestPath(Graph<V, E> g, V vOrig, V vDest, LinkedList<V> shortPath) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return -1;
        }
        V[] vertices = g.allkeyVerts();
        boolean[] vis = new boolean[g.numVertices()];
        double[] distance = new double[g.numVertices()];
        int[] keys = new int[g.numVertices()];
        shortPath.clear();
        for (int i = 0; i < g.numVertices(); i++) {
            distance[i] = Double.MAX_VALUE;
            keys[i] = -1;
        }
        shortestPathLength(g, vOrig, vertices, vis, keys, distance);
        double pathlength = distance[g.getKey(vDest)];

        if (pathlength != Double.MAX_VALUE) {
            getPath(g, vOrig, vDest, vertices, keys, shortPath);
            return pathlength;
        }
        return -1;
    }

    /**
     * Reverses the path
     *
     * @param path stack with path
     */
    private static <V, E> LinkedList<V> revPath(LinkedList<V> path) {

        LinkedList<V> pathcopy = new LinkedList<>(path);
        LinkedList<V> pathrev = new LinkedList<>();

        while (!pathcopy.isEmpty()) {
            pathrev.push(pathcopy.pop());
        }

        return pathrev;
    }
}
