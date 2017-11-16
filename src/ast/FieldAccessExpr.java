package ast;

public class FieldAccessExpr extends Expr {
	public StructTypeDecl sd;
	public VarDecl fd;
	public Expr structure;
	public String fieldname;
	public int i;
	public int eval() {return i;}
	public FieldAccessExpr(Expr st, String fn){structure = st; fieldname = fn; i = -1;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitFieldAccessExpr(this);
    }
}