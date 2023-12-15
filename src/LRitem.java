import java.util.ArrayList;
import java.util.Objects;

public class LRitem {
    String lhs;
    ArrayList<String> alfa;
    ArrayList<String> beta;

    public LRitem(String lhs, ArrayList<String> alfa, ArrayList<String> beta) {
        this.lhs = lhs;
        this.alfa = alfa;
        this.beta = beta;
    }

    public String getRhs() {
        return lhs;
    }

    public ArrayList<String> getAlfa() {
        return alfa;
    }

    public ArrayList<String> getBeta() {
        return beta;
    }

    public LRitem constructNewDotShift() {
        ArrayList<String> newalfa = new ArrayList<>(alfa);
        newalfa.add(beta.get(0));
        ArrayList<String> newbeta = new ArrayList<>(beta);
        newbeta.remove(0);
        return new LRitem(lhs, newalfa, newbeta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LRitem lRitem)) return false;
        return Objects.equals(lhs, lRitem.lhs) && Objects.equals(alfa, lRitem.alfa) && Objects.equals(beta, lRitem.beta);
    }

    @Override
    public String toString() {
        String alfa = String.join("", this.alfa);
        String beta = String.join("", this.beta);
        return "[" + lhs + "->" + alfa + "." + beta + "]";
    }
}
