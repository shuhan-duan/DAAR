package regEx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    public static List<Pair> search(DFA minDfa, String line) {

        List<Pair> matches = new ArrayList<>();

        DFAState currentState = minDfa.getStartState();
        int matchStartIndex = 0;
        int textIndex = 0;

        while (textIndex < line.length()) {
            int currentChar = line.charAt(textIndex);

            DFAState nextState = currentState.getTransitions(currentChar);
            if (nextState == null)
                nextState = currentState.getTransitions(SyntaxTreeBuilder.DOT);

            if (nextState != null) {
                currentState = nextState;

                if (currentState.isAccept()) {
                    int matchEndIndex = textIndex;
                    matches.add(new Pair(matchStartIndex, matchEndIndex));

                    // continue searching for the next match
                    currentState = minDfa.getStartState();
                    textIndex = matchStartIndex + 1;
                    matchStartIndex = textIndex;
                } else {
                    textIndex++;
                }
            } else {
                // no valid transition, return to the start state
                currentState = minDfa.getStartState();
                textIndex = matchStartIndex + 1;
                matchStartIndex = textIndex;
            }
        }

        return matches;
    }

    public static String readText(File file) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static boolean isSimpleConcatenation(String regex) {
        char[] specialChars = { '*', '|' };

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);

            if (c == '.' && (i == 0 || Character.isLetterOrDigit(regex.charAt(i - 1))) && 
                (i == regex.length() - 1 || Character.isLetterOrDigit(regex.charAt(i + 1)))) {
                continue; // Likely a period in a sentence, not a regex dot.
            }

            if (indexOf(specialChars, c) != -1) {
                if (i == 0 || regex.charAt(i - 1) != '\\' || (i >= 2 && regex.charAt(i - 2) == '\\')) {
                    return false;
                }
            }
        }

        return true;
    }


    private static int indexOf(char[] array, char c) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == c) {
                return i;
            }
        }
        return -1;
    }


    public static void printResult(List<Pair> matches, String line) {
        if (!matches.isEmpty()) {
            StringBuilder highlightedLine = new StringBuilder();
            int currentIndex = 0;
            for (Pair couple : matches) {
                // Append text before the match
                if (couple.getStartIndex() > currentIndex) {
                    highlightedLine.append(line, currentIndex, couple.getStartIndex());
                }

                // Append the matched text in red
                String matchedText = line.substring(couple.getStartIndex(), couple.getEndIndex() + 1);
                highlightedLine.append("\u001B[31m"); // ANSI escape code for red text
                highlightedLine.append(matchedText);
                highlightedLine.append("\u001B[0m"); // Reset color

                currentIndex = couple.getEndIndex() + 1;
            }
            // Append any remaining text after the last match
            if (currentIndex < line.length()) {
                highlightedLine.append(line.substring(currentIndex));
            }

            // Print the line with highlighted matches
            System.out.println(highlightedLine.toString());
        }
    }

    public static void main(String[] args) throws Exception {

        String regEx, filename;
        File file;


        regEx = "Sarg*on";
        filename = "56667-0.txt";
        System.out.println("RegEx : \"" + regEx + "\"");
        System.out.println("filename : " + filename);

        file = new File(filename);

        String text = readText(file);
        String[] lines = text.split("\\n");

        long startTime = System.currentTimeMillis();

        boolean simpleConcat = isSimpleConcatenation(regEx);

        if (simpleConcat) {
            System.out.println("KMP");
            KMP kmp = new KMP(regEx);
            for (String line : lines) {
                List<Pair> matches = kmp.search(line);
                printResult(matches, line);
            }

        } else {
            System.out.println("automaton");
            SyntaxTreeNode root = SyntaxTreeBuilder.buildSyntaxTree(regEx);
            NFA nfa = NFABuilder.syntaxTreeToNFA(root);
            //System.out.println(NFABuilder.generateDOT(nfa));
            DFA dfa = DFABuilder.NFAToDFA(nfa);
            //System.out.println(DFABuilder.generateDOT(dfa));
            DFA minDfa = DFABuilder.minimizeDFA(dfa);
            System.out.println(DFABuilder.generateDOT(minDfa));
            for (String line : lines) {
                List<Pair> matches = search(minDfa, line);
                printResult(matches, line);
            }
        }

        long searchEndTime = System.currentTimeMillis();

        System.out.println("Time used: " + (searchEndTime - startTime) + "ms");

        /////////////////

    }
}