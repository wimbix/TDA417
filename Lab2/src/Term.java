import java.util.Comparator;
import java.util.concurrent.ConcurrentMap;

public class Term{

    String query;
    long weight;

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if(query == null) {
            throw new NullPointerException();
        } else if (weight < 0 ) {
            throw new IllegalArgumentException();
        }

        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in lexicographic order by query.
    public static Comparator<Term> byLexicographicOrder() {

        return new LexicographicCmp();
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightCmp();
    }

    // Compares the two terms in lexicographic order,
    // but using only the first k characters of each query.
    public static Comparator<Term> byPrefixOrder(int k) {
        if(k < 0) {
            throw new IllegalArgumentException();
        }

        return new PrefixCmp(k);
    }

    static class LexicographicCmp implements Comparator<Term> {

        @Override
        public int compare(Term o1, Term o2) {
            return o1.query.compareTo(o2.query);
        }
    }

    static class ReverseWeightCmp implements Comparator<Term> {

        @Override
        public int compare(Term o1, Term o2) {
            return Long.compare(o1.weight, o2.weight);
        }
    }

    static class PrefixCmp implements Comparator<Term> {
        int k;

        public PrefixCmp(int k) {
            this.k = k;
        }

        @Override
        public int compare(Term o1, Term o2) {
            String newO1 = o1.query.substring(0, k - 1);
            String newO2 = o2.query.substring(0, k - 1);

            return newO1.compareTo(newO2);
        }
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by whitespace, followed by the query.
    public String toString() {
        return String.format("%12d    %s", this.weight, this.query);
    }
}
