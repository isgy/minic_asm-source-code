package ast;

public class Return extends Stmt {
 	public Expr ret;
 	public Type rtype;
 	public Return(Type r){ret = null; rtype = r;}
  	public Return(Expr e, Type r){ ret = e; rtype = r;}
    public <T> T accept(ASTVisitor<T> v) {
         return v.visitReturn(this);
     }
}