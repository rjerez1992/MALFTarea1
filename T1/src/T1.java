
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Reinaldo Jerez
 * Patricio Quezada
 * @author patin
 */
public class T1 {

    /**
     * Tarea 1 - MALF
     * @param args the command line arguments
     */
    public static void main(String[] args) {    
        //Leemos los datos desde el archivo
        /*Scanner s = new Scanner(System.in);
        String expresion = s.nextLine();
        String cadena = s.nextLine();*/

        //TODO: Hacer cambios de strings de prueba a strings de entrada
        String prueba =  "(a.b)*";  
        String prueba1 = "aaabababbbbbab";

        //Comprobamos la validez de la ER
        if (!Er.ValidarER(prueba)) {
           System.out.println("Expresion regular invalida");
           System.exit(0);
        }

        //Generamos la estructura de la ER
        Er er = new Er(prueba); 
        //er.Imprimir(); //TODO: Eliminar esto antes de enviar

        //Generamos el AFND a partir de la ER
        AFND afnd = new AFND(er.Estructura);
        afnd.AsignarAlfabeto(er.Alfabeto());
        afnd.Complementar();
        afnd.Imprimir();

        System.out.println("");

        //Generamos el AFD a partir del AFND
        AFD afd = new AFD(afnd);
        afd.Imprimir(); 

        System.out.println("");

        //Para comprobar los calces, necesitamos el automata sin los loops
        //iniciales que consumen cualquier cosa (de esa forma lo implementamos)       
        afnd.ReiniciarContador();
        afd.ReiniciarContador();
        afnd = new AFND(er.Estructura);                  
        afd = new AFD(afnd);

        //Comprobamos los calces
        afd.Comprobar(prueba1);
        //System.out.println(prueba);
        //System.out.println(prueba1);
    }    
    
  
}
