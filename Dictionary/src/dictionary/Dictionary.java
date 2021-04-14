package dictionary;

public class Dictionary {
    private final Word head;
    private Word tail;

    public Dictionary () {
        this.head = new Word("this just a null Word","nothing", "use less");
        tail = head;
    }

    public Word getHead() {
        return head;
    }

    public void add (Word a) {
        a.Word_targer = a.Word_targer.toLowerCase();
        a.next = tail.next;
        tail.next = a;
        tail = a;
    }

    public void lookUpAndAdd(Word a) {
        a.Word_targer = a.Word_targer.toLowerCase();
        a.Word_explain = a.Word_explain;
        Word pointer = findTheMostSimilar(a.Word_targer, head);
        if (pointer.next != null && pointer.next.Word_targer.equals(a.Word_targer)) {
            pointer.next.Word_explain = a.Word_explain;
        } else {
            a.next = pointer.next;
            pointer.next = a;
        }
    }
    
    public String searchStringFromWord (Word pointer, String str) {
        String ret = "";
        int count = 0;
        while (pointer != null && count < 7) {
            count++;
            if (pointer.Word_targer.length() < str.length()) {
                return ret;
            }
            for (int i = 0; i < str.length(); i++) {
                if (pointer.Word_targer.charAt(i) != str.charAt(i)) {
                    return ret;
                }
            }
            ret += pointer.Word_targer + "#";
            pointer = pointer.next;
        }
        return ret;
    }

    public void printAll (){
        Word current = head.next;
        int count = 1;
        System.out.println("No   |                 English |                Vietnamese");
        while (current != null) {
            System.out.printf("%4d | %23s | %25s \n",count,current.Word_targer,current.Word_explain);
            current = current.next;
            count ++;
        }
    }

    public Word findTheMostSimilar (String tar, Word start) {
        Word current = start.next;
        Word before = start;
        boolean bool;
        int length = 1;
        Character a,b;
        while (current != null) {
            bool = true;
            for (int i = 0; i < length; i++) {
                if (i < tar.length())
                    a = tar.charAt(i);
                else a = 0;

                if (i < current.Word_targer.length())
                    b = current.Word_targer.charAt(i);
                else b = 0;

                if( a > b) {
                    bool = false;
                    break;
                } else if (a < b) return before;
            }
            //
            if (bool) {
                if (length == tar.length()) return before;
                length++;
            } else {
                current = current.next;
                before = before.next;
            }
        }
        return before;
    }
    
    public String[] searchMostSimilarResults(String str, Word start) {
        int count = 1;
        int error = 1;
        StringBuilder out = new StringBuilder();
        int minLength = str.length() - 1;
        int maxLength = str.length() + 2;
        if (maxLength > 5) {
            if (maxLength > 8) {
                error = 3;
            } else {
                error = 2;
            }
        }
        Word w = start.next;
        while (w.Word_targer.charAt(0) == str.charAt(0)) {
            if (count > 7) {
                break;
            }
            
            if (w.Word_targer.length() <= maxLength && w.Word_targer.length() >= minLength) {
                if (similar(str, w.Word_targer, error)) {
                    out.append(w.Word_targer).append(",");
                    count ++;
                }
            }
            w = w.next;
        }
        return out.toString().split(",");
    }
    
    private boolean similar(String str1, String str2, int error) {
        String s1 = str1;
        String s2 = str2;
        if (str1.length() > str2.length()) {
            s1 = str2;
            s2 = str1;
        }
        int z = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                z++;
            }
            if (z >= error) {
                return false;
            }
        }
        return true;
    }
}
