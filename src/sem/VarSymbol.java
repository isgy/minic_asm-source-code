package sem;

import ast.VarDecl;

public class VarSymbol extends Symbol{
     VarDecl vd;
     char symtype = 'v';
    
     public VarSymbol(VarDecl vd){
    	 this.vd = vd;
    	 this.name = vd.varName;
     }
}
