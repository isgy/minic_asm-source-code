package ast;

public class ArrayAccessExpr extends Expr {
	public Expr array;     
	public Expr index;
	public int eval() {
		return index.eval();
	}
	public boolean isLocal;
	public ArrayAccessExpr(Expr a, Expr ind){array = a; index = ind; isLocal = true;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitArrayAccessExpr(this);
    }
}