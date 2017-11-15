package ast;

import java.util.LinkedList;
import java.util.List;

public class Block extends Stmt {

    public final List<VarDecl> vars;
    public final List<Stmt> stmts;

    public Block(List<VarDecl> v, List<Stmt> s) {
	    vars = v;
	    stmts = s;
    }
    public Block() {
	    vars = new LinkedList<VarDecl>();
	    stmts = new LinkedList<Stmt>();
    }
    

    public <T> T accept(ASTVisitor<T> v) {
	    return v.visitBlock(this);
    }
    public <T> T accept(ASTVisitor<T> v, List<VarDecl> p, FunDecl f) {
	    return v.visitBlock(this,p,f);
    }

}
