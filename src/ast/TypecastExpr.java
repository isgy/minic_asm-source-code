package ast;

public class TypecastExpr extends Expr {
	public Type cast;
	public Expr exp;
	public TypecastExpr(Type t, Expr e){cast = t; exp = e;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitTypecastExpr(this);
    }
}