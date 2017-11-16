package ast;

public class SizeOfExpr extends Expr {
	public Type tp;
	public int eval() {
		if(tp == BaseType.CHAR)
			return 1;
		else 
			return 4;}
	public SizeOfExpr(Type t){tp = t;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitSizeOfExpr(this);
    }
}