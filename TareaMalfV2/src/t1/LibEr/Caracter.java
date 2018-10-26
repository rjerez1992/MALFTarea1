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
public class Caracter extends NodoEr {
    
    private String valor;
    public Caracter(String s){
        this.valor=s;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
    
   
    @Override
    public void Imprimir(){
        System.out.print(this.valor);
    }
}
