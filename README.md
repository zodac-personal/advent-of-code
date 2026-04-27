# Advent Of Code: Java Edition

![2024](https://img.shields.io/badge/2024%20⭐-38-yellow)
![2023](https://img.shields.io/badge/2023%20⭐-32-yellow)
![2022](https://img.shields.io/badge/2022%20⭐-28-yellow)
![2021](https://img.shields.io/badge/2021%20⭐-19-orange)
![2020](https://img.shields.io/badge/2020%20⭐-0-red)
![2019](https://img.shields.io/badge/2019%20⭐-0-red)
![2018](https://img.shields.io/badge/2018%20⭐-4-orange)
![2017](https://img.shields.io/badge/2017%20⭐-0-red)
![2016](https://img.shields.io/badge/2016%20⭐-0-red)
![2015](https://img.shields.io/badge/2015%20⭐-50-green)

## Overview

This project includes solutions to the [Advent of Code](https://adventofcode.com/) programming challenges.
The solutions are written in Java.

## Purpose

This is not an attempt to '[code golf](https://en.wikipedia.org/wiki/Code_golf)' solutions; Java is already a verbose language, so it's not the right
tool for that sort of challenge.

Instead, I aim to create a project with re-usable algorithms and function, that can be re-used as needed rather than having to import large
dependencies like [Apache Commons](https://commons.apache.org/) or [Google Guava](https://github.com/google/guava).

I'm also not aiming for extreme performance. Readability and re-usability are the main goals here, so sometimes I'll use a solution with Collections
and generics rather than a more performant array-based solution. As long as they complete in a
reasonable time[™](https://en.wikipedia.org/wiki/Reasonable_time), I'll use the more readable and generic option.

## How To View Solutions

The solutions for a given year are saved in their respective module. The solution for each day is run as a unit test, where a puzzle input is read
and passed into the actual solution class. Sometimes some minor mapping of the input to a POJO will be done in the test set-up. The result from the
solution is then asserted in the unit test.

The test module will include assertions for the example from the [Advent of Code](https://adventofcode.com/) challenge, and part 1 and 2 using your
own input files.

## Input Files

As requested by the creator of [Advent of Code](https://adventofcode.com/), no individual input files are included in this repository. My specific
inputs are included as a git submodule in the `advent-of-code-inputs` directory. Feel free to provide your own inputs in that directory. Each year's
inputs should be placed in a module matching the module name in the source code. For example, for **2015, Day 01**, its input should be available
at: `advent-of-code/advent-of-code-inputs/2015/day01.txt`.

I use the [create.sh script](./create.sh) to generate a template for my solution files, and it also retrieves the input for the problem and commits it
to the git submodule. This requires an environment variable `AOC_COOKIE` to be set, with your own [Advent of Code](https://adventofcode.com/) cookie.

My inputs can be loaded with:

```shell
git submodule sync
git submodule update --init --recursive
```

## License

The source code is released under the [BSD Zero Clause License](https://opensource.org/licenses/0BSD).

## Progress And Ranks

<details open>
    <summary>2024 Results</summary>

```mermaid
---
config:
    xyChart:
        width: 900
        height: 400
    themeVariables:
        xyChart:
            titleColor: "#F0F76F"
            xAxisTitleColor: "#F0F76F"
            yAxisTitleColor: "#F0F76F"
---
xychart-beta
    title "2024 Results"
    x-axis "Day" [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25]
    y-axis "Ranking" 60000 --> 100
    %% (For reference) line "P1" [2369, 19045, 6666, 6532, 6528, 1686, 5892, 57893, 58016, 2066, 15266, 45080, 42105, 40754, 37374, 60000, 60000, 22587, 7778, 1890, 21025, 60000, 60000, 60000, 60000]
    line "P2" [2112, 16440, 4855, 4501, 5066, 2576, 5164, 55741, 50640, 5784, 41294, 35116, 37422, 37375, 29170, 60000, 60000, 21997, 6372, 11505, 19375, 60000, 60000, 60000, 60000]
```

| Day                                            |   Part 1 |   Part 2 |
|:-----------------------------------------------|---------:|---------:|
| [Day 1](https://adventofcode.com/2024/day/1)   |  2,369 ⭐ |  2,112 ⭐ |
| [Day 2](https://adventofcode.com/2024/day/2)   | 19,045 ⭐ | 16,440 ⭐ |
| [Day 3](https://adventofcode.com/2024/day/3)   |  6,666 ⭐ |  4,855 ⭐ |
| [Day 4](https://adventofcode.com/2024/day/4)   |  6,532 ⭐ |  4,501 ⭐ |
| [Day 5](https://adventofcode.com/2024/day/5)   |  6,528 ⭐ |  5,066 ⭐ |
| [Day 6](https://adventofcode.com/2024/day/6)   |  1,686 ⭐ |  2,576 ⭐ |
| [Day 7](https://adventofcode.com/2024/day/7)   |  5,892 ⭐ |  5,164 ⭐ |
| [Day 8](https://adventofcode.com/2024/day/8)   | 57,893 ⭐ | 55,741 ⭐ |
| [Day 9](https://adventofcode.com/2024/day/9)   | 58,016 ⭐ | 50,640 ⭐ |
| [Day 10](https://adventofcode.com/2024/day/10) |  2,066 ⭐ |  5,784 ⭐ |
| [Day 11](https://adventofcode.com/2024/day/11) | 15,266 ⭐ | 41,294 ⭐ |
| [Day 12](https://adventofcode.com/2024/day/12) | 45,080 ⭐ | 35,116 ⭐ |
| [Day 13](https://adventofcode.com/2024/day/13) | 42,105 ⭐ | 37,422 ⭐ |
| [Day 14](https://adventofcode.com/2024/day/14) | 40,754 ⭐ | 37,375 ⭐ |
| [Day 15](https://adventofcode.com/2024/day/15) | 37,374 ⭐ | 29,170 ⭐ |
| [Day 16](https://adventofcode.com/2024/day/16) |          |          |
| [Day 17](https://adventofcode.com/2024/day/17) |          |          |
| [Day 18](https://adventofcode.com/2024/day/18) | 22,587 ⭐ | 21,997 ⭐ |
| [Day 19](https://adventofcode.com/2024/day/19) |  7,778 ⭐ |  6,372 ⭐ |
| [Day 20](https://adventofcode.com/2024/day/20) |  1,890 ⭐ | 11,505 ⭐ |
| [Day 21](https://adventofcode.com/2024/day/21) | 21,025 ⭐ | 19,375 ⭐ |
| [Day 22](https://adventofcode.com/2024/day/22) |          |          |
| [Day 23](https://adventofcode.com/2024/day/23) |          |          |
| [Day 24](https://adventofcode.com/2024/day/24) |          |          |
| [Day 25](https://adventofcode.com/2024/day/25) |          |          |

</details>

<details>
    <summary>2023 Results</summary>

| Day                                            |   Part 1 |   Part 2 |
|:-----------------------------------------------|---------:|---------:|
| [Day 1](https://adventofcode.com/2023/day/1)   |  4,216 ⭐ |  6,251 ⭐ |
| [Day 2](https://adventofcode.com/2023/day/2)   |    697 ⭐ |    555 ⭐ |
| [Day 3](https://adventofcode.com/2023/day/3)   |  7,815 ⭐ |  5,683 ⭐ |
| [Day 4](https://adventofcode.com/2023/day/4)   |  1,382 ⭐ |  4,192 ⭐ |
| [Day 5](https://adventofcode.com/2023/day/5)   |  5,526 ⭐ | 13,381 ⭐ |
| [Day 6](https://adventofcode.com/2023/day/6)   | 20,700 ⭐ | 19,631 ⭐ |
| [Day 7](https://adventofcode.com/2023/day/7)   | 62,595 ⭐ | 57,325 ⭐ |
| [Day 8](https://adventofcode.com/2023/day/8)   | 58,501 ⭐ | 46,949 ⭐ |
| [Day 9](https://adventofcode.com/2023/day/9)   |  3,777 ⭐ |  3,923 ⭐ |
| [Day 10](https://adventofcode.com/2023/day/10) | 15,633 ⭐ |  9,375 ⭐ |
| [Day 11](https://adventofcode.com/2023/day/11) | 12,279 ⭐ | 14,530 ⭐ |
| [Day 12](https://adventofcode.com/2023/day/12) |  3,335 ⭐ |  5,223 ⭐ |
| [Day 13](https://adventofcode.com/2023/day/13) |  4,432 ⭐ |  7,987 ⭐ |
| [Day 14](https://adventofcode.com/2023/day/14) |  2,890 ⭐ |  5,790 ⭐ |
| [Day 15](https://adventofcode.com/2023/day/15) |  3,486 ⭐ |  3,046 ⭐ |
| [Day 16](https://adventofcode.com/2023/day/16) |  2,697 ⭐ |  2,557 ⭐ |
| [Day 17](https://adventofcode.com/2023/day/17) |          |          |
| [Day 18](https://adventofcode.com/2023/day/18) |          |          |
| [Day 19](https://adventofcode.com/2023/day/19) |          |          |
| [Day 20](https://adventofcode.com/2023/day/20) |          |          |
| [Day 21](https://adventofcode.com/2023/day/21) |          |          |
| [Day 22](https://adventofcode.com/2023/day/22) |          |          |
| [Day 23](https://adventofcode.com/2023/day/23) |          |          |
| [Day 24](https://adventofcode.com/2023/day/24) |          |          |
| [Day 25](https://adventofcode.com/2023/day/25) |          |          |

</details>

<details>
    <summary>2022 Results</summary>

| Day                                            |    Part 1 |    Part 2 |
|:-----------------------------------------------|----------:|----------:|
| [Day 1](https://adventofcode.com/2022/day/1)   | 181,122 ⭐ | 174,520 ⭐ |
| [Day 2](https://adventofcode.com/2022/day/2)   | 146,753 ⭐ | 140,513 ⭐ |
| [Day 3](https://adventofcode.com/2022/day/3)   | 112,894 ⭐ | 107,699 ⭐ |
| [Day 4](https://adventofcode.com/2022/day/4)   |  87,474 ⭐ |  85,471 ⭐ |
| [Day 5](https://adventofcode.com/2022/day/5)   |   9,071 ⭐ |   8,050 ⭐ |
| [Day 6](https://adventofcode.com/2022/day/6)   |   2,913 ⭐ |   3,596 ⭐ |
| [Day 7](https://adventofcode.com/2022/day/7)   |   9,084 ⭐ |   7,992 ⭐ |
| [Day 8](https://adventofcode.com/2022/day/8)   |   4,751 ⭐ |   3,356 ⭐ |
| [Day 9](https://adventofcode.com/2022/day/9)   |   7,495 ⭐ |   5,231 ⭐ |
| [Day 10](https://adventofcode.com/2022/day/10) |   7,670 ⭐ |   5,514 ⭐ |
| [Day 11](https://adventofcode.com/2022/day/11) |   2,664 ⭐ |   2,420 ⭐ |
| [Day 12](https://adventofcode.com/2022/day/12) |   7,311 ⭐ |   6,766 ⭐ |
| [Day 13](https://adventofcode.com/2022/day/13) |  14,960 ⭐ |  14,025 ⭐ |
| [Day 14](https://adventofcode.com/2022/day/14) |  30,919 ⭐ |  30,485 ⭐ |
| [Day 17](https://adventofcode.com/2022/day/15) |           |           |
| [Day 17](https://adventofcode.com/2022/day/16) |           |           |
| [Day 17](https://adventofcode.com/2022/day/17) |           |           |
| [Day 18](https://adventofcode.com/2022/day/18) |           |           |
| [Day 19](https://adventofcode.com/2022/day/19) |           |           |
| [Day 20](https://adventofcode.com/2022/day/20) |           |           |
| [Day 21](https://adventofcode.com/2022/day/21) |           |           |
| [Day 22](https://adventofcode.com/2022/day/22) |           |           |
| [Day 23](https://adventofcode.com/2022/day/23) |           |           |
| [Day 24](https://adventofcode.com/2022/day/24) |           |           |
| [Day 25](https://adventofcode.com/2022/day/25) |           |           |

</details>

<details>
    <summary>2021 Results</summary>

| Day                                            |   Part 1 |   Part 2 |
|:-----------------------------------------------|---------:|---------:|
| [Day 1](https://adventofcode.com/2021/day/1)   | 34,535 ⭐ | 30,143 ⭐ |
| [Day 2](https://adventofcode.com/2021/day/2)   | 43,645 ⭐ | 41,117 ⭐ |
| [Day 3](https://adventofcode.com/2021/day/3)   | 86,014 ⭐ | 67,217 ⭐ |
| [Day 4](https://adventofcode.com/2021/day/4)   | 17,500 ⭐ | 15,575 ⭐ |
| [Day 5](https://adventofcode.com/2021/day/5)   | 45,830 ⭐ | 43,865 ⭐ |
| [Day 6](https://adventofcode.com/2021/day/6)   | 52,188 ⭐ | 46,698 ⭐ |
| [Day 7](https://adventofcode.com/2021/day/7)   | 10,242 ⭐ | 17,459 ⭐ |
| [Day 8](https://adventofcode.com/2021/day/8)   | 27,980 ⭐ | 15,957 ⭐ |
| [Day 9](https://adventofcode.com/2021/day/9)   |          |          |
| [Day 10](https://adventofcode.com/2021/day/10) | 42,885 ⭐ | 41,392 ⭐ |
| [Day 11](https://adventofcode.com/2021/day/11) |          |          |
| [Day 12](https://adventofcode.com/2021/day/12) |          |          |
| [Day 13](https://adventofcode.com/2021/day/13) |          |          |
| [Day 14](https://adventofcode.com/2021/day/14) |          |          |
| [Day 15](https://adventofcode.com/2021/day/15) |          |          |
| [Day 16](https://adventofcode.com/2021/day/16) |          |          |
| [Day 17](https://adventofcode.com/2021/day/17) |          |          |
| [Day 18](https://adventofcode.com/2021/day/18) |          |          |
| [Day 19](https://adventofcode.com/2021/day/19) |          |          |
| [Day 20](https://adventofcode.com/2021/day/20) |          |          |
| [Day 21](https://adventofcode.com/2021/day/21) | 18,674 ⭐ |          |
| [Day 22](https://adventofcode.com/2021/day/22) |          |          |
| [Day 23](https://adventofcode.com/2021/day/23) |          |          |
| [Day 24](https://adventofcode.com/2021/day/24) |          |          |
| [Day 25](https://adventofcode.com/2021/day/25) |          |          |

</details>

<details>
    <summary>2020 Results</summary>

| Day                                            | Part 1 | Part 2 |
|:-----------------------------------------------|-------:|-------:|
| [Day 1](https://adventofcode.com/2020/day/1)   |        |        |
| [Day 2](https://adventofcode.com/2020/day/2)   |        |        |
| [Day 3](https://adventofcode.com/2020/day/3)   |        |        |
| [Day 4](https://adventofcode.com/2020/day/4)   |        |        |
| [Day 5](https://adventofcode.com/2020/day/5)   |        |        |
| [Day 6](https://adventofcode.com/2020/day/6)   |        |        |
| [Day 7](https://adventofcode.com/2020/day/7)   |        |        |
| [Day 8](https://adventofcode.com/2020/day/8)   |        |        |
| [Day 9](https://adventofcode.com/2020/day/9)   |        |        |
| [Day 10](https://adventofcode.com/2020/day/10) |        |        |
| [Day 11](https://adventofcode.com/2020/day/11) |        |        |
| [Day 12](https://adventofcode.com/2020/day/12) |        |        |
| [Day 13](https://adventofcode.com/2020/day/13) |        |        |
| [Day 14](https://adventofcode.com/2020/day/14) |        |        |
| [Day 15](https://adventofcode.com/2020/day/15) |        |        |
| [Day 16](https://adventofcode.com/2020/day/16) |        |        |
| [Day 17](https://adventofcode.com/2020/day/17) |        |        |
| [Day 18](https://adventofcode.com/2020/day/18) |        |        |
| [Day 19](https://adventofcode.com/2020/day/19) |        |        |
| [Day 20](https://adventofcode.com/2020/day/20) |        |        |
| [Day 21](https://adventofcode.com/2020/day/21) |        |        |
| [Day 22](https://adventofcode.com/2020/day/22) |        |        |
| [Day 23](https://adventofcode.com/2020/day/23) |        |        |
| [Day 24](https://adventofcode.com/2020/day/24) |        |        |
| [Day 25](https://adventofcode.com/2020/day/25) |        |        |

</details>

<details>
    <summary>2019 Results</summary>

| Day                                            | Part 1 | Part 2 |
|:-----------------------------------------------|-------:|-------:|
| [Day 1](https://adventofcode.com/2019/day/1)   |        |        |
| [Day 2](https://adventofcode.com/2019/day/2)   |        |        |
| [Day 3](https://adventofcode.com/2019/day/3)   |        |        |
| [Day 4](https://adventofcode.com/2019/day/4)   |        |        |
| [Day 5](https://adventofcode.com/2019/day/5)   |        |        |
| [Day 6](https://adventofcode.com/2019/day/6)   |        |        |
| [Day 7](https://adventofcode.com/2019/day/7)   |        |        |
| [Day 8](https://adventofcode.com/2019/day/8)   |        |        |
| [Day 9](https://adventofcode.com/2019/day/9)   |        |        |
| [Day 10](https://adventofcode.com/2019/day/10) |        |        |
| [Day 11](https://adventofcode.com/2019/day/11) |        |        |
| [Day 12](https://adventofcode.com/2019/day/12) |        |        |
| [Day 13](https://adventofcode.com/2019/day/13) |        |        |
| [Day 14](https://adventofcode.com/2019/day/14) |        |        |
| [Day 15](https://adventofcode.com/2019/day/15) |        |        |
| [Day 16](https://adventofcode.com/2019/day/16) |        |        |
| [Day 17](https://adventofcode.com/2019/day/17) |        |        |
| [Day 18](https://adventofcode.com/2019/day/18) |        |        |
| [Day 19](https://adventofcode.com/2019/day/19) |        |        |
| [Day 20](https://adventofcode.com/2019/day/20) |        |        |
| [Day 21](https://adventofcode.com/2019/day/21) |        |        |
| [Day 22](https://adventofcode.com/2019/day/22) |        |        |
| [Day 23](https://adventofcode.com/2019/day/23) |        |        |
| [Day 24](https://adventofcode.com/2019/day/24) |        |        |
| [Day 25](https://adventofcode.com/2019/day/25) |        |        |

</details>

<details>
    <summary>2018 Results</summary>

| Day                                            |   Part 1 |   Part 2 |
|:-----------------------------------------------|---------:|---------:|
| [Day 1](https://adventofcode.com/2018/day/1)   | 27,792 ⭐ | 22,808 ⭐ |
| [Day 2](https://adventofcode.com/2018/day/2)   | 66,502 ⭐ | 59,417 ⭐ |
| [Day 3](https://adventofcode.com/2018/day/3)   |          |          |
| [Day 4](https://adventofcode.com/2018/day/4)   |          |          |
| [Day 5](https://adventofcode.com/2018/day/5)   |          |          |
| [Day 6](https://adventofcode.com/2018/day/6)   |          |          |
| [Day 7](https://adventofcode.com/2018/day/7)   |          |          |
| [Day 8](https://adventofcode.com/2018/day/8)   |          |          |
| [Day 9](https://adventofcode.com/2018/day/9)   |          |          |
| [Day 10](https://adventofcode.com/2018/day/10) |          |          |
| [Day 11](https://adventofcode.com/2018/day/11) |          |          |
| [Day 12](https://adventofcode.com/2018/day/12) |          |          |
| [Day 13](https://adventofcode.com/2018/day/13) |          |          |
| [Day 14](https://adventofcode.com/2018/day/14) |          |          |
| [Day 15](https://adventofcode.com/2018/day/15) |          |          |
| [Day 16](https://adventofcode.com/2018/day/16) |          |          |
| [Day 17](https://adventofcode.com/2018/day/17) |          |          |
| [Day 18](https://adventofcode.com/2018/day/18) |          |          |
| [Day 19](https://adventofcode.com/2018/day/19) |          |          |
| [Day 20](https://adventofcode.com/2018/day/20) |          |          |
| [Day 21](https://adventofcode.com/2018/day/21) |          |          |
| [Day 22](https://adventofcode.com/2018/day/22) |          |          |
| [Day 23](https://adventofcode.com/2018/day/23) |          |          |
| [Day 24](https://adventofcode.com/2018/day/24) |          |          |
| [Day 25](https://adventofcode.com/2018/day/25) |          |          |

</details>

<details>
    <summary>2017 Results</summary>

| Day                                            | Part 1 | Part 2 |
|:-----------------------------------------------|-------:|-------:|
| [Day 1](https://adventofcode.com/2017/day/1)   |        |        |
| [Day 2](https://adventofcode.com/2017/day/2)   |        |        |
| [Day 3](https://adventofcode.com/2017/day/3)   |        |        |
| [Day 4](https://adventofcode.com/2017/day/4)   |        |        |
| [Day 5](https://adventofcode.com/2017/day/5)   |        |        |
| [Day 6](https://adventofcode.com/2017/day/6)   |        |        |
| [Day 7](https://adventofcode.com/2017/day/7)   |        |        |
| [Day 8](https://adventofcode.com/2017/day/8)   |        |        |
| [Day 9](https://adventofcode.com/2017/day/9)   |        |        |
| [Day 10](https://adventofcode.com/2017/day/10) |        |        |
| [Day 11](https://adventofcode.com/2017/day/11) |        |        |
| [Day 12](https://adventofcode.com/2017/day/12) |        |        |
| [Day 13](https://adventofcode.com/2017/day/13) |        |        |
| [Day 14](https://adventofcode.com/2017/day/14) |        |        |
| [Day 15](https://adventofcode.com/2017/day/15) |        |        |
| [Day 16](https://adventofcode.com/2017/day/16) |        |        |
| [Day 17](https://adventofcode.com/2017/day/17) |        |        |
| [Day 18](https://adventofcode.com/2017/day/18) |        |        |
| [Day 19](https://adventofcode.com/2017/day/19) |        |        |
| [Day 20](https://adventofcode.com/2017/day/20) |        |        |
| [Day 21](https://adventofcode.com/2017/day/21) |        |        |
| [Day 22](https://adventofcode.com/2017/day/22) |        |        |
| [Day 23](https://adventofcode.com/2017/day/23) |        |        |
| [Day 24](https://adventofcode.com/2017/day/24) |        |        |
| [Day 25](https://adventofcode.com/2017/day/25) |        |        |

</details>

<details>
    <summary>2016 Results</summary>

| Day                                            | Part 1 | Part 2 |
|:-----------------------------------------------|-------:|-------:|
| [Day 1](https://adventofcode.com/2016/day/1)   |        |        |
| [Day 2](https://adventofcode.com/2016/day/2)   |        |        |
| [Day 3](https://adventofcode.com/2016/day/3)   |        |        |
| [Day 4](https://adventofcode.com/2016/day/4)   |        |        |
| [Day 5](https://adventofcode.com/2016/day/5)   |        |        |
| [Day 6](https://adventofcode.com/2016/day/6)   |        |        |
| [Day 7](https://adventofcode.com/2016/day/7)   |        |        |
| [Day 8](https://adventofcode.com/2016/day/8)   |        |        |
| [Day 9](https://adventofcode.com/2016/day/9)   |        |        |
| [Day 10](https://adventofcode.com/2016/day/10) |        |        |
| [Day 11](https://adventofcode.com/2016/day/11) |        |        |
| [Day 12](https://adventofcode.com/2016/day/12) |        |        |
| [Day 13](https://adventofcode.com/2016/day/13) |        |        |
| [Day 14](https://adventofcode.com/2016/day/14) |        |        |
| [Day 15](https://adventofcode.com/2016/day/15) |        |        |
| [Day 16](https://adventofcode.com/2016/day/16) |        |        |
| [Day 17](https://adventofcode.com/2016/day/17) |        |        |
| [Day 18](https://adventofcode.com/2016/day/18) |        |        |
| [Day 19](https://adventofcode.com/2016/day/19) |        |        |
| [Day 20](https://adventofcode.com/2016/day/20) |        |        |
| [Day 21](https://adventofcode.com/2016/day/21) |        |        |
| [Day 22](https://adventofcode.com/2016/day/22) |        |        |
| [Day 23](https://adventofcode.com/2016/day/23) |        |        |
| [Day 24](https://adventofcode.com/2016/day/24) |        |        |
| [Day 25](https://adventofcode.com/2016/day/25) |        |        |

</details>

<details>
    <summary>2015 Results</summary>

| Day                                            |   Part 1 |   Part 2 |
|:-----------------------------------------------|---------:|---------:|
| [Day 1](https://adventofcode.com/2015/day/1)   | 16,209 ⭐ | 65,488 ⭐ |
| [Day 2](https://adventofcode.com/2015/day/2)   | 51,249 ⭐ | 46,112 ⭐ |
| [Day 3](https://adventofcode.com/2015/day/3)   | 41,971 ⭐ | 38,509 ⭐ |
| [Day 4](https://adventofcode.com/2015/day/4)   | 34,991 ⭐ | 33,645 ⭐ |
| [Day 5](https://adventofcode.com/2015/day/5)   | 34,146 ⭐ | 28,979 ⭐ |
| [Day 6](https://adventofcode.com/2015/day/6)   | 27,303 ⭐ | 26,097 ⭐ |
| [Day 7](https://adventofcode.com/2015/day/7)   | 19,007 ⭐ | 18,331 ⭐ |
| [Day 8](https://adventofcode.com/2015/day/8)   | 18,022 ⭐ | 17,020 ⭐ |
| [Day 9](https://adventofcode.com/2015/day/9)   | 15,559 ⭐ | 15,246 ⭐ |
| [Day 10](https://adventofcode.com/2015/day/10) | 16,779 ⭐ | 16,163 ⭐ |
| [Day 11](https://adventofcode.com/2015/day/11) | 14,370 ⭐ | 14,182 ⭐ |
| [Day 12](https://adventofcode.com/2015/day/12) | 14,601 ⭐ | 12,618 ⭐ |
| [Day 13](https://adventofcode.com/2015/day/13) | 12,312 ⭐ | 12,150 ⭐ |
| [Day 14](https://adventofcode.com/2015/day/14) | 13,165 ⭐ | 12,305 ⭐ |
| [Day 15](https://adventofcode.com/2015/day/15) | 10,850 ⭐ | 10,699 ⭐ |
| [Day 16](https://adventofcode.com/2015/day/16) | 11,389 ⭐ | 10,976 ⭐ |
| [Day 17](https://adventofcode.com/2015/day/17) | 10,461 ⭐ | 10,282 ⭐ |
| [Day 18](https://adventofcode.com/2015/day/18) |  9,975 ⭐ |  9,840 ⭐ |
| [Day 19](https://adventofcode.com/2015/day/19) |  9,462 ⭐ |  7,173 ⭐ |
| [Day 20](https://adventofcode.com/2015/day/20) |  8,617 ⭐ |  8,089 ⭐ |
| [Day 21](https://adventofcode.com/2015/day/21) |  7,838 ⭐ |  7,667 ⭐ |
| [Day 22](https://adventofcode.com/2015/day/22) |  6,084 ⭐ |  5,923 ⭐ |
| [Day 23](https://adventofcode.com/2015/day/23) |  7,328 ⭐ |  7,290 ⭐ |
| [Day 24](https://adventofcode.com/2015/day/24) |  6,323 ⭐ |  6,240 ⭐ |
| [Day 25](https://adventofcode.com/2015/day/25) |  7,497 ⭐ |  5,393 ⭐ |

</details>
