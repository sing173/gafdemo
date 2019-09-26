package gafdemo

import gafdemo.groovy.DslEvaluator
import gafdemo.groovy.pogo.event.CepEventGroovy
import gafdemo.groovy.pogo.event.CepPatternGroovy

/**
 * Created by luomingxing on 2019/9/23.
 */

def dslEvaluator = new DslEvaluator()
CepPatternGroovy cepPattern = dslEvaluator.executeDslForPattern('''
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

println(cepPattern.name)

CepEventGroovy cepEventGroovy = dslEvaluator.executeDslForEvent('''
                event {
                    id = "1234"
                    name = "childEvent1"
                    type = "risk"
                    patternGroup {
                        weight = 1
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
                        pattern {
                            weight = 2
                            name = "pattern2"
                            subtype = "childEvent1"
                            type = "begin"
                            condition {
                                and("rule(ruleSet)")
                            }
                            
                        }
                        pattern {
                            weight = 3
                            name = "pattern3"
                            subtype = "childEvent1"
                            type = "begin"
                            condition {
                                and("rule(rule5)")
                            }
                            
                        }
                    }
                    patternGroup {
                        weight = 2
                        pattern {
                            weight = 5
                            name = "patternGroup2"
                            subtype = "childEvent1"
                            type = "begin"
                            condition {
                                and("rule(rule8)")
                            }
                            
                        }
                    }
                }
            ''') as CepEventGroovy

    println(cepEventGroovy.name)
    if(cepEventGroovy.patternMap != null) {
        cepEventGroovy.patternMap.each { pattern ->
            println(pattern.getKey())
        }
    }

