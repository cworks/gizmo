/**
 * Created with love by corbett.
 * User: corbett
 * Date: 11/7/14
 * Time: 1:53 PM
 */
package github.cworks.gizmo.tasks
import cworks.json.JsonObject

abstract class GizmoTask {

    protected JsonObject context;

    GizmoTask(final JsonObject context) {
        this.context = context;
    }

    /**
     * A technical term for doing something with great gizziness
     */
    abstract void gizIt();
}
