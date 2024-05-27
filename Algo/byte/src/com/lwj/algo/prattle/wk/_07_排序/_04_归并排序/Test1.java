package com.lwj.algo.prattle.wk._07_排序._04_归并排序;

/**
 * create by lwj on 2019/2/11
 */
public class Test1 {
    //采用分治思想   分治算法一般使用递归来实现
    //通过将一个无序数组分成两个子数组，然后将排序后的子数组再进行合并成一个数组，
    //子数组的排序用同样的方式，分成更小的两个子子数组，排序后合并。。
    //分为子数组的过程叫做 归   两个子数组的合并叫做 并  这样就实现了归并排序。
    //平均时间复杂度  最坏时间复杂度      空间复杂度    是否稳定
    //    O(nlogn)       O(nlogn)            O(n)         稳定
    /*
    递推公式：
    merge_sort(p…r) = merge(merge_sort(p…q), merge_sort(q+1…r))

    终止条件：
    p >= r 不用再继续分解

    // 归并排序算法, A 是数组，n 表示数组大小
    merge_sort(A, n) {
        merge_sort_c(A, 0, n-1)
    }

    // 递归调用函数
    merge_sort_c(A, p, r) {
        // 递归终止条件
        if p >= r  then return

                // 取 p 到 r 之间的中间位置 q
                q = (p+r) / 2
        // 分治递归
        merge_sort_c(A, p, q)
        merge_sort_c(A, q+1, r)
        // 将 A[p...q] 和 A[q+1...r] 合并为 A[p...r]
        merge(A[p...r], A[p...q], A[q+1...r])
    }

    merge(A[p...r], A[p...q], A[q+1...r]) {
  var i := p，j := q+1，k := 0 // 初始化变量 i, j, k
  var tmp := new array[0...r-p] // 申请一个大小跟 A[p...r] 一样的临时数组
  while i<=q AND j<=r do {
    if A[i] <= A[j] {
      tmp[k++] = A[i++] // i++ 等于 i:=i+1
    } else {
      tmp[k++] = A[j++]
    }
  }

  // 判断哪个子数组中有剩余的数据
  var start := i，end := q
  if j<=r then start := j, end:=r

  // 将剩余的数据拷贝到临时数组 tmp
  while start <= end do {
    tmp[k++] = A[start++]
  }

  // 将 tmp 中的数组拷贝回 A[p...r]
  for i:=0 to r-p do {
    A[p+i] = tmp[i]
  }
}

    */

}
