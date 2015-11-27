/**
 * Created with love by corbett.
 * User: corbett
 * Date: 12/2/14
 * Time: 11:04 AM
 */
package github.cworks.algo

//
// SELECTION SORT
//

// first find smallest item in array and exchange it with first entry
// then find next smallest and exchange with second entry
// continue until data is sorted
// works by selecting the smallest remaining item
def oleSelectionSort(list) {
    def N = list.size;
    for(int i = 0; i < N; i++) {
        min = i;
        for (int j = i + 1; j < N; j++) {
            if (list[j] < list[min]) {
                min = j;
            }
        }
        swap = list[i];
        list[i] = list[min];
        list[min] = swap;
    }
}

println "oleSelectionSort ----- ";
data = [9,8,7,6,5,4,3,2,1,0];
oleSelectionSort(data);
data.each { n -> print n + " "};
println()
data = ["s", "o", "r", "t", "e", "x", "a", "m", "p", "l", "e"];
oleSelectionSort(data);
data.each { s -> print s + " "};

def externalCompareSelectionSort(list, closure) {
    def N = list.size;
    for(int i = 0; i < N; i++) {
        min = i;
        for (int j = i + 1; j < N; j++) {
            if (closure(list, j, min)) {
                min = j;
            }
        }
        swap = list[i];
        list[i] = list[min];
        list[min] = swap;
    }
}

println();
println "externalCompareSelectionSort ----- ";
data = [9,8,7,6,5,4,3,2,1,0];
externalCompareSelectionSort(data) { data, a, b ->
    data[a] < data[b]
}
data.each { n -> print n + " "};
println()
data = ["s", "o", "r", "t", "e", "x", "a", "m", "p", "l", "e"];
externalCompareSelectionSort(data) { data, a, b ->
    data[a] < data[b]
}
data.each { s -> print s + " "};

class Attendee implements Comparable<Attendee> {
    def firstName;
    def lastName;
    def email;
    Attendee(firstName, lastName, email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    @Override
    int compareTo(Attendee other) {
        return firstName <=> other.firstName ?:
               lastName <=> other.lastName ?:
               email <=> other.email;
    }
    String toString() {
        return this.firstName + "," + this.lastName + "," + this.email;
    }
}

def groovySelectionSort(list, comparison) {
    def N = list.size;
    (0..N-2).each { i ->
        min = i;
        ((i + 1)..N-1).each { j ->
            if(comparison(list[j], list[min])) {
                min = j;
            }
        }
        swap = list[i];
        list[i] = list[min];
        list[min] = swap;
    }
}

println();
println "groovySelectionSort ----- ";
data = [9,8,7,6,5,4,3,2,1,0];
groovySelectionSort(data) { a, b ->
    a < b;
}
data.each { n -> print n + " "};
println()
data = ["s", "o", "r", "t", "e", "x", "a", "m", "p", "l", "e"];
groovySelectionSort(data) { a, b ->
    a < b;
}
data.each { s -> print s + " "};
println()
data = [new Attendee("Bucky", "Martin", "bucky@foo.com"),
    new Attendee("Bucky", "Blue", "oleblue@foo.com"),
    new Attendee("Nacho", "Libre", "nacho@libre.com"),
    new Attendee("Bucky", "Blue", "oleblue@foo.com"),
    new Attendee("Nacho", "Martin", "thenache@foo.com")];
groovySelectionSort(data) { a, b ->
    a < b;
}
data.each { s -> println s.toString() };
println()
data = [new Attendee("Bucky", "Martin", "bucky@foo.com"),
        new Attendee("Bucky", "Blue", "oleblue@foo.com"),
        new Attendee("Nacho", "Libre", "nacho@libre.com"),
        new Attendee("Bucky", "Blue", "oleblue@foo.com"),
        new Attendee("Nacho", "Martin", "thenache@foo.com")];
groovySelectionSort(data) { a, b ->
    a > b;
}
data.each { s -> println s.toString() };

//
// INSERTION SORT
//
def groovyInsertionSortArrayStyle(list, comparison) {
    def N = list.size;
    for (i in 1..N-1) {
        for (j in i..1) {
            if(comparison(list[j], list[j-1])) {
                exchange(list, j, j-1);
            }
        }
        //list.each { n -> print n + " "};
        //println();
    }
}

def exchange(list, i, j) {
    swap = list[i];
    list[i] = list[j];
    list[j] = swap;
}

def groovyInsertionSort(list, comparison) {
    def N = list.size;
    (0..N-1).each { i ->
        (i..1).each { j ->
            if(comparison(list[j], list[j-1])) {
                exchange(list, j, j-1);
            }
        }
    }
}

println();
println "groovyInsertionSortArrayStyle ----- ";
data = [79,891,37,96,535,42,30,-12,21,2210];
//data = ["s", "o", "r", "t", "e", "x", "a", "m", "p", "l", "e"];
groovyInsertionSortArrayStyle(data) { a, b ->
    a < b;
}
data.each { n -> print n + " "};