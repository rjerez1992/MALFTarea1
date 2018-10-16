/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import t1.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import t1.LibEr.Operacion;
import t1.LibFnd.Estado;
import t1.LibFnd.Transicion;

/**
 *
 * @author patin
 */
public class AFD {
   
    /**
     * Estado inicial del AFD
     */
    public Estado EstadoInicial;
    
    /**
     * Lista de estados finales del AFD
     */
    public ArrayList<Estado> EstadoFinal;
    
    /**
     * Variable de utilidad para enumerar estados
     */
    public static int ContadorEstado = 0;

    /**
     * Lista de estados del AFD
     */
    public ArrayList<Estado> Nodos;
    
    /**
     * Almacena el alfabeto del AFD
     */
    public static ArrayList<String> Alfabeto;

    /**
     * Constructor. Requiere un AFND valido
     * @param afnd 
     */
    public AFD(AFND afnd) {
        Alfabeto = AFND.Alfabeto;
        this.EstadoFinal = new ArrayList<Estado>();
        this.Construir(afnd);
    }
    
    /**
     * Realiza la construcion del AFD a partir de AFND.
     * Utiliza el metodo de construccion del subset.
     * @param afnd 
     */
    public void Construir(AFND afnd) {
        //Lista estados 
        ArrayList<Estado> estados = new ArrayList<>();
        estados.addAll(afnd.Nodos);

        //Lista de caracteres + _ (epsilon)
        ArrayList<String> caracteres = new ArrayList<>();
        caracteres.addAll(Alfabeto);
        caracteres.add("_");

        //Armamos una matriz
        String[][] tablaA = new String[estados.size()][caracteres.size()];

        //Obtenemos la primera parte de la tabla
        int i=0, j = 0;
        for (Estado e : estados){
            j = 0;
            for (String c : caracteres) {
                if (c.equals("_")) {
                    continue;
                }
                int z = e.TransicionCon(c.charAt(0));
                tablaA[e.Identificador][j] = z+""; //UPDATE: i por e.Identificador para tenerlos ordenados             
                j++;
            }

            String aux = "";
            ArrayList<Integer> epsilonTransiciones = TransicionesEpsilon(e, estados);
            if (epsilonTransiciones.size() > 0)
            {
                // epsilonTransiciones.Reverse();
                for (Integer x : epsilonTransiciones)
                {
                    aux += x + ",";
                }
                aux = aux.substring(0, aux.length()-1);


                tablaA[e.Identificador][j] = aux; //UPDATE: i por e.Identificador para tenerlos ordenados        
            }
            else {
                tablaA[e.Identificador][j] = "-1"; //UPDATE: i por e.Identificador para tenerlos ordenados        
            }

            i++;
        }

        //this.ImprimirTabla(tablaA);

        //Teniendo la tabla anterior, podemos crear una nueva tabla            
        ArrayList<String[]> tablaB = new ArrayList<String[]>();

        //Tendremos una lista con los nuevos estados que aparezcan en la tabla
        ArrayList<String> nuevosEstados = new ArrayList<>();

        //Una lista con todos los estados
        ArrayList<String> estadosCompletos = new ArrayList<>();

        //Comenzamos con el estado inicial que siempre es el mismo
        nuevosEstados.add(tablaA[0][tablaA[0].length-1]); //Epsilon del estado inicial de la tabla A

        //Mientras aparezcan nuevos estados
        while (!nuevosEstados.isEmpty()){
            //Creamos una nueva fila
            String[] tmpstr = new String[caracteres.size()-1];       

            //Limpiador de -1
            ArrayList<Integer> Limpiador = new ArrayList<Integer>();
            Limpiador.add(-1);

            //Obtenemos el primer de los nuevos estados
            String estadoActual = nuevosEstados.remove(0);


            //Revisamos que no estemos revisando un estado ya revisado
            if (estadosCompletos.contains(estadoActual)){
                continue;
            } 

            //System.out.println("Estado Actual: "+estadoActual);                

            //Lo marcamos como ya revisado
            estadosCompletos.add(estadoActual);                


            //Obtenemos todos los subestados de que puede estar compuesto
            ArrayList<Integer> subEstados = Subestados(estadoActual);

            int z = 0;
            //Para cada caracter en el alfabeto repetimos el siguiente proceso
            for(String s : Alfabeto){     
                //Generamos una lista de estados alcanzables, que son todos los estados a los que se puede llegar
                //utilizando esa letra y en un subestado dado.
                ArrayList<Integer> estadosAlcanzables = new ArrayList<>();  

                //Lista especial para condensar los distintos subestados
                ArrayList<Integer> alcanzables = new ArrayList<>();

                //Para la letra dada, revisamos en cada subestado y donde podemos llegar
                for (int k : subEstados){
                    //Creamos una lista de estados alcanzables por este subestado
                    //alcanzables = new ArrayList<>();                                 
                    ArrayList<Integer> tmp = new ArrayList<Integer>();
                    //Si el subestado es distinto de -1, buscamos el destino del subestado
                    if (k != -1){
                        tmp = this.DestinosCon(s, k,tablaA);                            
                    }
                    //Agregamos los destinos diferentes a la lista de alcanzables del subestado                         
                    alcanzables.removeAll(tmp);
                    alcanzables.addAll(tmp);               
                }   

                //Agregamos los estados alcanzables distintos a la lista de estados
                estadosAlcanzables.removeAll(alcanzables);  
                estadosAlcanzables.addAll(alcanzables);   
                estadosAlcanzables.removeAll(Limpiador);

               //Obtenemos un string que represente los estados alcanzables
                String aux = obtenerString(estadosAlcanzables);

                //Si los estados completos no contienen a aux, lo agregamos.
                if (!estadosCompletos.contains(aux)){
                    if (!aux.equals("-1")){
                        nuevosEstados.add(aux);   
                    }                                            
                }  

                //Agregamos la columna a la tabla
                tmpstr[z] = aux;
                //Aumentamos el indice
                z++;                    
            }
            //Aumentamos el indice
            tablaB.add(tmpstr);             
        }

        //this.ImprimirTablaB(tablaB);      

        //A patir de aqui podemos armar el automata
        //En la tabla B tenemos las transiciones de los estados
        //En estados completos tenemos la lista de los estados

        //Por cada estado, crearemos un nodo, el nodo de inicio será el primer estado
        this.Nodos = new ArrayList<Estado>();

        for(String s : estadosCompletos){
            Estado e = new Estado(ContadorEstado);
            e.conglomerado = s;
            this.Nodos.add(e);               
            //System.out.println("ID: "+e.Identificador + " CON: "+e.conglomerado);
            ContadorEstado++;
        }            

        this.EstadoInicial = this.Nodos.get(0);

        //Agregamos las transiciones de cada nodo, para cada caracter
        int estadoe=0, caracter = 0;
        for(String[] s : tablaB){
            caracter = 0;
            for(String t : s){
                int idB = IdentificadorNodoConglomerado(t);
                this.Nodos.get(estadoe).Agregar(AFD.Alfabeto.get(caracter).charAt(0), idB);                    
                caracter++;
            }
            estadoe++;
        }    

        //En este punto ya tenemos armado el AFD, pero aun necesitamos marcar los estados finales.            
        //Es estado final si en conglomerado tiene el subestado que representa el estado final del AFD
        for(Estado e : this.Nodos){          
            if (Subestados(e.conglomerado).contains(afnd.EstadoFinal.Identificador)){               
                this.EstadoFinal.add(e);
            }
        }
    }
    
    /**
     * Obtiene el identificador de un nodo que represente un estado de la forma "1,2,3,4"
     * @param c
     * @return 
     */
    public int IdentificadorNodoConglomerado(String c){
        for(Estado e : this.Nodos){
            //System.out.println("CON E: "+e.conglomerado + " C: "+c);
            if (e.conglomerado.equals(c)){
                //System.out.println("MATCH!");
                return e.Identificador;
            }
        }
        return -1;
    }
    
    /**
     * Obtiene un de la forma "1,2,3" a partir de una lista de numeros.
     * @param a
     * @return 
     */
    public String obtenerString(ArrayList<Integer> a){
        String aux = "";
        for(int i : a){
            aux+=i+",";
        }
        if (aux.length() < 1){return "-1";}
        aux = aux.substring(0, aux.length()-1);
        return aux;                
    }
    
    /**
     * Obtiene las transiciones a traves de epsilon desde un estado
     * TODO: Borrar la funcion duplicada de esta.
     * @param estado
     * @param estados
     * @return 
     */
    public ArrayList<Integer> TransicionesEpsilon(Estado estado, ArrayList<Estado> estados)
    {
        ArrayList<Integer> estadosEpsilon = new ArrayList<>();
        //string cadena = "";
        Stack<Transicion> stack = new Stack<Transicion>();

        copiarTransicion(stack, estado);
        estadosEpsilon.add(estado.Identificador);
        Transicion transicion;
        while (stack.size() > 0)
        {
            transicion = stack.pop();

            if (transicion.getCaracter() == '_')
            {
                if (!estadosEpsilon.contains(transicion.getNodoB()))
                    estadosEpsilon.add(transicion.getNodoB());
                buscarTrasicion(transicion.getNodoB(), stack, estados);
                }            
        }

        return estadosEpsilon;
    }
    
