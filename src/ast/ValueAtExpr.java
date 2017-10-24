package ast;

public class ValueAtExpr extends Expr {
	public Expr valueat;
	public ValueAtExpr(Expr v){valueat = v;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitValueAtExpr(this);
    }
}