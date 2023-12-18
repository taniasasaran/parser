public class ParsingTreeNode {
    private Integer index;
    private String value;
    private ParsingTreeNode parent;
    private ParsingTreeNode sibling;

    public ParsingTreeNode(Integer index, String value) {
        this.index = index;
        this.value = value;
        this.parent = null;
        this.sibling = null;
    }

    public String getValue() {
        return value;
    }

    public ParsingTreeNode getParent() {
        return parent;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setParent(ParsingTreeNode parent) {
        this.parent = parent;
    }

    public ParsingTreeNode getSibling() {
        return sibling;
    }

    public void setSibling(ParsingTreeNode sibling) {
        this.sibling = sibling;
    }

    public String toString() {
        Integer pindex = (parent != null) ? parent.getIndex() : null;
        Integer sindex = (sibling != null) ? sibling.getIndex() : null;
        return index + "|" + value + "|f=" + pindex + "|s=" + sindex;
    }
}
