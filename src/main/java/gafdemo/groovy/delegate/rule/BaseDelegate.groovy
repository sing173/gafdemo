package gafdemo.groovy.delegate.rule

import gafdemo.groovy.pogo.rule.BaseRule

/**
 * Created by luomingxing on 2019/9/9.
 */
abstract class BaseDelegate {
    protected BaseRule rule

    /*
        下面这些getter和setter是为了实现下面这种赋值而写的：name = '规则1'和code = 'rule1'
        rule {
                name = '规则1'
                code = 'rule1'
        }
    */
    def getName(){
        return this.rule.name
    }

    def setName(String name){
        this.rule.name = name
    }

    def getCode(){
        return this.rule.code
    }

    def setCode(String code){
        this.rule.code = code
    }

    def getId(){
        return this.rule.id
    }

    def setId(String id){
        this.rule.id = id
    }

    def getType(){
        return this.rule.type
    }

    def setType(Integer type){
        this.rule.type = type
    }

    /*
        通过methodMissing也支持这种赋值：name '规则1'和code 'rule1'
        rule {
            info {
                name '规则1'
                code 'rule1'
            }
        }
    */
//    def methodMissing(String name, Object args) {
//        if('name' == name){
//            rule.name = args[0] as String
//        } else if('code' == name){
//            rule.code = args[0] as String
//        } else if('id' == name){
//            rule.id = args[0] as String
//        } else if('type' == name){
//            rule.type = args[0] as Integer
//        }
//    }
}
