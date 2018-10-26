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
public class NodoEr {
    
    /**
     * Construye de manera recursiva la expresion regular. Dado que es recursivo
     * la prioridad de operadores se invierte.
     * @param s
     * @return 
     */
    public NodoEr Construir(String s){        
        //En el caso especial que sea la ER vacia
        if (s.contains("~")) {
            if (s.length() != 1){
                //Si llegamos aqui, probablemente esté mal formada
                System.out.println("Error: No se puede parsear la expresion ingresada.");
                System.exit(0);
            }
            else{
                return new Caracter(s);
            }
        }

        //El caso base es si el string es caracter.
        if (Operacion.EsCaracter(s)) {
            return new Caracter(s);
        }

        //En caso que no lo sea, buscamos alguna operacion separable
        int i = Operacion.Encontrar('|', s);
        if (i != -1 && i+1 < s.length()) {            
            String a = s.substring(0, i);            
            String b = s.substring(i + 1); 
            return new Union(new NodoEr().Construir(a), new NodoEr().Construir(b));
        }

        i = Operacion.Encontrar('.', s);
        if (i != -1 && i+1 < s.length()) {
            String a = s.substring(0, i);
            String b = s.substring(i + 1);         
            return new Concatenacion(new NodoEr().Construir(a), new NodoEr().Construir(b));
        }

        //En caso que no encontremos una operacion separable
        //descomponemos clausulas de kleene que no esten en parentesis
        if (s.charAt(s.length() - 1) == '*') {
            String a = s.substring(0, s.length() - 1);               
            return new Kleene(new NodoEr().Construir(a));
        }

        //Finalmente descomponemos parentesis
        if (s.charAt(0) == '(' && s.charAt(s.length() -1) == ')') {
            String a = s.substring(1, s.length() - 1);               
            return new Parentesis(new NodoEr().Construir(a));
        }
     
        //Si llegamos aqui, probablemente esté mal formada
        System.out.println("Error: No se puede parsear la expresion ingresada.");
        System.exit(0);
        return null;
    }
    
    /**
     * Imprime la representacion del nodo
     */
    public void Imprimir(){}
}

