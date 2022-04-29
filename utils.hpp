#include <cstdio>
#include <iterator>
#include <vector>
#include <array>
#include <numeric>
#include <string>
#include <functional>
#include <algorithm>

inline bool arrow(const bool x, const bool y) {
    return !(x && !y);
}

namespace utils {
    template <class T> inline std::vector<T> filter(const std::vector<T>& vec, std::function<bool(const T&)> predicate) {
        std::vector<T> cpy(std::copy(vec));
        const auto end = std::remove_if(cpy.begin(), cpy.end(), predicate);
        std::vector<T> mv(std::distance(cpy.begin(), end));
        std::move(cpy.begin(), end, mv.begin());
        return mv;
    }

    template <class T, class R> inline std::vector<R> map(const std::vector<T>& vec, std::function<R(const T&)> transformator) {
        std::vector<R> cpy(vec.size());
        std::transform(vec.begin(), vec.end(), cpy.begin(), transformator);
        return cpy;
    }

    template <class T, class R> inline std::vector<R> scan(const std::vector<T>& vec, const R& start, std::function<R(const R&, const T&)> accumulator) {
        std::vector<R> cpy(vec.size() + 1); cpy[0] = start;

        for (int i = 1; i < cpy.size(); i++)
            cpy[i] = accumulator(cpy[i - 1], vec[i - 1]);

        return cpy;
    }
}
