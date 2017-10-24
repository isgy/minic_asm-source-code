package ast;

public class FieldAccessExpr extends Expr {
	public Expr structure;
	public String fieldname;
	public FieldAccessExpr(Expr st, String fn){structure = st; fieldname = fn;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitFieldAccessExpr(this);
    }
}