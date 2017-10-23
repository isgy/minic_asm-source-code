package ast;

public class SizeOfExpr extends Expr {
	Type tp;
	public SizeOfExpr(Type t){tp = t;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitSizeOfExpr(this);
    }
}