
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    public int contadorColumnaTabla;
    
    /**
     * Contador maximo para las filas de la tabla
     */
    public int contadorFilaTabla;

    //Contadores globales
    public  int maxRow;
    public  int maxColumn;
    
    
    public Queue<Integer> estadosPorRecorrer;
    public Stack<Transicion> transicionesPorRecorrer;
    
    public ArrayList<Integer> visitados;
    
    /**
     * 
     * @param afnd 
     */
     public AFD2(AFND afnd){
         this.contadorColumnaTabla = 0;
         this.contadorFilaTabla = 0;
         this.estadoInicial = afnd.EstadoInicial;
         this.alfabeto = AFND.Alfabeto;
         // Contadores maximos de la tabla
         this.maxRow = afnd.Nodos.size();
         this.maxColumn = this.alfabeto.size() + 1;
         
         Ini();

         // Agrega epsilon a los caracteres
         this.caracteres.add("_");
         this.caracteres.addAll(alfabeto);
         // Llena el hashmap
         llenarHashMap(afnd);
         //System.out.println("Estados"+this.estados.size());
         afndToAfd(afnd);
         
         imprimirTabla();
         
     }
     
     /**
      * Inicializa las instancias 
      */
     public void Ini(){
         //Tabla generadora
         // System.out.println(this.maxRow+" "+this.maxColumn);
         this.tabla = new Object[this.maxRow][this.maxColumn]; // [alfabeto + 1] = pos 0 
         //Caracteres del alfabeto
         this.caracteres = new ArrayList<>();
         //Hashmap de estados
         this.estados = new HashMap<>();
         //Estados visitados
         //this.visitados = new ArrayList<>();
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
       //System.out.println(estadoInicial.Identificador+"");
       // añade el estado inicial
       this.estadosPorRecorrer.add(Integer.parseInt(estadoInicial.Identificador+""));
       //Obtiene el arreglo visitado inicial;
       this.visitados = getArrayListVisitados(contadorFilaTabla,contadorColumnaTabla);
       while(!estadosPorRecorrer.isEmpty()){
           Integer e = estadosPorRecorrer.poll(); //Saca el estado a analizar
           guardarEstado(e); //guarda el estado como visitado
           apilarTransiciones(e); //Apila sus tensiciones
           recorrerTransiciones(); //Recorre las transiciones    
       }
       ++contadorColumnaTabla;
       
         for (int i = contadorFilaTabla; i < this.tabla.length; i++) {
             ArrayList<Integer> aux = getArrayListVisitados(i, 0);
             if(aux.isEmpty()){
                 break;
             }
             while(contadorColumnaTabla<this.tabla[0].length){
                 
                 
                 ++contadorColumnaTabla;
             }
             contadorColumnaTabla = 0;
         }
        // System.out.println(this.visitados);
        this.imprimirTabla();
     }
     
     
     
     public void afndToAfd(AFND afnd){
       
       this.contadorColumnaTabla=0;
       this.contadorFilaTabla=0;
       int llenado = contadorFilaTabla + 1; 
       
       while(this.contadorFilaTabla < llenado){
           while(this.contadorColumnaTabla < caracteres.size()){
               
               this.visitados = getArrayListVisitados(contadorFilaTabla,contadorColumnaTabla);
               if(contadorColumnaTabla==0 && contadorFilaTabla==0){ //Arranque para el primer caracter
                 //System.out.println("entro");
                 this.estadosPorRecorrer.add(Integer.parseInt(estadoInicial.Identificador+""));
                 construir(0);
                 ArrayList<Integer> aux = this.getArrayListVisitados(0,0);
                 Collections.sort(aux); 
                 //imprimirTabla();
               }else if(contadorColumnaTabla == 0){
                   ArrayList<Integer> aux = this.getArrayListVisitados(contadorFilaTabla, contadorColumnaTabla);
                   for(Integer i : aux){
                   this.estadosPorRecorrer.add(i);
                   }
                   construir(0);
                   Collections.sort(aux);
                   //imprimirTabla();
               }else{
                   ArrayList<Integer> aux = this.getArrayListVisitados(contadorFilaTabla, contadorColumnaTabla);
                   for(Integer i : aux){
                   this.estadosPorRecorrer.add(i);
                   }
                   construir(contadorColumnaTabla);
                   Collections.sort(aux);
                   //imprimirTabla();
               }
            ++contadorColumnaTabla;
           }
           //System.out.println("sale");
           this.contadorColumnaTabla = 0;
           for (int i = contadorColumnaTabla + 1; i < caracteres.size(); i++) {
               ArrayList a = this.getArrayListVisitados(contadorFilaTabla,i);
               boolean bandera = false; // false cuando no esta en la matriz 
               for (int j = 0; j < llenado; j++) {
                   if(sonListasIguales(a,this.getArrayListVisitados(j,0))){// 0 ya que siempre se pregunta al primer elemento de la fila 
                       bandera=true;        
                   }                       
               }
               if(!bandera && !a.isEmpty()){
                   //Se clona el arreglo
                    ArrayList<Integer> nueva  = new ArrayList<>();
                    nueva.addAll(a);
                    //Se guarda en la posicion 0 de la nueva fila
                    this.tabla[llenado][0] = nueva;
                   ++llenado;    
               }
               
           }
           ++contadorFilaTabla;
       }
       
        //this.imprimirTabla();
     }
     
     
     public void construir(int columna){
         //System.out.println("Estados "+estadosPorRecorrer.size());
         while(!estadosPorRecorrer.isEmpty()){
           Integer e = estadosPorRecorrer.poll(); //Saca el estado a analizar
           guardarEstado(e); //guarda el estado como visitado
           if(columna==0){
               apilarTransiciones(e); //Apila sus tensiciones
           }else{
               apilarSoloTransicionesEpsilon(e);
           }
           recorrerTransiciones(); //Recorre las transiciones    
       }
     }
     
     
     /**
      * 
      * @param num estado que apilará sus tranciciones.
      */
     public void apilarTransiciones(Integer num){
         Estado e = this.estados.get(num);
         for(Transicion t : e.Transiciones) {
             if(t.getCaracter() == '_'){
                 transicionesPorRecorrer.add(t);
             }else{
                 //System.out.println(caracteres);
                 for (int i = contadorColumnaTabla + 1; i < caracteres.size(); i++) {
                     //System.out.println("Alfabeto "+caracteres.get(i));
                     //System.out.println("I: "+i);
                     if(caracteres.get(i).equals(t.getCaracter()+"")){
                         ArrayList aux = getArrayListVisitados(this.contadorFilaTabla,i);
                         //imprimirTabla();
                         //System.out.println("Caracter"+t.getCaracter());
                         if(!aux.contains(t.getNodoB())){
                             //System.out.println("Deberia "+t.getNodoB());
                             aux.add(t.getNodoB());
                         }
                     }
                     
                 }
             }
         }
     }
     
     
     public void apilarSoloTransicionesEpsilon(Integer num){
         Estado e = this.estados.get(num);
         for(Transicion t : e.Transiciones) {
             if(t.getCaracter() == '_'){
                 transicionesPorRecorrer.add(t);
             }else{
                 
             }
         }
     }
     
     
     
     //Recorre las transiciones
     public void recorrerTransiciones(){
         boolean flag = true;
         while( !this.transicionesPorRecorrer.isEmpty() ){
             Transicion t = transicionesPorRecorrer.pop();
             //System.out.println(t.getNodoB() + "Transiciones por eliminar"+transicionesPorRecorrer.size());
             if(t.getCaracter() == '_'){
                 if(!visitados.contains(t)){
                     this.estadosPorRecorrer.add(t.getNodoB());
                 }
                 /*for (Integer i : visitados) {
                     if(!(i == t.getNodoB())){
                        
                     }
                 }*/
             }
        }
     }
     
     //Guarda los estados no repetidos
     public void guardarEstado(Integer estadoAGuardar){
         if(!this.visitados.contains(estadoAGuardar)){
            this.visitados.add(estadoAGuardar);
         }
        
        //System.out.println("Visitados "+estadoAGuardar);
     }
     
     ArrayList<Integer> getArrayListVisitados(int fila,int columna){
         if(this.tabla[fila][columna]==null){
             this.tabla[fila][columna] = new ArrayList<>();
             return (ArrayList<Integer>)this.tabla[fila][columna];
         }else{
             return (ArrayList<Integer>)this.tabla[fila][columna];
         }
     }
     
     
     public void imprimirTabla(){
         System.out.println("-------------------------------------");
         for (int i = 0; i < caracteres.size(); i++) {
             System.out.print(caracteres.get(i)+" ");
         }
         System.out.println("");
         for (int i = 0; i < tabla.length; i++) {
             for (int j = 0; j < tabla[0].length; j++) {
                 System.out.print((ArrayList)this.tabla[i][j]+" ");
             }
             System.out.println("");
         }
     }
     
     
     public boolean sonListasIguales(ArrayList l1 , ArrayList l2){
         //1era comparacion, si ambos tienen el mismo tamaño
         if(l1.size() != l2.size()){
             return false;
         }
         //2da comparacion, si tienen elementos iguales.
         ArrayList<Integer> a = new ArrayList<>();
         ArrayList<Integer> b = new ArrayList<>();
         
         a.addAll(l1);
         b.addAll(l2);
         
         a.retainAll(b);
         if(!a.isEmpty()){
             return true;
         }else{
             return false;
         }
         
     }
     
}
