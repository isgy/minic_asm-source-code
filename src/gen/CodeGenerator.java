package gen;

import ast.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class CodeGenerator implements ASTVisitor<Register> {

    /*
     * Simple register allocator.
     */
	String indent = "       ";
    // contains all the free temporary registers
    private Stack<Register> freeRegs = new Stack<Register>();

    public CodeGenerator() {
        freeRegs.addAll(Register.tmpRegs);
    }

    private class RegisterAllocationError extends Error {}

    private Register getRegister() {
        try {
            return freeRegs.pop();
        } catch (EmptyStackException ese) {
            throw new RegisterAllocationError(); // no more free registers, bad luck!
        }
    }

    private void freeRegister(Register reg) {
        freeRegs.push(reg);
    }

    private boolean firstv = true;



    private PrintWriter writer; // use this writer to output the assembly instructions


    public void emitProgram(Program program, File outputFile) throws FileNotFoundException {
        writer = new PrintWriter(outputFile);

        visitProgram(program);
        writer.close();
    }

    @Override
    public Register visitBaseType(BaseType bt) {
        return null;
    }

    @Override
    public Register visitStructTypeDecl(StructTypeDecl st) {
		int sizeof = 0;
        for(VarDecl vd : st.vardecls) {
        	sizeof = sizeof + 4;
        }
        writer.println(st.stype.ident+":");
        for(VarDecl vd: st.vardecls) {
        	if(vd.type.getClass() == ArrayType.class) {
        		
        		ArrayType at = (ArrayType) vd.type;
        		if(at.tp == BaseType.CHAR) {
        			writer.println(indent+".align  2");
        			writer.println(indent+vd.varName+":    .space  "+at.num_elems);
        		}
        		else
        			writer.println(indent+".align  2");
        			writer.println(indent+vd.varName+":     .word   0:"+at.num_elems);
        	}
        	else if(vd.type == BaseType.CHAR) {
        		writer.println(indent+vd.varName+":    .space  1");
            }
        	else if(vd.type == BaseType.INT) {
        		writer.println(indent+vd.varName+":    .word  1");
        	}
        	else if(vd.type.getClass() == PointerType.class) {
        		PointerType pt = (PointerType) vd.type;
        		writer.println(indent+vd.varName+":     .word 1");
        	}    	
        	writer.println();
        }
        return null;
    }

    @Override
    public Register visitBlock(Block b) {
        // TODO: to complete
        return null;
    }

    @Override
    public Register visitFunDecl(FunDecl p) {
    	Register result = getRegister();
    	writer.println(p.name+":");
    	if(p.name == "main") {
            writer.println(indent+".globl main");
            writer.println(p.name+":");
            for (VarDecl vd : p.params) {
                vd.accept(this);
                writer.println();
            }
            p.block.accept(this);
            writer.println(indent+"li   $v0, 10");        
            writer.println(indent+"syscall");	
    	}
        
        else if(p.name == "print_i") {
        	writer.println("li   $v0, 1");
        	writer.println("add  $a0, $t0, $zero");
        	writer.println("syscall");

        }
        else {
        writer.println(indent+"addi $sp, $sp, -4");
        
        writer.println(indent+"sw $fp, 0($sp)");
        writer.println(indent+"move $fp $sp");
        writer.println(indent+"sw $ra, 4($sp)");
        
        
        
        writer.println(indent+"lw $ra, 4($sp)");
        writer.println(indent+"addiu $sp, $sp, 4");
        writer.println("j $ra");
        }
        return result ;
    }

    @Override
    public Register visitProgram(Program p) {
    	writer.println(indent+".data");
    	for (StructTypeDecl std : p.structTypeDecls) {
            std.accept(this);
        }
        for (VarDecl vd : p.varDecls) {
            vd.accept(this);
        }
        firstv = false;
        writer.println(indent+".text");
        for (FunDecl fd : p.funDecls) {
            fd.accept(this);
        }

        return null;
    }

    @Override
    public Register visitVarDecl(VarDecl vd) {
        Register addrReg = getRegister();
        Register result = getRegister();
        if(firstv) {
            	if(vd.type.getClass() == ArrayType.class) {
            		
            		ArrayType at = (ArrayType) vd.type;
            		if(at.tp == BaseType.CHAR) {
            			writer.println(indent+".align  2");
            			writer.println(indent+vd.varName+":    .space  "+at.num_elems);
            		}
            		else
            			writer.println(indent+".align  2");
            			writer.println(indent+vd.varName+":     .word   0:"+at.num_elems);
            	}
            	else if(vd.type == BaseType.CHAR) {
            		writer.println(indent+".align  2");
            		writer.println(indent+vd.varName+":    .space  1");
                }
            	else if(vd.type == BaseType.INT) {
            		writer.println(indent+".align  2");
            		writer.println(indent+vd.varName+":    .word  1");
            	}
            	else if(vd.type.getClass() == PointerType.class) {
            		//PointerType pt = (PointerType) vd.type;
            		writer.println(indent+".align  2");
            		writer.println(indent+vd.varName+":     .word 1");
            	}    	
            	writer.println();
            	return null;
        }
        else{ 
        //addiu $sp, $sp, -4     
       // sw $zero, ($sp)     
        writer.println("la addrReg "+vd.varName);
        writer.println("la result ");
        return null;
        }
    }

    @Override
    public Register visitVarExpr(VarExpr v) {
        // TODO: to complete
        return null;
    }

	@Override
	public Register visitPointerType(PointerType p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitStructType(StructType s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitArrayType(ArrayType p) {
		
		return null;
	}

	@Override
	public Register visitIntLiteral(IntLiteral e) {
		Register result = getRegister();
		writer.println("li   "+result+", "+e.i);
		return result;
	}

	@Override
	public Register visitStrLiteral(StrLiteral e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitChrLiteral(ChrLiteral e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitFunCallExpr(FunCallExpr e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitBinOp(BinOp e) {
		Register lhsReg = e.lhs.accept(this);
		Register rhsReg = e.rhs.accept(this);
		Register result = getRegister();
		switch(e.op) {
		case ADD:
			writer.println(indent+"add "+result+", "+lhsReg+", "+rhsReg);
			break;
		case MUL:
			writer.println(indent+"mul "+result+", "+lhsReg+", "+rhsReg);
			break;
		case SUB:
			 //la  $t0, tmpval
			 // lw  $t1, 0($t0)  # $t1 == tmpval == 5
			writer.println(indent+"sub "+result+", "+lhsReg+", "+rhsReg);
			break;
		case DIV:
			writer.println(indent+"div "+lhsReg+", "+rhsReg);
			writer.println(indent+"mflo "+result);
			break;
		case MOD:
			writer.println(indent+"div "+lhsReg+", "+rhsReg);
			writer.println(indent+"mfhi "+result);
			break;
		case GT:
			writer.println(indent+"sgt "+result+", "+lhsReg+", "+rhsReg);
			break;
		case LT:
			writer.println(indent+"slt "+result+", "+lhsReg+", "+rhsReg);
			break;
		case GE:
		    writer.println(indent+"sge "+result+", "+lhsReg+", "+rhsReg);
		    break;
		case LE:
			writer.println(indent+"sle "+result+", "+lhsReg+", "+rhsReg);
			break;
		case NE:
			writer.println(indent+"sne "+result+", "+lhsReg+", "+rhsReg);
			break;
		case EQ:
			writer.println(indent+"seq "+result+", "+lhsReg+", "+rhsReg);
			break;
		case OR:
			writer.println(indent+"addi "+result+", $zero, $zero");
			writer.println(indent+"bnez "+lhsReg+", or");
			writer.println(indent+"bnez "+rhsReg+", or");
			writer.println(indent+"j endl");
			writer.println("or:");
			writer.println(indent+"addi "+result+", $zero, 1");
			writer.println("endl:");
		case AND:
			writer.println(indent+"addi "+result+", $zero, $zero");
			writer.println(indent+"beqz "+lhsReg+", endl");
			writer.println(indent+"beqz "+rhsReg+", endl");
			writer.println(indent+"addi "+result+", $zero, 1");
			writer.println("endl:");
			
		}
		freeRegister(lhsReg);
		freeRegister(rhsReg);
		return result;
	}

	@Override
	public Register visitArrayAccessExpr(ArrayAccessExpr e) {
	/*	Register arr = e.array.accept(this);
		Register ind = e.index.accept(this);
		Register result = getRegister();
		if(e.array.type) {
			if(i.array.accept(this).getClass() == PointerType.class) {
				
				if(i.index.accept(this) == BaseType.INT) {
					i.type = p.ptype;
					i.isArrayAcc= true;
					return p.ptype;
				}
			}
			else 
				error("arrayaccess not an array or pointer");
		}else {
			ArrayType a = (ArrayType) i.array.accept(this);
			if(i.index.accept(this) == BaseType.INT) {
				i.type = a.tp;
				i.isArrayAcc = true;
				return a.tp;
			}
		}
		return null;
		lw $t0, 32($s3) */
		return null;
	}

	@Override
	public Register visitFieldAccessExpr(FieldAccessExpr e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitValueAtExpr(ValueAtExpr e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitSizeOfExpr(SizeOfExpr e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitTypecastExpr(TypecastExpr e) {
		//         writer.print("TypecastExpr(");
      /*  t.cast.accept(this);
        writer.print(",");
        t.exp.accept(this);
        writer.print(")"); */
        
		return null;
	}

	@Override
	public Register visitOp(Op o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitExprStmt(ExprStmt e) {
     /*   writer.print("ExprStmt(");
        es.exp.accept(this);
        writer.print(")"); */
		return null;
	}

	@Override
	public Register visitWhile(While w) {
        writer.print("While(");
     /*   wh.exp.accept(this);
        writer.print(",");
        wh.stm.accept(this);
        writer.print(")"); */
        return null;
	}

	@Override
	public Register visitIf(If i) {
        Register res = i.ifexp.accept(this);
        Register result;
        if(i.else_stm != null) {
        writer.print(indent+"blez "+res+" Else"); 
        i.stm.accept(this);
        writer.print(indent+"j Endif");
        writer.print("Else: ");
        
        i.else_stm.accept(this);
        writer.print("Endif: ");
        }else {
        	 writer.print(indent+"blez "+res+" Endif"); 	
        	 i.stm.accept(this);
        }

		return null;
	}

	@Override
	public Register visitAssign(Assign a) {
        writer.print("Assign(");
        a.ex.accept(this);
        writer.print(",");
        a.isexp.accept(this);
        writer.print(")");
       
		return null;
	}

	@Override
	public Register visitBlock(Block b, List<VarDecl> p) {

		for(VarDecl v : b.vars) {
			v.accept(this);
		}
		for(Stmt s : b.stmts) {
			s.accept(this);
		}
		return null;
	}

	@Override
	public Register visitReturn(Return r) {
        writer.print("Return(");
        if(r.ret != null) {
           r.ret.accept(this);       	   
        }
        writer.print(")");
		return null;
	}

	@Override
	public Register visitProcType(ProcType procType) {
		// TODO Auto-generated method stub
		return null;
	}
}
