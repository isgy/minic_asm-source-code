package ast;

public class ArrayType implements Type {

	public Type tp;
	public int num_elems;
	public ArrayType(Type t, int n){tp = t; num_elems = n;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitArrayType(this);
    }
}
