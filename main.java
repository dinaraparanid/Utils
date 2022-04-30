public final class Main {

    private static final boolean arrow(final boolean f, final boolean s) {
        return !(f && !s);
    }

    private static final int countString(@NotNull final CharSequence s, final char c) {
        var cnt = 0;

        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == c)
                cnt++;

        return cnt;
    }

    private static final int countString(@NotNull final String s, @NotNull final String c) {
        var pattern = Pattern.compile(c);
        var matcher = pattern.matcher(s);

        int cnt = 0;
        while (matcher.find()) cnt++;
        return cnt;
    }

    private static final char maxLetterInString(@NotNull final CharSequence s) {
        final var charArr = s.toString().toCharArray();
        final var maxLetterMap = new HashMap<Character, Integer>();

        for (final var c : charArr)
            maxLetterMap.put(c, maxLetterMap.getOrDefault(c, 0) + 1);

        final int maxVal = maxLetterMap.values().stream().max(Comparator.comparingInt((x) -> x)).get();
        char c = ' ';
        final var entrySet = maxLetterMap.entrySet();

        for (final var p : entrySet) {
            if (p.getValue() == maxVal) {
                c = p.getKey();
                break;
            }
        }

        return c;
    }

    @NotNull
    private static final <T> List<T> minusElems(@NotNull final List<T> list, @NotNull final T... elems) {
        final var clone = new ArrayList<>(list);
        list.removeAll(Arrays.stream(elems).collect(Collectors.toList()));
        return clone;
    }

    @NotNull
    private static final <T> List<T> plusElems(@NotNull final List<T> list, @NotNull final T... elems) {
        final var clone = new ArrayList<>(list);
        clone.addAll(Arrays.stream(elems).collect(Collectors.toList()));
        return clone;
    }

    @NotNull
    private static final <T> List<T> listOf(@NotNull final T... elems) {
        final var list = new ArrayList<T>(elems.length);

        for (final var e : elems)
            list.add(e);

        return list;
    }

    private static final boolean areListsEqual(@NotNull final List<? extends Object> first, @NotNull final List<? extends Object> second) {
        if (first.size() != second.size())
            return false;

        for (int i = 0; i < first.size(); i++)
            if (!first.get(i).equals(second.get(i)))
                return false;

        return true;
    }

    private static final class Pair<F, S> {
        final F first;
        final S second;

        Pair(final F f, final S s) {
            first = f;
            second = s;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @NotNull
        @Contract(pure = true)
        @Override
        public String toString() {
            return "Pair{" +
                    "first=" + first +
                    ", second=" + second +
                    '}';
        }
    }

    @NotNull
    private static final <T> List<T> toList(@NotNull final Pair<T, T> p) {
        return listOf(p.first, p.second);
    }

    private static final Pair<Integer, Integer> INVALID = new Pair<>(1000000000, 0);

    @NotNull
    private static Pair<Integer, Integer> dfs(
            @NotNull final List<? extends List<Integer>> table,
            @NotNull final List<? extends List<Pair<Integer, Integer>>> dp,
            final int n,
            final int m
    ) {
        if (n < 0 || m < 0 || n >= table.size() || m >= table.size())
            return INVALID;

        if (!dp.get(n).get(m).equals(INVALID))
            return dp.get(n).get(m);

        if ((m == 2 && n >= 2 && n <= 6) || (m == 5 && n >= 11 && n <= 14)) {
            final var s = dfs(table, dp, n - 1, m);
            dp.get(n).set(m, new Pair<>(s.first + table.get(n).get(m), s.second + + table.get(n).get(m)));
        } else {
            final var f = dfs(table, dp, n, m + 1);
            final var s = dfs(table, dp, n - 1, m);
            dp.get(n).set(m, new Pair<>(
                    Math.min(f.first, s.first) + table.get(n).get(m),
                    Math.max(f.second, s.second) + table.get(n).get(m)
            ));
        }

        return dp.get(n).get(m);
    }

    private static int _recCnt = 0;

    private static final void rec(
            final int n,
            final int max,
            @NotNull final List<Integer> contains,
            @NotNull final List<Integer> notContains,
            @NotNull final List<? extends Function<Integer, Integer>> operations,
            @NotNull final List<Pair<Integer, Predicate<Integer>>> neededOperationsAmount,
            @NotNull final List<? super Integer> recList,
            @NotNull final List<Integer> countOperations
    ) {
        if (n >= max) {
            if (n == max
                    && (contains.isEmpty() || recList.containsAll(contains))
                    && (notContains.isEmpty() || !recList.containsAll(notContains)
                    &&  neededOperationsAmount.stream().allMatch(p -> p.second.test(countOperations.get(p.first))))
            ) _recCnt++;

            return;
        }

        final var plus = plusElems(recList, n);

        for (int i = 0; i < operations.size(); i++) {
            final var newCntOperation = new ArrayList<>(countOperations);
            newCntOperation.set(i, newCntOperation.get(i) + 1);

            rec(
                    operations.get(i).apply(n),
                    max,
                    contains,
                    notContains,
                    operations,
                    neededOperationsAmount,
                    plus,
                    newCntOperation
            );
        }
    }

    private static final void rec(
            final int n,
            final int max,
            @NotNull final List<Integer> contains,
            @NotNull final List<Integer> notContains,
            @NotNull final List<? extends Function<Integer, Integer>> operations,
            @NotNull final List<Pair<Integer, Predicate<Integer>>> neededOperationsAmount
    ) {
        rec(
                n,
                max,
                contains,
                notContains,
                operations,
                neededOperationsAmount,
                new ArrayList<>(),
                rangeList(0, operations.size() + 1)
                        .stream()
                        .map(x -> 0)
                        .collect(Collectors.toList())
        );
    }

    @Contract(pure = true)
    private static final boolean isPalindrom(@NotNull final String string) {
        final var s = string.toCharArray();
        final var endPoint = s.length / 2;

        for (int i = 0; i < endPoint; i++)
            if (s[i] != s[s.length - 1 - i])
                return false;

        return true;
    }

    private static final boolean isPalindrom(final int n) {
        final var s = Integer.toString(n).toCharArray();
        final var endPoint = s.length / 2;

        for (int i = 0; i < endPoint; i++)
            if (s[i] != s[s.length - 1 - i])
                return false;

        return true;
    }

    @NotNull
    private static final <T, R> R fold(
            @NotNull final R start,
            @NotNull final Iterable<? extends T> iterable,
            @NotNull final BiFunction<? super R, T, ? extends R> accumulator
    ) {
        var acc = start;

        for (final var x : iterable)
            acc = accumulator.apply(acc, x);

        return acc;
    }

    @NotNull
    private static final <T, R> List<R> scan(
            @NotNull final R start,
            @NotNull final Collection<? extends T> collection,
            @NotNull final BiFunction<? super R, T, ? extends R> accumulator
    ) {
        final var resultList = new ArrayList<R>(collection.size());
        var acc = start;

        for (final var x : collection) {
            acc = accumulator.apply(acc, x);
            resultList.add(acc);
        }

        return resultList;
    }

    @NotNull
    private static final <T, R> List<R> scanWithFirst(
            @NotNull final R start,
            @NotNull final Collection<? extends T> collection,
            @NotNull final BiFunction<? super R, T, ? extends R> accumulator
    ) {
        final var resultList = new ArrayList<R>(collection.size() + 1);
        var acc = start;
        resultList.add(start);

        for (final var x : collection) {
            acc = accumulator.apply(acc, x);
            resultList.add(acc);
        }

        return resultList;
    }

    @FunctionalInterface
    private interface DoubleFunction<F, S, R> {
        R apply(final F first, final S second);
    }

    @FunctionalInterface
    private interface TripleFunction<F, S, T, R> {
        R apply(final F first, final S second, final T third);
    }

    @FunctionalInterface
    private interface FourFunction<A, B, C, D, R> {
        R apply(final A first, final B second, final C third, final D fourth);
    }

    @NotNull
    private static final <T, R> R foldIndexed(
            @NotNull final R start,
            @NotNull final Iterable<? extends T> iterable,
            @NotNull final TripleFunction<? super R, T, ? super Integer, ? extends R> accumulator
    ) {
        var acc = start;
        var i = 0;

        for (final var x : iterable)
            acc = accumulator.apply(acc, x, i++);

        return acc;
    }

    @NotNull
    private static final <T, R> List<R> scanIndexed(
            @NotNull final R start,
            @NotNull final Collection<? extends T> collection,
            @NotNull final TripleFunction<? super R, T, ? super Integer, ? extends R> accumulator
    ) {
        final var resultList = new ArrayList<R>(collection.size());
        var acc = start;
        var i = 0;

        for (final var x : collection) {
            acc = accumulator.apply(acc, x, i++);
            resultList.add(acc);
        }

        return resultList;
    }

    @NotNull
    private static final <T, R> List<R> scanWithFirstIndexed(
            @NotNull final R start,
            @NotNull final Collection<? extends T> collection,
            @NotNull final TripleFunction<? super R, T, ? super Integer, ? extends R> accumulator
    ) {
        final var resultList = new ArrayList<R>(collection.size() + 1);
        var acc = start;
        resultList.add(start);
        var i = 0;

        for (final var x : collection) {
            acc = accumulator.apply(acc, x, i++);
            resultList.add(acc);
        }

        return resultList;
    }

    @NotNull
    private static final <T> Optional<T> find(
            @NotNull final Iterable<? extends T> collection,
            @NotNull final Predicate<? super T> predicate
    ) {
        for (final var x : collection)
            if (predicate.test(x))
                return Optional.of(x);

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<Pair<T, Integer>> findWithIndex(
            @NotNull final Iterable<? extends T> iterable,
            @NotNull final Predicate<? super T> predicate
    ) {
        var i = 0;

        for (final var x : iterable) {
            if (predicate.test(x))
                return Optional.of(new Pair<>(x, i));

            i++;
        }

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<T> findIndexed(
            @NotNull final Iterable<? extends T> iterable,
            @NotNull final DoubleFunction<? super T, ? super Integer, Boolean> predicate
    ) {
        var i = 0;

        for (final var x : iterable)
            if (predicate.apply(x, i++))
                return Optional.of(x);

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<Pair<T, Integer>> findWithIndexIndexed(
            @NotNull final Iterable<? extends T> iterable,
            @NotNull final DoubleFunction<? super T, ? super Integer, Boolean> predicate
    ) {
        var i = 0;

        for (final var x : iterable)
            if (predicate.apply(x, i++))
                return Optional.of(new Pair<>(x, i));

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<T> findLast(
            @NotNull final List<? extends T> list,
            @NotNull final Predicate<? super T> predicate
    ) {
        final var reverseIter = list.listIterator(list.size());

        for (int i = list.size() - 1; i >= 0; i--) {
            final var prev = reverseIter.previous();

            if (predicate.test(prev))
                return Optional.of(prev);
        }

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<T> findLast(
            @NotNull final Deque<? extends T> deque,
            @NotNull final Predicate<? super T> predicate
    ) {
        final var reverseIter = deque.descendingIterator();

        for (int i = deque.size() - 1; i >= 0; i--) {
            final var prev = reverseIter.next();

            if (predicate.test(prev))
                return Optional.of(prev);
        }

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<Pair<T, Integer>> findLastWithIndex(
            @NotNull final List<? extends T> list,
            @NotNull final Predicate<? super T> predicate
    ) {
        final var reverseIter = list.listIterator(list.size());

        for (int i = list.size() - 1; i >= 0; i--) {
            final var prev = reverseIter.previous();

            if (predicate.test(prev))
                return Optional.of(new Pair<>(prev, i));
        }

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<Pair<T, Integer>> findLastWithIndex(
            @NotNull final Deque<? extends T> deque,
            @NotNull final Predicate<? super T> predicate
    ) {
        final var reverseIter = deque.descendingIterator();

        for (int i = deque.size() - 1; i >= 0; i--) {
            final var prev = reverseIter.next();

            if (predicate.test(prev))
                return Optional.of(new Pair<>(prev, i));
        }

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<T> findLastIndexed(
            @NotNull final List<? extends T> list,
            @NotNull final DoubleFunction<? super T, ? super Integer, Boolean> predicate
    ) {
        final var reverseIter = list.listIterator(list.size());

        for (int i = list.size() - 1; i >= 0; i--) {
            final var prev = reverseIter.previous();

            if (predicate.apply(prev, i))
                return Optional.of(prev);
        }

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<Pair<T, Integer>> findLastWithIndexIndexed(
            @NotNull final List<? extends T> list,
            @NotNull final DoubleFunction<? super T, ? super Integer, Boolean> predicate
    ) {
        final var reverseIter = list.listIterator(list.size());

        for (int i = list.size() - 1; i >= 0; i--) {
            final var prev = reverseIter.previous();

            if (predicate.apply(prev, i))
                return Optional.of(new Pair<>(prev, i));
        }

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<T> findLastIndexed(
            @NotNull final Deque<? extends T> deque,
            @NotNull final DoubleFunction<? super T, ? super Integer, Boolean> predicate
    ) {
        final var reverseIter = deque.descendingIterator();

        for (int i = deque.size() - 1; i >= 0; i--) {
            final var prev = reverseIter.next();

            if (predicate.apply(prev, i))
                return Optional.of(prev);
        }

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<T> findLastWithIndexIndexed(
            @NotNull final Deque<? extends T> deque,
            @NotNull final DoubleFunction<? super T, ? super Integer, Boolean> predicate
    ) {
        final var reverseIter = deque.descendingIterator();

        for (int i = deque.size() - 1; i >= 0; i--) {
            final var prev = reverseIter.next();

            if (predicate.apply(prev, i))
                return Optional.of(prev);
        }

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<Integer> indexOf(
            @NotNull final Iterable<? extends T> iterable,
            @NotNull final Predicate<? super T> predicate
    ) {
        var i = 0;

        for (final var x : iterable) {
            if (predicate.test(x))
                return Optional.of(i);

            i++;
        }

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<Integer> indexOfLast(
            @NotNull final List<? extends T> list,
            @NotNull final Predicate<? super T> predicate
    ) {
        for (int i = list.size() - 1; i >= 0; i--)
            if (predicate.test(list.get(i)))
                return Optional.of(i);

        return Optional.empty();
    }

    @NotNull
    private static final <T> Optional<Integer> indexOfLast(
            @NotNull final Deque<? extends T> deque,
            @NotNull final Predicate<? super T> predicate
    ) {
        final var iter = deque.descendingIterator();

        for (int i = deque.size() - 1; i >= 0; i--)
            if (predicate.test(iter.next()))
                return Optional.of(i);

        return Optional.empty();
    }

    @NotNull
    private static final List<Integer> getDivs(final int n) {
        final var divs = new ArrayList<Integer>();

        for (int i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                divs.add(i);

                if (i * i != n)
                    divs.add(n / i);
            }
        }

        return divs;
    }

    @NotNull
    private static final List<Integer> getDivs(
            final int n,
            final Predicate<? super Integer> predicate
    ) {
        final var divs = new ArrayList<Integer>();

        for (int i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                if (predicate.test(i))
                    divs.add(i);

                if (i * i != n && predicate.test(n / i))
                    divs.add(n / i);
            }
        }

        return divs;
    }

    @NotNull
    private static final <T> boolean anyIndexed(
            @NotNull final Iterable<T> iterable,
            @NotNull final DoubleFunction<T, Integer, Boolean> predicate
    ) {
        var i = 0;

        for (final var x : iterable)
            if (predicate.apply(x, i++))
                return true;

        return false;
    }

    @NotNull
    private static final <T> boolean allIndexed(
            @NotNull final Iterable<T> iterable,
            @NotNull final DoubleFunction<T, Integer, Boolean> predicate
    ) {
        var i = 0;

        for (final var x : iterable)
            if (!predicate.apply(x, i++))
                return false;

        return true;
    }

    @NotNull
    private static final <T> Optional<T> last(@NotNull final List<? extends T> list) {
        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list.get(list.size() - 1));
    }

    @NotNull
    private static final List<Integer> rangeList(final int startInclusive, final int finishExclusive) {
        final var list = new ArrayList<Integer>(finishExclusive - startInclusive);

        for (var i = startInclusive; i < finishExclusive; i++)
            list.add(i);

        return list;
    }

    private enum FifteenCondition { MAX, MIN }

    private static final int fifteenRange(
            final int pStart,
            final int pFinish,
            final int qStart,
            final int qFinish,
            @NotNull final FourFunction<List<Integer>, List<Integer>, List<Integer>, Integer, Boolean> predicate,
            @NotNull final FifteenCondition condition
    ) {
        final var pArr = rangeList(pStart, pFinish + 1);
        final var qArr = rangeList(qStart, qFinish + 1);

        final var borders = listOf(pStart, pFinish, qStart, qFinish).stream().sorted().collect(Collectors.toList());
        var ans = condition.equals(FifteenCondition.MAX) ? 0 : 1000000000;

        for (int i = 0; i < borders.size(); i++) {
            for (int q = i; q < borders.size(); q++) {
                final var aArr = rangeList(borders.get(i), borders.get(q) + 1);
                var isOk = true;

                for (int x = 0; x < 100; x++) {
                    final var v = arrow(qArr.contains(x), (pArr.contains(x) == qArr.contains(x)) || (arrow(!pArr.contains(x), aArr.contains(x))));

                    if (!v) {
                        isOk = false;
                        break;
                    }
                }

                final var len = aArr.size();

                if (isOk && ((condition.equals(FifteenCondition.MAX) && len < ans) || (condition.equals(FifteenCondition.MIN) && len > ans)))
                    ans = len;
            }
        }

        return ans;
    }

    private static final long sumNum(@NotNull final String s) {
        final var arr = s.toCharArray();
        var sum = 0L;

        for (final var c : arr)
            sum += Integer.parseInt(Character.toString(c));

        return sum;
    }

    @NotNull
    private static final Stream<Character> toStream(@NotNull final String s) {
        return s.chars().mapToObj(c -> (char) c);
    }

    @NotNull
    private static final String toString(@NotNull final List<Character> list) {
        return fold("", list, (@NonNls final var acc, final var x) -> acc + x);
    }

    private static final <T> long count(@NotNull final List<T> list, @NotNull final T elem) {
        return list.stream().filter(e -> e.equals(elem)).count();
    }

    private static final <T> void forEachIndexed(@NotNull final List<? extends T> list, DoubleFunction<? super T, ? super Integer, Void> operation) {
        var ind = 0;

        for (final var e : list)
            operation.apply(e, ind++);
    }

    @NotNull
    private static final <T> List<T> filterIndexed(
            @NotNull final List<? extends T> list,
            @NotNull DoubleFunction<? super T, ? super Integer, Boolean> predicate
    ) {
        final var filtered = new ArrayList<T>();
        var ind = 0;

        for (final var e : list)
            if (predicate.apply(e, ind++))
                filtered.add(e);

        return filtered;
    }
}
