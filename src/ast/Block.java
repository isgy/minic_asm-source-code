package ast;

import java.util.List;

public class Block extends Stmt {

    public final List<VarDecl> vars;
    public final List<Stmt> stmts;

    public Block(List<VarDecl> v, List<Stmt> s) {
	    vars = v;
	    stmts = s;
    }

    public <T> T accept(ASTVisitor<T> v) {
	    return v.visitBlock(this);
    }
}
