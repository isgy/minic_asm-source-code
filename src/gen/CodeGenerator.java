package gen;

import ast.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class CodeGenerator implements ASTVisitor<Register> {

    /*
     * Simple register allocator.
     */
	private LinkedList<StrLiteral> stlist = new LinkedList<StrLiteral>();
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

    private boolean beforeStr = true;

    private PrintWriter writer; // use this writer to output the assembly instructions


    public void emitProgram(Program program, File outputFile) throws FileNotFoundException {
        writer = new PrintWriter(outputFile);
        visitProgram(program);
        beforeStr = false;
        visitProgram(program);
        writer.close();
    }

    @Override
    public Register visitBaseType(BaseType bt) {
        return null;
    }

    @Override
    public Register visitStructTypeDecl(StructTypeDecl st) {
    	if(beforeStr) {
    		return null;
    	}
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
        		writer.println(indent+".align  2");
        		writer.println(indent+vd.varName+":    .word  1");
        	}
        	else if(vd.type.getClass() == PointerType.class) {
        		PointerType pt = (PointerType) vd.type;
        		writer.println(indent+".align  2");
        		writer.println(indent+vd.varName+":     .word 1");
        	}    	
        	writer.println();
        }
        return null;
    }

    @Override
    public Register visitBlock(Block b) {
    	if(beforeStr) {
    		return null;
    	}
        // TODO: to complete
        return null;
    }

    @Override
    public Register visitFunDecl(FunDecl p) {
    	if(beforeStr) {
    		return null;
    	}
    	Register result = getRegister();
    	writer.println(p.name+":");
    	if(p.name == "main") {
            writer.println(indent+".globl main");
            writer.println(p.name+":");
            //for (VarDecl vd : p.params) {
              //  vd.accept(this);
            //}
            result = p.block.accept(this);
            writer.println(indent+"li   $v0, 10");        
            writer.println(indent+"syscall");	
            return result;
    	}
        


        else {
        int p8size = p.psize + 8;
        int p4size = p.psize + 4;
        int lsize = p.lsize;
        writer.println(indent+"sw $ra, 0($sp)");
        writer.println(indent+"addi $sp, $sp, -4");
        writer.println(indent+"sw $fp, 0($sp)");
        writer.println(indent+"addi $sp, $sp, -4");
        writer.println(indent+"addu $fp, $sp, "+p8size);
        writer.println(indent+"subu $sp, $sp, "+lsize);
        
        result = p.block.accept(this);
        writer.println(indent+"add $v0, $zero, "+result);
        writer.println(indent+"lw $ra, -"+p.psize+"($fp)");
        writer.println(indent+"move $t0, $fp");                
        writer.println(indent+"lw $fp, -"+p4size+"($fp)");
        writer.println(indent+"move $sp, $t0");
        writer.println("j $ra");
        }
        return result ;
    }

    @Override
    public Register visitProgram(Program p) {
    	if(beforeStr) {
        	for (StructTypeDecl std : p.structTypeDecls) {
                std.accept(this);
            }
            for (VarDecl vd : p.varDecls) {
                vd.accept(this);
            }
            
            firstv = false;
          
            for (FunDecl fd : p.funDecls) {
                fd.accept(this);
            }
    		return null;
    	}
    	writer.println(indent+".data");
    	for (StrLiteral s : stlist) {
    		writer.println(indent+".align 2");
    		writer.println(s.label+":    .asciiz "+s.str);
    	}
    	for (StructTypeDecl std : p.structTypeDecls) {
            std.accept(this);
        }
        for (VarDecl vd : p.varDecls) {
            vd.accept(this);
        }
        firstv = false;
        writer.println(indent+".align 2");
        writer.println(indent+".text");
        for (FunDecl fd : p.funDecls) {
            fd.accept(this);
        }
        writer.println("############################################################### Subroutines");
        writer.println("Push:");
        writer.println(indent+"addi $sp, $sp, -8");     
        writer.println(indent+"sb $a0, ($sp)");             
        writer.println(indent+"jr $ra");


        writer.println("Pop:");
        Register r = getRegister();
        writer.println(indent+"lw "+r+", ($sp)");             
        writer.println("addi $sp, $sp, 8");           
        writer.println("jr $ra");
        freeRegister(r);
        
        writer.println("read_c:");
        writer.println(indent+"li   $v0, 12");
    	writer.println(indent+"syscall");
    	writer.println(indent+"move $t0, $v0");
    	writer.println(indent+"j $ra");
        writer.println();
        writer.println(indent+"print_i:");
        writer.println(indent+"li   $v0, 1");
        writer.println(indent+"lw $a0, 0($sp)");
        writer.println(indent+"syscall");
        writer.println(indent+"j $ra");
        writer.println();;
        writer.println("print_s:");
        writer.println(indent+"li   $v0, 4");
        writer.println(indent+"lw $a0, 0($sp)");
        writer.println(indent+"syscall");
        writer.println(indent+"j $ra");
        writer.println();
        writer.println("print_c:");
        writer.println(indent+"li   $v0, 11");
        writer.println(indent+"lw $a0, 0($sp)");
        writer.println(indent+"syscall");
        writer.println(indent+"j $ra");
        writer.println();
        writer.println("read_i:");
        writer.println(indent+"li   $v0, 5");
        writer.println(indent+"syscall");
        writer.println(indent+"move $t0, $v0");
        writer.println(indent+"j $ra");
        writer.println();
        writer.println("read_c");
        writer.println("li   $v0, 12");
        writer.println(indent+"syscall");
        writer.println(indent+"move $t0, $v0");
        writer.println(indent+"j $ra");

		return null;
    }

    @Override
    public Register visitVarDecl(VarDecl vd) {
    	if(beforeStr) {
    		return null;
    	}
        Register addrReg = getRegister();
        Register result = getRegister();
        if(firstv) {                           //global declarations                    
            	if(vd.type.getClass() == ArrayType.class) {
            		
            		ArrayType at = (ArrayType) vd.type;
            		if(at.tp == BaseType.CHAR) {
            			writer.println(indent+vd.varName+":    .space  "+at.num_elems);
            			writer.println(indent+".align  2");
            		}
            		else
            			writer.println(indent+vd.varName+":     .word   0:"+at.num_elems);
            	}
            	else if(vd.type == BaseType.CHAR) {
            		writer.println(indent+vd.varName+":    .space  1");
            		writer.println(indent+".align  2");
                }
            	else if(vd.type == BaseType.INT) {
            		writer.println(indent+vd.varName+":    .word  1");
            	}
            	else if(vd.type.getClass() == PointerType.class) {
            		//PointerType pt = (PointerType) vd.type;
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
    	if(beforeStr) {
    		return null;
    	}
        if(v.vd.isLocal) {
        	
        }else
        {
        	if(v.vd.type.getClass() == ArrayType.class) {
        		Register base = getRegister();
        		Register ind = getRegister();
        		Register result = getRegister();
        		ArrayType at = (ArrayType) v.vd.type;
        	    if(at.tp == BaseType.CHAR) {
        	    writer.println(indent+"li "+ind+", "+at.num_elems);
        		writer.println(indent+"la "+base+", "+v.name); 
        		writer.println(indent+"add "+base+", "+base+", "+ind);
        		writer.println(indent+"lb "+result+", ("+base+")");
        	    }else {
            	    writer.println(indent+"li "+ind+", "+at.num_elems);
            		writer.println(indent+"la "+base+", "+v.name); 
            		writer.println(indent+"sll "+ind+", "+ind+", 2");
            		writer.println(indent+"add "+base+", "+base+", "+ind);
            		writer.println(indent+"lw "+result+", ("+base+")");
        	    }
        	}
        }
        return null;
    }

	@Override
	public Register visitPointerType(PointerType p) {
    	if(beforeStr) {
    		return null;
    	}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitStructType(StructType s) {
    	if(beforeStr) {
    		return null;
    	}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitArrayType(ArrayType p) {
    	if(beforeStr) {
    		p.tp.accept(this);
    		return null;
    	}

		return null;
	}

	@Override
	public Register visitIntLiteral(IntLiteral e) {
    	if(beforeStr) {
    		return null;
    	}
		Register result = getRegister();
		writer.println("li   "+result+", "+e.i);
	//	writer.println("sw   "+result+", ($sp)");
	//	writer.println("addi $sp, $sp, -4");
		return result;
	}

	@Override
	public Register visitStrLiteral(StrLiteral e) {
    	if(beforeStr) {
    		stlist.add(e);
    		return null;
    	}
		Register result = getRegister();
		Register addr = getRegister();
		writer.println("la "+addr+", "+e.label);
        writer.println("lb "+result+", 0("+addr+")");      
       // writer.println("sw   "+result+", ($sp)");        // push onto stack
       // writer.println("addi $sp, $sp, -4"); 
        freeRegister(addr);
		return result;
	}

	@Override
	public Register visitChrLiteral(ChrLiteral e) {
    	if(beforeStr) {
    		return null;
    	}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitFunCallExpr(FunCallExpr e) {
    	if(beforeStr) {
    		return null;
    	}
        return null;
	}

	@Override
	public Register visitBinOp(BinOp e) {
    	if(beforeStr) {
            e.lhs.accept(this);
            e.rhs.accept(this);
    		return null;
    	}
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
    	if(beforeStr) {
            e.array.accept(this);        
            e.index.accept(this);
    		return null;
    	}

		/*if 
		Register result = getRegister();
		Register addr = getRegister();
		Register ab = getRegister();
		if(p.tp == BaseType.CHAR) {
		    writer.println(indent+"la "+addr+", "+e.array.($fp)"); 
			sll reg1, reg1, 2 
			add reg2, reg2, reg1
			lw reg3, (reg2)
		}else if(p.tp == BaseType.INT) { 
			
		} */
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
    	if(beforeStr) {
    		e.structure.accept(this);
    		return null;
    	}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitValueAtExpr(ValueAtExpr e) {
    	if(beforeStr) {
    		return null;
    	}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitSizeOfExpr(SizeOfExpr e) {
		// TODO Auto-generated method stub
    	if(beforeStr) {
    		return null;
    	}
		return null;
	}

	@Override
	public Register visitTypecastExpr(TypecastExpr e) {
    	if(beforeStr) {
    		return null;
    	}
		//         writer.print("TypecastExpr(");
      /*  t.cast.accept(this);
        writer.print(",");
        t.exp.accept(this);
        writer.print(")"); */
        
		return null;
	}

	@Override
	public Register visitOp(Op o) {
    	if(beforeStr) {
    		return null;
    	}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitExprStmt(ExprStmt e) {
    	if(beforeStr) {
    		e.exp.accept(this);
    		return null;
    	}
     /*   writer.print("ExprStmt(");
        es.exp.accept(this);
        writer.print(")"); */
		return null;
	}

	@Override
	public Register visitWhile(While w) {
    	if(beforeStr) {
    		return null;
    	}
        writer.print("While(");
     /*   wh.exp.accept(this);
        writer.print(",");
        wh.stm.accept(this);
        writer.print(")"); */
        return null;
	}

	@Override
	public Register visitIf(If i) {
    	if(beforeStr) {
   	     i.ifexp.accept(this);
   	     i.stm.accept(this);
   	     if(i.else_stm != null) { 
   	      	i.else_stm.accept(this); 
   	     }
    		return null;
    	}
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
    	if(beforeStr) {
            a.ex.accept(this);
            a.isexp.accept(this);
    		return null;
    	}
        writer.print("Assign(");
        a.ex.accept(this);
        writer.print(",");
        a.isexp.accept(this);
        writer.print(")");
       
		return null;
	}

	@Override
	public Register visitBlock(Block b, List<VarDecl> p, FunDecl f) {
    	if(beforeStr) {
    		return null;
    	}

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
    	if(beforeStr) {
    		return null;
    	}
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
