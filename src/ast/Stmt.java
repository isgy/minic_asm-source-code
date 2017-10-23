package ast;

public abstract class Stmt implements ASTNode {
    public abstract <T> T accept(ASTVisitor<T> v);
 //   Block | While | If | Assign | Return | ExprStmt
    
}
    

