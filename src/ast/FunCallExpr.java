package ast;

import java.util.List;

public class FunCallExpr extends Expr {
	public final String name;
	public List<Expr> args;
	public FunDecl fd;    //for the name analyzer
	public int i;
	public int eval() {return i;}
	public FunCallExpr(String f, List<Expr> a){this.name = f; args = a; i = -1;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitFunCallExpr(this);
    }
}
