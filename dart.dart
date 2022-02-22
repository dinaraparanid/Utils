extension on int {
  List<int> getDivs(final bool Function(int) predicate) {
    final divs = List<int>.empty(growable: true);

    for (int i = 1; i * i <= this; i++) {
      if (this % i == 0) {
        if (predicate(i)) {
          divs.add(i);
        }

        if (i * i < this && predicate(this ~/ i)) {
          divs.add(this ~/ i);
        }
      }
    }

    return divs;
  }

  bool get isSimple {
    for (int i = 2; i * i <= this; i++) {
      if (this % i == 0) {
        return false;
      }
    }

    return true;
  }
}

extension on bool {
  bool arrow(final bool other) => !(this && !other);
}

extension on List<int> {
  List<int> sortAndGet([int Function(int, int)? compare]) {
    sort(compare);
    return this;
  }
}

extension IntIterableExt on Iterable<int> {
  int get sum => fold(0, (acc, x) => acc + x);

  static Iterable<int> createRange(
      final int startInclusive,
      [final int endExclusive = 2000000000]
      ) => Iterable.generate(endExclusive - startInclusive, (x) => x + startInclusive);

  static Iterable<int> createDownRange(
      final int startInclusive,
      final int endExclusive
      ) => Iterable.generate(startInclusive - endExclusive, (x) => startInclusive - x);
}

extension ComparableByIterableExt<T, V extends Comparable> on Iterable<T> {
  T maxBy(final V Function(T elem) func) {
    var iter = iterator; iter.moveNext();
    var max = iter.current;

    while (iter.moveNext()) {
      if (func(max).compareTo(func(iter.current)) < 0) {
        max = iter.current;
      }
    }

    return max;
  }

  T minBy(final V Function(T elem) func) {
    var iter = iterator; iter.moveNext();
    var max = iter.current;

    while (iter.moveNext()) {
      if (func(max).compareTo(func(iter.current)) > 0) {
        max = iter.current;
      }
    }

    return max;
  }
}

extension ComparableIterExt<T extends Comparable> on Iterable<T> {
  T get max => maxBy((elem) => elem);
  T get min => minBy((elem) => elem);
}

extension IterableExt<T> on Iterable<T> {
  int count(final T predicate) => where((element) => element == predicate).length;
  int countWhere(final bool Function(T it) func) => where((element) => func(element)).length;

  Iterable<T> stepBy(final int step) {
    final list = List<T>.empty(growable: true);
    final thisList = toList();

    for (int i = 0; i < length; i += step) {
      list.add(thisList[i]);
    }

    return list;
  }

  void forEachIndexed(final void Function(int index, T elem) action) {
    final iter = iterator;

    for (int i = 0; i < length; i++) {
      iter.moveNext();
      action(i, iter.current);
    }
  }

  Iterable<T> filterIndexed(final bool Function(int index, T elem) predicate) {
    final list = List<T>.empty(growable: true);
    final thisList = toList();

    for (int i = 0; i < length; i++) {
      final elem = thisList[i];
      if (predicate(i, elem)) {
        list.add(elem);
      }
    }

    return list;
  }

  bool allIndexed(final bool Function(int index, T elem) predicate) {
    final iter = iterator;

    for (int i = 0; i < length; i++) {
      iter.moveNext();
      if (!predicate(i, iter.current)) {
        return false;
      }
    }

    return true;
  }
}

extension ListExt<T> on List<T> {
  List<T> getSorted([final int Function(T x, T y)? compare]) {
    sort(compare);
    return this;
  }

  List<T> without(final T elem) {
    final copy = toList(growable: true);
    copy.remove(elem);
    return copy;
  }

  List<T> withElem(final T elem) {
    final copy = toList(growable: true);
    copy.add(elem);
    return copy;
  }

  List<T> operator-(final T elem) => without(elem);
  List<T> operator+(final T elem) => withElem(elem);
}

extension on String {
  Iterable<int> get digits => codeUnits.map((e) => e - '0'.codeUnitAt(0));

  String repeat(final int amount) {
    String value = "";

    for (var i = 0; i < amount; i++) {
      value += this;
    }

    return value;
  }

  int count(final Pattern patter) => split(patter).length - 1;

  int parseInt() => int.parse(this);
  int? tryParseInt() => int.tryParse(this);
}

extension HighOrderFunctions<T, R> on T {
  R let(final R Function(T it) func) => func(this);

  T also(final R Function(T it) func) {
    func(this);
    return this;
  }

  T? takeIf(final bool Function(T it) func) => func(this) ? this : null;
}

class Pair<F, S> {
  F first;
  S second;
  Pair(this.first, this.second);

  @override
  String toString() => '($first, $second)';

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! Pair<F, S>) return false;
    return first == other.first && second == other.second;
  }
}

final invalid = Pair(0, 1e9.toInt());

Pair<int, int> dfs(
    final int n,
    final int m,
    final List<List<int>> tab,
    final List<List<Pair<int, int>>> dp
) {
  if (n >= tab.length || m < 0) {
    return invalid;
  }

  if (dp[n][m] != invalid) {
    return dp[n][m];
  }

  if ((n == 3 && m >= 6 && m <= 10) || (n == 0 && m == 13) || (n == 7 && m == 2) || (n == 8 && m == 16)) {
    // границы -
    final f = dfs(n, m - 1, tab, dp);
    dp[n][m] = Pair(f.first + tab[n][m], f.second + tab[n][m]);
  } else if ((n == 1 && m == 14) || (n == 8 && m == 3) || (n == 9 && m == 17)) {
    final f = dfs(n + 1, m, tab, dp);
    dp[n][m] = Pair(f.first + tab[n][m], f.second + tab[n][m]);
  } else {
    // нет границ
    final f = dfs(n + 1, m, tab, dp);
    final s = dfs(n, m - 1, tab, dp);
    dp[n][m] = Pair(max(f.first, s.first) + tab[n][m], min(f.second, s.second) + tab[n][m]);
  }

  return dp[n][m];
}
