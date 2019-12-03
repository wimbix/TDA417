import java.util.Comparator;

public class RangeBinarySearch {
    // Returns the index of the first key in a[] that equals the search key, or -1 if no such key.
    // Complexity: O(log N), where N is the length of the array
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator comparator) {
        if (a == null || key == null || comparator == null) {
            throw new NullPointerException();
        }

        int low = 0;
        int high = a.length - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;

            if (comparator.compare(a[mid], key) >= 0) {
                high = mid;
            } else if (comparator.compare(a[mid], key) < 0) {
                low = mid + 1;
            }
        }

        return low;
    }

    // Returns the index of the last key in a[] that equals the search key, or -1 if no such key.
    // Complexity: O(log N)
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator comparator) {
        if (a == null || key == null || comparator == null) {
            throw new NullPointerException();
        }

        int low = 0;
        int high = a.length - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;

            if (comparator.compare(a[mid], key) > 0) {
                high = mid;
            } else if (comparator.compare(a[mid], key) <= 0) {
                low = mid + 1;
            }
        }

        return low;
    }
}