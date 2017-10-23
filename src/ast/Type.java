package ast;

import java.util.List;

/**
 * @author cdubach
 */
public interface Type extends ASTNode {
    public <T> T accept(ASTVisitor<T> v);

   
}

    
 



