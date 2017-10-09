package lexer;

import lexer.Token.TokenClass;
import lexer.Scanner;

import java.io.EOFException;
import java.io.IOException;
//import java.util.Scanner;

/**
 * @author cdubach
 */
public class Tokeniser {

    private Scanner scanner;

    private int error = 0;
    public int getErrorCount() {
	return this.error;
    }

    public Tokeniser(Scanner scanner) {
        this.scanner = scanner;
    }

    private void error(char c, int line, int col) {
        System.out.println("Lexing error: unrecognised character ("+c+") at "+line+":"+col);
	error++;
    }


    public Token nextToken() {
        Token result;
        try {
             result = next();
        } catch (EOFException eof) {
            // end of file, nothing to worry about, just return EOF token
            return new Token(TokenClass.EOF, scanner.getLine(), scanner.getColumn());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // something went horribly wrong, abort
            System.exit(-1);
            return null;
        }
        return result;
    }

    /*
     * To be completed
     */
    boolean iscomment = false;
    boolean ex = false;
    private Token next() throws IOException {

        int line = scanner.getLine();
        int column = scanner.getColumn();
        // get the next character
        char c = scanner.next();
        if (iscomment) {
        	    c = scanner.peek();
         		while (c != '\n') {
         			if (!ex && c !='*') {     // if false, we have seen the 2nd asterisk
         		    
     //       		System.out.println(column);
     //       		System.out.println(line);
     //   		    System.out.println(c);
            		scanner.next();
        			c = scanner.peek();
                     }
        	        if (c == '*') { 
        		      scanner.next();
        	          c = scanner.peek();
        	         
     //   	          System.out.println("aftersecondnew");
     //   	          System.out.println(c);
     //         	 	  System.out.println(column);
     //         		  System.out.println(line);
        	          if (c == '\n') {
        	        	  //System.out.println("n2");
        	        	  ex = false;
        	              continue;
        	          }
        	          if (c == '*') {
     //   	        	  System.out.println("afterextranew");
        	        	  ex = true;
        	        	  continue;
        	          }
        	          if (c == '/') {
     //   	        	  System.out.println("afterslash");
        	        	  scanner.next();  // 
        	        	  c = scanner.peek();
   //     	        	  System.out.println(c);
        	        	  ex = false;
        	        	  iscomment = false;
        	        	  return next();
        	          }
        	          else {
   //     	        	  System.out.println("whW");
        	        	  c = scanner.peek();
  //      	        	  System.out.println(c);
  //      	        	  System.out.println(column);
  //      	        	  System.out.println(line);
        	        	  ex = false;
        	        	  continue;
        	          }
        	        }
        		}
         		//System.out.println("endhere");
         		//iscomment = false;
         		return next();
        }
        if (c == '/') {
        	c = scanner.peek();
        	if (c != '*' && c != '/') {
   //     		System.out.println(column);
   //     		System.out.println(line);
        	    return new Token(TokenClass.DIV, Character.toString(c), line, column);
        	}
        	if (c == '/') { 
        		scanner.next();
        		c = scanner.peek();
        		while (c != '\n') {
        			scanner.next();
        			c = scanner.peek();
        		}
        //		System.out.println(c);
        		return next();
        	}
        	
        	if (c == '*') {
        	    scanner.next();
        		c = scanner.peek();
        		while (c != '\n') {
        			if (!ex && c != '*') {     // if false, we have seen the 2nd asterisk
        		    scanner.next();
        			c = scanner.peek();
      //  			System.out.println("middle");
      //  			System.out.println(column);
      //  			System.out.println(line);
      //  			System.out.println(c);
        			}
        	        if (c == '*') { 
        		         scanner.next();
        	             c = scanner.peek();
      //  	             System.out.println("aftersec");
        	             if (c == '\n') {
        	        	    ex = false;
        	                break;
        	             }
        	             if (c == '*') {
     //   	        	     System.out.println("extra");
        	        	     ex = true;
     //   	        	     System.out.println(column);
     //   	        	     System.out.println(line);
        	        	     continue;
        	        	     
        	             }
        	             if (c == '/') {
     //   	            	 System.out.println(c);
         	        	     scanner.next();
        	        	     ex = false;
        	        	     iscomment = false;
     //   	        	     System.out.println("end");
     //   	        	     System.out.println(c);
        	        	     return next();
        	             }
        	             else {
        	        	     ex = false;
        	        	     continue;
        	             }
        	        
        	        }
        		}
     //   		System.out.println("comm");
     //   		System.out.println(column);
     //   		System.out.println(line);
     //   		System.out.println(c);
        		iscomment = true;
        		return next();
        	}
        	else {
        	   // error(c, line, column);
                //return new Token(TokenClass.INVALID, line, column);	
        	}
        } 
        // skip white spaces
        if (Character.isWhitespace(c))
            return next();
       
        
        if (c == '{')
            return new Token(TokenClass.LBRA, Character.toString(c), line, column);
        if (c == '}')
            return new Token(TokenClass.RBRA, Character.toString(c), line, column);
        if (c == '(')
            return new Token(TokenClass.LPAR, Character.toString(c), line, column);
        if (c == ')')
            return new Token(TokenClass.RPAR, Character.toString(c), line, column);
        if (c == '[')
            return new Token(TokenClass.LSBR, Character.toString(c), line, column);
        if (c == ']')
            return new Token(TokenClass.RSBR, Character.toString(c), line, column);
        if (c == ';')
            return new Token(TokenClass.SC, Character.toString(c), line, column);
        if (c == ',')
            return new Token(TokenClass.COMMA, Character.toString(c), line, column);
        if (c == '.')
        	return new Token(TokenClass.DOT, Character.toString(c), line, column);
        if (c == '#'){
        	StringBuilder sb = new StringBuilder();
        	sb.append(c);
        	c = scanner.peek();
        	if (c == 'i') {
        		sb.append(c);	
        		scanner.next();
                c = scanner.peek();
                if (c == 'n') {
        		  sb.append(c);	
        		  scanner.next();
                  c = scanner.peek();
                  if (c == 'c') {
        		    sb.append(c);	
        		    scanner.next();
                    c = scanner.peek();
                    if (c == 'l') {
        		      sb.append(c);	
        		      scanner.next();
                      c = scanner.peek(); 
                      if (c == 'u') {
        		         sb.append(c);	
        		         scanner.next();
                         c = scanner.peek();  
                         if (c == 'd') {
        		            sb.append(c);	
        		            scanner.next();
                            c = scanner.peek();
                            if (c == 'e') {
        		              sb.append(c);	
        		              scanner.next();
                              return new Token(TokenClass.INCLUDE, sb.toString(), line, column);
                            }
                         }
                      }
                    }
                  }
                }
        	}
        	    error(c, line, column);
        		return new Token(TokenClass.INVALID, line, column);
        	}
        
        	
        // recognises the plus operator'=
        if (c == '+') {
            return new Token(TokenClass.PLUS, Character.toString(c), line, column);
        }
        if (c == '-')
            return new Token(TokenClass.MINUS, Character.toString(c), line, column);
        if (c == '*') {
        	return new Token(TokenClass.ASTERIX, Character.toString(c), line, column);
        }

        if (c == '%')
        	return new Token(TokenClass.REM, Character.toString(c), line, column);
        if (c == '&') {
        	c = scanner.peek();
        	if (c == '&')
        		return new Token(TokenClass.AND, line, column);
        	else {
        		error(c, line, column);
        		return new Token(TokenClass.INVALID, line, column);
        	}
        }
        if (c == '|') {
        	c = scanner.peek();
        	if (c == '|')
        		return new Token(TokenClass.OR, line, column);
        	else {
        		error(c, line, column);
        		return new Token(TokenClass.INVALID, line, column);
        	}
        }
        if (c == '!') {
        	c = scanner.peek();
        	if (c == '=')
        		return new Token(TokenClass.NE, line, column);
        	else {
        		error(c, line, column);
        		return new Token(TokenClass.INVALID, line, column);
        	}
        }       
        if (c == '>') {
        	c = scanner.peek();
        	if (c == '=')
        		return new Token(TokenClass.GE, line, column);
        	else {
        		return new Token(TokenClass.GT, line, column);
        	}
        }
        if (c == '<') {
        	c = scanner.peek();
        	if (c == '=')
        		return new Token(TokenClass.LE, line, column);
        	else {
        		return new Token(TokenClass.LT, line, column);
        	}
        }
        
        if (c == '=') {
        	c = scanner.peek();
        	if (c == '=')
        		return new Token(TokenClass.EQ, line, column);
        	else {
        		return new Token(TokenClass.ASSIGN, line, column);
        	}
        }      
        	
       
        if (Character.isLowerCase(c) || Character.isUpperCase(c) || c == '_') {
        	StringBuilder sa = new StringBuilder();
        	sa.append(c);
        	c = scanner.peek();
        	while (Character.isLetterOrDigit(c) || c == '_') {
        		sa.append(c);	
        		scanner.next();
                c = scanner.peek();	
        	}
        	if (sa.toString().equals("if"))
        	    return new Token(TokenClass.IF, line, column);
        	else if (sa.toString().equals("int")) 
        		return new Token(TokenClass.INT, line, column);
        	else if (sa.toString().equals("void"))
        		return new Token(TokenClass.VOID, line, column);
        	else if (sa.toString().equals("else"))
        		return new Token(TokenClass.ELSE, line, column);
        	else if (sa.toString().equals("char"))
        		return new Token(TokenClass.CHAR, line, column);
        	else if (sa.toString().equals("while"))
        		return new Token(TokenClass.WHILE, line, column);
        	else if (sa.toString().equals("return"))
        		return new Token(TokenClass.RETURN, line, column);
        	else if (sa.toString().equals("struct"))
        		return new Token(TokenClass.STRUCT, line, column);
        	else if (sa.toString().equals("sizeof")) 
        		return new Token(TokenClass.SIZEOF, line, column);
        	else 
        		return new Token(TokenClass.IDENTIFIER, sa.toString(), line, column);
        }
        
        if (Character.isDigit(c)) {
        	StringBuilder sd = new StringBuilder();
        	sd.append(c);
        	c = scanner.peek();
        	while (Character.isDigit(c)) {
        		sd.append(c);	
        		scanner.next();
                c = scanner.peek();	
        	}
        	return new Token(TokenClass.INT_LITERAL, sd.toString(), line, column);
        	

        }
        if (c == '\'') {
        	StringBuilder se = new StringBuilder();
        	c = scanner.peek();
        	if (c == '\\') {                             //if the char is an escape sequence
        	    se.append(c);
        		scanner.next();                         // t n b  r f
                c = scanner.peek();	
                if (c == '"' | c == '\'' | c == '\\') {
                    se.setCharAt(0, c);
                    scanner.next();
                    c = scanner.peek();
                    if (c == '\'') {
                        scanner.next();
                        return new Token(TokenClass.CHAR_LITERAL, se.toString(), line, column);
                    }
                }
                else if (c == 't' | c == 'n' | c == 'b' | c == 'r' | c == 'f') {
                    se.append(c);
                    scanner.next();
                    c = scanner.peek();
                    if (c == '\'') {
                        scanner.next();
                        return new Token(TokenClass.CHAR_LITERAL, se.toString(), line, column);
                    }

                }
                error(c, line, column);
                return new Token(TokenClass.INVALID, line, column);
        	}
        	else if (c != '\'') {
        		se.append(c);
        		scanner.next();
        		c = scanner.peek();
        		if (c == '\'') {
                    scanner.next();
        			return new Token(TokenClass.CHAR_LITERAL, se.toString(), line, column);
        		}
        	}
        	else {
        //		System.out.println("here");
        		error(c, line, column);
        	    return new Token(TokenClass.INVALID, line, column);
        	}
        
        }
        
        if (c == '"') {  
        //	boolean es = false;
        	StringBuilder sf = new StringBuilder();
        	c = scanner.peek();
        	while (c != '"') {
        	    if (c == '\\') {
        //	    	es = true;
        	        sf.append(c);
        		    scanner.next();
                    c = scanner.peek();	
                    if (c == '"' | c == '\'' | c == '\\') {
                      sf.setCharAt(sf.length()-1, c);
                      scanner.next();
                      c = scanner.peek();
                    }
                    else if (c == 't' | c == 'n' | c == 'b' | c == 'r' | c == 'f') {
                      sf.append(c);
                      scanner.next();
                      c = scanner.peek();
                    }
        	    }
        	    else {
        	    	sf.append(c);	
        		    scanner.next();
                    c = scanner.peek();	
        	    } 
        	}
        	scanner.next();
        	return new Token(TokenClass.STRING_LITERAL, sf.toString(), line, column);
        	

        }


   // ... to be completed
       // System.out.println("there");
        // if we reach this point, it means we did not recognise a valid token
        error(c, line, column);
        return new Token(TokenClass.INVALID, line, column);
    }


}
