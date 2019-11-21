
import java.util.Arrays;
import java.util.Comparator;

public class Autocomplete {
    Term[] terms;

    // Initializes the data structure from the given array of terms.
    // Complexity: O(N log N), where N is the number of terms
    public Autocomplete(Term[] terms) {
        if (terms == null) {
            throw new NullPointerException();
        }

        for (Term term: terms) {
            if (term == null) {
                throw new NullPointerException();
            }
        }

        this.terms = terms;
    }

    // Returns all terms that start with the given prefix, in descending order of weight.
    // Complexity: O(log N + M log M), where M is the number of matching terms
    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new NullPointerException();
        }

        Comparator<Term> prefixComparator = Term.byPrefixOrder(prefix.length());

        int numberOfMatches = numberOfMatches(prefix);

        Term[] matchedTerms = new Term[numberOfMatches];
        int index = 0;

        Term term1 = new Term(prefix, prefix.length());
        for (Term term : terms) {
            if (prefixComparator.compare(term, term1) == 0) {
                if (numberOfMatches != 0) {
                    matchedTerms[index] = term;
                    index++;
                }

            }
        }

        Comparator<Term> weightComparator = Term.byReverseWeightOrder;
        Arrays.sort(matchedTerms, weightComparator);

        return matchedTerms;
    }

    // Returns the number of terms that start with the given prefix.
    // Complexity: O(log N)
    public int numberOfMatches(String prefix) {
        if (prefix == null) {
            throw new NullPointerException();
        }

        Comparator<Term> lexi = Term.byLexicographicOrder;
        Comparator<Term> prefixComparator = Term.byPrefixOrder(prefix.length());
        try {
            Arrays.sort(terms, lexi);
        } catch (Error e) {
            System.out.println(e);
        }

        Term term1 = new Term(prefix, prefix.length());
        int firstIndex = RangeBinarySearch.firstIndexOf(terms, term1, prefixComparator);
        int lastIndex = RangeBinarySearch.lastIndexOf(terms, term1, prefixComparator);

        return lastIndex-firstIndex;
    }
}
