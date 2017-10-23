package ast;

import java.util.List;

public class FunCallExpr extends Expr {
	String function;
	List<Expr> args;
	public FunCallExpr(String f, List<Expr> a){function = f; args = a;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitFunCallExpr(this);
    }
}
