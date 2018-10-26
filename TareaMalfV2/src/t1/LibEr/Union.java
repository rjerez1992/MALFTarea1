
package t1.LibEr;

/**
 *
 * @author patin
 */
public class Union extends NodoEr{
    private NodoEr NodoA;
    private NodoEr NodoB;

    public Union(NodoEr nodoA, NodoEr nodoB)
    {
        this.NodoA = nodoA;
        this.NodoB = nodoB;
    }
    
    @Override
    public void Imprimir()
    {
        this.getNodoA().Imprimir();
        System.out.print("|");
        this.getNodoB().Imprimir();
    }

    /**
     * @return the NodoA
     */
    public NodoEr getNodoA() {
        return NodoA;
    }

    /**
     * @param NodoA the NodoA to set
     */
    public void setNodoA(NodoEr NodoA) {
        this.NodoA = NodoA;
    }

    /**
     * @return the NodoB
     */
    public NodoEr getNodoB() {
        return NodoB;
    }

    /**
     * @param NodoB the NodoB to set
     */
    public void setNodoB(NodoEr NodoB) {
        this.NodoB = NodoB;
    }
    
}
