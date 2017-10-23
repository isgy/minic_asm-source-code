package ast;

public class StrLiteral extends Expr {
	String str;
//	String toStr() {return ""+str;}
	public StrLiteral(String s){ str = s; } 
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitStrLiteral(this);
    }
	
}