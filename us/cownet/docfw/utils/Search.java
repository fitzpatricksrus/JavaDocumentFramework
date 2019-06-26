package us.cownet.docfw.utils;

/**
 * Example:
 * int dataToSearch = { 3, 5, 7, 19, 34 };
 * int valueToFind = 14;
 * int result = Search.BinarySearch.doSearch(
 * new Searchable() {
 * public int getSize() { return dataToSearch.length; }
 * public boolean isNeededValueBefore(int ndx) {
 * return valueToFind < dataToSearch[ndx];
 * }
 * }
 * );
 */
public abstract class Search {

	public abstract int search(Searchable data);

	/**
	 * A Searchable is both the data and the desired value
	 * for the search.
	 */
	public interface Searchable {
		int getSize();

		boolean isNeededValueBefore(int ndx);
	}

	//------------------------
	public static class LinearSearch extends Search {
		public static final LinearSearch global = new LinearSearch();

		public static int doSearch(Searchable data) {
			return global.search(data);
		}

		public int search(Searchable data) {
			for (int i = data.getSize() - 1; i > 0; i--) {
				if (data.isNeededValueBefore(i)) {
					return i;
				}
			}
			return 0;
		}
	}

	public static class BinarySearch extends Search {
		public static final BinarySearch global = new BinarySearch();
		private static final int[] powerOf2 =
				{1, 2, 4, 8, 16, 32, 64, 128, 256,
						512, 1024, 2048, 4096, 8192, 16384,
						32768, 65536, 131072, 262144};

		public static int doSearch(Searchable data) {
			return global.search(data);
		}

		//returns the index of then first item whose
		//value is <= the value of Searchable
		public final int search(final Searchable s) {
			final int dataSize = s.getSize();
			if (dataSize < 1) {
				throw new IllegalArgumentException();
			}
			if (dataSize >= powerOf2[powerOf2.length - 1]) {
				throw new IllegalArgumentException();
			}

			int power = 0;
			for (power = powerOf2.length - 1;
			     power > 0 && dataSize < powerOf2[power];
			     power--) {
			}

			int possibleStart = dataSize - powerOf2[power];
			int index = powerOf2[power] - 1;
			if (!s.isNeededValueBefore(possibleStart)) {
				index += possibleStart;
			}

			switch (power) {
				case 18:
					if (s.isNeededValueBefore(index - 131072)) index -= 131072;
				case 17:
					if (s.isNeededValueBefore(index - 65536)) index -= 65536;
				case 16:
					if (s.isNeededValueBefore(index - 32768)) index -= 32768;
				case 15:
					if (s.isNeededValueBefore(index - 16384)) index -= 16384;
				case 14:
					if (s.isNeededValueBefore(index - 8192)) index -= 8192;
				case 13:
					if (s.isNeededValueBefore(index - 4096)) index -= 4096;
				case 12:
					if (s.isNeededValueBefore(index - 2048)) index -= 2048;
				case 11:
					if (s.isNeededValueBefore(index - 1024)) index -= 1024;
				case 10:
					if (s.isNeededValueBefore(index - 512)) index -= 512;
				case 9:
					if (s.isNeededValueBefore(index - 256)) index -= 256;
				case 8:
					if (s.isNeededValueBefore(index - 128)) index -= 128;
				case 7:
					if (s.isNeededValueBefore(index - 64)) index -= 64;
				case 6:
					if (s.isNeededValueBefore(index - 32)) index -= 32;
				case 5:
					if (s.isNeededValueBefore(index - 16)) index -= 16;
				case 4:
					if (s.isNeededValueBefore(index - 8)) index -= 8;
				case 3:
					if (s.isNeededValueBefore(index - 4)) index -= 4;
				case 2:
					if (s.isNeededValueBefore(index - 2)) index -= 2;
				case 1:
					if (s.isNeededValueBefore(index - 1)) index -= 1;
				case 0:
					//          if (index > 0 && s.isNeededValueBefore(index-1)) index -= 1;
					if (s.isNeededValueBefore(index)) index -= 1;
			}
			return index;
		}
	}
}
