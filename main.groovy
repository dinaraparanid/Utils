import groovy.transform.Field

final boolean arrow(final boolean a, final boolean b) { return !(a && !b) }

final String repeat(final CharSequence string, final int count) {
    final builder = new StringBuilder(string.size() * count)
    1.upto(count) { builder.append(string) }
    return builder.toString()
}

@Field
final Tuple2<Integer, Integer> invalid = new Tuple2<>(1e9.toInteger(), 0)

final Tuple2<Integer, Integer> dfs(
        final List<List<Integer>> table,
        final List<List<Tuple2<Integer, Integer>>> dp,
        final int n = table.size() - 1,
        final int m = table.size() - 1
) {
    if (n < 0 || m < 0)
        return invalid

    if (dp[n][m] != invalid)
        return dp[n][m]

    final isOk = { final int x, final int y ->
        (x >= 1 && x <= 6 && y >= 1 && y <= 3) ||
                (x == 3 && y >= 8 && y <= 15) ||
                (x >= 9 && x <= 16 && y == 5) ||
                (x >= 9 && x <= 14 && y >= 11 && y <= 15)
    }

    final first = dfs(table, dp, n - 1, m)
    final second = dfs(table, dp, n, m - 1)
    final third = isOk(n - 1, m - 1) ? invalid : dfs(table, dp, n - 1, m - 1)

    dp[n][m] = new Tuple2<>(
            [first.first, second.first, third.first].min() + table[n][m],
            [first.second, second.second, third.second].max() + table[n][m]
    )

    return dp[n][m]
}

final int _rec(
        final int cnt = 0,
        final List<Integer> nums = new ArrayList<>(),
        final int n = 2,
        final int end = 25,
        final List<Integer> contains = [17],
        final List<Integer> notContains = [22]
) {
    if (n >= end)
        return cnt + (n == end &&
                contains.every { nums.contains(it) } &&
                !notContains.any { nums.contains(it) } ? 1 : 0)

    final plus = nums + n
    return _rec(cnt, plus, n + 1) + _rec(cnt, plus, n + 3) + _rec(cnt, plus, n * n)
}

final List<Integer> getDivs(final int n, final List<Integer> exclusive = [1, n]) {
    final list = new ArrayList()

    for (int i = 1; i * i <= n; i++) {
        if (n % i == 0 && !exclusive.contains(i)) {
            list.add(i)

            if (i * i != n && !exclusive.contains(n.intdiv(i)))
                list.add(n.intdiv(i))
        }
    }

    return list
}
