package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import frontend.Token;
import frontend.Token.Type;
import static frontend.Token.Type.*;

/**
 * This class contains unit tests for your lexer. Currently, there is only one test, but you
 * are strongly encouraged to write your own tests.
 */
public class LexerTests {
	// helper method to run tests; no need to change this
	private final void runtest(String input, Token... output) {
		Lexer lexer = new Lexer(new StringReader(input));
		int i=0;
		Token actual=new Token(MODULE, 0, 0, ""), expected;
		try {
			do {
				assertTrue(i < output.length);
				expected = output[i++];
				try {
					actual = lexer.nextToken();
					assertEquals(expected, actual);
				} catch(Error e) {
					if(expected != null)
						fail(e.getMessage());
					/* return; */
				}
			} while(!actual.isEOF());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/** Example unit test. */
	@Test
	public void testKWs() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("module false return while",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 0, 13, "return"),
				new Token(WHILE, 0, 20, "while"),
				new Token(EOF, 0, 25, ""));
	}

	@Test
	public void testKWsWithNewLine() {
		runtest("module false\n\treturn while",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 1, 1, "return"),
				new Token(WHILE, 1, 8, "while"),
				new Token(EOF, 1, 13, ""));
	}

	@Test
	public void testKWsWithNoSpace() {
		runtest("modulefalsereturnwhile",
				new Token(ID, 0, 0, "modulefalsereturnwhile"),
				new Token(EOF, 0, 22, ""));
	}

	@Test
	public void testKWBooleanAsKW() {
		runtest("boolean",
				new Token(BOOLEAN, 0, 0, "boolean"),
				new Token(EOF, 0, 7, ""));
	}

	@Test
	public void testKWBooleanAsID() {
		runtest("Boolean",
				new Token(ID, 0, 0, "Boolean"),
				new Token(EOF, 0, 7, ""));
	}

	@Test
	public void testPuncuations() {
		runtest(", ) false",
				new Token(COMMA, 0, 0,","),
				new Token(RPAREN, 0, 2, ")"),
				new Token(FALSE, 0, 4, "false"),
				new Token(EOF, 0, 9, ""));
	}

	@Test
	public void testOperators() {
		runtest("+-*/>=<=",
				new Token(PLUS, 0, 0,"+"),
				new Token(MINUS, 0, 1, "-"),
				new Token(TIMES, 0, 2, "*"),
				new Token(DIV, 0, 3, "/"),
				new Token(GEQ, 0, 4, ">="),
				new Token(LEQ, 0, 6, "<="),
				new Token(EOF, 0, 8, ""));
	}

	@Test
	public void testIDSuccess1() {
		runtest("aIdentifier",
				new Token(ID, 0, 0, "aIdentifier"),
				new Token(EOF, 0, 11, ""));
	}

	@Test
	public void testIDSuccess2() {
		runtest("aIdentifier1",
				new Token(ID, 0, 0, "aIdentifier1"),
				new Token(EOF, 0, 12, ""));
	}

	@Test
	public void testIDSuccess3() {
		runtest("aIdentifier_",
				new Token(ID, 0, 0, "aIdentifier_"),
				new Token(EOF, 0, 12, ""));
	}

	@Test
	public void testIDFail() {
		runtest("1Identifier",
				new Token(INT_LITERAL, 0, 0, "1"),
				new Token(ID, 0, 1, "Identifier"),
				new Token(EOF, 0, 11, ""));
	}

	@Test
	public void testStringLiteralWithDoubleQuote() {
		runtest("\"\"\"",
				new Token(STRING_LITERAL, 0, 0, ""),
				(Token)null,
				new Token(EOF, 0, 3, ""));
	}

	@Test
	public void testStringLiteralWithInt() {
		runtest("\"3007\"",
				new Token(STRING_LITERAL, 0, 0, "3007"),
				new Token(EOF, 0, 6, ""));
	}

	@Test
	public void testStringLiteralWithKW() {
		runtest("\"while\"",
				new Token(STRING_LITERAL, 0, 0, "while"),
				new Token(EOF, 0, 7, ""));
	}

	@Test
	public void testStringLiteral() {
		runtest("\"\\n\"", 
				new Token(STRING_LITERAL, 0, 0, "\\n"),
				new Token(EOF, 0, 4, ""));
	}

	@Test
	public void testStringLiteral2() {
		runtest("a\"",
				new Token(ID, 0, 0, "a"),
				(Token)null,
				new Token(EOF, 0, 2, ""));
	}

	@Test
	public void testIntegerLiteral() {
		runtest("12 +3456 -789 00001",
				new Token(INT_LITERAL, 0, 0, "12"),
				new Token(PLUS, 0, 3, "+"),
				new Token(INT_LITERAL, 0, 4, "3456"),
				new Token(MINUS, 0, 9, "-"),
				new Token(INT_LITERAL, 0, 10, "789"),
				new Token(INT_LITERAL, 0, 14, "00001"),
				new Token(EOF, 0, 19, ""));
	}
}
