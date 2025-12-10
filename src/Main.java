package src;

import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the code you want to analyse :");
        String code = sc.nextLine();// Files.readString(Paths.get("test/exmple.php")).trim();

        if (!code.startsWith("<?php") || !code.endsWith("?>")) {
            System.err.println("Syntax Error: File must start with '<?php' and end with '?>'");
            return;
        }

        // Remove the php tags for parsing
        code = code.substring("<?php".length(), code.length() - 2).trim();

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