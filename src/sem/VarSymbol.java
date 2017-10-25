package sem;

import ast.VarDecl;

public class VarSymbol extends Symbol{
     VarDecl vd;

     public boolean isVar() {
     	return true;
     }
     public boolean isProc() {
     	return false;
     }
     public boolean isStruc() {
     	return false;
     }
     public VarSymbol(VarDecl vd){
    	 this.vd = vd;
    	 this.name = vd.varName;
    	 
     }
}
