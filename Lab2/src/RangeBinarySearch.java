import java.util.Comparator;

public class RangeBinarySearch {
    // Returns the index of the first key in a[] that equals the search key, or -1 if no such key.
    // Complexity: O(log N), where N is the length of the array
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator comparator) {
        if (a == null || key == null || comparator == null) {
            throw new NullPointerException();
        }

        return RangeBinarySearch.binarySearch(a, key, 0, a.length - 1, comparator, true);
    }

    // Returns the index of the last key in a[] that equals the search key, or -1 if no such key.
    // Complexity: O(log N)
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator comparator) {
        if (a == null || key == null || comparator == null) {
            throw new NullPointerException();
        }

        return RangeBinarySearch.binarySearch(a, key, 0, a.length - 1, comparator, false);
    }

    public static <Key> int binarySearch(Key[] a, Key key, int low, int high, Comparator comparator, boolean findFirstIndex) {

        if(low == high && comparator.compare(a[low], key) == 0) {
            return low;
        } else if (low == high && comparator.compare(a[low], key) != 0) {
            return -1;
        }

        int mid = low + (high - low) / 2;

        if(comparator.compare(key, a[mid]) == 0 && findFirstIndex) {
            return binarySearch(a, key, low, mid, comparator, true);
        } else if (comparator.compare(key, a[mid]) == 0 && !findFirstIndex) {
            return binarySearch(a, key, mid, high, comparator, false);
        }

        if(comparator.compare(a[mid], key) < 0) {
            return binarySearch(a, key, low, mid-1, comparator, findFirstIndex);
        }

        return binarySearch(a, key, mid+1, high, comparator, findFirstIndex);
    }
}