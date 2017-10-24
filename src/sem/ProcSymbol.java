package sem;

import ast.FunDecl;

public class ProcSymbol extends Symbol{
    FunDecl fd;
    char symtype = 'p';
    public ProcSymbol(FunDecl fd){
   	 this.fd = fd;
   	 this.name = fd.name;
    }
}
