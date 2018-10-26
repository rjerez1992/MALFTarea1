
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
     * Estado final
     */
    private Estado estadoFinal;
    /**
     * Estados finales
     */
    public ArrayList<Estado> estadosFinales;
    /**
     * Lista de estados del AFD
     */
    public ArrayList<Estado> Nodos;
    
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
    
    public int llenado;
    
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
         this.estadoFinal = afnd.EstadoFinal;
         this.alfabeto = AFND.Alfabeto;
         // Contadores maximos de la tabla
         this.maxRow = afnd.Nodos.size();
         this.maxColumn = this.alfabeto.size() + 1;
         
         Ini();

         // Agrega epsilon a los caracteres
         this.caracteres.add("_");
         this.caracteres.addAll(alfabeto);
         // Llena el hashmap de los nodos existentes
         llenarHashMap(afnd);
         //System.out.println("Estados"+this.estados.size());
         //Transforma de afnd a afd
         afndToAfd(afnd);
         
         //Imprimir tabla -> comentar
         //imprimirTabla();
         //Tansforma al formato global
         transformar();
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
         
         this.estadosFinales = new ArrayList<>();
         this.Nodos = new ArrayList<>();
         
     }
     
     /**
      * 
      * @param afnd  
      */
     public void llenarHashMap(AFND afnd){
         for (Estado e : afnd.Nodos) {
             this.estados.put(e.Identificador,e);
         }
     }
     
     
   /*  
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
     */
     
     /**
      * 
      * @param afnd 
      */
     public void afndToAfd(AFND afnd){
       
       this.contadorColumnaTabla=0;
       this.contadorFilaTabla=0;
       llenado = contadorFilaTabla + 1; 
       
       while(this.contadorFilaTabla < llenado){
           while(this.contadorColumnaTabla < caracteres.size()){
               
               this.visitados = getArrayListVisitados(contadorFilaTabla,contadorColumnaTabla);
               
               if(contadorColumnaTabla==0 && contadorFilaTabla==0){ //Arranque para el Estado inicial
                 //System.out.println("entro");
                 this.estadosPorRecorrer.add(Integer.parseInt(estadoInicial.Identificador+""));
                 construir(0);
                 ArrayList<Integer> aux = this.getArrayListVisitados(0,0);
                 Collections.sort(aux); 
                 //imprimirTabla();
               }else if(contadorColumnaTabla == 0){ // Arranque para los estados no vistos
                   ArrayList<Integer> aux = this.getArrayListVisitados(contadorFilaTabla, contadorColumnaTabla);
                   for(Integer i : aux){
                   this.estadosPorRecorrer.add(i);
                   }
                   construir(0);
                   Collections.sort(aux);
                   //imprimirTabla();
               }else{ //Tansicion para los nodos no en posicion 0
                   ArrayList<Integer> aux = this.getArrayListVisitados(contadorFilaTabla, contadorColumnaTabla);
                   for(Integer i : aux){
                   this.estadosPorRecorrer.add(i);
                   }
                   construir(contadorColumnaTabla);
                   Collections.sort(aux);
                   //imprimirTabla();
               }
               //System.out.println("Arraytomado["+contadorFilaTabla+"]["+contadorColumnaTabla+"]"+this.visitados);
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
                    //System.out.println((ArrayList)this.tabla[llenado][0]);
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
      * @param num estado que apilará sus tranciciones. Utilizado para los estados q estan en la columna 0 de las filas.
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
     
     /**
      * 
      * @param num estado que apilará solo los estados epsilon, usado para los estados q estan desde 1 a n-1 de las columnas de la tabla
      * 
      */
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
     
     // Todo: Revisar
     public boolean sonListasIguales(ArrayList l1 , ArrayList l2){
         //1era comparacion, si ambos tienen el mismo tamaño
         if(l1.size() != l2.size()){
             //System.out.println(l1 + " "+ l2);
             return false;
         }
         //2da comparacion, si tienen elementos iguales.
         ArrayList<Integer> a = new ArrayList<>();
         ArrayList<Integer> b = new ArrayList<>();
         
         a.addAll(l1);
         b.addAll(l2);
         
         //Restando ambos conjuntos se sabe si son iguales.
         a.removeAll(l2);
         b.removeAll(l1);
         
         if(a.isEmpty() && b.isEmpty()){
             //System.out.println("Iguales: "+l1+ " " +l2);
             return true;
         }else{
             //System.out.println("Distintas: "+l1+ " " +l2);
             return false;
         }
         
     }
     
     public void transformar(){
         //System.out.println("Transforamacion");
         for (int i = 0; i < this.llenado; i++) {
             Estado estado = new Estado(i);
             //System.out.println("Estado "+ estado.Identificador);
             ArrayList nodoArrecorrer = (ArrayList)tabla[i][0];
             if(i==0){this.estadoInicial = estado;}
             
             for (int j = 1; j < caracteres.size(); j++) {
                 ArrayList aux = (ArrayList)tabla[i][j];
                 //System.out.print(aux+"");
                 if(!aux.isEmpty()){
                     
                    int x = getPosicionTabla(aux); // Posicion de la wea
                     //System.out.println("Posicion "+ x);
                    if(x!=-1){
                        estado.Agregar(caracteres.get(j).charAt(0), x);
                    }
                 }
             }
             //System.out.println("");
             if(nodoArrecorrer.contains(this.estadoFinal.Identificador)){
                 this.estadosFinales.add(estado);
             }
             //System.out.println("Cantidad " + estado.Transiciones.size());
             Nodos.add(estado);
             //System.out.println(tabla[i][0]);
         }
         
     }
     
     
     public int getPosicionTabla(ArrayList a){
         int i = -1;
         for (int j = 0; j < this.llenado; j++) {
             if(sonListasIguales(a, (ArrayList)this.tabla[j][0])){
                 i = j;
                 break;
             }
         }
         return i;
     }
     
     
      public void Imprimir() {
        System.out.println("AFD:");
        System.out.print("K={");

        String k = "";

        for(Estado e : this.Nodos) {
            k+="q"+e.Identificador+",";
        }

        System.out.print(k.substring(0, k.length() - 1));

        System.out.println("}");

        System.out.print("Sigma={");

        String sigma = "";

        for (int i=1;i<this.caracteres.size();i++) {
            sigma += caracteres.get(i) + ",";
        }

        if (caracteres.size() > 0)
        {
            System.out.print(sigma.substring(0, sigma.length() - 1));
        }

        System.out.println("}\nDelta:");

        for (Estado e : this.Nodos) {
            for(Transicion t : e.Transiciones) {
                System.out.println("(q" + e.Identificador + "," + t.getCaracter() + ",q" + t.getNodoB() + ")");
            }
        }

        System.out.println("s=q" + this.estadoInicial.Identificador);
        System.out.print("F={");
          for(int i=0 ; i<=estadosFinales.size()-1;i++){
              if(i>=estadosFinales.size()-1){
                  System.out.print("q"+estadosFinales.get(i).Identificador);
              }else{
                  System.out.print("q"+estadosFinales.get(i).Identificador+",");
              }
          }
          System.out.println("}");

    }
     
     
}
