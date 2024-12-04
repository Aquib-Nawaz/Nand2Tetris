package Parser;

import junit.framework.TestCase;
import java.io.*;

import static org.junit.Assert.assertThrows;

public class ParserTest extends TestCase {
    private final String testFile = "test.asm";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        File file = new File(testFile);
        file.delete();
    }

    public void testHasMoreCommandsWithEmptyOrComment() throws IOException {

        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(testFile))){
            fileWriter.write("       \n");
            fileWriter.write("//       \n");
            fileWriter.flush();

            Parser parser = new Parser(testFile);
            assertFalse(parser.hasMoreCommands());
            assertThrows(InstructionNotFoundException.class, parser::advance);
        }
    }

    public void testHasMoreCommandsTrue() throws IOException {

        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(testFile))){
            fileWriter.write("       \n");
            fileWriter.write("//       \n");
            fileWriter.write("@123\n");
            fileWriter.flush();

            Parser parser = new Parser(testFile);
            assertTrue(parser.hasMoreCommands());
        }
    }

    public void testAdvanceAndHasMoreCommands() throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(testFile))) {
            fileWriter.write("@123\n");

            fileWriter.flush();

            Parser parser = new Parser(testFile);
            assertTrue(parser.hasMoreCommands());
            parser.advance();
            assertFalse(parser.hasMoreCommands());
        }
    }

    public void testCommandType() throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(testFile))) {
            fileWriter.write("@123\n");
            fileWriter.write("(Loop)\n");
            fileWriter.write("D=D+1\n");

            fileWriter.flush();

            Parser parser = new Parser(testFile);
            parser.advance();

            assertEquals(InstructionType.A_COMMAND, parser.commandType());
            parser.advance();
            assertEquals(InstructionType.L_COMMAND, parser.commandType());
            parser.advance();
            assertEquals(InstructionType.C_COMMAND, parser.commandType());
        }
    }

    public void testSymbol() throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(testFile))) {
            fileWriter.write("@123\n");
            fileWriter.write("(Loop)\n");
            fileWriter.write("D=D+1\n");

            fileWriter.flush();

            Parser parser = new Parser(testFile);
            parser.advance();

            assertEquals("123", parser.symbol());
            parser.advance();
            assertEquals("Loop", parser.symbol());
            parser.advance();
            assertThrows(MethodNotSupportedException.class, parser::symbol);
        }
    }

    public void testDestMnemonics() throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(testFile))) {
            fileWriter.write("D=D+1\n");
            fileWriter.write("M=D+1;JMP\n");
            fileWriter.write("D\n");

            fileWriter.write("(Loop)\n");

            fileWriter.flush();

            Parser parser = new Parser(testFile);
            parser.advance();

            assertEquals("D", parser.dest());
            parser.advance();
            assertEquals("M", parser.dest());
            parser.advance();
            assertEquals("", parser.dest());
            parser.advance();
            assertThrows(MethodNotSupportedException.class, parser::dest);
        }
    }

    public void testCompMnemonics() throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(testFile))) {
            fileWriter.write("D;JMP\n");
            fileWriter.write("AM=D+1;JEQ\n");
            fileWriter.write("D\n");
            fileWriter.write("(Loop)\n");

            fileWriter.flush();

            Parser parser = new Parser(testFile);
            parser.advance();
            assertEquals("D", parser.comp());
            parser.advance();
            assertEquals("D+1", parser.comp());
            parser.advance();
            assertEquals("D", parser.comp());
            parser.advance();
            assertThrows(MethodNotSupportedException.class, parser::comp);
        }
    }

    public void testJumpMnemonics() throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(testFile))) {
            fileWriter.write("D;JMP\n");
            fileWriter.write("D=D+1;JEQ\n");

            fileWriter.write("(Loop)\n");

            fileWriter.flush();

            Parser parser = new Parser(testFile);
            parser.advance();
            assertEquals("JMP", parser.jump());

            parser.advance();
            assertEquals("JEQ", parser.jump());
            parser.advance();
            assertThrows(MethodNotSupportedException.class, parser::jump);
        }
    }

    public void testInlineComments() throws IOException{
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(testFile))) {
            fileWriter.write("   D=M              // D = first number\n");
            fileWriter.flush();

            Parser parser = new Parser(testFile);
            parser.advance();
            assertEquals(parser.comp(), "M");
            assertEquals(parser.dest(), "D");
        }
    }
}