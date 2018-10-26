/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t1.LibFnd;

import java.util.ArrayList;

/**
 * Representa un estado de un automata.
 * Contiene un identificador numerico, un conglomerado (cadena de string que representan una lista
 * de identificadores de otros estados para la transformacion de AFND a AFD) y una lista de transiciones.
 * @author patin
 */
public class Estado {
        public int Identificador; 
        public String conglomerado = "";
        public ArrayList<Transicion> Transiciones; 

        public Estado(int id)
        {
            this.Identificador = id;
            this.Transiciones = new ArrayList<>();
        }

        public void Agregar(char c, int i) {
            this.Transiciones.add(new Transicion(c, i));
        }

        public boolean LlevaA(int id) {
            for(Transicion t : this.Transiciones) {
                return t.getNodoB() == id;
            }
            return false;
        }

        public int TransicionCon(char c) {
            for (Transicion t : this.Transiciones)
            {
                if(t.getCaracter() == c) {
                    return t.getNodoB();
                }
            }
            return -1;
        }        
}
