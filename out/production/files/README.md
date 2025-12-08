# MiniPHPCompiler

A Java-based lexical and syntax analyzer for a simplified PHP-like language with support for variable declarations, assignments, control structures (including foreach), and expressions.

## Features
- Lexer recognizing keywords, identifiers, numbers, strings, and operators.
- Parser supporting variable decl, assignment, if/else, while, foreach, and expressions.
- Error reporting for syntactic mistakes.
- Easily extensible grammar (see `grammar/MiniPHPGrammar.txt`).

## Usage

Run:
```sh
javac -d bin src/*.java
java -cp bin src.Main
```
Test files are in `test/sample.php`.