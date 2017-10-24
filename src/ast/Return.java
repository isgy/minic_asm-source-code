package ast;

public class Return extends Stmt {
 	Expr ret;
 	public Return(){ret = null;}
  	public Return(Expr e){ ret = e; }
    public <T> T accept(ASTVisitor<T> v) {
         return v.visitReturn(this);
     }
}