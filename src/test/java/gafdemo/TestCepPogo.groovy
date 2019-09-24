package gafdemo

import gafdemo.groovy.DslEvaluator

/**
 * Created by luomingxing on 2019/9/23.
 */

def dslEvaluator = new DslEvaluator()
HashMap patternMap = dslEvaluator.executeDslForPattern('''
                pattern {
                    name = "frist"
                    type = "begin"
                    condition {
                        and("rule(rule1)")
                        or("rule(rule2)")
                    }
                    next {
                        name = "second"
                        type = "next"
                        condition {
                            and("rule(rule3)")
                        }
                        times('oneOrMore', 4)
                        next {
                            name = "third"
                            type = "followedBy"
                            condition {
                                and("rule(rule4)")
                            }
                        }
                    }
                    within('DAYS', 1)
                }
            ''')

patternMap.each {patternMapEntry ->
    println(patternMapEntry.getKey())
    println(patternMapEntry.getValue())
}

