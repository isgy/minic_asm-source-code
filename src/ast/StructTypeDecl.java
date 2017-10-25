package ast;
import java.util.List;
/**
 * @author cdubach
 */
public class StructTypeDecl implements ASTNode {

    public StructType stype;
    public List<VarDecl> vardecls;

    public StructTypeDecl(StructType s, List<VarDecl> v) {
	    stype = s;
	    vardecls = v;
    }
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitStructTypeDecl(this);
    }
}
