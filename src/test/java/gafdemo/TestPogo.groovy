package gafdemo

import gafdemo.groovy.DslEvaluator

/**
 * Created by luomingxing on 2019/9/9.
 */

def dslEvaluator = new DslEvaluator()
def rule1 = dslEvaluator.executeDslForRule('''
                rule {
                    name = "规则2"
                    code = "rule2"
                    id = "abc2"
                    filter("age > 18")
                    assign("result = 'pass'")
                    otherAssign("result = 'reject'")
                }
            ''')
println(rule1.toString())

def ruleSet = dslEvaluator.executeDslForRuleSet('''
                ruleSet {
                    name = "规则集1"
                    code = "ruleSet1"
                    id = "abc11"
                    type = 1
                    
                    rule {
                        name = "规则1"
                        code = "rule1"
                        id = "abc1"
                        filter("age > 18")
                        assign("result = 'pass'")
                        otherAssign("result = 'reject'")
                    }
                    rule {
                        name = "规则2"
                        code = "rule2"
                        id = "abc2"
                        filter("age < 18")
                        assign("result = 'pass'")
                        otherAssign("result = 'reject'")
                    }
                }
            ''')
println(ruleSet.toString())
ruleSet.ruleMap.each {rule ->
    println(rule.toString())
}

def ruleTable = dslEvaluator.executeDslForRuleTable('''
                ruleTable {
                    name = "决策表1"
                    code = "ruleTable1"
                    id = "ffff11"
                    type = 1
                    row {
                        rowItem(0,"age > 18")
                        rowItem(1,"age < 18")
                    }
                    column {
                        columnItem(0,"male = 1")
                        columnItem(1,"male = 0")
                    }
                    assign {
                        assignItem("result1 = 'r1c1'","result2 = 'r1c2'")
                        assignItem("result1 = 'r2c1'","result2 = 'r2c2'")
                    }
                }
            ''')

println(ruleTable.toString())
ruleTable.rowItemMap.each {row ->
    println(row.toString())
}

ruleTable.columnItemMap.each {column ->
    println(column.toString())
}

def ruleCard = dslEvaluator.executeDslForRuleCard('''
                ruleCard {
                    name = "评分卡1"
                    code = "ruleCard1"
                    id = "ccc11"
                    type = 1
                    propertyScore {
                        propertyCode = "event_age"
                        propertyWeight = 3
                        propertyItem(1,"age < 18")
                        propertyItem(2,"age > 18 && age < 25")
                        propertyItem(3,"age > 25 && age < 50")
                    }
                    propertyScore {
                        propertyCode = "event_male"
                        propertyWeight = 5
                        propertyItem(1,"male = 0")
                        propertyItem(2,"male = 1")
                    }
                }
            ''')
println(ruleCard.toString())
ruleCard.propertyScoreList.each {propertyScore ->
    println(propertyScore.toString())
}

def ruleFlow = dslEvaluator.executeDslForRuleFlow('''
                ruleFlow {
                    name = "决策流1"
                    code = "ruleFlow1"
                    id = "dddd11"
                    rootNode {
                        expression("rootNode == '1';age > 18")
                        leftChild {
                            expression("leftChild == '1.1';rule(rule1)")
                            leftChild {
                                expression("leftChild == '1.1.1' ")
                            }
                            rightChild {
                                expression("rightChild == '1.1.2';rule(ruleSet1)")
                                leftChild {
                                    expression("leftChild == '1.1.1.1' ")
                                }
                            }
                        }
                        rightChild {
                            expression("rightChild == '1.2' ")
                        }
                    }
                }
            ''')

println(ruleFlow.toString())
println(ruleFlow.rootNode.toString())

