package regEx;

import java.util.Arrays;

public class KMP {
    private String pattern;
    private int[] carryOver;

    public KMP(String pattern) {
        this.pattern = pattern;
        this.carryOver = computeCarryOver();
    }
    
    private int[] computeCarryOver() {
        int[] lps = initializeLPS();
        optimizeLPS(lps);
        return lps;
    }

    private int[] initializeLPS() {
        int[] lps = new int[pattern.length() + 1];
        lps[0] = -1;
        int j = -1;

        for (int i = 0; i < pattern.length(); i++) {
            while (j >= 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = lps[j];
            }
            j++;
            lps[i + 1] = j;
        }

        return lps;
    }

    private void optimizeLPS(int[] lps) {
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < pattern.length(); i++) {
                if (lps[i] != -1 && pattern.charAt(i) == pattern.charAt(lps[i])) {
                    if (lps[lps[i]] != -1) {
                        lps[i] = lps[lps[i]];
                        changed = true;
                    } else {
                        lps[i] = -1;
                        changed = true;
                    }
                }
            }
        } while (changed);
    }

    public int search(String text) {
        int i = 0, j = 0;

        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;

                if (j == pattern.length()) {
                    return i - j; // Match found at position i - j
                }
            } else if (j != 0) {
                j = carryOver[j];
                if (j == -1) {
                    j = 0;
                    i++;
                }
            } else {
                i++;
            }

        }

        return -1; // No match found
    }

    public static void main(String[] args) {
    	String text ="maman mamé mia ! mm maaah !";
        KMP kmp = new KMP("mamia");
        int position = kmp.search(text);
        System.out.println(Arrays.toString(kmp.computeCarryOver())); 
        
        if (position != -1) {
            System.out.println("Pattern found at position: " + position);
        } else {
            System.out.println("Pattern not found in the text.");
        }
    }
}
