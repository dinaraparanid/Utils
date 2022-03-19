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
            @NotNull final List<List<Integer>> table,
            @NotNull final List<List<Pair<Integer, Integer>>> dp,
            final int n,
            final int m
    ) {
        if (n < 0 || m < 0)
            return INVALID;

        if (!dp.get(n).get(m).equals(INVALID))
            return dp.get(n).get(m);

        if (n == 6 && m >= 4 && m <= 9) {
            final var f = dfs(table, dp, n, m - 1);
            dp.get(n).set(m, new Pair<>(f.first + table.get(n).get(m), f.second + table.get(n).get(m)));
        } else {
            final var f = dfs(table, dp, n, m - 1);
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
            @NotNull final List<Function<Integer, Integer>> operations,
            @NotNull final List<Integer> recList
    ) {
        if (n >= max) {
            if (n == max && recList.containsAll(contains) && !recList.containsAll(notContains))
                _recCnt++;

            return;
        }

        final var plus = plusElems(recList, n);
        operations.forEach((act) -> rec(act.apply(n), max, contains, notContains, operations, plus));
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
    private static final <T, R> List<R> scan(
            @NotNull final R start,
            @NotNull final Collection<? extends T> list,
            @NotNull final BiFunction<? super R, T, ? extends R> accumulator
    ) {
        final var resultList = new ArrayList<R>(list.size());
        var acc = start;

        for (final var x : list) {
            acc = accumulator.apply(acc, x);
            resultList.add(acc);
        }

        return resultList;
    }

    @NotNull
    private static final <T, R> List<R> scanWithFirst(
            @NotNull final R start,
            @NotNull final Collection<? extends T> list,
            @NotNull final BiFunction<? super R, T, ? extends R> accumulator
    ) {
        final var resultList = new ArrayList<R>(list.size() + 1);
        var acc = start;
        resultList.add(start);

        for (final var x : list) {
            acc = accumulator.apply(acc, x);
            resultList.add(acc);
        }

        return resultList;
    }

    @FunctionalInterface
    private interface TripleFunction<F, S, T, R> {
        R apply(final F first, final S second, final T third);
    }

    @NotNull
    private static final <T, R> List<R> scanIndexed(
            @NotNull final R start,
            @NotNull final List<T> list,
            @NotNull final TripleFunction<R, T, Integer, R> accumulator
    ) {
        final var resultList = new ArrayList<R>(list.size());
        var acc = start;

        for (int i = 0; i < list.size(); i++) {
            acc = accumulator.apply(acc, list.get(i), i);
            resultList.add(acc);
        }

        return resultList;
    }

    @NotNull
    private static final <T, R> List<R> scanWithFirstIndexed(
            @NotNull final R start,
            @NotNull final List<T> list,
            @NotNull final TripleFunction<R, T, Integer, R> accumulator
    ) {
        final var resultList = new ArrayList<R>(list.size() + 1);
        var acc = start;
        resultList.add(start);

        for (int i = 0; i < list.size(); i++) {
            acc = accumulator.apply(acc, list.get(i), i);
            resultList.add(acc);
        }

        return resultList;
    }
}