    /**
     * Copia una transicion
     * @param stack
     * @param e
     * @return 
     */
    public Stack<Transicion> copiarTransicion(Stack<Transicion> stack,Estado e)
    {
        for (Transicion t : e.Transiciones)
        {
            stack.push(t);
        }
        return stack;
    }
    
    /**
     * Busca cieta transicion desde un estado a otro
     * @param nodo
     * @param stack
     * @param estados
     * @return 
     */
    public Stack<Transicion>  buscarTrasicion(int nodo, Stack<Transicion> stack, ArrayList<Estado> estados)
    {
       for(Estado e : estados)
        {
            if(nodo == e.Identificador)
            {
                return copiarTransicion(stack, e);
            }
        }
        return stack;
    }

    /**
     * Imprime la tabla A, para fines de debug
     * @param t 
     */
    public void ImprimirTabla(String[][] t) {
        System.out.println("Tabla A");
        for (int i = 0; i < t.length; i++)
        {
            for (int j = 0; j < t[0].length; j++)
            {
                System.out.print("["+t[i][j] + "] ");
            }
            System.out.println("");
        }
    }

    /**
     * Imprime el AFD utilizando el formato establecido.
     */
    public void Imprimir()
    {
        System.out.println("AFD:");

        System.out.print("K={");

        String k = "";

        for (Estado e : this.Nodos)
        {
            k += "q" + e.Identificador + ",";
        }

        System.out.print(k.substring(0, k.length() - 1));

        System.out.println("}");

        System.out.print("Sigma={");

        String sigma = "";

        for (String c : Alfabeto)
        {
            sigma += c.toString() + ",";
        }

        if (Alfabeto.size() > 0)
        {
            System.out.print(sigma.substring(0, sigma.length() - 1));
        }

        System.out.print("}\ndelta:\n");

        for (Estado e : this.Nodos)
        {
            for (Transicion t : e.Transiciones)
            {
                System.out.println("(q" + e.Identificador + "," + t.getCaracter() + ",q" + t.getNodoB() + ")");
            }
        }

        System.out.println("s=q" + this.EstadoInicial.Identificador);

        String estadosFinalesStr = "";

        for(Estado e : this.EstadoFinal){
            estadosFinalesStr += "q"+e.Identificador+",";
        }          

        if (estadosFinalesStr.length() > 2){
            estadosFinalesStr = estadosFinalesStr.substring(0, estadosFinalesStr.length() - 1);
        }

        System.out.println("F={" + estadosFinalesStr + "}");
    }
    
    /**
     * Asigna el alfabeto c al afd actual
     * @param c 
     */
    public void AsignarAlfabeto(ArrayList<String> c)
    {
        Alfabeto = c;
    }

    /**
     * Retorna una lista con todos los estados accesibles con el caracter s[0] y n epsilon
     * desde el estado correspondiente a indice.
     * @param s 
     * @param indice
     * @param tabla
     * @return 
     */
    public ArrayList<Integer> DestinosCon(String s, int indice,String[][] tabla){
        ArrayList<Integer> tmp = new ArrayList<>();            

        int indiceCaracter = Alfabeto.indexOf(s);     

        //Si nos lleva a ningun lugar
        if (tabla[indice][indiceCaracter].equals("-1")){
            tmp.add(-1);
            return tmp; //Significa que no podemos llegar a ningun lugar con este simbolo desde aqui
        }

        //Sino ya sabemos que nos lleva a algun estado, obtenemos el estado
        int i = Integer.parseInt(tabla[indice][indiceCaracter]);
        //Agregamos el estado            
        tmp.add(i);          

        //Comenzamos a obtener los estados a los que se puede llegar desde este usando epsilon
        ArrayList<Integer> n = DestinosConEpsilon(i, tabla, new ArrayList<Integer>());
        tmp.removeAll(n);
        tmp.addAll(n);            

        return tmp;
    }
    
    /**
     * Obtiene los subestados enteros descritos por un string de la forma 1,2,3.
     * @param s
     * @return 
     */
    public static ArrayList<Integer> Subestados(String s){
        //System.out.println("Subestado de: "+s);
        ArrayList<Integer> i = new ArrayList<>();
        for (String t : s.split(",")){
            i.add(Integer.parseInt(t));
        }
        //System.out.println(i.toString());
        return i;
    }

