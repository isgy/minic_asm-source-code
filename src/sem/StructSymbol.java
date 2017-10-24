package sem;

import ast.StructTypeDecl;

public class StructSymbol extends Symbol{
    StructTypeDecl sd;
    char symtype = 's';
    
    public StructSymbol(StructTypeDecl sd){
   	 this.sd = sd;
   	 this.name = sd.stype.ident;
    }
}
