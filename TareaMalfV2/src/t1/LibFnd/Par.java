/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t1.LibFnd;

/**
 *
 * @author Reinaldo Jerez
 */
public class Par {
    public int a;
    public int b;
    
    public Par(int a, int b){
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
