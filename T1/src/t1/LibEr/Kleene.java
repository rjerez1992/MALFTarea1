/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t1.LibEr;

/**
 *
 * @author patin
 */
public class Kleene extends NodoEr{
    private NodoEr nodo; 

        public Kleene(NodoEr nodo) {
            this.nodo = nodo;
        }

    @Override
        public void Imprimir()
        {
            getNodo().Imprimir();
            System.out.print("*");
        }

    /**
     * @return the nodo
     */
    public NodoEr getNodo() {
        return nodo;
    }

    /**
     * @param nodo the nodo to set
     */
    public void setNodo(NodoEr nodo) {
        this.nodo = nodo;
    }
        
}
