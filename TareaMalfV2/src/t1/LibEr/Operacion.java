/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t1.LibEr;

/**
 * Esta clase no debería ser representada como tal. TOOD: Corregir.
 * @author patin
 */
 public class Operacion {     
     
     /**
      * Revisa si un string dado corresponde a un caracter
      * @param s
      * @return 
      */
    static public boolean EsCaracter(String s) {
        if (s.length() == 1) {
            if ((s.charAt(0) >= '0' && s.charAt(0) <= '9') || (s.charAt(0) >= 'a' && s.charAt(0) <= 'z') || (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z'))
            {
                //System.out.println("true");
                return true;
            }
        }
        return false;
    }

    /**
     * Encuentra la primera operacion utilizando el simbolo dado
     * que no este agrupada entre parentesis de ningun tipo. Retorna
     * el indice correspondiente al simbolo de esa operacion. -1 si
     * no existe tal operacion.
     * @param simbolo
     * @param s
     * @return 
     */
    static public int Encontrar(char simbolo, String s) {
        int contador = 0;
        int indiceExterno = 0;
        for(char c : s.toCharArray()) {
            if (c == '(') { contador++; }
            else if (c == ')') { contador--; }
            else if (c == simbolo && contador == 0) {
                //System.out.println(simbolo);
                return indiceExterno;
            }
            indiceExterno++;
        }

        return -1;
    }
    
    /**
     * Revisa si una cadena contiene solo caracteres del lenguaje aceptado por los automatas
     * @param s
     * @return 
     */
    static public boolean PerteneceAlLenguaje(String s){
        for(char c : s.toCharArray()){
            if (!EsCaracter(c+"")){
                return false;
            }
        }
        return true;
    }
    
}
