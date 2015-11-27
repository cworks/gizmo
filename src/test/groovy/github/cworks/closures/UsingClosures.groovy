/**
 * Created with love by corbett.
 * User: corbett
 * Date: 12/1/14
 * Time: 8:34 PM
 */
package github.cworks.closures

def sum(n) {
    total = 0;
    for(int i = 0; i <= n; i += 2) {
        total += i;
    }
    total;
}

def product(n) {
    prod = 1;
    for(int i = 2; i <= n; i += 2) {
        prod *= i;
    }
    prod;
}

def square(n) {
    squares = [];
    for(int i = 2; i <= n; i += 2) {
        squares << i ** 2;
    }
    squares;
}

println "Sum of even numbers from 1 to 10 is ${sum(10)}";
println "Product of even number from 1 to 10 is ${product(10)}";
println "Squares of even numbers from 1 to 10 is ${square(10)}";

// this function takes a code block
// other than their syntactic elegance, closures provide
// a simple and easy way for a function to delegate part
// of its implementation logic (i.e. strategy pattern)
def pickEven(n, closure) {
    for(int i = 2; i <= n; i += 2) {
        closure(i);
    }
}

// function that takes functions as arguments
pickEven(10, {
    print it + " ";
});

pickEven(20) {
    print it + " ";
}

pickEven(40) {
    evenNumber -> print evenNumber + " ";
}

// This block of code does something more than
// the sum() code above.  It reaches into the
// scope of the caller and accesses the total variable
// a closure is a function with variables bound to another context
// or environment in which it executes, thus its said to "close over" it.
total = 0;
pickEven(10) {
    total += it;
}
println "Sum of even numbers from 1 to 10 is ${total}";

//
// ways to use closures
//
def totalSelectedValues(n, closure) {
    total = 0;
    for(i in 1..n) {
        if(closure(i)) {
            total += i;
        }
    }
    total;
}

// just-in-time use
println totalSelectedValues(10, {
    it % 2 == 0;
});

// save to variable
def isOdd = { it % 2 != 0 };

println totalSelectedValues(10, isOdd);

// strategy pattern application

class Equipment {
    // cache a calculator closure
    def calculator

    Equipment(calculator) {
        this.calculator = calculator;
    }

    def simulate() {
        println "Running Simulation";
        calculator();
    }
}

def equip1 = new Equipment({ println "Calculator 1" });
def calc1 = { println "Calculator 2" };
def equip2 = new Equipment(calc1);
def equip3 = new Equipment(calc1);

equip1.simulate();
equip2.simulate();
equip3.simulate();

// passing parameters to closures
// closure with 2 parameters

def tellFortune(closure) {
    closure(new Date("12/01/2014"), "Enjoy it's Christmas time in the city.");
}

tellFortune({ date, fortune ->
    println "Fortune is: ${date} is ${fortune}";
});

// parasitic form
tellFortune() { date, fortune ->
    println "I'm a parasite fortune: ${date} is ${fortune}";
}

// parasitic form with optional types
tellFortune() { Date date, String fortune ->
    println "I'm a tightly typed fortune: ${date} is ${fortune}";
}

def fortuneMessage = { date, fortune ->
    println "A reused fortune is a cheap way to go but here it tis: ${date} is ${fortune}";
};

tellFortune(fortuneMessage);

// using closures for resource clean up
// closures can help ensure methods get called
new FileWriter('output.txt').withWriter() {writer ->
    writer.write("I am singing at the party.");
}; // no need to close...how?

// execute-around-pattern
class Resource {

    def open() {
        println "open";
    }

    def close() {
        println "close";
    }

    def read() {
        println "read";
    }

    def write() {
        println "write";
    }

    // execute-around-method
    def static use(closure) {
        def res = new Resource();
        try {
            res.open();
            closure(res);
        } finally {
            res.close();
        }
    }

}

// Thanks to the closure the resource is now opened and closed automatically
Resource.use() { resource ->
    resource.read();
    resource.write();
}

// How functions and closures interact

// coroutines
// this is an interesting concept

// first we define a function that accepts a closure as last argument
// when called iterate creates its own scope
// within that scope is a call to a closure
def iterate(n, closure) {
    1.upto(n) {
        println "In iterate with value ${it}";
        closure(it);
    }
}

println "Calling iterate";
total = 0;

// calling the iterate function and passing in a closure block
// the closure reaches out to access total, while the function
// places it within the scope of the closure
iterate(4) {
    total += it;
    println "In closure total so far: ${total}";
}
println "done";

// simplify calling a closure by currying

def saySomething(closure) {
    Date date = new Date("12/25/2014");
    // example closure calls
    // closure(date, "Hello");
    // closure(date, "Bye");
    postFortune = closure.curry(date);
    postFortune "Hello";
    postFortune "Bye";
}

saySomething() { date, message ->
    println "${date} message: ${message}";
}

// dynamic closures
// useful in determining if caller provided a closure or not
// if not we may choose to use a default closure

def doSomething(closure) {
    if(closure) {
        closure();
    } else {
        println "Using default implementation";
    }
}

doSomething() {
    println "Using a special implementation";
}

doSomething(); // use default

// querying closures, also a good example of strategy pattern
def completeOrder(amount, taxComputer) {
    tax = 0;
    if(taxComputer.maximumNumberOfParameters == 2) {
        // expects tax rate
        tax = taxComputer(amount, 6.05);
    } else {
        // use default rate
        tax = taxComputer(amount);
    }
    println "Sales tax is: ${tax}";
}

completeOrder(100) { amount ->
    amount * 0.0825;
}

completeOrder(100) { amount, rate ->
    amount * (rate / 100);
}

// querying closures, finding parameter types
def examine(closure) {
    println "$closure.maximumNumberOfParameters parameter(s) given:";
    for(param in closure.parameterTypes) {
        println param.name
    }
    println "---";
}

examine() { }
examine() { it }
examine() { -> }
examine() { val -> }
examine() {Date val -> }
examine() {Date val1, val2 -> }
examine() {Date val1, String val2 -> }

// closure delegation
// 3 properties of a closure: this, owner and delegation determine
// which object handles a method call

def examiningClosure(closure) {
    closure();
}

examiningClosure() {
    println "In first closure";
    println "Class is: " + getClass().name
    println "this is: " + this + ", super of this is: " + this.getClass().superclass.name;
    println "owner is: " + owner + ", super of owner is: " + owner.getClass().superclass.name;
    println "delegate is: " + delegate + ", super of delegate is: " + delegate.getClass().superclass.name;

    examiningClosure() {
        println "In first closure";
        println "Class is: " + getClass().name
        println "this is: " + this + ", super of this is: " + this.getClass().superclass.name;
        println "owner is: " + owner + ", super of owner is: " + owner.getClass().superclass.name;
        println "delegate is: " + delegate + ", super of delegate is: " + delegate.getClass().superclass.name;
    }
}













