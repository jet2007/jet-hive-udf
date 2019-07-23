// Copyright 2011 The Go Authors.  All rights reserved.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

// Original Go source here:
// http://code.google.com/p/go/source/browse/src/pkg/regexp/syntax/simplify.go

package com.github.aaronshan.functions.regexp.re2j;

import java.util.ArrayList;

class Simplify {

    private Simplify() {
    }  // uninstantiable

    // Simplify returns a regexp equivalent to re but without counted
    // repetitions and with various other simplifications, such as
    // rewriting /(?:a+)+/ to /a+/.  The resulting regexp will execute
    // correctly but its string representation will not produce the same
    // parse tree, because capturing parentheses may have been duplicated
    // or removed.  For example, the simplified form for /(x){1,2}/ is
    // /(x)(x)?/ but both parentheses capture as $1.  The returned regexp
    // may share structure with or be the original.
    static com.github.aaronshan.functions.regexp.re2j.Regexp simplify(com.github.aaronshan.functions.regexp.re2j.Regexp re) {
        if (re == null) {
            return null;
        }
        switch (re.op) {
            case CAPTURE:
            case CONCAT:
            case ALTERNATE: {
                // Simplify children, building new Regexp if children change.
                com.github.aaronshan.functions.regexp.re2j.Regexp nre = re;
                for (int i = 0; i < re.subs.length; ++i) {
                    com.github.aaronshan.functions.regexp.re2j.Regexp sub = re.subs[i];
                    com.github.aaronshan.functions.regexp.re2j.Regexp nsub = simplify(sub);
                    if (nre == re && nsub != sub) {
                        // Start a copy.
                        nre = new com.github.aaronshan.functions.regexp.re2j.Regexp(re);  // shallow copy
                        nre.runes = null;
                        nre.subs = com.github.aaronshan.functions.regexp.re2j.Parser.subarray(re.subs, 0, re.subs.length);  // clone
                    }
                    if (nre != re) {
                        nre.subs[i] = nsub;
                    }
                }
                return nre;
            }
            case STAR:
            case PLUS:
            case QUEST: {
                com.github.aaronshan.functions.regexp.re2j.Regexp sub = simplify(re.subs[0]);
                return simplify1(re.op, re.flags, sub, re);
            }
            case REPEAT: {
                // Special special case: x{0} matches the empty string
                // and doesn't even need to consider x.
                if (re.min == 0 && re.max == 0) {
                    return new com.github.aaronshan.functions.regexp.re2j.Regexp(com.github.aaronshan.functions.regexp.re2j.Regexp.Op.EMPTY_MATCH);
                }

                // The fun begins.
                com.github.aaronshan.functions.regexp.re2j.Regexp sub = simplify(re.subs[0]);

                // x{n,} means at least n matches of x.
                if (re.max == -1) {
                    // Special case: x{0,} is x*.
                    if (re.min == 0) {
                        return simplify1(com.github.aaronshan.functions.regexp.re2j.Regexp.Op.STAR, re.flags, sub, null);
                    }

                    // Special case: x{1,} is x+.
                    if (re.min == 1) {
                        return simplify1(com.github.aaronshan.functions.regexp.re2j.Regexp.Op.PLUS, re.flags, sub, null);
                    }

                    // General case: x{4,} is xxxx+.
                    com.github.aaronshan.functions.regexp.re2j.Regexp nre = new com.github.aaronshan.functions.regexp.re2j.Regexp(com.github.aaronshan.functions.regexp.re2j.Regexp.Op.CONCAT);
                    ArrayList<com.github.aaronshan.functions.regexp.re2j.Regexp> subs = new ArrayList<com.github.aaronshan.functions.regexp.re2j.Regexp>();
                    for (int i = 0; i < re.min - 1; i++) {
                        subs.add(sub);
                    }
                    subs.add(simplify1(com.github.aaronshan.functions.regexp.re2j.Regexp.Op.PLUS, re.flags, sub, null));
                    nre.subs = subs.toArray(new com.github.aaronshan.functions.regexp.re2j.Regexp[subs.size()]);
                    return nre;
                }

                // Special case x{0} handled above.

                // Special case: x{1} is just x.
                if (re.min == 1 && re.max == 1) {
                    return sub;
                }

                // General case: x{n,m} means n copies of x and m copies of x?
                // The machine will do less work if we nest the final m copies,
                // so that x{2,5} = xx(x(x(x)?)?)?

                // Build leading prefix: xx.
                ArrayList<com.github.aaronshan.functions.regexp.re2j.Regexp> prefixSubs = null;
                if (re.min > 0) {
                    prefixSubs = new ArrayList<com.github.aaronshan.functions.regexp.re2j.Regexp>();
                    for (int i = 0; i < re.min; i++) {
                        prefixSubs.add(sub);
                    }
                }

                // Build and attach suffix: (x(x(x)?)?)?
                if (re.max > re.min) {
                    com.github.aaronshan.functions.regexp.re2j.Regexp suffix = simplify1(com.github.aaronshan.functions.regexp.re2j.Regexp.Op.QUEST, re.flags, sub, null);
                    for (int i = re.min + 1; i < re.max; i++) {
                        com.github.aaronshan.functions.regexp.re2j.Regexp nre2 = new com.github.aaronshan.functions.regexp.re2j.Regexp(com.github.aaronshan.functions.regexp.re2j.Regexp.Op.CONCAT);
                        nre2.subs = new com.github.aaronshan.functions.regexp.re2j.Regexp[]{sub, suffix};
                        suffix = simplify1(com.github.aaronshan.functions.regexp.re2j.Regexp.Op.QUEST, re.flags, nre2, null);
                    }
                    if (prefixSubs == null) {
                        return suffix;
                    }
                    prefixSubs.add(suffix);
                }
                if (prefixSubs != null) {
                    com.github.aaronshan.functions.regexp.re2j.Regexp prefix = new com.github.aaronshan.functions.regexp.re2j.Regexp(com.github.aaronshan.functions.regexp.re2j.Regexp.Op.CONCAT);
                    prefix.subs = prefixSubs.toArray(new com.github.aaronshan.functions.regexp.re2j.Regexp[prefixSubs.size()]);
                    return prefix;
                }

                // Some degenerate case like min > max or min < max < 0.
                // Handle as impossible match.
                return new com.github.aaronshan.functions.regexp.re2j.Regexp(com.github.aaronshan.functions.regexp.re2j.Regexp.Op.NO_MATCH);
            }
        }

        return re;
    }

