public class ParsingTreeNode {
    private String value;
    private ParsingTreeNode parent;
    private ParsingTreeNode sibling;

    public ParsingTreeNode(String value) {
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

    public void setParent(ParsingTreeNode parent) {
        this.parent = parent;
    }

    public ParsingTreeNode getSibling() {
        return sibling;
    }

    public void setSibling(ParsingTreeNode sibling) {
        this.sibling = sibling;
    }
}
