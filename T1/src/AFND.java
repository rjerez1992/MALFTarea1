/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import t1.LibEr.Caracter;
import t1.LibEr.Concatenacion;
import t1.LibEr.Kleene;
import t1.LibEr.NodoEr;
import t1.LibEr.Parentesis;
import t1.LibEr.Union;
import t1.LibFnd.Estado;
import t1.LibFnd.Transicion;

/**
 *
 * @author patin
 */
public class AFND {    
    /**
     * Referencia al estado inicial del automata.
     */
    public Estado EstadoInicial;

    /**
     * Referencia al estado final del automata.
     */
    public Estado EstadoFinal ;  

    /**
     * Valor creciente utilizado para asignar identificadores a los estados.
     */
    public static int ContadorEstado = 0;
    
    /**
     * Lista de nodos en el automata.
     */
    public ArrayList<Estado> Nodos;

    /**
     * Alfabeto del automata.
     */
    public static ArrayList<String> Alfabeto = new ArrayList<>();

    /**
     * Constructor. Requiere una ER valida
     * @param ner 
     */
    public AFND(NodoEr ner) {
        this.Nodos = new ArrayList<Estado>();            

        this.EstadoInicial = new Estado(ContadorEstado);
        this.Nodos.add(this.EstadoInicial);
        ContadorEstado++;

        this.Construir(ner);            
    }
    
    /**
     * Construye el AFND a partir de una ER estructurada.
     * La construccion se realiza de forma recursiva.
     * @param ner 
     */
    private void Construir(NodoEr ner) {        
        //Desecha los parentesis
        while (ner instanceof Parentesis)
        {
            ner = ((Parentesis)ner).nodo;
        }

        if (ner instanceof Caracter)
        {
            GenerarEstadoFinal();
            if (!(((Caracter)ner).getValor().charAt(0) == '~')) {
                this.EstadoInicial.Agregar(((Caracter)ner).getValor().charAt(0), this.EstadoFinal.Identificador);
            }
        }

        else if (ner instanceof Union)
        {
            AFND a = new AFND(((Union)ner).getNodoA());
            AFND b = new AFND(((Union)ner).getNodoB());
            GenerarEstadoFinal();
            this.EstadoInicial.Agregar('_', a.EstadoInicial.Identificador);
            this.EstadoInicial.Agregar('_', b.EstadoInicial.Identificador);
            a.EstadoFinal.Agregar('_', this.EstadoFinal.Identificador);
            b.EstadoFinal.Agregar('_', this.EstadoFinal.Identificador);

            this.Agregar(a.Nodos);
            this.Agregar(b.Nodos);
        }

        else if (ner instanceof Concatenacion)
        {
            AFND a = new AFND(((Concatenacion)ner).getNodoA());
            AFND b = new AFND(((Concatenacion)ner).getNodoB());
            GenerarEstadoFinal();
            this.EstadoInicial.Agregar('_', a.EstadoInicial.Identificador);
            a.EstadoFinal.Agregar('_', b.EstadoInicial.Identificador);
            b.EstadoFinal.Agregar('_', this.EstadoFinal.Identificador);

            this.Agregar(a.Nodos);
            this.Agregar(b.Nodos);
        }

        else if (ner instanceof Kleene) {
            AFND a = new AFND(((Kleene)ner).getNodo());
            GenerarEstadoFinal();
            this.EstadoInicial.Agregar('_', a.EstadoInicial.Identificador);
            this.EstadoInicial.Agregar('_', this.EstadoFinal.Identificador);
            a.EstadoFinal.Agregar('_', this.EstadoInicial.Identificador);
            a.EstadoFinal.Agregar('_', this.EstadoFinal.Identificador);

            this.Agregar(a.Nodos);
        }
    }

    /**
     * Agrega una lista de estados al AFND
     * @param l 
     */
    private void Agregar(ArrayList<Estado> l) {
        //this.Nodos.AddRange(l);
        this.Nodos.addAll(l);
    }
    
    /**
     * Imprime el AFND utilizando el formato establecido.
     */
    public void Imprimir() {
        System.out.println("AFND:");
        System.out.print("K={");

        String k = "";

        for(Estado e : this.Nodos) {
            k+="q"+e.Identificador+",";
        }

        System.out.print(k.substring(0, k.length() - 1));

        System.out.println("}");

        System.out.print("Sigma={");

        String sigma = "";

        for (String c : Alfabeto) {
            sigma += c + ",";
        }

        if (Alfabeto.size() > 0)
        {
            System.out.print(sigma.substring(0, sigma.length() - 1));
        }

        System.out.println("}\nDelta:");

        for (Estado e : this.Nodos) {
            for(Transicion t : e.Transiciones) {
                System.out.println("(q" + e.Identificador + "," + t.getCaracter() + ",q" + t.getNodoB() + ")");
            }
        }

        System.out.println("s=q" + this.EstadoInicial.Identificador);
        System.out.println("F={q" + this.EstadoFinal.Identificador + "}");

    }
    
    /**
     * Genera el estado final del AFND.
     */
    private void GenerarEstadoFinal() {
        this.EstadoFinal = new Estado(ContadorEstado);
        this.Nodos.add(this.EstadoFinal);
        ContadorEstado++;
    }
    
    /**
     * Asigna el alfabeto al AFND.
     * @param c 
     */
    public void AsignarAlfabeto(ArrayList<String> c) {
        Alfabeto = c;
    }
    
    /**
     * Agrega el complemento de loop en el estado inicial para consumir
     * caracteres iniciales de la cadena.
     */
    public void Complementar(){
        for(String s : Alfabeto){
            this.EstadoInicial.Agregar(s.charAt(0), this.EstadoInicial.Identificador);
        }
    }
    
    public void ReiniciarContador(){
        ContadorEstado = 0;
    }
}
