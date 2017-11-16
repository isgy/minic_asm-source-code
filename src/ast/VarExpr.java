package ast;

public class VarExpr extends Expr {
    public final String name;
    public VarDecl vd; // to be filled in by the name analyser
    public int i;
    public int eval() {return i;}
    public VarExpr(String name){
	this.name = name;
	i = -1;
    }
    
    public <T> T accept(ASTVisitor<T> v) {
	    return v.visitVarExpr(this);
    }
}
