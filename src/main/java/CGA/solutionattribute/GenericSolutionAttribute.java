package CGA.solutionattribute;


import CGA.Model.Chrome;

public class GenericSolutionAttribute<S extends Chrome, V> implements SolutionAttribute<S, V> {
    private Object identifier;

    public GenericSolutionAttribute() {
        this.identifier = this.getClass();
    }

    public GenericSolutionAttribute(Object id) {
        this.identifier = id;
    }

    public V getAttribute(Chrome solution) {
        return (V) solution.getAttribute(this.getAttributeIdentifier());
    }

    public void setAttribute(S solution, V value) {
        solution.setAttribute(this.getAttributeIdentifier(), value);
    }

    public Object getAttributeIdentifier() {
        return this.identifier;
    }
}
