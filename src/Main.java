package src;

import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String code = Files.readString(Paths.get("test/exmple.php")); // Change path as desired
        lexical lexer = new lexical(code);
        List<Token> tokens = lexer.tokenize();

        System.out.println("Tokens:");
        for (Token t : tokens) {
            System.out.println(t);
        }

        syntaxe parser = new syntaxe(tokens);
        try {
            parser.analyseProgram();
            System.out.println("Parsing success!");
        } catch (SyntaxException e) {
            System.err.println(e.getMessage());
        }
    }
}