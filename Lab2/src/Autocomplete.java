import com.sun.tools.hat.internal.util.ArraySorter;

import java.util.Arrays;
import java.util.Comparator;

public class Autocomplete {
    Term[] terms;

    // Initializes the data structure from the given array of terms.
    // Complexity: O(N log N), where N is the number of terms
    public Autocomplete(Term[] terms) {
        this.terms = terms;
    }

    // Returns all terms that start with the given prefix, in descending order of weight.
    // Complexity: O(log N + M log M), where M is the number of matching terms
    public Term[] allMatches(String prefix) {
        Comparator<Term> prefixComparator = Term.byPrefixOrder(prefix.length());
        Arrays.sort(terms, prefixComparator);

        Term term1 = new Term(prefix, prefix.length());
        int firstIndex = RangeBinarySearch.firstIndexOf(terms, term1, prefixComparator);
        int lastIndex = RangeBinarySearch.lastIndexOf(terms, term1, prefixComparator);

        Term[] matchedTerms = new Term[lastIndex - firstIndex];
        int index = 0;

        for (Term term : terms) {
            if (prefixComparator.compare(term, term1) == 0) {
                matchedTerms[index] = term;
                index++;
            }
        }

        Comparator<Term> weightComparator = Term.byReverseWeightOrder;
        Arrays.sort(matchedTerms, weightComparator);

        return matchedTerms;
    }

    // Returns the number of terms that start with the given prefix.
    // Complexity: O(log N)
    public int numberOfMatches(String prefix) {
        return 0;
    }
}
