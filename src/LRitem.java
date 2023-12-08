import java.util.ArrayList;
import java.util.Objects;

public class LRitem {
    String rhs;
    ArrayList<String> alfa;
    ArrayList<String> beta;

    public LRitem(String rhs, ArrayList<String> alfa, ArrayList<String> beta) {
        this.rhs = rhs;
        this.alfa = alfa;
        this.beta = beta;
    }

    public String getRhs() {
        return rhs;
    }

    public void setRhs(String rhs) {
        this.rhs = rhs;
    }

    public ArrayList<String> getAlfa() {
        return alfa;
    }

    public void setAlfa(ArrayList<String> alfa) {
        this.alfa = alfa;
    }

    public ArrayList<String> getBeta() {
        return beta;
    }

    public void setBeta(ArrayList<String> beta) {
        this.beta = beta;
    }

    public LRitem constructNewDotShift() {
        ArrayList<String> newalfa = new ArrayList<>(alfa);
        newalfa.add(beta.get(0));
        ArrayList<String> newbeta = new ArrayList<>(beta);
        newbeta.remove(0);
        return new LRitem(rhs, newalfa, newbeta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LRitem lRitem)) return false;
        return Objects.equals(rhs, lRitem.rhs) && Objects.equals(alfa, lRitem.alfa) && Objects.equals(beta, lRitem.beta);
    }
}
