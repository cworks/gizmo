/**
 * Created with love by corbett.
 * User: corbett
 * Date: 12/6/14
 * Time: 9:37 AM
 */
package github.cworks.dsl

enum SortOrder {
    DEFAULT, DESC, ASC
}

enum Size {
    ;
    public static Size Kilobyte;
    public static Size Megabyte;
}

def ls = new Ls();
ls.path("/Users/corbett/dev")
    .glob("*")
    .depth(1)
    .sortByCreated(SortOrder.DESC) { item ->

    println(item);
}

class Ls {
    def _path;
    def _glob;
    def _depth;
    def _sortOrder;
    def _unit;

    class GlobOrDepthOrSortOrUnit {

        class DepthOrSortOrUnit {
            class SortOrUnit {
                class Unit {
                    def unit(n = Size.Kilobyte, closure = null) {
                        _unit = n;
                        if(closure) {
                            closure("unit");
                            return;
                        }
                    }
                }

                def sortByCreated(sortOrder = SortOrder.ASC, closure = null) {
                    _sortOrder = sortOrder;
                    if(closure) {
                        closure("sortByCreated");
                        return;
                    }
                    return new Unit();
                }
            }

            def depth(n = 1, closure = null) {
                _depth = n;
                if(closure) {
                    closure("depth");
                    return;
                }
                return new SortOrUnit();
            }
        }

        class GlobOrSortOrUnit {

        }

        class GlobOrDepthOrUnit {

        }

        class GlobOrDepthOrSort {

        }

        def glob(glob, closure = null) {
            _glob = glob;
            if(closure) {
                closure("glob");
                return;
            }
            return new DepthOrSortOrUnit();
        }
        def depth(n, closure) {
            _depth = n;
            return new GlobOrSortOrUnit();
        }
        def sortByCreated(sortOrder, closure) {
            _sortOrder = sortOrder;
            return new GlobOrDepthOrUnit();
        }
        def unit(unit, closure) {
            _unit = unit;
            return new GlobOrDepthOrSort();
        }
    }


    def path(path, closure = null) {
        _path = path;
        if(closure) {
            closure("path");
        }
        return new GlobOrDepthOrSortOrUnit();
    }
}








