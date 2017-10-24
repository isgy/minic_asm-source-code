package ast;

public class StructType implements Type {
    public StructTypeDecl sd;
	public String ident;
	public StructType(String s){ident = s;}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitStructType(this);
    }
    

}
