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
public class Concatenacion extends NodoEr{
    
    private NodoEr NodoA;
    private NodoEr NodoB; 

    public Concatenacion(NodoEr nodoA, NodoEr nodoB)
    {
        this.NodoA = nodoA;
        this.NodoB = nodoB;
    }

    @Override
    public void Imprimir()
    {
        getNodoA().Imprimir();
        System.out.print(".");
        getNodoB().Imprimir();
    }

    /**
     * @return the NodoA
     */
    public NodoEr getNodoA() {
        return NodoA;
    }

    /**
     * @param NodoA the NodoA to set
     */
    public void setNodoA(NodoEr NodoA) {
        this.NodoA = NodoA;
    }

    /**
     * @return the NodoB
     */
    public NodoEr getNodoB() {
        return NodoB;
    }

    /**
     * @param NodoB the NodoB to set
     */
    public void setNodoB(NodoEr NodoB) {
        this.NodoB = NodoB;
    }
    
    
    
}