    /**
     * Encuentra todos los destinos que se pueden alcanzar con epsilon desde un cierto estado.
     * TODO: La implementacion de esta funcion está demas, en la tabla A ya vienen calculados.
     * @param indice
     * @param tabla
     * @param aniadidos
     * @return 
     */
    public ArrayList<Integer> DestinosConEpsilon(int indice, String[][] tabla, ArrayList<Integer> aniadidos){
        //Destinos
        ArrayList<Integer> destinos = new ArrayList<Integer>();
        //Indice de la columna epsilon
        int i = tabla[0].length-1;
        //Indices alcanzables a traves de epsilon
        ArrayList<Integer> alcanzables = Subestados(tabla[indice][i]);
        for(int j : alcanzables){
            if (!aniadidos.contains(j)){
                aniadidos.add(j);
                destinos.add(j);
                ArrayList<Integer> sub = DestinosConEpsilon(j, tabla, aniadidos);
                destinos.removeAll(sub);
                destinos.addAll(sub);
            }
        }
        //Retornamos
        return destinos;
    }

    /**
     * Funcion que imprime la tabla B de la transformacion, para fines de debug.
     * @param tablaB 
     */
    private void ImprimirTablaB(ArrayList<String[]> tablaB) {
        System.out.println("Tabla B");
        for(String[] s : tablaB){
            for(String k : s){
                System.out.print("["+k+"] ");
            }
            System.out.print("\n");
        }
    }
    
    /**
     * Ejecuta la comprobacion de ocurrencias utilizando el AFD y recibiendo una cadena
     * @param s 
     */
    public void Comprobar(String s) { 
        System.out.println("Ocurrencias:");
        ArrayList<Par> calces = new ArrayList<Par>();
        
        //Revisamos si s contiene caracteres indebidos
        if (!Operacion.PerteneceAlLenguaje(s)){
            System.out.println("La cadena ingresada contiene caracteres que no pertenecen al lenguaje del automata.");
            System.exit(0);
        }
        
        //Comenzamos en el estado inicial
        //Estado estadoActual = this.EstadoInicial;
        
        //Por cada posicion en el string, hacemos la revision de ahi en adelante
        //aumentando el rango en que se revisa cada vez y guardando los matchs
        //en otra variable para luego ver calzes solapados o contiguos
        for (int i = 0; i <= s.length(); i++) {                        
            for (int j = i; j <= s.length(); j++) {
                String pivote = s.substring(i, j);
                if (pivote.equals("")){
                    continue;
                }
                //System.out.println("PIVOTE: "+pivote);
                //System.out.println("I: "+i+" J: "+j);
                if (esMatch(pivote)){
                    calces.add(new Par(i, j));
                }
            }            
        }
        
        //Revisamos los calces solapados y contiguos
        calces = EliminarSubcalces(calces); //Funcion definida abajo
        
        //Imprimimos el resultado
        for(Par p : calces){
            System.out.println(p.toString());
        }
    }
    
    /**
     * Verifica si el string dado es un match para el automata actual o no.
     * @param s
     * @return 
     */
    public boolean esMatch(String s){
        Estado actual = this.EstadoInicial;
        //Por cada caracter efectuamos la transicion
        for(char c : s.toCharArray()){
            
            int p = actual.TransicionCon(c);
            if (p == -1){
                p = actual.TransicionCon('%');
            }
            if (p == -1){
                return false;
            }
            actual = estadoConId(p);            
        }
        //Si al final logramos llegar a uno de los estados finales, entonces
        //el string ingresado es un calce.
        if (this.EstadoFinal.contains(actual)){
            return true;
        }
        return false;        
    }
    
    /**
     * Busca el estado con el id (i)
     * @param i
     * @return 
     */
    public Estado estadoConId(int i){    
        for(Estado e : this.Nodos){
            //System.out.println("I: "+i+" E.i: "+e.Identificador);
            if (e.Identificador == i){
                return e;
            }
        }
        //Nunca debería llegar a este punto
        System.out.println("Advertencia: Busqueda por estado inexistente.");
        return null;
        
    }    
    
    public void ReiniciarContador(){
        ContadorEstado = 0;
    }
    
    public ArrayList<Par> EliminarSubcalces(ArrayList<Par> p){
        //Necesitamos identificar los posibles calces aninados
        //Primero revisamos los contiguos        
        
        //Luego revisamos los solapados
        
        //Luego verificamos si efectivamente cumplen con ser calces anidados
                
        //Entonces eliminamos esos calces anidados
        
        //Luego retornamos la lista actualizada        
        return p;
    }
}

/**
 * Representa pares para marcar los calces
 * @author rjerez1992
 */
class Par{
    public int a;
    public int b;
    
    Par(int a, int b){
        this.a = a;
        this.b = b;
    }
    
    @Override
    public String toString(){
        return a+" "+(b-1);
    }
    
    public boolean Contiene(Par p){
        if (this.a <= p.a && this.b >=p.b){
            //Si lo contiene
            if (this.a != p.a || this.b != p.b){
                //Si no es el mismo nodo
                return true;
            }         
        }
        return false;
    }
    
    public Par Clone(){
        Par p = new Par(this.a, this.b);
        return p;
    }
}
