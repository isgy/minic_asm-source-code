package sem;
import ast.*;

import java.util.HashMap;
import java.util.Map;

public class Scope {
	private Scope outer;
	private Map<String, Symbol> symbolTable;
	private StructTypeDecl sd;
	
	public Scope(Scope outer) { 
		this.outer = outer; 
		symbolTable = new HashMap<String, Symbol>();
		//typeTable = new HashMap<>
	}
	
	public Scope() { 
		symbolTable = new HashMap<String, Symbol>();
	}
	

/*	public String getouter() {
		return "hasouter";
	} */
	public Symbol lookup(String name) {
		Symbol sym = null;
		if(symbolTable.containsKey(name)) {
			sym = symbolTable.get(name);
			return sym;
		}else {
		    if(outer != null) {
			sym = outer.lookup(name); }
		}
	    return sym;
	}

	
	public Symbol lookupCurrent(String name) {
		Symbol sym = symbolTable.get(name);
		return sym;
	}
	
	public void put(Symbol sym) {
		symbolTable.put(sym.name, sym);
	}
}
