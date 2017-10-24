package sem;

import ast.*;

public class TypeCheckVisitor extends BaseSemanticVisitor<Type> {


	@Override
	public Type visitArrayAccessExpr(ArrayAccessExpr i) {
		return null;
	}
	@Override
	public Type visitArrayType(ArrayType i) {
		return null;
	}
	@Override
	public Type visitAssign(Assign i) {
		return null;
	}
	@Override
	public Type visitBaseType(BaseType bt) {
		return null;
	}
	@Override
	public Type visitBinOp(BinOp i) {
		return null;
	}
	@Override
	public Type visitChrLiteral(ChrLiteral i) {
		return null;
	}
	@Override
	public Type visitExprStmt(ExprStmt i) {
		return null;
	}
	@Override
	public Type visitFieldAccessExpr(FieldAccessExpr i) {
		return null;
	}
	@Override
	public Type visitIf(If i) {
		return null;
	}
	@Override
	public Type visitIntLiteral(IntLiteral i) {
		return null;
	}
	@Override
	public Type visitOp(Op i) {
		return null;
	}
	@Override
	public Type visitPointerType(PointerType i) {
		return null;
	}
	@Override
	public Type visitReturn(Return i) {
		return null;
	}
	@Override
	public Type visitSizeOfExpr(SizeOfExpr i) {
		return null;
	}
	@Override
	public Type visitStrLiteral(StrLiteral i) {
		return null;
	}

	@Override
	public Type visitStructType(StructType i) {
		return null;
	}
	@Override
	public Type visitStructTypeDecl(StructTypeDecl i) {
		return null;
	}
	@Override
	public Type visitTypecastExpr(TypecastExpr i) {
		return null;
	}
	@Override
	public Type visitValueAtExpr(ValueAtExpr i) {
		return null;
	}

	@Override
	public Type visitWhile(While i) {
		return null;
	}


	@Override
	public Type visitBlock(Block b) {
		return null;
	}
	
	@Override
	public Type visitProgram(Program p) {
		return null;
	}

	@Override
	public Type visitVarDecl(VarDecl vd) {
		return null;
	}

	@Override
	public Type visitVarExpr(VarExpr v) {
		return null;
	}
	@Override
	public Type visitFunDecl(FunDecl p) {
		return null;
	}

	@Override
	public Type visitFunCallExpr(FunCallExpr f) {
		return null;
	}
	
	

}
