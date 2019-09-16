/* You do not need to change anything up here. */
package lexer;

import frontend.Token;
import static frontend.Token.Type.*;

%%

%public
%final
%class Lexer
%function nextToken
%type Token
%unicode
%line
%column

%{
	/* These two methods are for the convenience of rules to create toke objects.
	* If you do not want to use them, delete them
	* otherwise add the code in 
	*/
	
	private Token token(Token.Type type) {
        return new Token(type, yyline, yycolumn, yytext());
	}

	/* Use this method for rules where you need to process yytext() to get the lexeme of the token.
	 *
	 * Useful for string literals; e.g., the quotes around the literal are part of yytext(),
	 *       but they should not be part of the lexeme.
	*/
	private Token token(Token.Type type, String text) {
		return new Token(type, yyline, yycolumn, text);
	}

	private String trimQuotes(String text) {
	    return text.replaceAll("^\"|\"$", "");
	}
%}

/* This definition may come in handy. If you wish, you can add more definitions here. */
WhiteSpace = [ ] | \t | \f | \n | \r
Letter = [a-zA-Z]
Digit= [0-9]
SpecialChar = "_"
Character = {Letter} | {Digit} | {SpecialChar}
Identifier = {Letter}{Character}*
IntegerLiteral = {Digit}+
StringLiteral = \"([^\"\n]*)\"

%%
/* put in your rules here.    */

/* keywords */
"import" { return token(Token.Type.IMPORT); }
"module" { return token(Token.Type.MODULE); }
"boolean" { return token(Token.Type.BOOLEAN); }
"true" { return token(Token.Type.TRUE); }
"false" { return token(Token.Type.FALSE); }
"if" { return token(Token.Type.IF); }
"else" { return token(Token.Type.ELSE); }
"while" { return token(Token.Type.WHILE); }
"public" { return token(Token.Type.PUBLIC); }
"break" { return token(Token.Type.BREAK); }
"return" { return token(Token.Type.RETURN); }
"int" { return token(Token.Type.INT); }
"type" { return token(Token.Type.TYPE); }
"void" { return token(Token.Type.VOID); }

/* punctuations */
"," { return token(Token.Type.COMMA); }
"[" { return token(Token.Type.LBRACKET); }
"{" { return token(Token.Type.LCURLY); }
"(" { return token(Token.Type.LPAREN); }
"]" { return token(Token.Type.RBRACKET); }
"}" { return token(Token.Type.RCURLY); }
")" { return token(Token.Type.RPAREN); }
";" { return token(Token.Type.SEMICOLON); }

/* operators */
"/" { return token(Token.Type.DIV); }
"==" { return token(Token.Type.EQEQ); }
"=" { return token(Token.Type.EQL); }
">=" { return token(Token.Type.GEQ); }
">" { return token(Token.Type.GT); }
"<=" { return token(Token.Type.LEQ); }
"<" { return token(Token.Type.LT); }
"-" { return token(Token.Type.MINUS); }
"!=" { return token(Token.Type.NEQ); }
"+" { return token(Token.Type.PLUS); }
"*" { return token(Token.Type.TIMES); }

/* identifier */
{Identifier} { return token(Token.Type.ID); }

/* literals */
{StringLiteral} { return token(Token.Type.STRING_LITERAL, trimQuotes(yytext())); }
{IntegerLiteral} { return token(Token.Type.INT_LITERAL); }

{WhiteSpace} { /* ignore */ }

/* You don't need to change anything below this line. */
.							{ throw new Error("unexpected character '" + yytext() + "'"); }
<<EOF>>						{ return token(EOF); }
