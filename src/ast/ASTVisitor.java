package ast;

import java.util.List;

public interface ASTVisitor<T> {
    public T visitBaseType(BaseType bt);
    public T visitPointerType(PointerType p);
    public T visitStructType(StructType s);
    public T visitArrayType(ArrayType p);
    
    public T visitStructTypeDecl(StructTypeDecl st);
    
    
    public T visitIntLiteral(IntLiteral e);
    public T visitStrLiteral(StrLiteral e);
    public T visitChrLiteral(ChrLiteral e);
    
    public T visitFunCallExpr(FunCallExpr e);
    public T visitBinOp(BinOp e);
    public T visitArrayAccessExpr(ArrayAccessExpr e);
    public T visitFieldAccessExpr(FieldAccessExpr e);
    public T visitValueAtExpr(ValueAtExpr e);
    public T visitSizeOfExpr(SizeOfExpr e);
    public T visitTypecastExpr(TypecastExpr e);
    public T visitOp(Op o);
    
    public T visitExprStmt(ExprStmt e);
	public T visitWhile(While w);
	public T visitIf(If i);
	public T visitAssign(Assign a);
	public T visitBlock(Block b);
	public T visitBlock(Block b, List<VarDecl> p, FunDecl f);
    
    public T visitFunDecl(FunDecl p);
    public T visitProgram(Program p);
    public T visitVarDecl(VarDecl vd);
    public T visitVarExpr(VarExpr v);
	public T visitReturn(Return r);
	public T visitProcType(ProcType procType);
	

	
    
    
    //IntLiteral | StrLiteral | ChrLiteral | VarExpr | FunCallExpr | BinOp | ArrayAccessExpr | FieldAccessExpr | ValueAtExpr | SizeOfExpr | TypecastExpr

    
    
    

    // to complete ... (should have one visit method for each concrete AST node class)
}
