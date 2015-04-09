/**
 * Created with love by corbett.
 * User: corbett
 * Date: 11/7/14
 * Time: 1:53 PM
 */
package github.cworks.gizmo.tasks
import cworks.json.JsonObject
import github.cworks.gizmo.ShellInput
import github.cworks.gizmo.ShellOutput

abstract class GizmoTask {

    protected JsonObject context;
    protected ShellInput input;
    protected ShellOutput output;

    GizmoTask(final JsonObject context, final ShellInput input, final ShellOutput output) {
        this.context = context;
        this.input = input;
        this.output = output;
    }

    /**
     * A technical term for doing something with great gizziness
     */
    abstract void gizIt();
}
