/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameproject;

/**
 *
 * @author 1120608 Norberto Sousa 1161155 Hugo Fernandes
 */
public class GameBase {

    private AdjacencyMatrixGraph<Locale, Roads> matrix;
    private AdjacencyMatrixGraph<Character, Aliance> map; // mudar isto para map e nao matrix

    public void GameBase() {
        this.matrix = new AdjacencyMatrixGraph<Locale, Roads>();
        this.map = new AdjacencyMatrixGraph<Character, Aliance>();
    }

    public Locale searchForLocal(String s) {
        Locale l = new Locale();
        for (int i = 0; i < matrix.numVertices; i++) {
            if(matrix.vertices.get(i).getName().equalsIgnoreCase(s)){
            l = matrix.vertices.get(i);
            }
        }
        return l;
    }

    public AdjacencyMatrixGraph<Locale, Roads> getMatrix() {
        return matrix;
    }

    public AdjacencyMatrixGraph<Character, Aliance> getMap() {
        return map;
    }

    public boolean insertLocale(String n, int d) {
        return matrix.insertVertex(new Locale(n, d));

    }

    public boolean insertRoads(int d, Locale l1, Locale l2) {
        return matrix.insertEdge(l1, l2, new Roads(d, l1, l2));

    }

    public boolean insertCharacter(String n, int s,Locale l) {
        return map.insertVertex(new Character(n, s,l));

    }

    public boolean insertAliance(boolean p, float cf, float pw, Character c1, Character c2) {
        return map.insertEdge(c1, c2, new Aliance(p, cf, pw, c1, c2));

    }

    Character searchForCharacter(String s) {
        Character c = new Character();
        for (int i = 0; i < map.numVertices; i++) {
            if(map.vertices.get(i).getName().equalsIgnoreCase(s)){
            c = map.vertices.get(i);
            }
        }
        return c;
    }
}