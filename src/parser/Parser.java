package parser;
import ast.*;
import lexer.Token;
import lexer.Tokeniser;
import lexer.Token.TokenClass;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * @author cdubach
 */
public class Parser {

    private Token token;

    // use for backtracking (useful for distinguishing decls from procs when parsing a program for instance)
    private Queue<Token> buffer = new LinkedList<>();

    private final Tokeniser tokeniser;



    public Parser(Tokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    public Program parse() {
        // get the first token
        nextToken();

        return parseProgram();
    }

    public int getErrorCount() {
        return error;
    }

    private int error = 0;
    private Token lastErrorToken;

    private void error(TokenClass... expected) {

        if (lastErrorToken == token) {
            // skip this error, same token causing trouble
            return;
        }

        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (TokenClass e : expected) {
            sb.append(sep);
            sb.append(e);
            sep = "|";
        }
        System.out.println("Parsing error: expected ("+sb+") found ("+token+") at "+token.position);

        error++;
        lastErrorToken = token;
    }

    /*
     * Look ahead the i^th element from the stream of token.
     * i should be >= 1
     */
    private Token lookAhead(int i) {
        // ensures the buffer has the element we want to look ahead
        while (buffer.size() < i)
            buffer.add(tokeniser.nextToken());
        assert buffer.size() >= i;

        int cnt=1;
        for (Token t : buffer) {
            if (cnt == i)
                return t;
            cnt++;
        }

        assert false; // should never reach this
        return null;
    }


    /*
     * Consumes the next token from the tokeniser or the buffer if not empty.
     */
    private void nextToken() {
        if (!buffer.isEmpty())
            token = buffer.remove();
        else
            token = tokeniser.nextToken();
    }

    /*
     * If the current token is equals to the expected one, then skip it, otherwise report an error.
     * Returns the expected token or null if an error occurred.
     */
    private Token expect(TokenClass... expected) {
        for (TokenClass e : expected) {
            if (e == token.tokenClass) {
                Token cur = token;
                nextToken();
                return cur;
            }
        }

        error(expected);
        return null;
    }

    /*
    * Returns true if the current token is equals to any of the expected ones.
    */
    private boolean accept(TokenClass... expected) {
        boolean result = false;
        for (TokenClass e : expected)
            result |= (e == token.tokenClass);
        return result;
    }


    private Program parseProgram() {
        parseIncludes();
        
     //   parseStructDecls();
     //   System.out.println("parsedstructs");
     //   parseVarDecls();
     //   System.out.println("parsedvars");
     //   parseFunDecls();
     //   System.out.println("parsedfuncs");
        List<StructTypeDecl> stds = parseStructDecls();
        List<VarDecl> vds = parseVarDecls();
        List<FunDecl> fds = parseFunDecls();
        expect(TokenClass.EOF);
        return new Program(stds, vds, fds);
    }

    // includes are ignored, so does not need to return an AST node
    private void parseIncludes() {
        if (accept(TokenClass.INCLUDE)) {
            nextToken();
            expect(TokenClass.STRING_LITERAL);
            parseIncludes();
        }
    }
    private Type parseType() {
    	Type s;
    	if(accept(TokenClass.STRUCT)) {
    		s = parseStructType();
    		if (accept(TokenClass.ASTERIX)) {
    			nextToken();
    		    return new PointerType(s);
    		}
    		else 
    			return s;
    	}
    	else {
    		//expect(TokenClass.INT,TokenClass.CHAR,TokenClass.VOID);
    	    if(accept(TokenClass.INT)) {
    	    	nextToken();
        	    s = BaseType.INT;
        	}else if(accept(TokenClass.CHAR)) {
        		nextToken();
        		s = BaseType.CHAR;
        	}else {
        		expect(TokenClass.VOID);
        		s = BaseType.VOID;
        	}
    			
    		if (accept(TokenClass.ASTERIX)) {
    			nextToken();       
    		    return new PointerType(s);         //pointertype
    		}else 
    			return s;
    	}
    	
    }
    private void parseTI() {
    	parseType();
    	expect(TokenClass.IDENTIFIER);
    }
    private StructType parseStructType() {
    	expect(TokenClass.STRUCT);
    //	expect(TokenClass.IDENTIFIER);
    	Token n = expect(TokenClass.IDENTIFIER);
    	return new StructType(n.data);
    }
    private StructTypeDecl parseStructDecl() {
    	List<VarDecl> vlist = new LinkedList<VarDecl>();
    	StructType t = parseStructType();
    	expect(TokenClass.LBRA);
    	VarDecl v = parseVarDecl();
    	vlist.add(v);
    	vlist = parseVarDecls(vlist);
    	expect(TokenClass.RBRA);
    	expect(TokenClass.SC);
    	return new StructTypeDecl(t,vlist);
    }
    private List<StructTypeDecl> parseStructDecls(){
    	List<StructTypeDecl> sl = new LinkedList<StructTypeDecl>();
    	return parseStructDecls(sl);
    }
    private List<StructTypeDecl> parseStructDecls(List<StructTypeDecl> l) {
    	List<StructTypeDecl> slist = l;
        if(accept(TokenClass.STRUCT)) {
        	StructTypeDecl st = parseStructDecl();
        	slist.add(st);
        	parseStructDecls(slist);
        }
        return slist;
    }
    private VarDecl parseVarDecl() {
       // parseTI();  
    	Type t = parseType();
    	String s = parseIdentifier().name;
    	if(accept(TokenClass.LSBR)) {
    		nextToken();
    		//expect(TokenClass.INT_LITERAL);
    		Token il = expect(TokenClass.INT_LITERAL);
    		expect(TokenClass.RSBR);
    		expect(TokenClass.SC);
    		return new VarDecl(new ArrayType(t,Integer.parseInt(il.data)),s);
    	}
    	else 
    		expect(TokenClass.SC);
    	    return new VarDecl(t,s);
    }

 /*   private void parseVarTail() {
    	if(accept(TokenClass.LSBR)) {
    		nextToken();
    		expect(TokenClass.INT_LITERAL);
    		expect(TokenClass.RSBR);
    		expect(TokenClass.SC);
    	}
    	else 
    		expect(TokenClass.SC);
    }*/

    private List<VarDecl> parseVarDecls() {
    	List<VarDecl> vds = new LinkedList<VarDecl>();
    	return parseVarDecls(vds);
    }
    private List<VarDecl> parseVarDecls(List<VarDecl> vds) {
    	//List<VarDecl> vds = new LinkedList<VarDecl>();
        if(accept(TokenClass.INT,TokenClass.CHAR,TokenClass.VOID,TokenClass.STRUCT)) {
            
        	System.out.println("var1");
           	if(accept(TokenClass.STRUCT)) {
           		if(lookAhead(1).tokenClass==TokenClass.IDENTIFIER) {
           			
        		     if(lookAhead(2).tokenClass==TokenClass.ASTERIX) {
        			     if(lookAhead(3).tokenClass==TokenClass.IDENTIFIER) {
                    	     if(lookAhead(4).tokenClass==TokenClass.LSBR || lookAhead(4).tokenClass==TokenClass.SC) {
                    		     VarDecl vd = parseVarDecl();
                    	    	 vds.add(vd);
                    		     parseVarDecls(vds);
                           	 }
            		      }
        		     }
        	    	 else if(lookAhead(2).tokenClass==TokenClass.IDENTIFIER) {
                   	    if(lookAhead(3).tokenClass==TokenClass.LSBR || lookAhead(3).tokenClass==TokenClass.SC) {
                   	    	VarDecl vd = parseVarDecl();
               	    	    vds.add(vd);
               		        parseVarDecls(vds);
                 	    }
        		     }
        		     
           		}

        	}
        	else {
        		if(accept(TokenClass.INT,TokenClass.CHAR,TokenClass.VOID)) {
            	//	System.out.println("var2");
            	//	System.out.println(lookAhead(1).tokenClass.toString());
            	//	System.out.println(lookAhead(2).tokenClass.toString());
        			if(lookAhead(1).tokenClass==TokenClass.ASTERIX) {
            			if(lookAhead(2).tokenClass==TokenClass.IDENTIFIER) {
                        	if(lookAhead(3).tokenClass==TokenClass.LSBR || lookAhead(3).tokenClass==TokenClass.SC) {
                        	    System.out.println("before_var_pointer");
                        	    VarDecl vd = parseVarDecl();
                   	    	    vds.add(vd);
                   		        parseVarDecls(vds);
                        	}
                		}
            		}
            		else if(lookAhead(1).tokenClass==TokenClass.IDENTIFIER) {
                    	if(lookAhead(2).tokenClass==TokenClass.LSBR || lookAhead(2).tokenClass==TokenClass.SC) {
                    		System.out.println("before_var");
                    		VarDecl vd = parseVarDecl();
               	    	    vds.add(vd);
               		        parseVarDecls(vds);
                    	}
            		} 	
            		
        		}
        	}
            
        }
       return vds;
		//return vds;
    }
 

    private Expr parseExp() {
    	Expr lhs = parseExp1();
    	if(accept(TokenClass.OR)) {
    		Op op = Op.OR;
    		nextToken();
    		Expr rhs = parseExp();
    		return new BinOp(op,lhs,rhs);
    	}
    	return lhs;
    }
    private Expr parseExp1() {
    	Expr lhs = parseExp2();
    	if(accept(TokenClass.AND)) {
    		Op op = Op.AND;
    		nextToken();
    		Expr rhs = parseExp1();
    		return new BinOp(op,lhs,rhs);
    	}
    	return lhs;
    }
    private Expr parseExp2() {
    	Expr lhs = parseExp3();
    	if(accept(TokenClass.EQ,TokenClass.NE)) {
    		Op op = Op.NE;
    		if(accept(TokenClass.EQ)) {
    			op = Op.EQ;
    		}
    		nextToken();
    		Expr rhs = parseExp2();
    		return new BinOp(op,lhs,rhs);
    	}
    	return lhs;
    }
    private Expr parseExp3() {
    	Expr lhs = parseExp4();
    	if(accept(TokenClass.LT,TokenClass.LE,TokenClass.GT,TokenClass.GE)) {
    		Op op = Op.LT;
    		if(accept(TokenClass.LE)) {
    			op = Op.LE;
    		}else if(accept(TokenClass.GT)){
    			op = Op.GT;
    		}else if(accept(TokenClass.GE)) {
    			op = Op.GE;
    		}
    		nextToken();
    		Expr rhs = parseExp3();
    		return new BinOp(op,lhs,rhs);
    	}
    	return lhs;
    }
    private Expr parseExp4() {
    	Expr lhs = parseExp5();
    	if(accept(TokenClass.PLUS,TokenClass.MINUS)) {
    		Op op = Op.ADD;
    		if(accept(TokenClass.MINUS)) {
    			op = Op.SUB;
    		}
    		System.out.println("before_plusminus");
    		nextToken();
    		Expr rhs = parseExp4();
    		return new BinOp(op,lhs,rhs);
    	}
    	return lhs;
    }
    private Expr parseExp5() {
    	Expr lhs = parseExp6();
    	if(accept(TokenClass.ASTERIX,TokenClass.DIV,TokenClass.REM)) {
    		Op op = Op.MUL;
    		if(accept(TokenClass.DIV)) {
    			op = Op.DIV;
    		}else if(accept(TokenClass.REM)) {
    			op = Op.MOD;
    		}
    		nextToken();
    		Expr rhs = parseExp5();
    		return new BinOp(op,lhs,rhs);
    	}
    	return lhs;
    }
    private VarExpr parseIdentifier() {
    	Token n = expect(TokenClass.IDENTIFIER);
    	return new VarExpr(n.data);
    }
    private IntLiteral parseInt_Literal() {
    	Token n = expect(TokenClass.INT_LITERAL);
    	return new IntLiteral(Integer.parseInt(n.data));
    }
    private ChrLiteral parseChr_Literal() {
    	Token n = expect(TokenClass.CHAR_LITERAL);
    	return new ChrLiteral(n.data.charAt(0));
    }
    private StrLiteral parseStr_Literal() {
    	Token n = expect(TokenClass.STRING_LITERAL);
    	return new StrLiteral(n.data);
    }
    private Expr parseExp6() {
    	if (accept(TokenClass.MINUS)) {
    		System.out.println("before_exp6minus");
    		nextToken();
    		// expect(TokenClass.IDENTIFIER,TokenClass.INT_LITERAL);
    		Expr e;
    		if(accept(TokenClass.IDENTIFIER)){
    			e = parseIdentifier();
    		}else
    			e = parseInt_Literal();
    		
    		if(accept(TokenClass.LSBR,TokenClass.DOT)) {   //arrayaccess or fieldaccess
    			
    			if(accept(TokenClass.LSBR)) {
    	  			nextToken();
    	  			Expr ex = parseExp();
    	  			expect(TokenClass.RSBR);
    	  			return new ArrayAccessExpr(e, ex);
    	  		}
    	  		else {
    	  			expect(TokenClass.DOT);
    	  		//	expect(TokenClass.IDENTIFIER);
    	  			VarExpr id = parseIdentifier();
    	  			return new FieldAccessExpr(e, id.name);
    	  		}
    		
    		}else {
    			return new BinOp(Op.SUB,new IntLiteral(0),e);
    		}
    		
    		
    	}
        else if(accept(TokenClass.SIZEOF)) {
    		return parseSizeof();
    	}
    	else if(accept(TokenClass.ASTERIX)) {
    		nextToken();
    		Expr s = parseExp();                            //valueat
    		return new ValueAtExpr(s);
    	}
    	else if(accept(TokenClass.LPAR) && (lookAhead(1).tokenClass==TokenClass.INT || lookAhead(1).tokenClass==TokenClass.CHAR || lookAhead(1).tokenClass==TokenClass.VOID || lookAhead(1).tokenClass==TokenClass.STRUCT) ) {
    		return parseTcast();
    	}
    	else 
    	   return parseExp7();
    }
    
    private Expr parseExp7() {
    	System.out.println("before_lastexp");
    	if(accept(TokenClass.INT_LITERAL,TokenClass.CHAR_LITERAL,TokenClass.STRING_LITERAL,TokenClass.IDENTIFIER)) {
    		if(accept(TokenClass.IDENTIFIER)) {
        		if(lookAhead(1).tokenClass==TokenClass.LPAR) {             //funcall
        			return parseFuncall();
        		}
        		else {
        			//expect(TokenClass.IDENTIFIER);
        			VarExpr e = parseIdentifier();
        			if(accept(TokenClass.LSBR,TokenClass.DOT)) {            //arr/field access
            			if(accept(TokenClass.LSBR)) {
            	  			nextToken();                                      
            	  			Expr ex = parseExp();
            	  			expect(TokenClass.RSBR);
            	  			return new ArrayAccessExpr(e,ex);
            	  		}
            	  		else {
            	  			expect(TokenClass.DOT);
            	  			//expect(TokenClass.IDENTIFIER);
            	  			VarExpr ex = parseIdentifier();
            	  			return new FieldAccessExpr(e, ex.name);
            	  		}
            		}else
            			return e;
        			
        		}
    		}                                                               ///////////////////////////////////////////////
    		else {
    			//expect(TokenClass.INT_LITERAL,TokenClass.CHAR_LITERAL,TokenClass.STRING_LITERAL);
    			Expr e;
    			if(accept(TokenClass.INT_LITERAL)) {
    			    e = parseInt_Literal();
    			}else if(accept(TokenClass.CHAR_LITERAL)) {
    				e = parseChr_Literal();
    			}else 
    				e = parseStr_Literal();
    			
    			if(accept(TokenClass.LSBR,TokenClass.DOT)) {
        			if(accept(TokenClass.LSBR)) {
        	  			nextToken();
        	  			Expr ex = parseExp();
        	  			expect(TokenClass.RSBR);
        	  			return new ArrayAccessExpr(e,ex);
        	  		}
        	  		else {
        	  			expect(TokenClass.DOT);
        	  			//expect(TokenClass.IDENTIFIER);
        	  			VarExpr ex = parseIdentifier();
        	  			return new FieldAccessExpr(e, ex.name);
        	  		}
        		}else
        			return e;
    		}
    	  		
    	}
    	else {
    	  if(accept(TokenClass.LPAR)) {
    		System.out.println(token.toString());
    		expect(TokenClass.LPAR);
    		Expr e = parseExp();
    		expect(TokenClass.RPAR);
    		if(accept(TokenClass.LSBR,TokenClass.DOT)) {
    			if(accept(TokenClass.LSBR)) {
    	  			nextToken();
    	  			Expr ex = parseExp();
    	  			expect(TokenClass.RSBR);
    	  			return new ArrayAccessExpr(e,ex);
    	  		}
    	  		else {
    	  			expect(TokenClass.DOT);
    	  			//expect(TokenClass.IDENTIFIER);
    	  			VarExpr ex = parseIdentifier();
    	  			return new FieldAccessExpr(e, ex.name);
    	  		}
    		}else
    			return e;
    	  }
    	  else 
    		  expect(TokenClass.LPAR,TokenClass.INT_LITERAL,TokenClass.CHAR_LITERAL,TokenClass.STRING_LITERAL,TokenClass.IDENTIFIER); //error  
    	}
    	return null;
 
    }
    
    private Expr parseFuncall() {
    	System.out.println("before_funcall");
    	String id = parseIdentifier().name;
    	expect(TokenClass.LPAR);
    	List<Expr> args = parseArglist();
    	expect(TokenClass.RPAR);
    	return new FunCallExpr(id,args);
    }
    private List<Expr> parseArglist() {
    	List<Expr> argslist = new LinkedList<Expr>();
    	if(accept(TokenClass.INT_LITERAL,TokenClass.MINUS,TokenClass.ASTERIX,TokenClass.CHAR_LITERAL,TokenClass.STRING_LITERAL,TokenClass.SIZEOF,TokenClass.LPAR,TokenClass.IDENTIFIER)) {
    	 	Expr e = parseExp();
    	 	argslist.add(e);
    	 	parseArgrep(argslist);
    	}
    	return argslist;
    }
    private void parseArgrep(List<Expr> list) {
    	if(accept(TokenClass.COMMA)) {
    		nextToken();
    		Expr e = parseExp();
    		list.add(e);
    		parseArgrep(list);
    	}
    }

    private Expr parseSizeof() {
    	expect(TokenClass.SIZEOF);
    	expect(TokenClass.LPAR);
    	Type t = parseType();
    	expect(TokenClass.RPAR);
    	return new SizeOfExpr(t);
    }
    private Expr parseTcast() {
    	expect(TokenClass.LPAR);
    	Type t = parseType();
    	expect(TokenClass.RPAR);
    	Expr e = parseExp();
    	return new TypecastExpr(t,e);
    }
    private Stmt parseStmt() {
    	if(accept(TokenClass.LBRA)) {
    		
    		return parseBlock();
    	}
    	else if(accept(TokenClass.IF)) {
    		System.out.println("before_if");
        	nextToken();
       // 	System.out.println(token.toString());
        	expect(TokenClass.LPAR);
        	Expr ie = parseExp();
       // 	System.out.println(token.toString());
        	expect(TokenClass.RPAR);
        	Stmt s = parseStmt();
        	if(accept(TokenClass.ELSE)) {
        		nextToken();
        		Stmt es = parseStmt();
        		return new If(ie,s,es);
        	}
        	else 
        		return new If(ie,s);
    	}
    	else if(accept(TokenClass.WHILE)) {
    		nextToken();
    		expect(TokenClass.LPAR);	
    		Expr ex = parseExp();
    		expect(TokenClass.RPAR);
    		Stmt st = parseStmt();
    		return new While(ex,st);
    	}
    	else if(accept(TokenClass.RETURN)) {
    		nextToken();
    		if(accept(TokenClass.LPAR,TokenClass.CHAR_LITERAL,TokenClass.INT_LITERAL, TokenClass.STRING_LITERAL, TokenClass.IDENTIFIER, TokenClass.MINUS, TokenClass.ASTERIX, TokenClass.SIZEOF)) {
    			Expr e = parseExp();
    		    expect(TokenClass.SC);
    		    return new Return(e);
    		}
    		else{
    		    expect(TokenClass.SC);
    		    return new Return();
    		}
    	}
    	else {
    		Expr ea = parseExp();
    		if(accept(TokenClass.ASSIGN)) {
    			System.out.println("before_assign");
    			nextToken();
    			Expr e = parseExp();
    			expect(TokenClass.SC);
    			return new Assign(ea,e);
    		}
    		else {
    			expect(TokenClass.SC);
    			return new ExprStmt(ea);
    		}
    	}

    }
    
    private List<Stmt> parseStmts() {
    	List<Stmt> s = new LinkedList<Stmt>();
    	return parseStmts(s);
    }
    private List<Stmt> parseStmts(List<Stmt> sl) {
    	if(accept(TokenClass.LBRA,TokenClass.IF,TokenClass.WHILE,TokenClass.RETURN,TokenClass.MINUS,TokenClass.LPAR,TokenClass.SIZEOF,TokenClass.ASTERIX,TokenClass.INT_LITERAL,TokenClass.CHAR_LITERAL,TokenClass.STRING_LITERAL,TokenClass.IDENTIFIER)) {
    		
    		System.out.println("before_stmts");
    		Stmt stm = parseStmt();
    		sl.add(stm);
    		parseStmts(sl);
    	}
    	return sl;
    }
    private Block parseBlock() {
    	System.out.println("before_block");
    	expect(TokenClass.LBRA);
    	List<VarDecl> vars = parseVarDecls();
    	List<Stmt> stmts = parseStmts();
    	expect(TokenClass.RBRA);
    	System.out.println("end_block");
    	return new Block(vars,stmts);
    }
    private FunDecl parseFunDecl() {
    	//parseTI();
    	Type t = parseType();
    	String s = parseIdentifier().name;
    	expect(TokenClass.LPAR);
    	List<VarDecl> params = parseParams();
    	expect(TokenClass.RPAR);
    	Block b = parseBlock();
    	return new FunDecl(t, s, params, b);
    }

    private List<FunDecl> parseFunDecls() {
    	List<FunDecl> fds = new LinkedList<FunDecl>();
    	return parseFunDecls(fds);
    }
    

    private List<VarDecl> parseParams() {
    	List<VarDecl> plist = new LinkedList<VarDecl>();
    	if(accept(TokenClass.INT,TokenClass.CHAR,TokenClass.VOID,TokenClass.STRUCT)) {
    		System.out.println("before_params");
    		//parseTI();
    		Type t = parseType();
        	String s = parseIdentifier().name;
        	VarDecl pa = new VarDecl(t,s);
        	plist.add(pa);
    		parseParamsList(plist);
    	}
    	return plist;
    }
    private void parseParamsList(List<VarDecl> p) {
    	if(accept(TokenClass.COMMA)) {
    		nextToken();
    		Type t = parseType();
        	String s = parseIdentifier().name;
        	VarDecl pa = new VarDecl(t,s);
        	p.add(pa);
    		parseParamsList(p);
    	}
    }

    
    private List<FunDecl> parseFunDecls(List<FunDecl> f) {
    	if(accept(TokenClass.INT,TokenClass.CHAR,TokenClass.VOID,TokenClass.STRUCT)) {
           	if(accept(TokenClass.STRUCT)) {
           		if(lookAhead(1).tokenClass==TokenClass.IDENTIFIER) {
           			
        		     if(lookAhead(2).tokenClass==TokenClass.ASTERIX) {
        			     if(lookAhead(3).tokenClass==TokenClass.IDENTIFIER) {
                    	     if(lookAhead(4).tokenClass==TokenClass.LPAR) {
                         	     FunDecl fd = parseFunDecl();
                    	    	 f.add(fd);
                    		     parseFunDecls(f);
                   
                           	 }
            		      }
        		     }
        	    	 else if(lookAhead(2).tokenClass==TokenClass.IDENTIFIER) {
                   	    if(lookAhead(3).tokenClass==TokenClass.LPAR) {
                    	     FunDecl fd = parseFunDecl();
               	    	     f.add(fd);
               		         parseFunDecls(f);
                 	    }
        		     }
        		     
           		}

        	}
        	else {
        		if(accept(TokenClass.INT,TokenClass.CHAR,TokenClass.VOID)) {
            		
        			if(lookAhead(1).tokenClass==TokenClass.ASTERIX) {
            			if(lookAhead(2).tokenClass==TokenClass.IDENTIFIER) {
                        	if(lookAhead(3).tokenClass==TokenClass.LPAR) {
                        	     FunDecl fd = parseFunDecl();
                   	    	     f.add(fd);
                   		         parseFunDecls(f);
                        	}
                		}
            		}
            		else if(lookAhead(1).tokenClass==TokenClass.IDENTIFIER) {
                    	if(lookAhead(2).tokenClass==TokenClass.LPAR) {
                    		 System.out.println("before_func");
                    	     FunDecl fd = parseFunDecl();
               	    	     f.add(fd);
               		         parseFunDecls(f);
                    	}
            		} 	
            		
        		}
        	}
    	}return f;


    }  
}
   

    // to be completed ...:

