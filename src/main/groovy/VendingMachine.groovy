
/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12/07/22
 * Time: 11:22
 * To change this template use File | Settings | File Templates.
 */
class VendingMachine {

    def profit = 0
    def stockList = []
    def acceptList = [10, 50, 100, 500, 1000]
    def total = 0
    def insert(money) {
        if (acceptList.contains(money)) {
            total += money
            return  true
        }
        false
    }

    def repay() {
        total
    }

    def reportStock() {
        if (stockList.isEmpty())  return [name: "", price: 0, stock:  0]
        def map = [:]

        stockList.each {
            map.name = it.name
            map.price = it.price
            map.stock = it.stock
        }
        map
    }

    def addJuice(String name, Integer price, Integer stock) {
        if (name == "") {
            return
        }

        def map = [:]
        map.name = name
        map.price = price
        map.stock = stock

        stockList << map
    }

    def isPurchasable(name) {

        if (name == "") return  false

        def map = stockList.find {
            it.name == name
        }
        if (map.stock == 0) return  false

         map.price <= total
    }

    def purchase(name) {
       if (isPurchasable(name) == false)  return  false

        def map = stockList.find {
            it.name == name
        }
       def stock =   map.stock
        stock--
        map.stock = stock
        def price = map.price
        profit += price
        total -= price

        true
    }

    def getProfit() {
        profit
    }


}
