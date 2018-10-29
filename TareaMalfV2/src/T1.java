import java.util.Scanner;

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
        Scanner s = new Scanner(System.in);
        String expresion = s.nextLine();
        String cadena = s.nextLine();

        //TODO: Hacer cambios de strings de prueba a strings de entrada
        //String prueba =  "(((a.b*)*)*)*";  
        //String prueba1 = "ababcaaacbbb";

        //Generamos la estructura de la ER
        Er er = new Er(expresion); 
        //er.Imprimir(); //TODO: Eliminar esto antes de enviar

        //Generamos el AFND a partir de la ER
        AFND afnd = new AFND(er.Estructura);
        afnd.AsignarAlfabeto(er.Alfabeto());
       // afnd.Complementar(); -> esto esta demas
        afnd.Imprimir();

        System.out.println("");

        //Generamos el AFD a partir del AFND
        AFD2 afd = new AFD2(afnd);
        afd.Imprimir();
        //afd.Imprimir(); 

        System.out.println("");

        //Para comprobar los calces, necesitamos el automata sin los loops
        //iniciales que consumen cualquier cosa (de esa forma lo implementamos)       
        //afnd.ReiniciarContador();
        //afd.ReiniciarContador();
        //afnd = new AFND(er.Estructura);                  
        //afd = new AFD2(afnd);

        //Comprobamos los calces
        afd.Comprobar(cadena);
        //System.out.println(prueba);
        //System.out.println(prueba1);
    }    
    
  
}
