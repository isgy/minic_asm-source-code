package ast;

public class ArrayAccessExpr extends Expr {
	Expr array;     
	Expr index;
	public ArrayAccessExpr(Expr a, Expr ind){array = a; index = ind;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitArrayAccessExpr(this);
    }
}