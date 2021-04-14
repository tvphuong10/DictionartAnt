package dictionary;

public class Word {
    public String Word_targer;
    public String Word_spell;
    public String Word_explain;
    public Word next;
    
    Word (Word a) {
        this.Word_targer = a.Word_targer;
        this.Word_spell = a.Word_spell;
        this.Word_explain = a.Word_explain;
        this.next = null;
    }

    Word (String tar, String spe, String exp) {
        this.Word_targer = tar;
        this.Word_spell = spe;
        this.Word_explain = exp;
        this.next = null;
    }
}
