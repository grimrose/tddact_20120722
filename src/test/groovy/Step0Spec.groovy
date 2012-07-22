import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12/07/22
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
class Step0Spec extends Specification {

    @Unroll
    def "#money を1枚投入して #result を取得できる"() {
        when:
        VendingMachine machine  = new VendingMachine()
        machine.insert(money)

        then:
        result == machine.total

        where:
        money   |result
        10      |10
        50      |50
        100     |100
        500     |500
        1000    |1000

    }

    @Unroll
    def "#money を投入して#resultが戻る"() {
        when:
        VendingMachine machine = new VendingMachine()

        def total = 0
        money.each {
            machine.insert(it)

             total = machine.total

        }
        then:
        result == total

        where:
        money       |result
        [10,10]     |20
        [10,10,10]  |30
        [10,50,100,500,1000]    |1660
    }

    def "10円玉を1枚投入して払い戻すと10円が返る"() {
        when:
        VendingMachine machine = new VendingMachine()
        machine.insert(10)

        then:
        10 == machine.repay()
    }

    def "10円玉と50円玉を投入して総計60円が返る"() {
        when:
        VendingMachine machine = new VendingMachine()

        machine.insert(10)
        machine.insert(50)
        then:
        60 == machine.repay()
    }

}

class Step1Spec extends Specification {
    def "1円玉を投入するとfalseが返る"() {
        when:
        VendingMachine machine = new VendingMachine()

        then:
        machine.insert(1) == false
    }

    def "10円玉を投入するとtrueが返る"() {
        when:
        VendingMachine machine = new VendingMachine()


        then:
        machine.insert(10) == true
    }

    @Unroll
    def "#money を投入すると#resultが返る"() {
        when:
        VendingMachine machine = new VendingMachine()

        then:
        machine.insert(money) == result

        where:
        money   |result
        1       |false
        5       |false
        10      |true
        50      |true
        100     |true
        500     |true
        1000    |true
        2000    |false
        5000    |false
        10000   |false
        0       |false
        -1      |false

    }
}

class Step2Spec extends Specification {
    @Unroll
    def "値段:#price、名前:#name、在庫:#stockのマップを返す"() {
        when:
        VendingMachine machine = new VendingMachine()
        machine.addJuice(name, price, stock)
        def report = machine.reportStock()

        then:
        name == report.name
        price == report.price
        stock == report.stock

        where:
        name    |price  |stock
        ""      |0      |0
        "コーラ"   |120    |5

    }
}

class Step3Spec extends Specification {
    def "購入可能か確認する"() {
        when:
        VendingMachine machine = new VendingMachine()

        then:
        machine.isPurchasable("") == false
    }

    @Unroll
    def "#money円を投入してコーラ購入可能であることを確認する"() {
        when:
        VendingMachine machine = new VendingMachine()
        machine.addJuice("コーラ",120,5)

        money.each {
            machine.insert(it)
        }

        then:
        result == machine.isPurchasable("コーラ")

        where:
        money   |result
        [100,10,10] |true
        [100]       |false
    }

    def "120円を投入してコーラを購入する"() {
        when:
        VendingMachine machine = new VendingMachine()
        machine.addJuice("コーラ", 120, before_stock)
        [100,10,10].each {
            machine.insert(it)
        }

        def p_result = machine.purchase("コーラ")
        def report = machine.reportStock()

        then:
        result == p_result
        after_stock == report.stock

        where:
        before_stock    |result |after_stock
        5               |true   |4
    }

    def "購入なしの状態で、売上金額ゼロを取得する"() {
        when:
        VendingMachine machine = new VendingMachine()
        machine.addJuice("コーラ", 120, 5)
        [100,10,10].each {
            machine.insert(it)
        }

        then:
        0 == machine.getProfit()
    }

    def "コーラを1本購入した状態で、売上金額120円を取得する"() {
        when:
        VendingMachine machine = new VendingMachine()
        machine.addJuice("コーラ", 120, 5)
        [100,10,10].each {
            machine.insert(it)
        }
        machine.purchase("コーラ")

        then:
        120 == machine.getProfit()
    }

    @Unroll
    def "#money円を投入してコーラを購入（#result）"() {
        when:
        VendingMachine machine = new VendingMachine()
        machine.addJuice("コーラ", 120, 5)
        money.each {
            machine.insert(it)
        }

        def p_result = machine.purchase("コーラ")
        def report = machine.reportStock()

        then:
        result == p_result
        after_stock == report.stock

        where:
        money    |result |after_stock    |profit
        [100,10,10]               |true   |4              |120
        [100]               |false   |5              |0
    }

    def "在庫なしの状態で購入操作をして売上金額ゼロを確認する"() {
        when:
        VendingMachine machine = new VendingMachine()
        [100,10,10].each {
            machine.insert(it)
        }

        then:
        0 == machine.getProfit()
    }

    @Unroll
    def "#money円を投入して#nameを購入した結果払い戻し金額#changeを取得する"() {
        when:
        VendingMachine machine = new VendingMachine()
        machine.addJuice("コーラ",120, 5)
        money.each { machine.insert(it)   }
        machine.purchase(name)


        then:
        change == machine.repay()

        where:
        name    |money   |change
        "コーラ"   |[100,10,10] |0
        "コーラ"   |[100,50]    |30
        ""          |[100,10,10]    |120

    }


}