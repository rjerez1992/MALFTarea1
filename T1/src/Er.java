/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import t1.LibEr.NodoEr;
import t1.LibEr.Operacion;

/**
 *
 * @author patin
 */
public class Er {
    /**
     * Comodin que representa todos los caracteres sin uso.
     */
    public static char Comodin = '%';

    /**
     * Representacion en string de la expresion regular.
     */
    private String Representacion; 

    /**
     * Estructura de clases de la expresion regular.
     */
    public NodoEr Estructura;

    /**
     * Construye la representacion de clases y registra la representacion en string.
     * @param s 
     */
    public Er(String s) {
        this.Representacion = s;
        try{
            this.Estructura = new NodoEr().Construir(this.Representacion);
        }
        catch(Exception e){
            System.out.println("Error: No se puede parsear la expresion ingresada.");
            System.exit(0);
        }
    }
    
    /**
     * Imprime la ER a partir de su estructura (para fines de pruebas).
     */
    public void Imprimir() {
        this.Estructura.Imprimir();
        System.out.println("");
    }
    
    /**
     * Obtiene el alfabeto de la ER actual
     * @return 
     */
    public ArrayList<String> Alfabeto(){
        ArrayList<String> lc = new ArrayList<>();
        for(char c : this.Representacion.toCharArray()) {
            if (Operacion.EsCaracter(c+"")) {
                if (!contieneCaracter(lc, c+""))
                    lc.add(c+"");
            }
        }
        lc.add(Comodin+"");
        return lc;
    }

    //TODO: Implementar validacion de ER
    static public boolean ValidarER(String s) { return true; }
    
    /**
     * Verifica si la ER contiene cierto caracter.
     * @param a
     * @param letra
     * @return 
     */
    public boolean contieneCaracter(ArrayList<String> a,String letra){
        for(String s: a){
            if(s.equals(letra)){
                return true;
            }
        }
        return false;
    }    
}
