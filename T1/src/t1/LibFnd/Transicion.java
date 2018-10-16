/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t1.LibFnd;

/**
 * Representa una transicion de un estado a otro
 * @author patin
 */
public class Transicion {
    private char Caracter; 
    private int NodoB; 

    public Transicion( char c, int b)
    {
        this.Caracter = c;
        this.NodoB = b;
    }

    /**
     * @return the Caracter
     */
    public char getCaracter() {
        return Caracter;
    }

    /**
     * @param Caracter the Caracter to set
     */
    public void setCaracter(char Caracter) {
        this.Caracter = Caracter;
    }

    /**
     * @return the NodoB
     */
    public int getNodoB() {
        return NodoB;
    }

    /**
     * @param NodoB the NodoB to set
     */
    public void setNodoB(int NodoB) {
        this.NodoB = NodoB;
    }
    
    
}
