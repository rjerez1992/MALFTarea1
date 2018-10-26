
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import t1.LibFnd.Estado;
import t1.LibFnd.Transicion;


/**
 *
 * @author patricio
 */
public class AFD2 {
    /**
     * Alfabeto
     */
    public ArrayList<String> alfabeto = new ArrayList<>();
    /**
     * Caracteres a recorrer
     */
    public ArrayList<String> caracteres;
    /**
     * Estado inicial
     */
    public Estado estadoInicial;
    
    /**
     * Estados del automata
     */
    public HashMap<Integer,Estado> estados;
   
    
    /*
     Tabla de estados
    */
    public Object[][] tabla; 
    
    /**
     * Contador que recorre la tabla
     */
    public int contadorTabla;
    
    //Contadores globales
    public static int maxRow;
    public static int maxColumn;
    
    
    public Queue<Integer> estadosPorRecorrer;
    public Stack<Transicion> transicionesPorRecorrer;
    
    public ArrayList<Integer> visitados;
    
    /**
     * 
     * @param afnd 
     */
     public AFD2(AFND afnd){
         Initialize();
         this.alfabeto = AFND.Alfabeto;
         this.contadorTabla = 0;
         // Contadores maximos de la tabla
         AFD2.maxRow = afnd.Nodos.size();
         AFD2.maxColumn = this.alfabeto.size() + 1;
         // Agrega epsilon a los caracteres
         this.caracteres.add("_");
         this.caracteres.addAll(alfabeto);
         // Llena el hashmap
         llenarHashMap(afnd);
         construir(afnd);
         imprimirTabla();
         
     }
     
     /**
      * Inicializa las instancias 
      */
     public void Initialize(){
         //Tabla generadora
         this.tabla = new Object[AFD2.maxRow][AFD2.maxColumn]; // [alfabeto + 1] = pos 0 
         //Caracteres del alfabeto
         this.caracteres = new ArrayList<>();
         //Hashmap de estados
         this.estados = new HashMap<>();
         //Estados visitados
         this.visitados = new ArrayList<>();
         this.estadosPorRecorrer = new LinkedList<>();
         this.transicionesPorRecorrer = new Stack<>();
     }
     
     public void llenarHashMap(AFND afnd){
         for (Estado e : afnd.Nodos) {
             this.estados.put(e.Identificador,e);
         }
     }
     
     
     
     public void construir(AFND afnd){
       //Añadimos el estado inicial
         for(Estado e:afnd.Nodos){
             System.out.print(e.Identificador+" ");
         }
       System.out.println("");
       // añade el estado inicial
       estadosPorRecorrer.add(estadoInicial.Identificador);
       
       while(estadosPorRecorrer.isEmpty()){
           Integer e = estadosPorRecorrer.peek(); //Saca el estado a analizar
           guardarEstado(e); //guarda el estado como visitado
           apilarTransiciones(e); //Apila sus tensiciones
           recorrerTransiciones(); //Recorre las transiciones 
               
       }
       
         System.out.println(this.visitados);
     }
     
     public void apilarTransiciones(Integer num){
         Estado e = this.estados.get(num);
         for(Transicion t : e.Transiciones) {
             if(t.getCaracter() == '_'){
                 transicionesPorRecorrer.add(t);
             }
         }
     }
     
     public void recorrerTransiciones(){
         while( !this.transicionesPorRecorrer.isEmpty() ){
             Transicion t = transicionesPorRecorrer.pop();
             if(t.getCaracter() == '_' && !this.visitados.contains(t.getNodoB())){
                 this.estadosPorRecorrer.add(t.getNodoB());
             }
         }
     }
     
     //Guarda los estados no repetidos
     public void guardarEstado(Integer estadoAGuardar){
        this.visitados.add(estadoAGuardar);
     }
     
     public void imprimirTabla(){
         for (int i = 0; i < caracteres.size(); i++) {
             System.out.print(caracteres.get(i)+" ");
         }
         System.out.println("");
         for (int i = 0; i < AFD2.maxRow; i++) {
             for (int j = 0; j < AFD2.maxColumn; j++) {
                 System.out.print(((ArrayList)this.tabla[i][j])+" ");
             }
             System.out.println("");
         }
     }
     
}
