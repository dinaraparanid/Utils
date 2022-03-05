use std::{
    fmt::Debug,
    fs::File,
    io,
    io::Read,
    iter::FromIterator,
    num::ParseIntError,
    str::{FromStr, SplitWhitespace},
};
 
#[inline]
#[allow(dead_code)]
fn input() -> String {
    let mut input = String::new();
    unsafe {
        io::stdin().read_line(&mut input).unwrap_unchecked();
    }
    input
}
 
#[inline]
#[allow(dead_code)]
fn input_parse<T: FromStr<Err = ParseIntError> + Debug>() -> T {
    let mut input = String::new();
    unsafe {
        io::stdin().read_line(&mut input).unwrap_unchecked();
        input.trim().parse().unwrap_unchecked()
    }
}
 
#[inline]
#[allow(dead_code)]
fn input_container<T, C>() -> C
where
    T: FromStr<Err = ParseIntError> + Debug,
    C: FromIterator<T>,
{
    let mut input = String::new();
    unsafe {
        io::stdin().read_line(&mut input).unwrap_unchecked();
    }
 
    input
        .split_whitespace()
        .map(|x| unsafe { x.parse::<T>().unwrap_unchecked() })
        .collect::<C>()
}
 
#[inline]
#[allow(dead_code)]
fn input_pair<F, S>() -> (F, S)
where
    F: FromStr<Err = ParseIntError> + Debug,
    S: FromStr<Err = ParseIntError> + Debug,
{
    let mut input = String::new();
    unsafe {
        io::stdin().read_line(&mut input).unwrap_unchecked();
    }
 
    let mut input = input.split_whitespace();
 
    unsafe {
        (
            input.next().unwrap().parse().unwrap_unchecked(),
            input.next().unwrap().parse().unwrap_unchecked(),
        )
    }
}
 
#[inline]
#[allow(dead_code)]
fn input_it(mut input: &mut String) -> SplitWhitespace<'_> {
    unsafe {
        io::stdin().read_line(&mut input).unwrap_unchecked();
    }
    input.split_whitespace()
}
 
trait IterExt: std::iter::ExactSizeIterator {
    #[inline]
    fn filter_indexed(&mut self, mut f: impl FnMut(&Self::Item, usize) -> bool) -> Vec<Self::Item> {
        let mut filter = Vec::with_capacity(self.len());
 
        for i in 0..self.len() {
            let nxt = unsafe { self.next().unwrap_unchecked() };
 
            if f(&nxt, i) {
                filter.push(nxt)
            }
        }
 
        filter.shrink_to_fit();
        filter
    }
}
 
impl<T> IterExt for std::slice::Iter<'_, T> {}
 
trait VecExt<T: Ord + Clone> {
    fn sorted_self(self) -> Self;
    fn sorted_cloned(&self) -> Self;
}
 
impl<T: Ord + Clone> VecExt<T> for Vec<T> {
    #[inline]
    fn sorted_self(mut self) -> Self {
        self.sort();
        self
    }
 
    #[inline]
    fn sorted_cloned(&self) -> Self {
        Vec::from(self.as_slice()).sorted_self()
    }
}
