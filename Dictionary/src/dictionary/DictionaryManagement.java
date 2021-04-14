package dictionary;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DictionaryManagement {
    private final Dictionary dic;
    private Word checkPointWord1;
    private Word checkPointWord2;
    private Word checkPointWord3;
    private Word checkPointWord4;
    private Word lastSearchWord;
    Character[] lastSearch;

    public DictionaryManagement() {
        dic = new Dictionary();
        lastSearch = new Character[3];
        lastSearch[0] = '\0';
        lastSearch[1] = '\0';
        lastSearch[2] = '\0';
    }
    
    public void insertFromFileDict(String path) {
        BufferedReader input = null;
        String line;
        StringBuilder exp = new StringBuilder();
        String[] a;
        String spe = "";
        String tar = "";
        try {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
            while ((line = input.readLine()) != null) { 
                if (line.length() > 0 && line.charAt(0) == '@') {
                    if (!tar.equals("")) dic.add( new Word(tar,spe ,exp.toString()));
                    tar = line.substring(1);
                    a = tar.split("/");
                    if (a.length > 1) {
                        tar = a[0];
                        spe = a[1];
                    }
                    exp = new StringBuilder();
                } else {
                        exp.append(line).append("\n");
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                if (input != null)
                input.close();
            } catch (IOException e) {
            }
        }
    }

    public void updateData(String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        try (Writer f = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF8"))) {
            Word w  = dic.getHead().next;
            f.append("\n");
            while (w != null) {
                f.append("@").append(w.Word_targer).append("/").append(w.Word_spell).append("/\n");
                f.append(w.Word_explain);
                w = w.next;
            }
            f.append("@ ");
            f.flush();
        }
    }
    
    public boolean removeWord(String str) {
        str = str.toLowerCase().strip();
        if (str.equals("")) {
            return false;
        }
        Word w = dic.findTheMostSimilar(str, checkPointWord1);
        if (w.next.Word_targer.strip().equals(str)) {
            w.next = w.next.next;
            lastSearch[0] = '\0';
            lastSearch[1] = '\0';
            lastSearch[2] = '\0';
            return true;
        } else {
            return false;
        }
    }
    
    public void addWord(String str, String spell, String exp) {
        str = str.toLowerCase().strip();
        Word w = dic.findTheMostSimilar(str, checkPointWord1);
        if (w.next.Word_targer.strip().equals(str)) {
            w.next.Word_spell = spell;
            w.next.Word_explain = exp;
        } else {
            dic.lookUpAndAdd(new Word(str, spell, exp));
        }
        lastSearch[0] = '\0';
        lastSearch[1] = '\0';
        lastSearch[2] = '\0';
    }
    
    public void printAll () {
        dic.printAll();
    }

    public String[] search (String str) {
        Word w = dic.findTheMostSimilar(str, checkPointWord1);
        String out = new String();
        out += "/" + w.next.Word_spell + "/\n";
        String a = w.next.Word_explain;
        a = a.replace("\n-", "\n       - ").replace("\n=", "\n              ").replace("+", ": ").replace("\n!", "\n! ");
        out += a;
        return out.split("\n");
    }
    
    public String[] searchResults (String str) {
        str = str.toLowerCase();
        String[] out;
        if (str.charAt(0) == lastSearch[0]) {
            if (str.length() > 1 && str.charAt(1) == lastSearch[1]) {
                if (str.length() > 2 && str.charAt(2) == lastSearch[2]) {
                    checkPointWord4 = dic.findTheMostSimilar(str, checkPointWord3); 
                    lastSearchWord = checkPointWord4;
                    out = dic.searchStringFromWord(checkPointWord4.next, str).split("#");
                } else {
                    if (str.length() > 2) lastSearch[2] = str.charAt(2);
                    else lastSearch[2] = '\0';
                    checkPointWord3 = dic.findTheMostSimilar(str, checkPointWord2);
                    lastSearchWord = checkPointWord3;
                    out = dic.searchStringFromWord(checkPointWord3.next, str).split("#");
                }
            } else {
                if (str.length() > 1) lastSearch[1] = str.charAt(1);
                else lastSearch[1] = '\0';
                checkPointWord2 = dic.findTheMostSimilar(str, checkPointWord1);
                lastSearchWord = checkPointWord2;
                out = dic.searchStringFromWord(checkPointWord2.next, str).split("#");
            }
        } else {
            lastSearch[0] = str.charAt(0);
            checkPointWord1 = dic.findTheMostSimilar(str, dic.getHead());
            lastSearchWord = checkPointWord1;
            out = dic.searchStringFromWord(checkPointWord1.next, str).split("#");
        }
        return out;
    }
    
    public String[] searchOriginal (String str) {
        String[] out = new String[2];
        str = str.toLowerCase();
        Word w = dic.findTheMostSimilar(str, lastSearchWord).next;
        out[0] = w.Word_spell;
        out[1] = w.Word_explain;
        return out;
    }
    
    public boolean isExist(String str) {
        str = str.toLowerCase().strip();
        Word w = dic.findTheMostSimilar(str, lastSearchWord);
        return w.next.Word_targer.strip().equals(str);
    }
    
    public String[] searchSimilar(String str) {
        lastSearchWord = checkPointWord1;
        return dic.searchMostSimilarResults(str, checkPointWord1);
    }
}
