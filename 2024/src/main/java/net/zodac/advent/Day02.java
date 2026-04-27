/*
 * BSD Zero Clause License
 *
 * Copyright (c) 2021-2026 zodac.net
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package net.zodac.advent;

import java.util.Collection;
import java.util.List;
import net.zodac.advent.pojo.Report;
import net.zodac.advent.util.PermutationUtils;

/**
 * Solution for 2024, Day 2.
 *
 * @see <a href="https://adventofcode.com/2024/day/2">[2024: 02] Red-Nosed Reports</a>
 */
public final class Day02 {

    private static final int MAX_DIFF_BETWEEN_NUMBERS = 3;

    private Day02() {

    }

    /**
     * Given a {@link Collection} of {@link Report}s, we must determine the {@link Report}s that are 'safe'.
     *
     * @param reports            the input {@link Report}s
     * @param allowSingleFailure if {@code true}, allows for a single failure to be ignored when determining if the {@link Report} is safe
     * @return the number of safe {@link Report}s
     * @see Report#isSafe(int)
     */
    public static long countSafeReports(final Collection<Report> reports, final boolean allowSingleFailure) {
        return reports
            .stream()
            .filter(report -> isSafeReport(report, allowSingleFailure))
            .count();
    }

    private static boolean isSafeReport(final Report report, final boolean allowSingleFailure) {
        final boolean isReportValid = report.isSafe(MAX_DIFF_BETWEEN_NUMBERS);
        return isReportValid || (allowSingleFailure && doesReportHaveSingleFailure(report));
    }

    private static boolean doesReportHaveSingleFailure(final Report report) {
        final List<Long> reportValues = report.inputs();
        for (final List<Long> permutation : PermutationUtils.generateAllWithOneRemovedEntry(reportValues)) {
            final Report reportPermutation = Report.create(permutation);
            if (reportPermutation.isSafe(MAX_DIFF_BETWEEN_NUMBERS)) {
                return true;
            }
        }

        return false;
    }
}
