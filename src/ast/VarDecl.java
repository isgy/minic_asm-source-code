package ast;

public class VarDecl implements ASTNode {
    public final Type type;
    public final String varName;
    public int offset = 0;
    public boolean isLocal;
    
    public VarDecl(Type type, String varName) {
	    this.type = type;
	    this.varName = varName;
	    this.isLocal = false;
    }

    public <T> T accept(ASTVisitor<T> v) {
	return v.visitVarDecl(this);
    }
}
