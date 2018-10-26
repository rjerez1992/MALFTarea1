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
public class Parentesis extends NodoEr{
     public NodoEr nodo;

    public Parentesis(NodoEr nodo)
    {
        this.nodo = nodo;
    }

     @Override
    public void Imprimir()
    {
        System.out.print("(");
        nodo.Imprimir();
        System.out.print(")");
    }
}
