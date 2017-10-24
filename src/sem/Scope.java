package sem;

import java.util.Map;

public class Scope {
	private Scope outer;
	private Map<String, Symbol> symbolTable;
	
	public Scope(Scope outer) { 
		this.outer = outer; 
        
	}
	
	public Scope() { this(null); }
	
	public Symbol lookup(String name) {
		Symbol sym;
		if(symbolTable.containsKey(name)) {
			sym = symbolTable.get(name);
			return sym;
		}else {
			sym = outer.lookup(name);
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