    // simplify1 implements Simplify for the unary OpStar,
    // OpPlus, and OpQuest operators.  It returns the simple regexp
    // equivalent to
    //
    //      Regexp{Op: op, Flags: flags, Sub: {sub}}
    //
    // under the assumption that sub is already simple, and
    // without first allocating that structure.  If the regexp
    // to be returned turns out to be equivalent to re, simplify1
    // returns re instead.
    //
    // simplify1 is factored out of Simplify because the implementation
    // for other operators generates these unary expressions.
    // Letting them call simplify1 makes sure the expressions they
    // generate are simple.
    private static com.github.aaronshan.functions.regexp.re2j.Regexp simplify1(com.github.aaronshan.functions.regexp.re2j.Regexp.Op op, int flags, com.github.aaronshan.functions.regexp.re2j.Regexp sub,
                                                                               com.github.aaronshan.functions.regexp.re2j.Regexp re) {
        // Special case: repeat the empty string as much as
        // you want, but it's still the empty string.
        if (sub.op == com.github.aaronshan.functions.regexp.re2j.Regexp.Op.EMPTY_MATCH) {
            return sub;
        }
        // The operators are idempotent if the flags match.
        if (op == sub.op &&
                (flags & com.github.aaronshan.functions.regexp.re2j.RE2.NON_GREEDY) == (sub.flags & com.github.aaronshan.functions.regexp.re2j.RE2.NON_GREEDY)) {
            return sub;
        }
        if (re != null && re.op == op &&
                (re.flags & com.github.aaronshan.functions.regexp.re2j.RE2.NON_GREEDY) == (flags & com.github.aaronshan.functions.regexp.re2j.RE2.NON_GREEDY) &&
                sub == re.subs[0]) {
            return re;
        }

        re = new com.github.aaronshan.functions.regexp.re2j.Regexp(op);
        re.flags = flags;
        re.subs = new com.github.aaronshan.functions.regexp.re2j.Regexp[]{sub};
        return re;
    }

}
